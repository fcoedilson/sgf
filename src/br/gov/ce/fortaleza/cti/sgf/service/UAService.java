package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.UA;

@Transactional
@Repository
public class UAService  extends BaseService<String, UA>{

	@SuppressWarnings("unchecked")
	public List<UA> retrieveByUG(String ug){
		Query query = entityManager.createQuery("Select v from UA v where v.ug.id = ? ");
		query.setParameter(1, ug);
		List<UA> result =  query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UA> findUASorted(){
		return executeResultListGenericQuery("findSorted");
	}
}
