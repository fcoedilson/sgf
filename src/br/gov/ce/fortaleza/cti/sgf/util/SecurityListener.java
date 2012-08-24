package br.gov.ce.fortaleza.cti.sgf.util;

import java.security.Principal;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.gov.ce.fortaleza.cti.sgf.entity.LogUsuario;
import br.gov.ce.fortaleza.cti.sgf.entity.User;
import br.gov.ce.fortaleza.cti.sgf.service.LogUsuarioService;
import br.gov.ce.fortaleza.cti.sgf.service.UsuarioService;

public class SecurityListener implements ServletRequestListener {

	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
		Principal principal = request.getUserPrincipal();
		HttpSession session = request.getSession();
		if (principal != null && session.getAttribute(SgfUtil.USUARIO_LOGADO) == null) {
			String[] properties = principal.getName().split("");
			Integer id = Integer.parseInt(properties[0]);
			User usuario = retrieveUsuario(id, session);
			session.setAttribute(SgfUtil.USUARIO_LOGADO, usuario);
		}
	}

	public void requestDestroyed(ServletRequestEvent event) {}

	private User retrieveUsuario(Integer id, HttpSession request) {
		UsuarioService service = getUsuarioService(UsuarioService.class, request);
		return service.retrieve(id);
	}

	public static void createLogUsuario(User usuario, HttpSession request) {
		try {
			LogUsuarioService service = getLogUsuarioService(LogUsuarioService.class, request);
			UsuarioService usuarioService = getUsuarioService(UsuarioService.class, request);
			usuario = usuarioService.retrieve(usuario.getCodPessoaUsuario());

			if(usuario.getLogado() == false){
				LogUsuario log = new LogUsuario();
				log.setUsuario(usuario);
				log.setDataLogin(DateUtil.getDateNow());
				log.setIpcon(SgfUtil.getRequest().getRemoteAddr());
				service.save(log);

				usuario.setLogado(true);
				usuarioService.update(usuario);
				request.setAttribute(SgfUtil.CONECTED_IP, SgfUtil.getRequest().getRemoteAddr());
				SgfUtil.getSession().setAttribute(SgfUtil.SESSION_OPEN, Boolean.TRUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateLogUsuario(User usuario, HttpSession request) {
		try {
			LogUsuarioService service = getLogUsuarioService(LogUsuarioService.class, request);
			UsuarioService usuarioService = getUsuarioService(UsuarioService.class, request);
			usuario = usuarioService.retrieve(usuario.getCodPessoaUsuario());
			if(usuario.getLogado()){
				LogUsuario log = service.findUltimoLogin(usuario.getCodPessoaUsuario());
				log.setUsuario(usuario);
				log.setDataLogout(DateUtil.getDateNow());
				service.update(log);
				usuario.setLogado(false);
				usuarioService.update(usuario);
				SgfUtil.getSession().setAttribute(SgfUtil.SESSION_OPEN, Boolean.FALSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	protected static <E> E getUsuarioService(Class<E> clazz, HttpSession session) {
		ApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
		return (E) wac.getBean("usuarioService");
	}

	@SuppressWarnings("unchecked")
	protected static <E> E getLogUsuarioService(Class<E> clazz, HttpSession session) {
		ApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
		return (E) wac.getBean("logUsuarioService");
	}
}