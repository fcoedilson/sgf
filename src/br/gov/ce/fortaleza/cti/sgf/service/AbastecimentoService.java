/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Abastecimento;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.StatusAbastecimento;

/**
 * @author Deivid
 * @since 11/12/09
 */
@Repository
@Transactional
public class AbastecimentoService extends BaseService<Integer, Abastecimento> {
	/**
	 * Retorna o total abastecido pelo ve�culo no m�s.
	 * 
	 * @param veiculo
	 *            veiculo a ser pesquisado o total abastecido
	 * @return o total j� abastecido
	 */
	public Double pesquisarTotalAbastecidoMes(Veiculo veiculo) {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

		Query query = entityManager.createNamedQuery("Abastecimento.findCota");
		query.setParameter("id", veiculo.getId());
		query.setParameter("inicio", cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		query.setParameter("fim", cal.getTime());

		Object o = query.getSingleResult();

		if (o == null) {
			o = Double.valueOf(0);
		}

		return (Double) o;
	}

	@SuppressWarnings("unchecked")
	public List<Abastecimento> pesquisarAbastecimentoVeiculoPorPlaca(Date dataIni, Date dataFim, UG ug, String placa, StatusAbastecimento status) {

		List<Abastecimento> result = new ArrayList<Abastecimento>();
		String queryString = "SELECT a from Abastecimento a WHERE a.dataAutorizacao <= :currentDate and a.dataAutorizacao BETWEEN :dataIni AND :dataFim ";
		StringBuffer queryBuffer = new StringBuffer(queryString);

		if(status != null){
			queryBuffer.append(" AND a.status = :status");
		}
		if(ug != null){
			queryBuffer.append(" AND a.veiculo.ua.ug = :ug");
		}
		if(placa != null){
			queryBuffer.append(" AND a.veiculo.placa like :placa");
		}

		Query query = entityManager.createQuery(queryBuffer.toString());
		query.setParameter("currentDate", DateUtil.getDateStartDay(new Date()));
		query.setParameter("dataIni", dataIni);
		query.setParameter("dataFim", dataFim);
		
		queryBuffer.append(" orde by a.dataAutorizacao desc");

		if(status != null){
			query.setParameter("status", status);
		}

		if(ug != null){
			query.setParameter("ug", ug);
		}

		if(placa != null){
			query.setParameter("placa", placa);
		}
		queryBuffer.append(" order by a.dataAutorizacao desc");
		result = query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Abastecimento> pesquisarAbastecimentosPorPeriodo(Date dtInicial, Date dtFinal, UG ug, StatusAbastecimento status) {

		List<Abastecimento> abastecimentos = null;
		String queryString = "select a from Abastecimento a where a.dataAutorizacao between :dataInicial and :dataFinal";
		StringBuffer queryBuffer = new StringBuffer(queryString);
		
		if(ug != null){
			queryBuffer.append(" and a.veiculo.ua.ug.id = :orgao");
		}
		
		queryBuffer.append(" and a.status = :status order by a.dataAutorizacao desc");
		
		Query query = entityManager.createQuery(queryBuffer.toString());
		query.setParameter("dataInicial", dtInicial);
		query.setParameter("dataFinal", dtFinal);
		
		if(ug != null){
			query.setParameter("orgao", ug.getId());
		}
		
		query.setParameter("status", status);
		abastecimentos = query.getResultList();
		return abastecimentos;
	}
	/**
	 * Verificar se o veículo possui alguma autorizaçao de abstecimento para o dia e como o mesmo tipo
	 * de combustível
	 * @param v
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean validarAutorizacaoVeiculo(Veiculo v, Date data, Integer tipoCombustivel){
		List<Abastecimento> result;
		Query query = entityManager.createQuery("select a from Abastecimento a where a.veiculo.id = :veiculoId and " +
				"a.dataAutorizacao = :currentDate and a.combustivel.id = :tipoCombustivel");
		query.setParameter("veiculoId", v.getId());
		query.setParameter("currentDate", data);
		query.setParameter("tipoCombustivel", tipoCombustivel);
		result = query.getResultList();
		if(result != null){
			return result.size() > 0;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Abastecimento> pesquisarAbastecimentos(Date dtInicial, Date dtFinal, StatusAbastecimento status) {

		List<Abastecimento> abastecimentos = null;
		Query query = entityManager.createQuery("select a from Abastecimento a where a.dataAutorizacao between :dataInicial and :dataFinal and " +
				"a.status = :status order by a.dataAutorizacao desc");
		//query.setParameter("currentDate", DateUtil.getDateStartDay(new Date()));
		query.setParameter("dataInicial", dtInicial);
		query.setParameter("dataFinal", dtFinal);
		query.setParameter("status", status);
		abastecimentos = query.getResultList();
		return abastecimentos;
	}

	public List<Abastecimento> findByPosto(Integer postoId, StatusAbastecimento status){
		return executeResultListQuery("findByPosto", postoId, status);
	}

	public List<Abastecimento> findByPeriodoAndPosto(String ugId, Integer postoId, Date dataIni, Date dataFim, StatusAbastecimento status){
		if(ugId == null){
			return executeResultListQuery("findByPeriodoAndPosto", postoId, dataIni, dataFim, status);
		} else {
			return executeResultListQuery("findByPeriodoAndPostoUG", ugId, postoId, dataIni, dataFim, status);
		}
	}
}
