package br.gov.ce.fortaleza.cti.sgf.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.context.SecurityContextHolder;

import br.gov.ce.fortaleza.cti.sgf.entity.User;

public class SgfUtil{

	public static final String MAIL = "suporte.sgf@@fortaleza.ce.gov.br";
	public static final String USUARIO_LOGADO = "usuarioLogado";
	public static final String SESSION_OPEN = "sessionOpened";
	public static final String CONECTED_IP = "conectedIp";
	public static final String ADMIN = "ADMIN";
	public static final Integer DEFAULT_SRID = 54004;
	

	public static boolean isUserInRole(String role) {
		return getRequest().isUserInRole(role);
	}

	public static HttpSession getSession() {
		HttpServletRequest request = getRequest();
		return request.getSession();
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static User usuarioLogado(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static Boolean isUser(User user){
		if(user.getRole().getAuthority().equals("ROLE_USER")){
			return true;
		}
		return false;
	}

	public static Boolean isOperador(User user){
		if(user.getRole().getAuthority().equals("ROLE_OPERADOR")){
			return true;
		}
		return false;
	}

	public static Boolean isChefeSetor(User user){
		if(user.getRole().getAuthority().equals("ROLE_CHEFE_SETOR")){
			return true;
		}
		return false;
	}

	public static Boolean isChefeTransporte(User user){
		if(user.getRole().getAuthority().equals("ROLE_CHEFE_TRANSP")){
			return true;
		}
		return false;
	}

	public static Boolean isCoordenador(User user){
		if(user.getRole().getAuthority().equals("ROLE_COORD_TRANSP")){
			return true;
		}
		return false;
	}

	public static Boolean isAdministrador(User user){
		if(user.getRole().getAuthority().equals("ROLE_ADMIN")){
			return true;
		}
		return false;
	}

	/**
	 * EXEMPLO: 234.456.785-XX
	 * SEJA S=S0,...,S10, A STRING Q REPRESENTA O CPF SEM OS PONTOS E O TRA�O
	 * Si S�O OS NUMERAIS, ONDE i=[0,10]
	 * SOMA1 = (S1*10 + S2*9 + S3*8 + S4*7 + S5*6 + S6*5 + S7*4 + S8*3 + S9*2) .: SOMA1 = 209
	 * V1 = SOMA1 % 11 (SE V1 < 2 V1 = 0 SEN�O V1 = SOMA2 % 11) .: V1 = 0
	 * SOMA2 = (S1*11 + S2*10 + S3*9 + S4*8 + S5*7 + S6*6 + S7*5 + S8*4 + S9*3 + V1*2) .: SOMA2 = 273
	 * V2 = SOMA2 % 11 .: V2 = 9 (SE V2 < 2 V2 = 0 SEN�O V2 = SOMA2 % 11)
	 * SE V1 = S10 E V2 = S11 ENT�O DIGITO-VERIFICADOR ok
	 * @param cpf
	 * @return
	 */
	public static Boolean cpfValidaDV(String cpf){
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		
		//if(cpf.matches("[0-9]*{0,10}")){
			//return false;
		//}
		int s1 = 0;
		int s2 = 0;
		int m1 = 10;
		int m2 = 11;
		for (int i = 0; i < (cpf.length() - 2); i++) {
			s1 += Integer.parseInt(cpf.substring(i, i+1))*m1;
			s2 += Integer.parseInt(cpf.substring(i, i+1))*m2;
			m1--;
			m2--;
		}
		s1 = (s1 % 11);

		if(s1  > 1){
			s1 = 11- s1;
		} else {
			s1 = 0;
		}

		s2 += s1*m2;
		s2 = (s2 % 11);
		if(s2 > 1){
			s2 = 11 - s2;
		} else {
			s2 = 0;
		}
		if( Integer.parseInt(cpf.substring(cpf.length() - 2, cpf.length() - 1)) == s1 && 
				Integer.parseInt(cpf.substring(cpf.length() - 1, cpf.length())) == s2 ){
			return  true;
		} else {
			return false;
		}
	}

	public static String md5(String valor) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(valor.getBytes()));
			String s = hash.toString(16);
			if (s.length() %2 != 0) s = "0" + s;
			return s;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static synchronized String sendMailToUser(String mail, User user) throws Exception{
		String msg = "Sr. " + user.getPessoa().getNome() + "\n seu login atual é " + 
		user.getLogin() + "\n sua senha atual é:" + user.getPassword();
		return MailUtil.sendEmail(mail, "recuperação de login e senha", msg);
	}
}