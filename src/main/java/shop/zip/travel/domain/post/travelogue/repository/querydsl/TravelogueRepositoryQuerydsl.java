package shop.zip.travel.domain.post.travelogue.repository.querydsl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchFilter;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;

public interface TravelogueRepositoryQuerydsl {


  Slice<TravelogueSimpleRes> filtering(String keyword, Pageable pageable,
      TravelogueSearchFilter searchFilter);

  Slice<TravelogueSimpleRes> search(String keyword, Pageable pageable);

  boolean isLiked(Long travelogueId, Long memberId);

  Long countLikes(Long travelogueId);
}

