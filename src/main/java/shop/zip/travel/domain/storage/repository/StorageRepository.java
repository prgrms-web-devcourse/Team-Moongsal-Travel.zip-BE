package shop.zip.travel.domain.storage.repository;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import shop.zip.travel.domain.storage.document.Storage;

public interface StorageRepository extends MongoRepository<Storage, ObjectId> {

  List<Storage> findByMemberId(Long memberId);
}
