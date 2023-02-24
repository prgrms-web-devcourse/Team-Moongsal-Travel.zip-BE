package shop.zip.travel.domain.post.travelogue.repository.querydsl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimpleRes;

public interface TravelogueRepositoryQuerydsl {
  Slice<TravelogueSimpleRes> search(String keyword, PageRequest pageRequest);
}
