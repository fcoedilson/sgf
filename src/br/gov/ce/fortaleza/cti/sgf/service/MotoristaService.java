/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.Motorista;
import br.gov.ce.fortaleza.cti.sgf.entity.UG;

/**
 * @author Deivid
 * @since 07/12/09
 */
@Repository
@Transactional
public class MotoristaService extends BaseService<Integer, Motorista>{

	@Autowired
	private PessoaService pessoaService;

	public List<Motorista> findMotoristasBloqueados() {
		return executeResultListQuery("findMotoristasBloqueados", "false");
	}

	public List<Motorista> findByStatus(String status) {
		return executeResultListQuery("findByStatus", status);
	}

	public List<Motorista> findByUG(String ugid){
		return executeResultListQuery("findByUG", ugid);
	}

	public List<Motorista> findByUGStatus(String ugid, String status){
		return executeResultListQuery("findByUGStatus", ugid, status);
	}

	@SuppressWarnings("unchecked")
	public List<Motorista> findByOrgao(UG ug){
		List<Motorista> motoristas = new ArrayList<Motorista>();
		Query query = null;
		query = entityManager.createQuery("select o from Motorista o where o.pessoa.ua.ug = :ug");
		query.setParameter("ug", ug);
		motoristas = query.getResultList();
		return motoristas;
	}

	@SuppressWarnings("unchecked")
	public List<Motorista> findByName(String name){
		List<Motorista> motoristas = new ArrayList<Motorista>();
		Query query = null;
		query = entityManager.createQuery("select o from Motorista o where o.pessoa.nome LIKE ?");
		query.setParameter(1, "%"+ name +"%");
		motoristas = query.getResultList();
		return motoristas;
	}

	@Transactional
	public List<Motorista> findByNameStatus(String filter, String status){

		return executeResultListQuery("findByNameStatus", filter, status);
	}

	@Transactional
	public List<Motorista> findByUGNameStatus(String ugid, String filter, String status){
		List<Motorista> motoristas = new ArrayList<Motorista>();
		if(ugid == null){
			if(filter != null){
				filter = "%" + filter + "%";
				motoristas = findByNameStatus(filter.toUpperCase(), status);
			} else {
				motoristas = findByStatus(status);
			}
		} else {
			if(filter != null){
				filter = "%" + filter + "%";
				motoristas = executeResultListQuery("findByUGNameStatus", ugid, filter.toUpperCase(), status);
			} else {
				motoristas = findByUGStatus(ugid, status);
			}
		}
		return motoristas;
	}

	@Transactional
	public Motorista bloquear(Integer id) {
		Motorista motorista = retrieve(id);
		motorista.setAtivo("false");
		update(motorista);
		return motorista;
	}

	@Transactional
	public Motorista desbloquear(Integer id) {
		Motorista motorista = retrieve(id);
		motorista.setAtivo("true");
		update(motorista);
		return motorista;
	}

	@Transactional
	public Motorista findMotoristaByCpf(String cpf) {
		return executeSingleResultQuery("findMotoristaByCPF", "%"+cpf+"%");
	}

	@SuppressWarnings("unchecked")
	public List<UG> findMotoristaOrgao() {
		List<UG> ugs = null;
		Query query = entityManager.createQuery("select distinct(o.pessoa.ua.ug) from Motorista o where o.pessoa.ua is not null ");
		ugs = query.getResultList();
		query = entityManager.createQuery("select distinct(o.pessoa.ua.ug) from Motorista o where o.pessoa.ua is not null and o.pessoa.ua is null");
		List<UG> ugs2 = query.getResultList();
		ugs.addAll(ugs2);
		return ugs;
	}
}
