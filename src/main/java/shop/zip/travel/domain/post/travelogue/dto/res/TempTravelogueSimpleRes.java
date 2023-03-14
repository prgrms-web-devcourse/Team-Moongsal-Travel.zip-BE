package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.ArrayList;
import java.util.List;
import shop.zip.travel.domain.member.dto.response.MemberSimpleRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

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
    List<Long> subTravelogueId
) {

  public static TempTravelogueSimpleRes toDto(TempTravelogueSimple travelogueSimple) {
    Travelogue travelogue = travelogueSimple.travelogue();
    long nights = travelogueSimple.travelogue().getPeriod().getNights();
    Member member = travelogue.getMember();

    List<Long> subIdList = new ArrayList<>();

    if (!travelogue.getSubTravelogues().isEmpty()) {
      subIdList = travelogue.getSubTravelogues()
          .stream()
          .map(SubTravelogue::getId)
          .toList();
    }

    return new TempTravelogueSimpleRes(
        travelogue.getId(),
        DefaultValue.STRING.isEqual(travelogue.getTitle()) ? "" : travelogue.getTitle(),
        nights,
        nights + 1,
        travelogue.getCost().getTotal(),
        DefaultValue.STRING.isEqual(travelogue.getCountry().getName()) ? ""
            : travelogue.getCountry().getName(),
        DefaultValue.STRING.isEqual(travelogue.getThumbnail()) ? "" : travelogue.getThumbnail(),
        MemberSimpleRes.toDto(member),
        travelogueSimple.likeCount(),
        subIdList
    );
  }
}
