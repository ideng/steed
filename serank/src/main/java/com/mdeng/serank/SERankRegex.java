package com.mdeng.serank;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regex helper for SERank
 * 
 * @author Administrator
 *
 */
public class SERankRegex {

  public List<String> matchValues(String content, String regex) {
    Matcher matcher =
        Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content);

    List<String> matchs = new ArrayList<String>();
    while (matcher.find()) {
      matchs.add(matcher.group());
    }

    return matchs;
  }

  public String matchNthValue(String content, String regex, int group) {
    Matcher matcher =
        Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content);

    if (matcher.find()) {
      return matcher.group(group);
    }

    return "";
  }
}
