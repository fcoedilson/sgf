/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Role;

/**
 * @author Deivid
 *
 */
@Repository
@Transactional
public class RoleService extends BaseService<Integer, Role>{

	public Role findById(Integer id){

		return executeSingleResultQuery("findById", id);
	}

	@SuppressWarnings("unchecked")
	public List<Role> findAll(){

		Query query = entityManager.createQuery("SELECT r FROM Role r");

		return query.getResultList();
	}
}
