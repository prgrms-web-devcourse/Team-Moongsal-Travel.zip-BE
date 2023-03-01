package shop.zip.travel.domain.post.data;

public enum DefaultValue {
  LOCAL_DATE("1000-01-01"),
  STRING("1234567890qwertyuiop"),
  LONG("0");

  private final String value;


  DefaultValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
