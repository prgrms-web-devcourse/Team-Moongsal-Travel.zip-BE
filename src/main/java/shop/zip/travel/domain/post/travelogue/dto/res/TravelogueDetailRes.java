package shop.zip.travel.domain.post.travelogue.dto.res;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueDetailRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TravelogueDetailRes(
    String profileImageUrl,
    String nickname,
    Long id,
    String title,
    String country,
    Long nights,
    Long days,
    Long totalCost,
    List<SubTravelogueDetailRes> subTravelogues,
    Set<Transportation> transportations,
    Long countLikes,
    boolean isLiked,
    Long viewCount,
    Boolean bookmarked
) {

  public static TravelogueDetailRes toDto(Travelogue travelogue, Long countLikes, Boolean isLiked,  Boolean isBookmarked) {

    return new TravelogueDetailRes(
        travelogue.getMember().getProfileImageUrl(),
        travelogue.getMember().getNickname(),
        travelogue.getId(),
        travelogue.getTitle(),
        travelogue.getCountry().getName(),
        travelogue.getPeriod().getNights(),
        travelogue.getPeriod().getNights() + 1,
        travelogue.getCost().getTotal(),
        travelogue.getSubTravelogues()
            .stream()
            .map(SubTravelogueDetailRes::toDto)
            .toList(),
        travelogue.getSubTravelogues()
            .stream()
            .map(SubTravelogue::getTransportationSet)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet()),
        countLikes,
        isLiked,
        travelogue.getViewCount(),
        isBookmarked
    );

  }

}
