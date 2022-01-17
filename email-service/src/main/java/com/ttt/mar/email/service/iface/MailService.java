package com.ttt.mar.email.service.iface;

import com.ttt.mar.email.dto.notifyhistory.InfoEmailRequestFromNotifyServiceDto;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface MailService {

  Map<String, Object> receiveEmail(InfoEmailRequestFromNotifyServiceDto requestDto)
      throws Exception;
}
