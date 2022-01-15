//package com.ttt.mar.email.dto;
//
//import com.ttt.mar.email.dto.mail.EmailMessage;
//import com.ttt.mar.email.provider.SMTPMailProvider;
//import com.ttt.mar.email.dto.mail.EmailRecipient;
//import com.ttt.mar.email.dto.mail.EmailRecipientType;
//import java.util.Collections;
//
///**
// * @author bontk
// * @created_date 16/07/2020
// */
//public class Mail {
//
//  public static void main(String[] args) {
//    EmailRecipient emailRecipient = new EmailRecipient();
//    emailRecipient.setEmail("bontk95@gmail.com");
//    emailRecipient.setEmailRecipientType(EmailRecipientType.TO);
//
//    SMTPMailProvider sendSMTPMail = new SMTPMailProvider();
//    EmailMessage emailMessage = new EmailMessage();
//    emailMessage.setFromEmail("bontk3t@thinhphat.vn");
//    emailMessage.setContent("from ");
//    emailMessage.setBrandName("TEST");
//
//    emailMessage.setRecipients(Collections.singletonList(emailRecipient));
//
//    String user = "test";
//    String pass = "Abc@4321";
//    String host = "mail.taichinhdidong.com";
////    String host = "mail.mobifi.vn";
//    String port = "465";
//
//    System.out.println("Sending...");
//    boolean result = sendSMTPMail.sendSMTPMail(emailMessage, user, pass, host, port);
//    System.out.println(result ? "Thanh cong" : "That bai");
//  }
//
//}
