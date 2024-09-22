package br.com.api.projeto.model.services;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Value("${email.username}")
	private String usernameEmail;
	
	@Value("${email.password}")
	private String passwordEmail;

	public String sendEmail(String destiny,String user) {
		
		String host = "smtp.gmail.com";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		
		 Session session = Session.getInstance(properties, new Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	             return new PasswordAuthentication(usernameEmail, passwordEmail);
	         }
	     });
		 
		 try {
			 Message message = new MimeMessage(session);
			 message.setFrom(new InternetAddress(usernameEmail));
			 message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destiny));
			 message.setSubject("Cadastro");
			 message.setText("Bem-vindo(a) "+user+", se você recebeu esse e-mail significa que seu cadastro foi concluido!");
			 
			 Transport.send(message);
			 return "Email enviado";
		 }catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public String changePassword(String email,String user,String password) {
		
		String host = "smtp.gmail.com";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		
		 Session session = Session.getInstance(properties, new Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	             return new PasswordAuthentication(usernameEmail, passwordEmail);
	         }
	     });
		 try {
			 Message message = new MimeMessage(session);
			 message.setFrom(new InternetAddress(usernameEmail));
			 message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
			 message.setSubject("Redefinição de senha");
			 message.setText("Você solicitou a redefinição de senha, essa é a sua nova senha "+password+
					 "\n Não compartilhe-a com ninguém!");
			 
			 Transport.send(message);
			 return "Foi enviado uma mensagem para o seu e-mail "+email;
		 }catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	


}
