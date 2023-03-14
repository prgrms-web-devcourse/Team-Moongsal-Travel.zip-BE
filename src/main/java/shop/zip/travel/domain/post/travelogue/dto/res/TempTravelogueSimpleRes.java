package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.List;
import shop.zip.travel.domain.member.dto.response.MemberSimpleRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;

public record TempTravelogueSimpleRes(
    Long travelogueId,
    String title,
    Long nights,
    Long days,
    Long totalCost,
    String country,
    String thumbnail,
    MemberSimpleRes member,
    Long likeCount,
    List<Long> subTraveloguesId
) {

  public static TempTravelogueSimpleRes toDto(
      TempTravelogueSimple travelogueSimple
  ) {
    long nights = travelogueSimple.getPeriod().getNights();
    Long likeCount = travelogueSimple.getLikeCount();

    return new TempTravelogueSimpleRes(
        travelogueSimple.getTravelogueId(),
        travelogueSimple.getTitle(),
        nights,
        nights + 1,
        travelogueSimple.getTotalCost(),
        travelogueSimple.getCountry(),
        travelogueSimple.getThumbnail(),
        MemberSimpleRes.toDto(travelogueSimple),
        likeCount,
        travelogueSimple.getSubTravelogueList()
            .stream()
            .map(SubTravelogue::getId)
            .toList()
    );
  }
}
