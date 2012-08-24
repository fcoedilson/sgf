package br.gov.ce.fortaleza.cti.sgf.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.cti.sgf.util.JSFUtil;
import br.gov.ce.fortaleza.cti.sgf.util.SgfUtil;

/**
 * Bean que gerencia o download dos manuais do sistema
 * 
 * @author lafitte
 * @since 15/04/2010
 * 
 */
@Scope("session")
@Component("downloadBean")
public class DownloadBean extends BaseBean {
	private String nomeArquivo;

	/**
	 * Construtor default, verifica qual o perfil do usu�rio e define o nome
	 * do arquivo do manual referente ao perfil.
	 */
	public DownloadBean() {

		if (SgfUtil.usuarioLogado().getRole().getAuthority().equals(
		"ROLE_CHEFE_SETOR")) {
			this.nomeArquivo = "ROLE_CHEFE_SETOR.pdf";
		}

		if (SgfUtil.usuarioLogado().getRole().getAuthority().equals(
		"ROLE_ADMIN")) {
			this.nomeArquivo = "ROLE_ADMIN.pdf";
		}

		if (SgfUtil.usuarioLogado().getRole().getAuthority().equals(
		"ROLE_COORD_TRANSP")) {
			this.nomeArquivo = "ROLE_COORD_TRANSP.pdf";
		}

		if (SgfUtil.usuarioLogado().getRole().getAuthority().equals(
		"ROLE_CHEFE_TRANSP")) {
			this.nomeArquivo = "ROLE_CHEFE_TRANSP.pdf";
		}

		if (SgfUtil.usuarioLogado().getRole().getAuthority().equals(
		"ROLE_OPERADOR")) {
			this.nomeArquivo = "ROLE_OPERADOR.pdf";
		}
	}

	/**
	 * M�todo chamado da p�gina para realizar o download do arquivo
	 */
	public String download() throws IOException{
		return downloadFile(this.nomeArquivo, "/manuais/", "application/pdf", FacesContext.getCurrentInstance());
	}

	/**
	 * M�todo que realiza todo o fluxo necess�rio para o download do arquivo.
	 * 
	 * @param fileName
	 * @param fileLocation
	 * @param mimeType
	 * @param facesContext
	 */
	public static synchronized String downloadFile(String fileName,
			String fileLocation, String mimeType, FacesContext facesContext) throws IOException{

		ExternalContext context = facesContext.getExternalContext(); // Context
		HttpServletRequest request = (HttpServletRequest) context.getRequest();

		String path = request.getSession().getServletContext().getRealPath("manuais/"+fileName); // Localiza��o do arquivo
		File file = new File(path); // Objeto arquivo mesmo :)

		if(file.length() <= 0){
			//throw new IOException();
			JSFUtil.getInstance().addErrorMessage("msg.error.download");
			return FAIL;
		} 

		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=\""+ fileName + "\""); // aqui eu seto o header e o nome que vai
		// aparecer na hora do donwload
		response.setContentLength((int) file.length()); // O tamanho do arquivo
		response.setContentType(mimeType); // e obviamente o tipo

		try {
			FileInputStream in = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();

			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			JSFUtil.getInstance().addErrorMessage("msg.error.download");
			ex.printStackTrace();
		}
		
		return null;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

}
