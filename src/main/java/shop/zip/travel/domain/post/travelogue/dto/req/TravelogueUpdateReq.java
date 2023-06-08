package shop.zip.travel.domain.post.travelogue.dto.req;

import java.util.Objects;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;

public class TravelogueUpdateReq {

  private PeriodCreateReq period;
  private String title;
  private CountryCreateReq country;
  private String thumbnail;
  private CostCreateReq cost;

  private TravelogueUpdateReq() {
  }

  public TravelogueUpdateReq(PeriodCreateReq period, String title, CountryCreateReq country,
      String thumbnail, CostCreateReq cost) {
    this.period = period;
    this.title = title;
    this.country = country;
    this.thumbnail = thumbnail;
    this.cost = cost;
  }

  public Period getPeriod() {
    return period.toPeriod();
  }

  public String getTitle() {
    return title;
  }

  public Country getCountry() {
    return country.toCountry();
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public Cost getCost() {
    return cost.toCost();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TravelogueUpdateReq that = (TravelogueUpdateReq) o;
    return Objects.equals(period, that.period) && Objects.equals(title,
        that.title) && Objects.equals(country, that.country) && Objects.equals(
        thumbnail, that.thumbnail) && Objects.equals(cost, that.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, title, country, thumbnail, cost);
  }
}
