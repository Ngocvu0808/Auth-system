package com.ttt.mar.notify.mapper;

import com.ttt.mar.notify.dto.typesend.TypeSendResponseDto;
import com.ttt.mar.notify.entities.TypeSend;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TypeSendMapper {

  public abstract TypeSendResponseDto toDto(TypeSend typeSend);

}
