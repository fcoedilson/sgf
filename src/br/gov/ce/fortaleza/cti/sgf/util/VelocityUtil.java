package br.gov.ce.fortaleza.cti.sgf.util;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityUtil {

	public static byte[] merge(String templateName, String names[], Object[] values) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",ClasspathResourceLoader.class.getName());
        ve.init(p);
        VelocityContext context = new VelocityContext();
        Template t = ve.getTemplate("resources/" + templateName);
        for (int i = 0; i < names.length; i++) {
        	context.put(names[i], values[i]);
		}
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString().getBytes("UTF-8");
    }

	public static String mergeAsString(String templateName, String names[], Object[] values) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",ClasspathResourceLoader.class.getName());
        ve.init(p);
        VelocityContext context = new VelocityContext();
        Template t = ve.getTemplate("resources/" + templateName);
        for (int i = 0; i < names.length; i++) {
        	context.put(names[i], values[i]);
		}
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
	}
}
