package br.gov.ce.fortaleza.cti.sgf.util;

import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Mail {

	public static String FROM = "sgfadmin@cti.fortaleza.ce.gov.br";
	public static String TO = "fcoedilson@gmail.com;francisco.edilson@cti.fortaleza.ce.gov.br";

	@SuppressWarnings("unchecked")
	public static void sendMailSsl(String from, String listTo, String subject, String body){
		Properties props = new Properties();
		props.put("mail.smtp.host", "172.31.2.5");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication(){ 
				return new PasswordAuthentication("sgfadmin@cti.fortaleza.ce.gov.br","!!SgTp324");	
			}
		});
		try {
			Vector localVector = new Vector();
			String[] list = listTo.split(";");
			for (int k = 0; k < list.length; k++) {
				InternetAddress localAddress = new InternetAddress();
				localAddress.setAddress(list[k]);
				localVector.addElement(localAddress);
			}
			InternetAddress[] internetAddressList = new InternetAddress[localVector.size()];
			localVector.copyInto(internetAddressList);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, internetAddressList);
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
