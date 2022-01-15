package com.ttt.mar.email.service.impl.kafka;

import com.ttt.mar.email.config.ServiceMessageCode;
import com.ttt.mar.email.dto.mail.EmailMessage;
import com.ttt.mar.email.dto.mail.EmailRequestDto;
import com.ttt.mar.email.dto.mail.EmailRequestSendDto;
import com.ttt.mar.email.entities.EmailConfig;
import com.ttt.mar.email.entities.EmailPublisher;
import com.ttt.mar.email.provider.SMTPMailProvider;
import com.ttt.mar.email.repositories.EmailConfigRepository;
import com.ttt.mar.email.service.iface.AsyncService;
import com.ttt.mar.email.service.iface.EmailHistoryService;
import com.ttt.mar.email.service.iface.kafka.consumer.ConsumerService;
import com.ttt.mar.email.service.iface.kafka.producer.ProducerService;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService<EmailRequestSendDto> {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ConsumerServiceImpl.class);

  @Autowired
  private SMTPMailProvider smtpMailProvider;

  @Autowired
  private ProducerService<EmailRequestSendDto> producerService;

  @Autowired
  private AsyncService asyncService;

  @Autowired
  private EmailHistoryService emailHistoryService;

  @Autowired
  private EmailConfigRepository emailConfigRepository;

  private static final String SEND_EMAIL_TO_CLIENT_FAIL = "SEND_FAILED";

  private static final String SEND_EMAIL_TO_CLIENT_SUCCESS = "SENT";

  private static final String SEND_EMAIL_TO_PUBLISHER_SUCCESS = "PROCESSED";

  private static final String SEND_EMAIL_TO_PUBLISHER_FAIL = "PROCESS_FAILED";

  private static final String STATUS_ERROR_CODE = "ERROR";

  @KafkaListener(topics = "#{'${email-service.topics.email-info}'.split(',')}")
  public void consume(EmailRequestSendDto data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    EmailRequestDto emailRequestDto = new EmailRequestDto();
    EmailMessage emailMessage = new EmailMessage();
    boolean checkError = true;
    String result = "";
    try {
      // Kiểm tra lại thonh tin.
      validateEmailRequestSend(data, emailRequestDto, emailMessage);
      logger.info("Topic {}", topic);
      result = smtpMailProvider.sendSMTPMail(emailMessage);
      emailHistoryService.addEmailHistory(emailRequestDto);
    } catch (IdentifyBlankException e) {
      logger.error("Status is Error: {}", e.getMessage());
      try {
        checkError = false;
        asyncService.updateStatusNotifyHistoryToNotifyService(data.getRequestId(),
            STATUS_ERROR_CODE, e.getMessage());
      } catch (Exception ex) {
        logger.error(ex.getMessage());
      }
    } catch (ResourceNotFoundException e) {
      logger.error("Status is Error: {}", e.getMessage());
      try {
        checkError = false;
        asyncService.updateStatusNotifyHistoryToNotifyService(data.getRequestId(),
            STATUS_ERROR_CODE, e.getMessage());
      } catch (Exception ex) {
        logger.error(ex.getMessage());
      }
    } catch (Exception ex) {
      logger.info("error detail: {}", ex.getMessage());
      producerService.sendDataError(data, "err-" + topic);
    } finally {
      try {
        if (checkError) {
          if (StringUtils.isBlank(result)) {
            logger.error("Result is Null");
            asyncService.updateStatusNotifyHistoryToNotifyService(data.getRequestId(),
                SEND_EMAIL_TO_PUBLISHER_FAIL, null);
          } else {
            boolean checkSendEmail = true;
            if (SEND_EMAIL_TO_CLIENT_SUCCESS.equals(result)) {
              logger.error("Status is Processed");
              asyncService.updateStatusNotifyHistoryToNotifyService(data.getRequestId(),
                  SEND_EMAIL_TO_PUBLISHER_SUCCESS, null);
            } else if(SEND_EMAIL_TO_CLIENT_FAIL.equals(result)) {
              logger.error("Status is Processed failed");
              checkSendEmail = false;
              asyncService.updateStatusNotifyHistoryToNotifyService(data.getRequestId(),
                  SEND_EMAIL_TO_PUBLISHER_FAIL, null);
            }
            if (checkSendEmail) {
              logger.error("Status is {}", result);
              asyncService.updateStatusNotifyHistoryToNotifyService(data.getRequestId(),
                  result, null);
            }
          }
        }
      } catch (Exception e) {
        logger.error("error-detail in finally block: {}", e.getMessage());
      }
    }
  }

  public void validateEmailRequestSend(EmailRequestSendDto data, EmailRequestDto emailRequestDto,
      EmailMessage emailMessage)
      throws IdentifyBlankException, ResourceNotFoundException {
    String accountCode = data.getAccountCode();
    if (StringUtils.isBlank(accountCode)) {
      throw new IdentifyBlankException("Account-Code Empty",
          ServiceInfo.getId() + ServiceMessageCode.EMAIL_RECEIVER_INVALID);
    }
    EmailConfig emailConfig = emailConfigRepository
        .findByAccountCodeAndIsDeletedFalse(accountCode);
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

    // Set Info emailRequestDto.
    emailRequestDto.setRequestId(data.getRequestId());
    emailRequestDto.setCc(data.getCc());
    emailRequestDto.setBcc(data.getBcc());
    emailRequestDto.setContent(data.getContent());
    emailRequestDto.setIdEmailConfig(emailConfig.getId());
    emailRequestDto.setReceiver(data.getReceiver());
    logger.info("Info emailRequestDto: [{}]", emailRequestDto.toString());

    // Set Info emailMessage.
    emailMessage.setUserName(emailConfig.getUsername());
    emailMessage.setPassword(emailConfig.getPassword());
    emailMessage.setHost(emailPublisher.getHost());
    if (emailPublisher.getPort() != null) {
      emailMessage.setPort(String.valueOf(emailPublisher.getPort()));
    }
    emailMessage.setToken(emailConfig.getToken());
    emailMessage.setFromEmail(emailConfig.getSender());
    if (StringUtils.isNotBlank(data.getBrandName())) {
      emailMessage.setBrandName(data.getBrandName());
    } else {
      emailMessage.setBrandName(emailConfig.getBrandName());
    }
    emailMessage.setRecipients(data.getRecipients());
    emailMessage.setEmailProtocol(emailPublisher.getProtocol());
    emailMessage.setType(data.getType());
    emailMessage.setSubject(data.getSubject());
    emailMessage.setBody(data.getBody());
    emailMessage.setEmailAttachments(data.getEmailAttachments());
  }
}