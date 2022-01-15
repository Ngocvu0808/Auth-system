package com.ttt.mar.leads.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public class Utils {

  public static String bytesToHex(byte[] hashInBytes) {

    StringBuilder sb = new StringBuilder();
    for (byte b : hashInBytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  public static Set<Integer> strToIntegerSet(String data) {
    if (data == null || data.isEmpty()) {
      return new HashSet<>();
    }
    return Arrays.stream(data.split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toSet());
  }

  public static Set<String> strToStringSet(String data) {
    if (data == null || data.isEmpty()) {
      return new HashSet<>();
    }
    return Arrays.stream(data.split(","))
        .collect(Collectors.toSet());
  }

  public static String readFileAsString(String fileName) throws Exception {
    StringBuilder builder = new StringBuilder();
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
    Resource resource = context.getResource(fileName);
    if (resource.isReadable()) {
      InputStream ip = resource.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(ip));
      reader.lines().forEach(builder::append);
      reader.close();
    }
    return builder.toString();
  }

  public static String textSpecialCharacters(String text) {
    if (StringUtils.isBlank(text)) {
      return text;
    }
    if (text.indexOf("_") != -1 || text.indexOf("%") != -1) {
      text = text.replaceAll("_", "\\\\_").replaceAll("%", "\\\\%");
    }
    return text;
  }

  public static String escapeMetaCharacters(String inputString) {
    final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+",
        "?", "|", "<", ">", "-", "&", "%"};

    for (int i = 0; i < metaCharacters.length; i++) {
      if (inputString.contains(metaCharacters[i])) {
        inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
      }
    }
    return inputString;
  }
}
