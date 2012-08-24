package br.gov.ce.fortaleza.cti.sgf.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.postgis.Point;

import br.gov.ce.fortaleza.cti.sgf.entity.Transmissao;
import br.gov.ce.fortaleza.cti.sgf.util.DateUtil;
import br.gov.ce.fortaleza.cti.sgf.util.XmlUtil;

public class ArenaService {

	public static final Logger log = Logger.getLogger(ArenaService.class);

	private HttpClient client;

	public ArenaService() {
		client = new HttpClient();
		client.getHostConfiguration().setHost("174.142.214.78",80,"http");
	}

	public static ArenaService login() throws Exception {

		ArenaService connection = new ArenaService();
		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<USER>");
		xml.append("  <LOGIN>pmf</LOGIN>");
		xml.append("  <PASSWORD>pmf</PASSWORD>");
		xml.append("  <FORCELOGIN>0</FORCELOGIN>");
		xml.append("</USER>");
		String result = connection.requestHttpService("login", xml.toString());
		if (result != null && result.contains("ERROR")) {
			log.info("Autenticação: ERROR ...");
			return null;
		}
		log.info("Autenticação: OK...");
		return connection;
	}

	public List<Transmissao> retrieveTransmissions(Date ini, Date fim, Integer veiculoIdArena, Integer veiculoIdSgf) throws Exception {

		List<Transmissao> transmissoes = new ArrayList<Transmissao>();
		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);

		xml.append("<COURSES>");
		xml.append("	<VEHICLES>");
		xml.append("		<VEHICLE_ID>" + veiculoIdArena + "</VEHICLE_ID>");
		xml.append("	</VEHICLES>");
		xml.append("	<START>" + DateUtil.parseArenaDateAsString(ini) + "</START>");
		xml.append("	<END>" + DateUtil.parseArenaDateAsString(fim) + "</END>");
		xml.append("	<PERIOD_PER_DAY>1</PERIOD_PER_DAY>");
		xml.append("	<PERIOD_START>" + DateUtil.parseTimeAsString(ini) + "</PERIOD_START>");
		xml.append("	<PERIOD_END>" + DateUtil.parseTimeAsString(fim) + "</PERIOD_END>");
		xml.append("	<DECREASING>0</DECREASING>");
		xml.append("	<SHOW_FIELDS>");
		xml.append("		<ID>1</ID>");
		xml.append("		<Y>1</Y>");
		xml.append("		<X>1</X>");
		xml.append("		<DATE_TIME>21</DATE_TIME>");
		xml.append("		<VEL>1</VEL>");
		xml.append("		<IGNITION>1</IGNITION>");
		xml.append("		<IN1>1</IN1>");
		xml.append("		<IN2>1</IN2>");
		xml.append("		<IN3>1</IN3>");
		xml.append("		<IN4>1</IN4>");
		xml.append("		<ODOMETER>1</ODOMETER>");
		xml.append("		<HOURMETER>1</HOURMETER>");
		xml.append("		<TEMPERATURE>1</TEMPERATURE>");
		xml.append("		<COUNT1>1</COUNT1>");
		xml.append("		<COUNT2>1</COUNT2>");
		xml.append("		<COUNT3>1</COUNT3>");
		xml.append("	</SHOW_FIELDS>");
		xml.append("</COURSES>");
		
		String result = requestHttpService("mapcourses", xml.toString());
		if (result != null && result.contains("ERROR")) {
			xml = new StringBuffer(XmlUtil.XML_HEADER);
			xml.append("<USER>");
			xml.append("<LOGIN>pmf</LOGIN>");
			xml.append("<PASSWORD>pmf</PASSWORD>");
			xml.append("<FORCELOGIN>0</FORCELOGIN>");
			xml.append("</USER>");
			requestHttpService("login", xml.toString());
			throw new RuntimeException(result);
		}

		Element root = XmlUtil.parse(result);
		List<Element> elements = XmlUtil.retrieveElements(root, "/COURSES/VEHICLE");
		
		for (Element element : elements) {
			List<Element> positions = XmlUtil.retrieveElements(root, "/COURSES/VEHICLE[ID = " + element.elementText("ID") + "]/POSITION");
			for (Element position : positions) {
				Transmissao t = new Transmissao();
				t.setVeiculoId(veiculoIdSgf);
				t.setIgnicao(position.elementText("IGNITION").equals("0") ? false : true);
				t.setVelocidade(Float.valueOf(position.elementText("VEL")));
				t.setX(Double.valueOf(position.elementText("X")));
				t.setY(Double.valueOf(position.elementText("Y")));
				t.setGeometry(new Point(Double.valueOf(position.elementText("X")), Double.valueOf(position.elementText("Y"))));
				t.setDataTransmissao(DateUtil.parseStringAsDate("yyyy-MM-dd HH:mm:ss", position.elementText("DATE_TIME")));
				transmissoes.add(t);
			}
		}

		return transmissoes;
	}

	public void showVehicles() throws Exception{
		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<FILTER>");
		xml.append("<ID>4774</ID>");
		xml.append("</FILTER>");
		String result = requestHttpService("showvehicles", xml.toString());
		Element root = XmlUtil.parse(result);
		List<Element> elements = XmlUtil.retrieveElements(root, "/ROUTES/ROUTE");
		for (Element element : elements) {
			System.out.println(element.elementText("CLIENT_ID"));
		}
	}

	public void showRoutes() throws Exception{
		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<FILTER>");
		xml.append("</FILTER>");
		String result = requestHttpService("showroutes", xml.toString());
		Element root = XmlUtil.parse(result);
		List<Element> elements = XmlUtil.retrieveElements(root, "/ROUTES/ROUTE");
		for (Element element : elements) {
			System.out.println(element.elementText("ID"));
		}
	}

	public void showClients(Integer id) throws Exception{
		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<FILTER>");
		xml.append("<ID>"+ id +"</ID>");
		xml.append("</FILTER>");
		String result = requestHttpService("showclients", xml.toString());
		Element root = XmlUtil.parse(result);
		List<Element> elements = XmlUtil.retrieveElements(root, "/CLIENTS/CLIENT");
		for (Element element : elements) {
			System.out.println(element.elementText("ID"));
			System.out.println(element.elementText("DESC"));
			System.out.println(element.elementText("DOCUMENT"));
			System.out.println(element.elementText("ADDRESS"));
			System.out.println(element.elementText("NUMBER"));
			System.out.println(element.elementText("DISTRICT"));
			System.out.println(element.elementText("CITY"));
		}
	}


	public void describeVehicle(Integer id) throws Exception{

		StringBuffer xml = new StringBuffer(XmlUtil.XML_HEADER);
		xml.append("<ITEM>");
		xml.append("	<ID>" + id + "</ID>");
		xml.append("</ITEM>");

		String result = requestHttpService("describevehicle", xml.toString());
		Element root = XmlUtil.parse(result);

		List<Element> elements = XmlUtil.retrieveElements(root, "/VEHICLE");

		for (Element element : elements) {
			System.out.println(element.elementText("ID"));
			System.out.println(element.elementText("EQUIPMENT_SERIAL"));
			System.out.println(element.elementText("DESC"));
			System.out.println(element.elementText("CLIENT_ID"));
			System.out.println(element.elementText("MODEL"));
			System.out.println(element.elementText("COLOR"));
			System.out.println(element.elementText("YEAR"));
		}

	}

	public String requestHttpService(String url, String data) throws Exception {

		String result = null;
		PostMethod method = new PostMethod("/scripts/arenawebservice.dll/" + url);
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

		} finally {
			method.releaseConnection();
		}
		return result.toString();
	}
}