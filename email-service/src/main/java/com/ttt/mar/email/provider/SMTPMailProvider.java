package com.ttt.mar.email.provider;

import com.sun.istack.ByteArrayDataSource;
import com.ttt.mar.email.dto.mail.EmailAttachment;
import com.ttt.mar.email.dto.mail.EmailMessage;
import com.ttt.mar.email.dto.mail.EmailRecipient;
import com.ttt.mar.email.dto.mail.EmailRecipientType;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.stereotype.Component;

@Component
public class SMTPMailProvider {

  private static final String SEND_EMAIL_TO_CLIENT_FAIL = "SEND_FAILED";
  private static final String SEND_EMAIl_TO_CLIENT_SUCCESS = "SENT";

  public String sendSMTPMail(EmailMessage emailMessage) {
    Properties props = System.getProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.host", emailMessage.getHost());
    props.put("mail.smtp.port", emailMessage.getPort());
//    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    Authenticator auth = new Authenticator() {
      //override the getPasswordAuthentication method
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(emailMessage.getUserName(), emailMessage.getPassword());
      }
    };
    Session session = Session.getInstance(props, auth);
    session.setDebug(true);
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(emailMessage.getFromEmail(), emailMessage.getBrandName()));
      message.setSubject(emailMessage.getSubject(), "UTF-8");

      if (emailMessage.getRecipients() != null) {
        for (EmailRecipient emailRecipient : emailMessage.getRecipients()) {
          Message.RecipientType recipientType;
          if (EmailRecipientType.BCC.equals(emailRecipient.getEmailRecipientType())) {
            recipientType = Message.RecipientType.BCC;
          } else if (EmailRecipientType.CC.equals(emailRecipient.getEmailRecipientType())) {
            recipientType = Message.RecipientType.CC;
          } else {
            recipientType = Message.RecipientType.TO;
          }
          message.addRecipient(recipientType,
              new InternetAddress(emailRecipient.getEmail()));
        }
      }
      MimeMultipart msg_body = new MimeMultipart("alternative");
      MimeBodyPart wrap = new MimeBodyPart();
      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(emailMessage.getBody(), "text/html; charset=UTF-8");

      msg_body.addBodyPart(htmlPart);
      wrap.setContent(msg_body);

      MimeMultipart msg = new MimeMultipart("mixed");

      message.setContent(msg);
      msg.addBodyPart(wrap);
      if (emailMessage.getEmailAttachments() != null) {
        for (EmailAttachment emailAttachment : emailMessage.getEmailAttachments()) {
          MimeBodyPart att = new MimeBodyPart();
          DataSource fds = new ByteArrayDataSource(emailAttachment.getContent(),
              emailAttachment.getMimeType());
          att.setDataHandler(new DataHandler(fds));
          att.setFileName(emailAttachment.getName());

          msg.addBodyPart(att);
        }
      }

      Transport transport = session.getTransport();
      transport.connect(emailMessage.getHost(), emailMessage.getUserName(), emailMessage.getPassword());

      Transport.send(message, message.getAllRecipients());

      return SEND_EMAIl_TO_CLIENT_SUCCESS;
    } catch (Exception ex) {
      ex.printStackTrace();
      return SEND_EMAIL_TO_CLIENT_FAIL;
    }
  }
}
