package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.SolicitacaoLubrificante;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;


@Transactional
@Repository
public class SolicitacaoLubrificanteService extends BaseService<Integer, SolicitacaoLubrificante>{

	@SuppressWarnings("unchecked")
	public List<SolicitacaoLubrificante> findSolicitacoes(Integer status, Integer veiculoId, Integer postoId, Date dtInicial, Date dtFinal, UG ug) {

		List<SolicitacaoLubrificante> result = new ArrayList<SolicitacaoLubrificante>();
		StringBuffer hql = new StringBuffer("select s from SolicitacaoLubrificante s where  1 = 1");

		if(status != null && status != 3){
			hql.append(" and s.status = " + status);
		}
		if(veiculoId != null){
			hql.append(" and s.veiculo.id = " + veiculoId);
		}

		if(postoId != null){
			hql.append(" and s.posto.codPosto = " + postoId);
		}

		if (dtInicial != null && dtFinal != null) {
			//dtInicial = DateUtil.getDateEndDay(dtInicial);
			//dtFinal = DateUtil.getDateEndDay(dtFinal);
			hql.append(" and s.dataAutorizacao between '" + dtInicial + "' and '" + dtFinal + "'");
		}

		if (ug != null) {
			hql.append(" and s.veiculo.ua.ug.id = '" + ug.getId()+"'");
		}

		hql.append(" order by s.dataAutorizacao desc");
		Query query = entityManager.createQuery(hql.toString());
		result = query.getResultList();

		return result;
	}

	@Transactional
	public Map<String, List<SolicitacaoLubrificante>>retrieveSolicLubrificanteMap(Integer status, Integer veiculoId, Integer postoId, Date dtInicial, Date dtFinal, UG ug) {

		Map<String, List<SolicitacaoLubrificante>> resultMap = new HashMap<String, List<SolicitacaoLubrificante>>();
		StringBuffer hql = new StringBuffer("select s from SolicitacaoLubrificante s where  1 = 1");

		if(status != null && status != 3){
			hql.append(" and s.status = " + status);
		}
		if(veiculoId != null){
			hql.append(" and s.veiculo.id = " + veiculoId);
		}

		if(postoId != null){
			hql.append(" and s.posto.codPosto = " + postoId);
		}

		if (dtInicial != null && dtFinal != null) {
			dtInicial = DateUtil.getDateEndDay(dtInicial);
			dtFinal = DateUtil.getDateEndDay(dtFinal);
			hql.append(" and s.dataAutorizacao between '" + dtInicial + "' and '" + dtFinal + "'");
		}

		if (ug != null) {
			hql.append(" and s.veiculo.ua.ug.id = '" + ug.getId()+"'");
		}

		hql.append(" order by s.dataAutorizacao desc");
		Query query = entityManager.createQuery(hql.toString());
		List<SolicitacaoLubrificante> resultList = query.getResultList();

		for (SolicitacaoLubrificante sol : resultList) {
			String key = sol.getVeiculo().getUa().getUg().getId();
			if(resultMap.containsKey(key)){
				resultMap.get(key).add(sol);
			} else {
				resultMap.put(key, new ArrayList<SolicitacaoLubrificante>());
				resultMap.get(key).add(sol);
			}
		}

		return resultMap;
	}

	public SolicitacaoLubrificante findUltimoRegistroByVeiculo(Integer id, Integer tsid, Integer pid){
		SolicitacaoLubrificante result = executeSingleResultQuery("findUltimoByVeiculoTipoServico", tsid, id, pid, id);
		result = result == null ? null : result;
		return result;
	}

	public SolicitacaoLubrificante findUltimoRegistroByVeiculo2(Integer veiculoId, Integer tipoServicoId, Integer postoId){

		StringBuilder sql = new StringBuilder("select s from SolicitacaoLubrificante as s where 1 = 1 ");
		if(veiculoId != null){
			sql.append("and s.veiculo.id = " + veiculoId);
		}
		if(tipoServicoId != null){
			sql.append(" and s.tipoServico.codTipoServico = " + tipoServicoId);
		}
		if(postoId != null){
			sql.append(" and s.posto.codPosto = " + postoId);
		}

		sql.append(" and s.status != -1 and s.dataAutorizacao =  (select max(x.dataAutorizacao) " +
				"from SolicitacaoLubrificante as x where x.veiculo.id = "+ veiculoId + " and x.status = 2)");
		Query query = entityManager.createQuery(sql.toString());
		SolicitacaoLubrificante result = null;
		try {
			result = (SolicitacaoLubrificante) query.getSingleResult();
		} catch (NoResultException e) {
			result = null;
		}
		return result;
	}
	/**
	 * Recupera as solicita��es de lubrificantes ordenada por data decrescente
	 * com o objetivo de que as primeiras solicita��es listadas sejam as feitas
	 * no dia corrente
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SolicitacaoLubrificante> findSolicitacoesDoDia() {
		List<SolicitacaoLubrificante> result = new ArrayList<SolicitacaoLubrificante>();

		Query query = entityManager.createNamedQuery("SolicitacaoLubrificante.findBySolicitacaoDodia");

		result = query.getResultList();

		return result;
	}

	public List<SolicitacaoLubrificante> findByPosto(Integer id){

		return executeResultListQuery("findByPosto", id);
	}

	public List<SolicitacaoLubrificante> findByPostoStatus(Integer id, Integer st){

		return executeResultListQuery("findByPostoStatus", id, st);
	}
}
