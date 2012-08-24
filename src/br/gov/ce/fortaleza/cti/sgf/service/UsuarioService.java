package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.UG;
import br.gov.ce.fortaleza.cti.sgf.entity.User;

@Repository
@Transactional
public class UsuarioService extends BaseService<Integer, User> {

	@Autowired
	private PessoaService pessoaService;

	@Transactional
	public User save(User user) {
		user.setStatus("true");
		return update(user);
	}

	@Transactional
	public User bloquear(Integer id) {
		User user = retrieve(id);
		user.setStatus("false");
		update(user);
		return user;
	}

	@Transactional
	public User desbloquear(Integer id) {
		User user = retrieve(id);
		user.setStatus("true");
		update(user);
		return user;
	}
	
	@Transactional
	public User alterarSenha(Integer id, String senha){
		User user = retrieve(id);
		user.setSenha(senha);
		update(user);
		return user;
	}

	@Transactional
	public List<User> findByUA(String id) {
		return (List<User>) executeResultListQuery("findByUA", id);
	}

	@Transactional
	public User findByLogin(String login) throws NonUniqueResultException{
		return executeSingleResultQuery("findByLogin", login, "FALSE", "false");
	}

	@Transactional
	public List<User> findByStatus(String status) {
		return executeResultListQuery("findByStatus", status, status.toLowerCase());
	}

	@Transactional
	public List<User> findByUGStatus(String ug, String status) {
		return executeResultListQuery("findByUG", ug, status);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> findByLogin(String login, String status) {
		Query query = entityManager.createQuery("select o from User o where o.login like ? and (o.status = ? or o.status = ?)");
		query.setParameter(1, "%" + login + "%");
		query.setParameter(2, status);
		query.setParameter(3, status.toLowerCase());
		return query.getResultList();
	}

	public List<User> findUsuariosBloqueados() {
		List<User> listaUsersBloqueados = executeResultListQuery("User.findByStatus", "TRUE", "true");
		return listaUsersBloqueados;
	}

	@Transactional(readOnly = true)
	public List<User> retriveByMail(String mail){
		return executeResultListQuery("findByMail", mail, true);
	}

	@Transactional
	public List<User> findByUGAndLogin(String ugid, String filter, String status){
		List<User> users = new ArrayList<User>();
		if(ugid == null){
			if(filter != null && !filter.equals("")){
				users = findByLogin(filter, status);
			} else {
				users = findByStatus(status);
			}
		} else {
			if(filter != null && !filter.equals("")){
				users = executeResultListQuery("findByUGAndLogin", ugid, filter, status);
			} else {
				users = findByUGStatus(ugid, status);
			}
		}
		return users;
	}

	public Boolean findUserByCpf(String cpf, UG ug){
		try {
			if(ug != null){
				return executeSingleResultQuery("findByUGAndCpf", cpf, ug.getId()) != null;
			} else {
				return executeSingleResultQuery("findByCpf", cpf) != null;
			}
		} catch (NoResultException e) {
		}
		return null;
	}
	/**
	 * Verifica se o usuário já existe entre os usuários válidos
	 * @param login
	 * @param id
	 * @return
	 */
	public Boolean loginExistente(String login, Integer id){
		Query query;
		if(id != null){
			query = entityManager.createQuery("SELECT u FROM User u WHERE u.login = ? and u.codPessoaUsuario not in (?)");
			query.setParameter(1,login);
			query.setParameter(2, id);
		} else {
			query = entityManager.createQuery("SELECT u FROM User u WHERE u.login = ? and (u.status = ? or u.status = ?)");
			query.setParameter(1, login);
			query.setParameter(2, "TRUE");
			query.setParameter(3, "true");
		}
		List<User> users = query.getResultList();
		return users.size() > 0;
	}
}