package shop.zip.travel.domain.post.data;

public record TempCountry(
  String name
) {

  public Country toCountry() {
    return new Country(
      (name == null) ? DefaultValue.STRING.getValue() : name
    );
  }
}
