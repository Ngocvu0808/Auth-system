package com.ttt.mar.email.mapper;

import com.ttt.mar.email.dto.emailhistory.EmailHistoryDtoResponse;
import com.ttt.mar.email.entities.EmailHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EmailHistoryMapper {

  public abstract EmailHistoryDtoResponse toEmailHistoryDtoResponse(EmailHistory emailHistory);


}
