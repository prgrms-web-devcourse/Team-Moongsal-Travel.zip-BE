package shop.zip.travel.domain.bookmark.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.querydsl.BookmarkRepositoryQuerydsl;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;

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

  @Query("select new shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple("
    + "b.travelogue.id, b.travelogue.title, b.travelogue.period, b.travelogue.cost.total, b.travelogue.country.name, b.travelogue.thumbnail, m.nickname, m.profileImageUrl)"
    + "from Bookmark b "
    + "inner join b.member m "
    + "where b.member.id = :memberId ")
  Slice<TravelogueSimple> getBookmarkedTravelogues(Long memberId);
}
