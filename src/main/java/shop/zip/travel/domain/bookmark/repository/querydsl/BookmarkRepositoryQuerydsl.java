package shop.zip.travel.domain.bookmark.repository.querydsl;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;

public interface BookmarkRepositoryQuerydsl {

  boolean exists(Long memberId, Long travelogueId);

  Slice<TravelogueSimpleRes> getBookmarkedList(Long memberId, Pageable pageable);
}
