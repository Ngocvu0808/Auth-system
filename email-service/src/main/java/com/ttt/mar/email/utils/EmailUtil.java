package com.ttt.mar.email.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class EmailUtil {

  public static Boolean validateEmail(String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
  }

  public static List<String> convertStringFromEmail(String stringFromEmail,
      List<String> listEmailInvalid) {
    if (StringUtils.isBlank(stringFromEmail)) {
      return Collections.emptyList();
    }
    String[] emailArrays = stringFromEmail.trim().split(",");
    List<String> emailList = new ArrayList<>();
    for (String email : emailArrays) {
      if (!validateEmail(email)) {
        listEmailInvalid.add(email);
      } else {
        emailList.add(email);

      }
    }
    return emailList;
  }
}
