package br.gov.ce.fortaleza.cti.sgf.webservice;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.ce.fortaleza.cti.sgf.entity.Veiculo;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.VelocityUtil;
import br.gov.ce.fortaleza.cti.sgf.util.XmlUtil;

public class GpsService {

	private static final Log logger = LogFactory.getLog(GpsService.class);

	private HttpClient client;
	private String sessionId;

	public class Comando {

		private int saida1;
		private int saida2;
		private int saida3;
		private int saida4;
		private int saida5;
		private int saida6;
		private int saida7;
		private int saida8;
		private Veiculo[] veiculos;

		public Comando() {
			this.saida1 = 0;
			this.saida2 = 0;
			this.saida3 = 0;
			this.saida4 = 0;
			this.saida5 = 0;
			this.saida6 = 0;
			this.saida7 = 0;
			this.saida8 = 0;
		}

		public int getSaida1() {
			return saida1;
		}

		public void setSaida1(int saida1) {
			this.saida1 = saida1;
		}

		public int getSaida2() {
			return saida2;
		}

		public void setSaida2(int saida2) {
			this.saida2 = saida2;
		}

		public int getSaida3() {
			return saida3;
		}

		public void setSaida3(int saida3) {
			this.saida3 = saida3;
		}

		public int getSaida4() {
			return saida4;
		}

		public void setSaida4(int saida4) {
			this.saida4 = saida4;
		}

		public int getSaida5() {
			return saida5;
		}

		public void setSaida5(int saida5) {
			this.saida5 = saida5;
		}

		public int getSaida6() {
			return saida6;
		}

		public void setSaida6(int saida6) {
			this.saida6 = saida6;
		}

		public int getSaida7() {
			return saida7;
		}

		public void setSaida7(int saida7) {
			this.saida7 = saida7;
		}

		public int getSaida8() {
			return saida8;
		}

		public void setSaida8(int saida8) {
			this.saida8 = saida8;
		}

		public Veiculo[] getVeiculos() {
			return veiculos;
		}

		public void setVeiculos(Veiculo[] veiculos) {
			this.veiculos = veiculos;
		}

	}
	
	public static void main(String[] args){
		
		login();
	}

	private GpsService() {
		client = new HttpClient();
		client.getHostConfiguration().setHost("174.142.214.78", 80, "http");
	}

	public static GpsService login() {

		GpsService connection = new GpsService();
		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<USER>");
		xml.append("<LOGIN>login</LOGIN>");
		xml.append("<PASSWORD>senha</PASSWORD>");
		xml.append("<FORCELOGIN>1</FORCELOGIN>");
		xml.append("</USER>");

		String result = connection.requestHttpService("/login", xml.toString());
		logger.info(result);
		return connection;
	}

	public static GpsService loginMaxTrack() {

		GpsService connection = new GpsService();
		connection.client.getHostConfiguration().setHost("201.49.44.41", 80, "http");
		connection.sessionId = null;

		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<USER>");
		xml.append("  <LOGIN>master</LOGIN>");
		xml.append("  <PASSWORD>chk@7982</PASSWORD>");
		xml.append("  <FORCELOGIN>1</FORCELOGIN>");
		xml.append("  <JUST_VERIFY>0</JUST_VERIFY>");
		xml.append("</USER>");

		try {
			String result = connection.requestHttpService("/login?WebBrokerSessionMac=" + getMacAddress(), xml.toString());

			if (result.indexOf("<ERROR>") > 0) {
				result = connection.requestHttpService("/login?WebBrokerSessionMac=" + getMacAddress(), xml.toString());
				if (result.indexOf("<ERROR>") > 0) {
					throw new Exception(result);
				}
			}

			int ini = result.indexOf("<SESSION_ID>");
			int fim = result.indexOf("</SESSION_ID>");

			connection.sessionId = result.substring(ini + "<SESSION_ID>".length(), fim);

			logger.info(result);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return connection;
	}

	private void logoffMaxTrack() {
		String result = requestHttpService("/logoff?WebBrokerSessionId=" + sessionId, "");
		if (result.contains("<ERROR>")) {
			logger.info(result);
		}
	}

	public void bloquearVeiculo(Veiculo veiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);

		Comando comando = new Comando();
		comando.setSaida1(1);
		comando.setVeiculos(new Veiculo[]{veiculo});

		try {
			xml.append(
					VelocityUtil.mergeAsString(
							"maxtrack/mtcoutputcontrol.vm",
							new String[]{"comando"},
							new Object[]{comando}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (sessionId != null) {
			String result = requestHttpService("/mtcoutputcontrol?WebBrokerSessionId=" + sessionId, xml.toString());
			logger.info(result);
		}

		logoffMaxTrack();
	}

	public void bloquearVeiculo(Integer codigoExternoVeiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("	<SERIAL>1</SERIAL>");
		xml.append("	<TEXT>CFG:1000[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void desbloquearVeiculo(Veiculo veiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);

		Comando comando = new Comando();
		comando.setSaida1(0);
		comando.setVeiculos(new Veiculo[]{veiculo});

		try {
			xml.append(
					VelocityUtil.mergeAsString(
							"maxtrack/mtcoutputcontrol.vm",
							new String[]{"comando"},
							new Object[]{comando}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (sessionId != null) {
			String result = requestHttpService("/mtcoutputcontrol?WebBrokerSessionId=" + sessionId, xml.toString());
			logger.info(result);
		}

		logoffMaxTrack();
	}

	public void desbloquearVeiculo(Integer codigoExternoVeiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("	<SERIAL>1</SERIAL>");
		xml.append("	<TEXT>CFG:0000[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void bloquearSireneVeiculo(Veiculo veiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);

		Comando comando = new Comando();
		comando.setSaida1(1);
		comando.setSaida3(1);
		comando.setVeiculos(new Veiculo[]{veiculo});

		try {
			xml.append(
					VelocityUtil.mergeAsString(
							"maxtrack/mtcoutputcontrol.vm",
							new String[]{"comando"},
							new Object[]{comando}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (sessionId != null) {
			String result = requestHttpService("/mtcoutputcontrol?WebBrokerSessionId=" + sessionId, xml.toString());
			logger.info(result);
		}

		logoffMaxTrack();
	}

	public void bloquearSireneVeiculo(Integer codigoExternoVeiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("	<SERIAL>1</SERIAL>");
		xml.append("	<TEXT>CFG:1010[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void desbloquearMaisSireneVeiculo(Veiculo veiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);

		Comando comando = new Comando();
		comando.setSaida1(0);
		comando.setSaida3(0);
		comando.setVeiculos(new Veiculo[]{veiculo});

		try {
			xml.append(
					VelocityUtil.mergeAsString(
							"maxtrack/mtcoutputcontrol.vm",
							new String[]{"comando"},
							new Object[]{comando}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (sessionId != null) {
			String result = requestHttpService("/mtcoutputcontrol?WebBrokerSessionId=" + sessionId, xml.toString());
			logger.info(result);
		}

		logoffMaxTrack();
	}

	public void desbloquearSireneVeiculo(Integer codigoExternoVeiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("	<SERIAL>1</SERIAL>");
		xml.append("	<TEXT>CFG:0000[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>	");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void acionarSirene(Veiculo veiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);

		Comando comando = new Comando();
		comando.setSaida3(1);
		comando.setVeiculos(new Veiculo[]{veiculo});

		try {
			xml.append(
					VelocityUtil.mergeAsString(
							"maxtrack/mtcoutputcontrol.vm",
							new String[]{"comando"},
							new Object[]{comando}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (sessionId != null) {
			String result = requestHttpService("/mtcoutputcontrol?WebBrokerSessionId=" + sessionId, xml.toString());
			logger.info(result);
		}

		logoffMaxTrack();
	}

	public void acionarSirene(Integer codigoExternoVeiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("	<SERIAL>1</SERIAL>");
		xml.append("	<TEXT>CFG:0010[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>	");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void desligarSirene(Veiculo veiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);

		Comando comando = new Comando();
		comando.setSaida3(0);
		comando.setVeiculos(new Veiculo[]{veiculo});

		try {
			xml.append(
					VelocityUtil.mergeAsString(
							"maxtrack/mtcoutputcontrol.vm",
							new String[]{"comando"},
							new Object[]{comando}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (sessionId != null) {
			String result = requestHttpService("/mtcoutputcontrol?WebBrokerSessionId=" + sessionId, xml.toString());
			logger.info(result);
		}

		logoffMaxTrack();
	}

	public void desligarSirene(Integer codigoExternoVeiculo) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("	<SERIAL>1</SERIAL>");
		xml.append("	<TEXT>CFG:0000[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>	");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void habilitarEscuta(Integer codigoExternoVeiculo, String telefoneEscuta) {

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>");
		xml.append("<SERIAL>1</SERIAL>");
		xml.append("<TEXT>VIV:" + telefoneEscuta + "[</TEXT>");
		xml.append("<ATTEMPTS>1</ATTEMPTS>");
		xml.append("<VEHICLES>");
		xml.append("<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>");
		xml.append("</VEHICLES>");
		xml.append("</COMMAND_DATA_TERMINAL>");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	public void enviarMensagemTexto(Integer codigoExternoVeiculo, String mensagem) {

		String dataHora = DateUtil.parseAsString("dd/MM/yyyy HH:mm:ss", DateUtil.getDateNow());

		StringBuilder xml = new StringBuilder(XmlUtil.XML_HEADER);
		xml.append("<COMMAND_DATA_TERMINAL>\n");
		xml.append("	<SERIAL>1</SERIAL>\n");
		xml.append("	<TEXT>MSG:,00,02,MS," + "ID_MENSAGEM" + "," + dataHora + "," + mensagem + "[</TEXT>");
		xml.append("	<ATTEMPTS>1</ATTEMPTS\n>");
		xml.append("	<VEHICLES>\n");
		xml.append("		<VEHICLE_ID>" + codigoExternoVeiculo + "</VEHICLE_ID>\n");
		xml.append("	</VEHICLES>\n");
		xml.append("</COMMAND_DATA_TERMINAL>\n");

		requestHttpService("/mtcdataterminalfreetext", xml.toString());
	}

	private String requestHttpService(String url, String data) {

		String result = null;
		PostMethod method = new PostMethod("/scripts/ArenaWebService.dll" + url);

		try {
			method.setRequestEntity(new ByteArrayRequestEntity(data.getBytes()));
			if (client.executeMethod(method) == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[10240];
				int bytes = -1;
				while ((bytes = in.read(buffer, 0, 10240)) > 0 ) {
					out.write(buffer, 0, bytes);
				}
				result = out.toString();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return (result == null ? null : result.toString());
	}

	private static String getMacAddress() {
		String result = "00-1C-C0-EA-92-26";
		return result;
	}


}