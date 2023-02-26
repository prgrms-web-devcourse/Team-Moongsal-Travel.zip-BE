package shop.zip.travel.domain.post.travelogue.repository.querydsl;

import java.util.List;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;

public interface TravelogueRepositoryQuerydsl {

  List<TravelogueSimpleRes> search(Long lastTravelogue, String keyword, String orderType,
      long size);
}
