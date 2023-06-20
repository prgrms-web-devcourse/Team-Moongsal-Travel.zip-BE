package shop.zip.travel.domain.post.travelogue.repository.querydsl;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchFilter;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimpleDetail;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;

public interface TravelogueRepositoryQuerydsl {


  Slice<TravelogueSimpleRes> filtering(String keyword, Pageable pageable,
      TravelogueSearchFilter searchFilter);

  Slice<TravelogueSimpleRes> search(String keyword, Pageable pageable);

  Optional<TravelogueSimpleDetail> getTravelogueDetail(Long travelogueId, Long memberId);
}

