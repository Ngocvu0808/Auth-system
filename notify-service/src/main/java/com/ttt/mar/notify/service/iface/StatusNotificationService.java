package com.ttt.mar.notify.service.iface;

import com.ttt.mar.notify.dto.statusnotify.StatusNotificationResponseDto;
import java.util.List;

public interface StatusNotificationService {

  List<StatusNotificationResponseDto> getAll();

}
