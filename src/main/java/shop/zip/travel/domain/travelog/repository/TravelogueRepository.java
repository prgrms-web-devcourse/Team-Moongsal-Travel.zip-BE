package shop.zip.travel.domain.travelog.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.travelog.entity.Travelogue;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long> {

  Slice<Travelogue> findSliceBy(Pageable pageable);
}
