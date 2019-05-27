package smartspace.data.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SendEmailTLS {
	
	private String username ; // = "smartVisionAmit@gmail.com";
    private String password ; // = "smartvision123";
	private String smartspace;
    
    @Value("${smartspace.emailUser:smartspace@gmail.com}")
	public void setUsername(String username) {
		this.username = username;
	}
    
    @Value("${smartspace.emailPassword:12345678}")
	public void setPassword(String password) {
		this.password = password;
	}
    
    @Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

    public void sendMail(String recipientEmail , String messageSubject , String messageText ) {

        if(messageSubject == null)
        	messageSubject = "This is some mail from " + smartspace;

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject(messageSubject);
            message.setText(messageText);

            Transport.send(message);

            System.err.println("Done sending mail to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}