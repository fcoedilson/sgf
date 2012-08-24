/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Bomba;
import br.gov.ce.fortaleza.cti.sgf.entity.DiarioBomba;
import br.gov.ce.fortaleza.cti.sgf.entity.PostoServico;

/**
 * @author Deivid
 * @since 11/12/09
 */
@Repository
@Transactional
public class BombaService extends BaseService<Integer, Bomba>{

	@SuppressWarnings("unchecked")
	public List<DiarioBomba> findDiariosHoje(Bomba bomba) {
		List<DiarioBomba> diarioBombas = new ArrayList<DiarioBomba>();		
		Query query = entityManager.createQuery("select o from DiarioBomba o where o.bomba = :bomba");
		query.setParameter("bomba", bomba);
		diarioBombas = query.getResultList();
		return diarioBombas;
	}

	public boolean findBombaPosto(Bomba bomba) throws NoResultException{

		boolean existe = false;
		Query query = entityManager.createQuery("select o from Bomba o where o.numero = :numero and o.posto.id = :idPosto");
		query.setParameter("numero", bomba.getNumero());
		query.setParameter("idPosto", bomba.getPosto().getCodPosto());
		query.setMaxResults(1);
		Bomba bombaAux = null;
		try{
			 bombaAux = (Bomba) query.getSingleResult();
		}catch (NoResultException e) {
			return false;
		}
		
		if(bombaAux != null){
			existe = true;
		}else{
			existe = false;
		}
		
		return existe;
	}

	@SuppressWarnings("unchecked")
	public List<Bomba> pesquisar(Bomba bomba) {
		List<Bomba> bombas = null;
		Session session = (Session) entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Bomba.class).add(Example.create(bomba).enableLike(MatchMode.ANYWHERE).ignoreCase());
		if(bomba.getPosto() != null){
			criteria.createCriteria("posto").add(Example.create(bomba.getPosto()));
		}
		if(bomba.getCombustivel() != null){			
			criteria.createCriteria("combustivel").add(Example.create(bomba.getCombustivel()));
		}
		bombas = criteria.list();
		return bombas;
	}

	public boolean findPostoCombustivel(Bomba bomba) {

		Query query = entityManager.createQuery("select o from PostoServico o where o.posto.codPosto = :postoID " +
				"and o.tipoCombustivel.id = :combustivelID");
		query.setParameter("postoID", bomba.getPosto().getCodPosto());
		query.setParameter("combustivelID", bomba.getCombustivel().getId());
		query.setMaxResults(1);
		PostoServico postoServico = null;
		try{
			 postoServico = (PostoServico) query.getSingleResult();
		}catch (NoResultException e) {
			return false;
		}
		
		if(postoServico != null){
			return true;
		}else{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Bomba> findBombaByPosto(Integer idPosto){
		return  executeResultListGenericQuery("findBombasPosto", idPosto);
	}
}
