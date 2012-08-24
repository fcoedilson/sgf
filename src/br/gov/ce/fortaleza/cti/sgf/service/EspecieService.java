package br.gov.ce.fortaleza.cti.sgf.service;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Especie;

@Repository
@Transactional
public class EspecieService extends BaseService<Integer, Especie> {
	
	@Transactional
	public Boolean verificaSeExiste(String desc){
		
		String consulta = "select e from Especie e where upper(trim(e.descricao)) like :desc";
		StringBuffer hql = new StringBuffer(consulta);
		
		Query query = entityManager.createQuery(hql.toString());
		
		query.setParameter("desc",  desc);
		return query.getResultList().size() > 0;
	}

}