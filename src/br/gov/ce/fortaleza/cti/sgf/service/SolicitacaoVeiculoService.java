/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.SolicitacaoVeiculo;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusSolicitacaoVeiculo;

/**
 * @author Deivid
 * @since 22/12/2009
 */
@Repository
@Transactional
public class SolicitacaoVeiculoService extends BaseService<Integer, SolicitacaoVeiculo> {

	private static final Log logger = LogFactory.getLog(BaseService.class);

	@Autowired
	private VeiculoService veiculoService;

	@SuppressWarnings("unchecked")
	public List<SolicitacaoVeiculo> findByUGAndStatus(UG ug, StatusSolicitacaoVeiculo status) {

		List<SolicitacaoVeiculo> solicitacaoVeiculos = new ArrayList<SolicitacaoVeiculo>();
		try {
			Query query = null;
			if(SgfUtil.isAdministrador(SgfUtil.usuarioLogado()) || SgfUtil.isCoordenador(SgfUtil.usuarioLogado())){
				query = entityManager.createQuery("select s from SolicitacaoVeiculo s where s.solicitante.veiculo.ua.ug.id = ? and s.status = ? order by s.dataHoraSaida desc");
			} else if(SgfUtil.isChefeTransporte(SgfUtil.usuarioLogado()) || SgfUtil.isChefeSetor(SgfUtil.usuarioLogado())){
				query = entityManager.createQuery("select s from SolicitacaoVeiculo s where s.solicitante.pessoa.ua.ug.id = ? and s.status = ? order by s.dataHoraSaida desc");
			}
			query.setParameter(1, ug.getId());
			query.setParameter(2, status);
			solicitacaoVeiculos = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return solicitacaoVeiculos;
	}

	@SuppressWarnings("unchecked")
	public List<SolicitacaoVeiculo> findSolVeiculoOrgao(Date dtInicial, Date dtFinal, UG orgao, StatusSolicitacaoVeiculo status) {

		String sql = "select o from SolicitacaoVeiculo o where o.veiculo != null AND o.dataHoraSaida >= :inicio and o.dataHoraRetorno <= :fim";
		StringBuffer hql = new StringBuffer(sql);
		if(orgao != null){
			hql.append(" and o.veiculo.ua.ug.id = :ugid");
		}
		hql.append(" and o.status = :status order by o.dataHoraSaida desc");

		Query query = entityManager.createQuery(hql.toString());
		query.setParameter("inicio", dtInicial);
		query.setParameter("fim", dtFinal);
		query.setParameter("status", status);
		if(orgao != null){
			query.setParameter("ugid", orgao.getId());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<SolicitacaoVeiculo> findSolicitacoesVeiculos(String placa, StatusSolicitacaoVeiculo status) {
		placa = placa.trim();
		String sql = "select o from SolicitacaoVeiculo o where o.veiculo != null AND o.veiculo.placa = :placa and o.status = :status order by o.dataHoraSaida desc";
		StringBuffer hql = new StringBuffer(sql);
		Query query = entityManager.createQuery(hql.toString());
		query.setParameter("placa", placa.toUpperCase());
		query.setParameter("status", status);
		return query.getResultList();
	}

	/**
	 * 
	 * @param retorna um mapeamento dos kilometros rodados por ve�culo
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<Veiculo, Float>  mapKilometragem(UG ug, Date begin, Date end){
		Map<Veiculo, Float> result = new HashMap<Veiculo, Float>();
		try {

			List<Veiculo> veiculos = veiculoService.findByUG(ug);

			for (Veiculo veiculo : veiculos) {
				Query query = entityManager.createQuery("SELECT s FROM SolicitacaoVeiculo s WHERE  s.veiculo.id = ? and s.dataHoraSaida = " +
				"(SELECT min(s1.dataHoraSaida) FROM SolicitacaoVeiculo s1 WHERE  s1.veiculo.id = ? and s1.status != 3 and s1.dataHoraRetorno BETWEEN ? and ?)");
				Query query2 = entityManager.createQuery("SELECT s FROM SolicitacaoVeiculo s WHERE  s.veiculo.id = ? and s.dataHoraRetorno = " +
				"(SELECT max(s1.dataHoraRetorno) FROM SolicitacaoVeiculo s1 WHERE  s1.veiculo.id = ? and s1.status != 3 and s1.dataHoraRetorno BETWEEN ? and ?)");
				query.setParameter(1, veiculo.getId());
				query.setParameter(2, veiculo.getId());
				query.setParameter(3, begin);
				query.setParameter(4, end);
				query2.setParameter(1, veiculo.getId());
				query2.setParameter(2, veiculo.getId());
				query2.setParameter(3, begin);
				query2.setParameter(4, end);

				SolicitacaoVeiculo min = null;
				SolicitacaoVeiculo max = null;
				try {
					min = (SolicitacaoVeiculo) query.getSingleResult();
					max = (SolicitacaoVeiculo) query2.getSingleResult();
				} catch (Exception e) {
				}

				if(min != null && max != null){
					if(min != null){
						Float kmmin = null;
						Float kmmax = null;
						Float kmrod = null;
						try {
							kmmin = min.getKmSaida().floatValue();
							kmmax = max.getKmRetorno().floatValue();
							kmrod = kmmax - kmmin;
						} catch (Exception e) {
						}
						if(result.containsKey(veiculo)){
							result.put(veiculo, kmrod);
						} else {
							result.put(veiculo, kmrod);
						}
					}
				} else {
					if(result.containsKey(veiculo)){
						result.put(veiculo, null);
					} else {
						result.put(veiculo, null);
					}
				}
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * s.veiculo.status = 4 � o caso em o ve�culo est� indispon�vel
	 * @param user
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SolicitacaoVeiculo> findByUserAndStatus(User user, StatusSolicitacaoVeiculo status) {
		List<SolicitacaoVeiculo> solicitacaoVeiculos = new ArrayList<SolicitacaoVeiculo>();
		try {
			StringBuffer sql = new StringBuffer("select s from SolicitacaoVeiculo s where s.solicitante.codPessoaUsuario = ?");
			if (status != null) {
				sql.append(" and s.status = ?");
			}
			sql.append(" order by s.dataHoraSaida");
			Query query = entityManager.createQuery(sql.toString());
			query.setParameter(1, user.getCodPessoaUsuario());
			if (status != null) {
				query.setParameter(2, status);
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return solicitacaoVeiculos;
	}

	/**
	 * Encontra os ve�culos dispon�veis em qualquer UG, no caso de adminstrador ou coordenador
	 * Encontra os ve�culos dispon�veis na UG do usu�rio, no caso de chefe de transporte ou chefe se setor
	 * @param solicitacao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Veiculo> findVeiculosDisponiveis(SolicitacaoVeiculo solicitacao, UG aux) {

		List<Veiculo> veiculos = new ArrayList<Veiculo>();
		List<SolicitacaoVeiculo> solicitacaoVeiculos = new ArrayList<SolicitacaoVeiculo>();
		StringBuffer hql = new StringBuffer("SELECT s FROM SolicitacaoVeiculo s WHERE ((s.dataHoraRetorno BETWEEN :saida and :retorno) AND " +
		"(s.dataHoraSaida BETWEEN :saida and :retorno)) or (s.dataHoraRetorno BETWEEN :saida AND :retorno) or (s.dataHoraSaida BETWEEN :saida AND :retorno)");
		UG ug = null;
		if(aux != null){
			ug = aux;
		} else {
			if(!SgfUtil.isAdministrador(solicitacao.getSolicitante()) && !SgfUtil.isCoordenador(solicitacao.getSolicitante())){
				ug = solicitacao.getSolicitante().getPessoa().getUa().getUg();
			}
		}
		if (ug != null) {
			hql.append("and s.solicitante.pessoa.ua.ug.id = :ugId");
		}
		Query query = entityManager.createQuery(hql.toString());
		query.setParameter("saida", solicitacao.getDataHoraSaida());
		query.setParameter("retorno", solicitacao.getDataHoraRetorno());
		if (ug != null) {
			query.setParameter("ugId", ug.getId());
		}
		solicitacaoVeiculos = query.getResultList();
		List<Veiculo> remove = new ArrayList<Veiculo>();

		for (SolicitacaoVeiculo sol : solicitacaoVeiculos) {
			remove.add(sol.getVeiculo());
		}

		veiculos = veiculoService.veiculosDisponiveis(ug);
		Collections.sort(veiculos, new Comparator<Veiculo>() {
			public int compare(Veiculo o1, Veiculo o2) {
				return o1.getPlaca().compareTo(o2.getPlaca());
			}
		});
		//veiculos.removeAll(remove);
		return veiculos;
	}
	/**
	 * Verifica se o ve�culo possui alguma solicita��o, autoriza��o ou o ve�culo se encontra em rota pra o per�odo informado
	 * @param vid
	 * @param horaSaida
	 * @param horaRetorno // status = 0 => SOLICITADO;  status = 1 => AUTORIZADO; status = 2 => NEGADO;  status = 3 => (EXTERNO OU EM ROTA); status = 4 => FINALIZADO
	 * @return
	 */
	public Boolean isVeiculoDisponivel(Integer vid, Date horaSaida, Date horaRetorno){
		StringBuffer stringQuery = new StringBuffer("SELECT s FROM SolicitacaoVeiculo s WHERE s.veiculo.id = :veiculo and (s.status = 0 or s.status = 1 or s.status = 3) and " +
				"(((s.dataHoraRetorno BETWEEN :saida and :retorno) AND (s.dataHoraSaida BETWEEN :saida and :retorno)) or (s.dataHoraRetorno BETWEEN :saida AND :retorno) or " +
		"(s.dataHoraSaida BETWEEN :saida AND :retorno))");
		Query query = entityManager.createQuery(stringQuery.toString());
		query.setParameter("veiculo", vid);
		query.setParameter("saida", horaSaida);
		query.setParameter("retorno", horaRetorno);
		return query.getResultList().size() > 0;
	}

	@SuppressWarnings("unchecked")
	public List<SolicitacaoVeiculo> pesquisarSolUsuario(User usuario, StatusSolicitacaoVeiculo status) {
		List<SolicitacaoVeiculo> solicitacaoVeiculos = new ArrayList<SolicitacaoVeiculo>();
		Query query = entityManager.createQuery("select s from SolicitacaoVeiculo s where s.solicitante.codPessoaUsuario = ? and s.status = ?");
		query.setParameter(1, usuario.getCodPessoaUsuario());
		query.setParameter(2, status);
		solicitacaoVeiculos = query.getResultList();
		return solicitacaoVeiculos;
	}

	public List<SolicitacaoVeiculo> pesquisarSolicitacaoUser(Integer id, StatusSolicitacaoVeiculo status) {
		return executeResultListQuery("findByUsuarioStatus", id, status);
	}

	/**
	 * retorna a lista de solicitações de veículos
	 * @param veiculo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SolicitacaoVeiculo> findSolicitacoesVeiculo(Veiculo veiculo, StatusSolicitacaoVeiculo status) {
		List<SolicitacaoVeiculo> solicitacaoVeiculos = null;
		StringBuilder sql = new StringBuilder("select s from SolicitacaoVeiculo s where s.veiculo.id = :id");
		if(status != null){
			sql.append(" and s.status = :status");
		}

		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("id", veiculo.getId());

		if(status != null){
			query.setParameter("status", status);
		}

		solicitacaoVeiculos = query.getResultList();
		return solicitacaoVeiculos;
	}
}
