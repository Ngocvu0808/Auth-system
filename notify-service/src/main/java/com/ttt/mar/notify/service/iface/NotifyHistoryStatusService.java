package com.ttt.mar.notify.service.iface;

import com.ttt.mar.notify.dto.notify.PayLoadStatusHistoryUpdateDto;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;

public interface NotifyHistoryStatusService {

  Boolean updateStatusHistory(PayLoadStatusHistoryUpdateDto payLoadStatusHistoryUpdateDto)
      throws ResourceNotFoundException, IdentifyBlankException, OperationNotImplementException;

}
