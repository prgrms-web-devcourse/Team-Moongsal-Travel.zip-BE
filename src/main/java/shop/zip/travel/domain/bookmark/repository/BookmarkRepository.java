package shop.zip.travel.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.querydsl.BookmarkRepositoryQuerydsl;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>,
  BookmarkRepositoryQuerydsl {

  @Query(
    "select b.id "
      + "from Bookmark b "
      + "where b.member.id = :memberId "
      + "and  b.travelogue.id = :travelogueId"
  )
  Long getBookmarkId(@Param("memberId") Long memberId,
    @Param("travelogueId") Long travelogueId);
}
