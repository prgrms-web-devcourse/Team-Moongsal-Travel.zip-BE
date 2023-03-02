package shop.zip.travel.domain.bookmark.repository.querydsl;


public interface BookmarkRepositoryQuerydsl {

  boolean existsBy(Long memberId, Long travelogueId);
}
