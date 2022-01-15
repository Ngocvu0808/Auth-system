package com.ttt.mar.email.service.impl;

import com.google.gson.Gson;
import com.ttt.mar.email.config.ServiceMessageCode;
import com.ttt.mar.email.controller.EmailController;
import com.ttt.mar.email.dto.mail.EmailAttachment;
import com.ttt.mar.email.dto.mail.EmailContentRequestDto;
import com.ttt.mar.email.dto.mail.EmailMessage;
import com.ttt.mar.email.dto.mail.EmailRecipient;
import com.ttt.mar.email.dto.mail.EmailRecipientType;
import com.ttt.mar.email.dto.mail.EmailRequestDto;
import com.ttt.mar.email.dto.mail.EmailRequestSendDto;
import com.ttt.mar.email.dto.notifyhistory.InfoEmailRequestFromNotifyServiceDto;
import com.ttt.mar.email.entities.EmailConfig;
import com.ttt.mar.email.entities.EmailPublisher;
import com.ttt.mar.email.repositories.EmailConfigRepository;
import com.ttt.mar.email.repositories.TypeSendRepository;
import com.ttt.mar.email.service.iface.AsyncService;
import com.ttt.mar.email.service.iface.MailService;
import com.ttt.mar.email.service.iface.kafka.producer.ProducerService;
import com.ttt.mar.email.utils.EmailUtil;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bontk
 * @created_date 17/07/2020
 */

@Service
public class MailServerImpl implements MailService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(EmailController.class);

  @Autowired
  private TypeSendRepository typeSendRepository;

  @Autowired
  private ProducerService<EmailRequestSendDto> producerService;

  @Autowired
  private EmailConfigRepository emailConfigRepository;

  public Map<String, Object> receiveEmail(InfoEmailRequestFromNotifyServiceDto requestDto)
      throws Exception {
    EmailRequestSendDto emailRequestSendDto = new EmailRequestSendDto();
    // Kiểm tra điều thông tin truyền vào hợp lệ.
    List<String> listEmailInvalid = validateNotifyHistoryAndGetEmailInvalid(requestDto,
        emailRequestSendDto);
    // Kiểm tra Email Config theo accountCode tồn tại.
    EmailConfig emailConfig = emailConfigRepository
        .findByAccountCodeAndIsDeletedFalse(requestDto.getAccountCode());
    if (emailConfig == null) {
      throw new ResourceNotFoundException("Email-Config Not Found",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_CONFIG_NOT_FOUND);
    }
    logger.info("Email-Config Id: [{}]", emailConfig.getId());
    EmailPublisher emailPublisher = emailConfig.getEmailPublisher();
    if (emailPublisher == null || emailPublisher.getIsDeleted()) {
      throw new ResourceNotFoundException("Email-Publisher Not Found",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_PUBLISHER_NOT_FOUND);
    }
    logger.info("Email-Publisher Id: [{}]", emailPublisher.getId());
//    if (StringUtils.isNotBlank(requestDto.getBrandName())) {
//      emailMessage.setBrandName(requestDto.getBrandName());
//    } else {
//      emailMessage.setBrandName(emailConfig.getBrandName());
//    }
//    emailMessage.setUserName(emailConfig.getUsername());
//    emailMessage.setPassword(emailConfig.getPassword());
//    emailMessage.setFromEmail(emailConfig.getSender());
//    emailMessage.setHost(emailPublisher.getHost());
//    emailMessage.setPort(String.valueOf(emailPublisher.getPort()));
//    emailMessage.setEmailProtocol(emailPublisher.getProtocol());
//    logger.info("Email-Message [{}]", emailMessage);

    Map<String, Object> response = new HashMap<>();
    if (StringUtils.isNotBlank(requestDto.getBrandName())) {
      response.put("brandName", requestDto.getBrandName());
    } else {
      response.put("brandName", emailConfig.getBrandName());
    }
    response.put("publisher", emailPublisher.getName());
    response.put("from", emailConfig.getSender());
    response.put("invalidEmails", listEmailInvalid);
    logger.info("Info Response [{}]", response);

    Boolean result = producerService.sendMessage(emailRequestSendDto);
    if (result) {
      return response;
    }
    return null;
  }

  public List<String> validateNotifyHistoryAndGetEmailInvalid(
      InfoEmailRequestFromNotifyServiceDto request,
      EmailRequestSendDto emailRequestSendDto)
      throws IdentifyBlankException {
    String receiver = request.getReceiver();
    if (StringUtils.isBlank(receiver)) {
      throw new IdentifyBlankException("Receiver Empty",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_RECEIVER_INVALID);
    }
    EmailContentRequestDto content = request.getContent();
    if (content == null) {
      throw new IdentifyBlankException("Content Empty",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_RECEIVER_INVALID);
    }
    String accountCode = request.getAccountCode();
    if (StringUtils.isBlank(accountCode)) {
      throw new IdentifyBlankException("Account-Code Empty",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_RECEIVER_INVALID);
    }
    List<EmailRecipient> recipients = new ArrayList<>();

    List<String> listEmailInvalid = new ArrayList<>();
    List<String> emailReceiverList = EmailUtil.convertStringFromEmail(receiver, listEmailInvalid);
    if (emailReceiverList.isEmpty()) {
      throw new IdentifyBlankException("invalid Receiver",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (request.getType() == null || request.getType().isEmpty()) {
      throw new IdentifyBlankException("type null",
          ServiceInfo.getId() + ServiceMessageCode.OPERATION_NOT_IMPLEMENT);
    }
    if (!emailReceiverList.isEmpty()) {
      for (String email : emailReceiverList) {
        EmailRecipient emailRecipient = new EmailRecipient();
        emailRecipient.setEmail(email);
        emailRecipient.setEmailRecipientType(EmailRecipientType.TO);
        recipients.add(emailRecipient);
      }
    }

    if (StringUtils.isNotBlank(request.getCc())) {
      List<String> emailReceiverToCC = EmailUtil
          .convertStringFromEmail(request.getCc(), listEmailInvalid);
      if (!emailReceiverToCC.isEmpty()) {
        for (String email : emailReceiverToCC) {
          EmailRecipient emailRecipient = new EmailRecipient();
          emailRecipient.setEmail(email);
          emailRecipient.setEmailRecipientType(EmailRecipientType.CC);
          recipients.add(emailRecipient);
        }
      }
    }
    if (StringUtils.isNotBlank(request.getBcc())) {
      List<String> emailReceiverToBCC = EmailUtil
          .convertStringFromEmail(request.getBcc(), listEmailInvalid);
      if (!emailReceiverToBCC.isEmpty()) {
        for (String email : emailReceiverToBCC) {
          EmailRecipient emailRecipient = new EmailRecipient();
          emailRecipient.setEmail(email);
          emailRecipient.setEmailRecipientType(EmailRecipientType.BCC);
          recipients.add(emailRecipient);
        }
      }
    }
    emailRequestSendDto.setReceiver(receiver);
    emailRequestSendDto.setCc(request.getCc());
    emailRequestSendDto.setBcc(request.getBcc());
    emailRequestSendDto.setRequestId(request.getRequestId());
    emailRequestSendDto.setAccountCode(accountCode);
    emailRequestSendDto.setRecipients(recipients);
    emailRequestSendDto.setBrandName(request.getBrandName());
    emailRequestSendDto.setSubject(content.getSubject());
    emailRequestSendDto.setBody(content.getBody());
    emailRequestSendDto.setType(content.getType());

    List<EmailAttachment> emailAttachmentList = request.getEmailAttachmentList();
    if (emailAttachmentList != null && !emailAttachmentList.isEmpty()) {
      emailRequestSendDto.setEmailAttachments(emailAttachmentList);
    }
    return listEmailInvalid;
  }
}
