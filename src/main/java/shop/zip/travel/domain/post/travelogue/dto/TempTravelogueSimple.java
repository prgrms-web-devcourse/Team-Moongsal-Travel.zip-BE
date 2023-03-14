package shop.zip.travel.domain.post.travelogue.dto;

import java.util.ArrayList;
import java.util.List;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Period;

public class TempTravelogueSimple {

  Long travelogueId;
  String title;
  Period period;
  Long totalCost;
  String country;
  String thumbnail;
  String memberNickname;
  String memberProfileImageUrl;
  Long likeCount;
  List<SubTravelogue> subTravelogueList = new ArrayList<>();

  public TempTravelogueSimple(Long travelogueId, String title, Period period, Long totalCost,
      String country, String thumbnail, String memberNickname, String memberProfileImageUrl,
      Long likeCount, List<SubTravelogue> subTravelogueList) {
    this.travelogueId = travelogueId;
    this.title = title;
    this.period = period;
    this.totalCost = totalCost;
    this.country = country;
    this.thumbnail = thumbnail;
    this.memberNickname = memberNickname;
    this.memberProfileImageUrl = memberProfileImageUrl;
    this.likeCount = likeCount;
    this.subTravelogueList = subTravelogueList;
  }

  public Long getTravelogueId() {
    return travelogueId;
  }

  public String getTitle() {
    return title;
  }

  public Period getPeriod() {
    return period;
  }

  public Long getTotalCost() {
    return totalCost;
  }

  public String getCountry() {
    return country;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public String getMemberNickname() {
    return memberNickname;
  }

  public String getMemberProfileImageUrl() {
    return memberProfileImageUrl;
  }

  public Long getLikeCount() {
    return likeCount;
  }

  public List<SubTravelogue> getSubTravelogueList() {
    return subTravelogueList;
  }
}
