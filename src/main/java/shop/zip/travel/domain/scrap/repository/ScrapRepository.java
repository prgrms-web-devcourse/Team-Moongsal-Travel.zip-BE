package shop.zip.travel.domain.scrap.repository;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import shop.zip.travel.domain.scrap.document.Scrap;

public interface ScrapRepository extends MongoRepository<Scrap, ObjectId> {

  List<Scrap> findByMemberId(Long memberId);
}
