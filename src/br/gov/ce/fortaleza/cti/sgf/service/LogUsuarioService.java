package br.gov.ce.fortaleza.cti.sgf.service;

import java.util.Date;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.fortaleza.cti.sgf.entity.LogUsuario;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;

@Repository
@Transactional
public class LogUsuarioService extends BaseService<Integer, LogUsuario> {

	@Transactional(readOnly = true)
	public List<LogUsuario> retrieveByLogUsuario(LogUsuario logUsuario) {
		Date ini = DateUtil.getDateStartDay(logUsuario.getDataLogin());
		Date fim = DateUtil.getDateEndDay(logUsuario.getDataLogin());
		return executeResultListQuery("findByUsuarioAndData", logUsuario.getUsuario().getCodPessoaUsuario(), ini, fim);
	}
	
	@Transactional(readOnly = true)
	public LogUsuario findUltimoLogin(Integer id)  throws NonUniqueResultException {
		return executeSingleResultQuery("findUltimoLogin", id, id);
	}
}