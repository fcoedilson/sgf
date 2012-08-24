package br.gov.ce.fortaleza.cti.sgf.util;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.gov.ce.fortaleza.cti.sgf.entity.User;

import com.sun.xml.internal.ws.util.ByteArrayDataSource;

public class MailUtil{

	public static final String E_MAIL = "fcoedilson@gmail.com";

	public User user = SgfUtil.usuarioLogado();

	public static String sendEmail(String to, String subject, String msg) throws Exception  {

		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator authenticator = new Authenticator() {

			public PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("edilson.cti@gmail.com", "97turdrub@234");
			}
		};

		Session session = Session.getInstance(props, authenticator);

		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(MailUtil.E_MAIL));
		mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(to)});
		mimeMessage.setSubject(subject);
		mimeMessage.setText(msg);
		mimeMessage.setSentDate(new Date());

		Transport.send(mimeMessage);
		return "SUCCESS";
	}

	public static String sendEmailWithFile(String to, String subject, String fileName, String msg, byte[] bytes, String mime) throws Exception  {

		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");

		Authenticator authenticator = new Authenticator() {

			public PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("edilson.cti@gmail.com", "97turdrub@234");
			}
		};

		Session session = Session.getInstance(props, authenticator);
		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(MailUtil.E_MAIL));
		mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(to)});
		mimeMessage.setSubject(subject);
		MimeBodyPart texto = new MimeBodyPart();
		texto.setText(msg);
		MimeBodyPart anexo = new MimeBodyPart();
		ByteArrayDataSource fds = new ByteArrayDataSource(bytes, mime);
		anexo.setDataHandler(new DataHandler(fds));
		anexo.setFileName(fileName);
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(texto);
		mp.addBodyPart(anexo);
		mimeMessage.setContent(mp);
		mimeMessage.setSentDate(new Date());
		Transport.send(mimeMessage);

		return "SUCCESS";
	}
}