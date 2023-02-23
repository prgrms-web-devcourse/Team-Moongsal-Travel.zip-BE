package shop.zip.travel.domain.post.travelogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long> {

}
