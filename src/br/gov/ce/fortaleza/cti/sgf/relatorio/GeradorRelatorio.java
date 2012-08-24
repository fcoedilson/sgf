package br.gov.ce.fortaleza.cti.sgf.relatorio;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.hibernate.Session;

public class GeradorRelatorio {
	
	@SuppressWarnings("deprecation")
	public static byte[] gerarPdfBD(Map<?, ?> parametros, String jasperPath)
			throws IOException, JRException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sgf");
		EntityManager em = factory.createEntityManager();
		Session session = (Session) em.getDelegate();
		Connection con = session.connection();
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperPath);
		byte array[] = JasperRunManager.runReportToPdf(jasperReport, (Map<String, Object>) parametros, con);
		return array;
	}

	public static byte[] gerarPdfCollection(Map<?, ?> parametros, Collection<?> colecao, 
			String jasperPath) throws IOException, JRException{
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(colecao);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperPath);
		byte array[] = JasperRunManager.runReportToPdf(jasperReport,(Map<String, Object>) parametros, ds);
		return array;
	}
	
}
