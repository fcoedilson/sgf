package br.gov.ce.fortaleza.cti.sgf.util;

import java.io.File;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class RelatorioUtil {
	
	private static RelatorioUtil instance;	
	
	private RelatorioUtil(){		
	}
	
    public static synchronized RelatorioUtil getInstance() {
        if (instance == null) {
            instance = new RelatorioUtil();
        }
        return instance;
    }		

	@SuppressWarnings("deprecation")
	public String retornarJasperDir() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest req = JSFUtil.getInstance().getRequest(context);
		String propFileName = JSFUtil.getInstance().getContextParam("properties_file");
		String dirBase = req.getRealPath("");
		Object[] parametros = new Object[] { File.separator, File.separator,File.separator };
		String dir = PropertiesUtil.getInstance().getProperty(propFileName,"relat.jasper_dir", parametros);
		String jasperDir = dirBase + dir;
		return jasperDir;
	}

	@SuppressWarnings("deprecation")
	public String retornarImagensDir() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest req = JSFUtil.getInstance().getRequest(context);
		String propFileName = JSFUtil.getInstance().getContextParam("properties_file");
		String dirBase = req.getRealPath("");
		Object[] parametros = new Object[] { File.separator, File.separator, File.separator };
		String dir = PropertiesUtil.getInstance().getProperty(propFileName, "relat.imagens_dir", parametros);
		String imagensDir = dirBase + dir;
		return imagensDir;
	}

	public String retornarJasperPath(String filePropertie) {
		FacesContext context = FacesContext.getCurrentInstance();
		String propFileName = JSFUtil.getInstance().getInitParameter(context,"properties_file");
		String jasperDir = retornarJasperDir();
		Object[] parametros = new Object[] { File.separator, File.separator,File.separator };
		String arquivoJasper = PropertiesUtil.getInstance().getProperty(propFileName, filePropertie, parametros);
		String jasperPath = jasperDir + arquivoJasper;
		return jasperPath;
	}

}
