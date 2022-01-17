package com.ttt.mar.notify.service.iface;

import com.ttt.mar.notify.dto.typesend.TypeSendResponseDto;
import java.util.List;

public interface TypeSendService {

  List<TypeSendResponseDto> getAll();

}
