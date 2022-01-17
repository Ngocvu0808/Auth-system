package com.ttt.mar.notify.utils;

import com.ttt.mar.notify.config.ServiceMessageCode;
import com.ttt.rnd.common.config.ServiceInfo;
import com.ttt.rnd.common.exception.OperationNotImplementException;
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

  public static Boolean validateEmail(String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
  }

  public static void validateEmails(String emails) throws OperationNotImplementException {
    if (StringUtils.isBlank(emails)) {
      return;
    }
    String[] emailArrays = emails.trim().split(",");
    for (String email : emailArrays) {
      if (!validateEmail(email)) {
        throw new OperationNotImplementException("Email Invalid.",
            ServiceInfo.getId() + ServiceMessageCode.EMAIL_INVALID);
      }
    }
  }

  public static Boolean validatePhone(String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }
    String regex = "(84|0)+([0-9]{2,10})\\b";
    return email.matches(regex);
  }

  public static void validatePhones(String phones) throws OperationNotImplementException {
    if (StringUtils.isBlank(phones)) {
      return;
    }
    String[] phoneArrays = phones.trim().split(",");
    for (String phone : phoneArrays) {
      if (!validatePhone(phone)) {
        throw new OperationNotImplementException("Phone Invalid.",
            ServiceInfo.getId() + ServiceMessageCode.PHONE_INVALID);
      }
    }
  }

  public static String percentEncode(String encodeMe) {
    if (encodeMe == null) {
      return "";
    }
    String encoded = encodeMe.replace("%", "%25");
    encoded = encoded.replace(" ", "%20");
    encoded = encoded.replace("!", "%21");
    encoded = encoded.replace("#", "%23");
    encoded = encoded.replace("$", "%24");
    encoded = encoded.replace("&", "%26");
    encoded = encoded.replace("'", "%27");
    encoded = encoded.replace("(", "%28");
    encoded = encoded.replace(")", "%29");
    encoded = encoded.replace("*", "%2A");
    encoded = encoded.replace("+", "%2B");
    encoded = encoded.replace(",", "%2C");
    encoded = encoded.replace("/", "%2F");
    encoded = encoded.replace(":", "%3A");
    encoded = encoded.replace(";", "%3B");
    encoded = encoded.replace("=", "%3D");
    encoded = encoded.replace("?", "%3F");
    encoded = encoded.replace("@", "%40");
    encoded = encoded.replace("[", "%5B");
    encoded = encoded.replace("]", "%5D");
    return encoded;
  }
}
