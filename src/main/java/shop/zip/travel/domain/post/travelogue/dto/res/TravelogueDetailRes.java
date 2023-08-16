package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueDetailRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimpleDetail;

public record TravelogueDetailRes(
    String profileImageUrl,
    String nickname,
    Long id,
    String title,
    String country,
    Long nights,
    Long days,
    Long totalCost,
    String thumbnail,
    List<SubTravelogueDetailRes> subTravelogues,
    Set<Transportation> transportations,
    long countLikes,
    boolean isLiked,
    long viewCount,
    boolean bookmarked,
    boolean isWriter
) {

  public static TravelogueDetailRes toDto(TravelogueSimpleDetail simpleDetailDto,
      boolean isWriter
  ) {

    return new TravelogueDetailRes(
        simpleDetailDto.profileImageUrl(),
        simpleDetailDto.nickName(),
        simpleDetailDto.travelogue().getId(),
        simpleDetailDto.travelogue().getTitle(),
        simpleDetailDto.travelogue().getCountry().getName(),
        simpleDetailDto.travelogue().getPeriod().getNights(),
        simpleDetailDto.travelogue().getPeriod().getNights() + 1,
        simpleDetailDto.travelogue().getCost().getTotal(),
        simpleDetailDto.travelogue().getThumbnail(),
        simpleDetailDto.subTravelogues()
            .stream()
            .map(SubTravelogueDetailRes::toDto)
            .toList(),
        simpleDetailDto.subTravelogues()
            .stream()
            .map(SubTravelogue::getTransportationSet)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet()),
        simpleDetailDto.countLikes(),
        simpleDetailDto.isLiked(),
        simpleDetailDto.travelogue().getViews().getCount(),
        simpleDetailDto.isBookmarked(),
        isWriter
    );
  }

}
