package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.app.LogRequestExport;
import com.ttt.mar.auth.dto.app.LogRequestResponseDto;
import com.ttt.rnd.lib.entities.LogRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class LogRequestMapper {

  private static final String SUCCESS = "Success";

  private static final String BAD_REQUEST = "Bad request";

  private static final String INTERNAL_SERVER = "Internal Server Error";

  private static final String FORBIDDEN = "Forbidden";

  private static final String NOT_FOUND = "Not found";

  private static final String CONFLICT = "Conflict";

  private static final String UN_AUTHORIZED = "Un authorized";


  public abstract LogRequestResponseDto toLogResponseDto(LogRequest logRequest);

  @AfterMapping
  public void afterMapping(@MappingTarget LogRequestResponseDto logResponseDto,
      LogRequest logRequest) {
    Integer httpCode = logResponseDto.getResHttpCode();
    if (httpCode == null) {
      httpCode = logResponseDto.getResHttpStatus();
    }
    if (httpCode != null) {
      switch (httpCode) {
        case 200:
          logResponseDto.setStatus(SUCCESS);
          break;
        case 400:
          logResponseDto.setStatus(BAD_REQUEST);
          break;
        case 401:
          logResponseDto.setStatus(UN_AUTHORIZED);
          break;
        case 403:
          logResponseDto.setStatus(FORBIDDEN);
          break;
        case 404:
          logResponseDto.setStatus(NOT_FOUND);
          break;
        case 409:
          logResponseDto.setStatus(CONFLICT);
          break;
        case 500:
          logResponseDto.setStatus(INTERNAL_SERVER);
          break;
      }
    }

    if (logResponseDto.getCreatedTime() != null) {
      logResponseDto.setRequestTimeStamp(logResponseDto.getCreatedTime().getTime());
    }

    if (logResponseDto.getResTime() != null) {
      logResponseDto.setResponseTimeStamp(logResponseDto.getResTime().getTime());
    }
  }

  public abstract LogRequestExport toLogExport(LogRequestResponseDto logRequest);
}
