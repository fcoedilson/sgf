/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Bomba;
import br.gov.ce.fortaleza.cti.sgf.entity.DiarioBomba;
import br.gov.ce.fortaleza.cti.sgf.entity.Posto;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;

/**
 * @author Deivid
 * @since 11/12/09
 */
@Repository
@Transactional
public class DiarioBombaService extends BaseService<Integer, DiarioBomba>{

	public DiarioBomba findLastDiariaByDate(Bomba bomba) {
		Query query = entityManager.createQuery("select db from DiarioBomba db where db.dataCadastro = (select max(db.dataCadastro) " +
		"from DiarioBomba db where db.bomba = :bomba) and db.bomba = :bomba");
		query.setParameter("bomba", bomba);
		return (DiarioBomba) query.getSingleResult();
	}

	/**
	 * Busca o registro da �ltima di�ria da bomba
	 * @param bomba
	 * @param day
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<DiarioBomba> ultimasTrintaDiarias(){

		Date term = new Date();
		term = DateUtil.getDateEndDay(term);
		
		Date init = DateUtil.adicionarOuDiminuir(term, -30*DateUtil.DAY_IN_MILLIS);
		init = DateUtil.getDateStartDay(init);
		Query query = entityManager.createQuery("select d from DiarioBomba d where d.dataCadastro between ? and ? order by d.dataCadastro desc");
		query.setParameter(1, init);
		query.setParameter(2, term);
		return (List<DiarioBomba>)query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DiarioBomba> ultimasTrintaDiarias(Date init, Date term, Posto posto){

		StringBuffer stringBuffer = new StringBuffer("select d from DiarioBomba d where d.dataCadastro between ? and ?");
		if(posto != null){
			stringBuffer.append(" and d.bomba.posto = ?");
		}
		stringBuffer.append("  order by d.bomba.posto.descricao desc");
		
		Query query = entityManager.createQuery(stringBuffer.toString());

		query.setParameter(1, init);
		query.setParameter(2, term);
		if(posto != null){
			query.setParameter(3, posto);
		}

		return (List<DiarioBomba>)query.getResultList();
	}

	public DiarioBomba findCurrentDiaryByBomba(Integer bombaId) {
		try {
			Query query = entityManager.createQuery("select d from DiarioBomba d where d.bomba.id = ? and " +
			"d.horaInicial = (select max(db.horaInicial) from DiarioBomba db where db.bomba.id = ?)");
			query.setParameter(1, bombaId);
			query.setParameter(2, bombaId);
			return (DiarioBomba) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Date findUltimaDiariaByBomba(Integer bombaId) {
		Query query = entityManager.createQuery("select max(db.horaInicial) from DiarioBomba db where db.bomba.id = :id");
		query.setParameter("id", bombaId);
		return (Date) query.getSingleResult();
	}

	/**
	 * Esse m�todo est� trazendo todas as di�rias e gerando desperd�cio de tr�fego
	 * @param bombaSelecionada
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DiarioBomba> findDiariasByBomba(Bomba bombaSelecionada) {
		List<DiarioBomba> diarioBombas = executeResultListGenericQuery("findDiariasByBomba", bombaSelecionada);
		return diarioBombas;
	}

	@SuppressWarnings("unchecked")
	public List<DiarioBomba> findByPeriodoPosto(Date dtInicial, Date dtFinal, Posto posto) {

		List<DiarioBomba> diarioBombas = new ArrayList<DiarioBomba>();
		StringBuffer hql = new StringBuffer("select d from DiarioBomba d where ");
		if(dtInicial != null && dtFinal != null){
			hql.append("d.dataCadastro >= :dtInicial and d.dataCadastro <= :dtFinal ");
		}
		if(posto != null){
			hql.append("and d.bomba.posto.codPosto = :postoID ");
		}

		hql.append("order by d.dataCadastro");
		Query query = entityManager.createQuery(hql.toString()); 
		query.setParameter("dtInicial", dtInicial);
		query.setParameter("dtFinal", dtFinal);
		if(posto != null){
			query.setParameter("postoID", posto.getCodPosto());
		}
		diarioBombas = query.getResultList();
		return diarioBombas;
	}

	@SuppressWarnings("unchecked")
	public List<Posto> findDiariosPeriodoPosto(Date dtInicial, Date dtFinal) {
		List<Posto> postos = null;
		StringBuffer hql = new StringBuffer("select distinct(d.bomba.posto) from DiarioBomba d where ");
		if(dtInicial != null && dtFinal != null){
			hql.append("d.dataCadastro >= :dtInicial and d.dataCadastro <= :dtFinal ");
		}
		hql.append("order by d.bomba.posto.descricao");
		Query query = entityManager.createQuery(hql.toString()); 
		query.setParameter("dtInicial", dtInicial);
		query.setParameter("dtFinal", dtFinal);
		postos = query.getResultList();
		return postos;
	}
	
	@SuppressWarnings("unchecked")
	public List<DiarioBomba> findByDate(Date date, Posto posto) {
		
		Date dt1 = DateUtil.getDateStartDay(date);
		Date dt2 = DateUtil.getDateEndDay(date);
		
		StringBuffer hql = new StringBuffer("select d from DiarioBomba d where ");
		hql.append("d.dataCadastro between :dtInicial and  :dtFinal ");
		
		if(posto != null){
			hql.append(" and d.bomba.posto = :posto");
		}
		
		Query query = entityManager.createQuery(hql.toString()); 

		query.setParameter("dtInicial", dt1);
		query.setParameter("dtFinal", dt2);
		
		if(posto != null){
			query.setParameter("posto", posto);
		}

		return query.getResultList();
	}
}
