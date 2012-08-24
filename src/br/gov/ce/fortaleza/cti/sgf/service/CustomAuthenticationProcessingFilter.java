package br.gov.ce.fortaleza.cti.sgf.service;

import javax.security.auth.login.CredentialExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.concurrent.ConcurrentLoginException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.security.util.TextUtils;
import org.springframework.stereotype.Component;

/**
 * Classe respons�vel por realizar o direcionamento din�mico do sistema
 * ap�s o usu�rio efetuar o login no sistema
 * 
 * @author lafitte
 * @since 27/04/2010
 *
 */
@Scope("Request")
@Component("customAuthenticationProcessingFilter")
public class CustomAuthenticationProcessingFilter extends  AuthenticationProcessingFilter {

	private static final String HOME_TARGET_URL = "/pages/home.jsf";
	private static final String NON_ROLE_TARGET_URL = "/login.jsf?login_error=true";
	private static final String INVALID_SESSION_TARGET_URL = "/loginError.jsp";
	private static final String ROOT = "/";

	/**
	 * Determina a url da p�gina para qual ser� direcionado o sistema de acordo com o perfil do usu�rio
	 * logado
	 * 
	 * @return String
	 */
	@Override
	protected String determineTargetUrl(HttpServletRequest request) {

		return HOME_TARGET_URL;
	}

	protected String determineFailureUrl(HttpServletRequest request, AuthenticationException failed){

		try {
			if(failed.contains(ConcurrentLoginException.class)){
				request.getSession().setAttribute("messageLogin", "Usuário já realizou login...");
			} else if(failed.contains(BadCredentialsException.class)){
				request.getSession().setAttribute("messageLogin", "Usuário ou senha inválido...");
			} else if(failed.contains(CredentialExpiredException.class)){
				request.getSession().setAttribute("messageLogin", "Sessão está sendo reiniciada");
			} else {
				request.getSession().setAttribute("messageLogin", "Erro: desconhecido...");
			}
			return NON_ROLE_TARGET_URL;
		} catch (ConcurrentLoginException e) {
			e.printStackTrace();
			return INVALID_SESSION_TARGET_URL;
		} catch(BadCredentialsException e){
			e.printStackTrace();
			return NON_ROLE_TARGET_URL;
		}
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException{

		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		HttpSession session = request.getSession(false);
		if ((session != null) || (super.getAllowSessionCreation())) {
			request.getSession().setAttribute("SPRING_SECURITY_LAST_USERNAME", TextUtils.escapeEntities(username));
		}
		setDetails(request, authRequest);
		return super.getAuthenticationManager().authenticate(authRequest);
	}

}

