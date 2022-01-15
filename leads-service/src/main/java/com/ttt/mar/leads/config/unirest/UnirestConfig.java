package com.ttt.mar.leads.config.unirest;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.annotation.PostConstruct;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

public class UnirestConfig {

  private final com.fasterxml.jackson.databind.ObjectMapper mapper;

  public UnirestConfig(com.fasterxml.jackson.databind.ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @PostConstruct
  public void postConstruct() {
    Unirest.config().reset().enableCookieManagement(true).setObjectMapper(new ObjectMapper() {

      public String writeValue(Object value) {
        try {
          return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }

      public <T> T readValue(String value, Class<T> valueType) {
        try {
          return mapper.readValue(value, valueType);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
