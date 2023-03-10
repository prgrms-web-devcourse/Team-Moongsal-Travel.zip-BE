package shop.zip.travel.domain.post.travelogue.dto.res;

import static shop.zip.travel.domain.post.data.DefaultValue.orGetDefaultImage;
import static shop.zip.travel.domain.post.data.DefaultValue.orGetStringReturnValue;

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
    String thumbnail,
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
        orGetStringReturnValue(travelogue.getTitle()),
        orGetStringReturnValue(travelogue.getCountry().getName()),
        travelogue.getPeriod().getNights(),
        travelogue.getPeriod().getNights() + 1,
        travelogue.getCost().getTotal(),
        orGetDefaultImage(travelogue.getThumbnail()),
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
