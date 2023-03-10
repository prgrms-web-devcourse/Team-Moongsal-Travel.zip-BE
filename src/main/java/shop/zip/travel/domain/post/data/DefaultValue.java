package shop.zip.travel.domain.post.data;

import java.time.LocalDate;

public enum DefaultValue {
  LOCAL_DATE("1899-02-01"),
  STRING("1234567890qwertyuiop"),
  LONG("0");

  private static final String STRING_RETURN_VALUE = "";
  private static final String DEFAULT_IMAGE_URL =
      "https://unsplash.com/ko/%EC%82%AC%EC%A7%84/BWCgQw25XUE?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText";

  private final String value;


  DefaultValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public boolean isEqual(String request) {
    return value.equals(request);
  }

  public static LocalDate orGetLocalDateReturnValue(LocalDate requestLocalDate) {
    if (LOCAL_DATE.isEqual(requestLocalDate.toString())) {
      return null;
    }
    return requestLocalDate;
  }

  public static String orGetStringReturnValue(String requestString) {
    if (STRING.isEqual(requestString)) {
      return STRING_RETURN_VALUE;
    }
    return requestString;
  }

  public static String orGetDefaultImage(String request) {
    if (STRING.isEqual(request)) {
      return DEFAULT_IMAGE_URL;
    }
    return request;
  }

  public static boolean isBefore(LocalDate request) {
    return request.isBefore(LocalDate.parse(LOCAL_DATE.getValue())) ||
        request.equals(LocalDate.parse(LOCAL_DATE.getValue()));
  }
}
