package br.gov.ce.fortaleza.cti.sgf.util;

import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

@SuppressWarnings("unchecked")
public class XmlUtil {

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

	public static Element parse(String xml) {
		try {
			SAXReader sb = new SAXReader();
			Document document = sb.read(new StringReader(xml));
			return document.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Element> retrieveElements(Element element, String path) {
		if (element != null) {
			XPath xpathSelector = DocumentHelper.createXPath(path);
			return xpathSelector.selectNodes(element);
		}
		return null;
	}
}