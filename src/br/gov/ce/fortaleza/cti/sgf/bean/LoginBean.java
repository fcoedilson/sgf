/**
 * 
 */
package br.gov.ce.fortaleza.cti.sgf.bean;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * @author Deivid
 *
 */
@Scope("session")
@Component("loginBean")
public class LoginBean implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean isLoginErro() {
		return SgfUtil.getRequest().getParameter("login_error") != null;
	}

	public boolean isUsuarioLogado() {
		return SgfUtil.getRequest().getParameter("usuario_logado") != null;
	}
}
