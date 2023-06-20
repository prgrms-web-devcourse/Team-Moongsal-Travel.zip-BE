package shop.zip.travel.domain.post.travelogue.dto;

import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TravelogueSimpleDetail(
    Travelogue travelogue,
    Long memberId,
    String profileImageUrl,
    String nickName,
    long countLikes,
    boolean isLiked,
    boolean isBookmarked
) {

}
