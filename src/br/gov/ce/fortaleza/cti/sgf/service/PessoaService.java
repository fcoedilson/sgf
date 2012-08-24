/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Pessoa;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;

/**
 * @author Deivid
 *
 */
@Repository
@Transactional
public class PessoaService extends BaseService<Integer, Pessoa> {

	@Transactional
	public List<Pessoa> findByUG(String ug) throws NonUniqueResultException{
		return executeResultListQuery("findByUG", ug);
	}

	@Transactional
	public Pessoa findByCPF(String cpf, UG ug) throws NonUniqueResultException{
		try {
			if(ug == null){
				Query query = entityManager.createQuery("select p from Pessoa p where p.cpf = ?");
				query.setParameter(1, cpf);
				return (Pessoa) query.getSingleResult();
			} else {
				Query query = entityManager.createQuery("select p from Pessoa p where p.ua.ug.id = ? and p.cpf = ?");
				query.setParameter(1, ug.getId());
				query.setParameter(2, cpf);
				return (Pessoa) query.getSingleResult();
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Transactional
	public Boolean findPessoaByCpf(String cpf){
		return executeSingleResultQuery("findByCPF", cpf) != null;
	}
}
