package shop.zip.travel.domain.post.travelogue.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long>,
    TravelogueRepositoryQuerydsl {

  @Query("select travelogue from Travelogue travelogue join fetch travelogue.member where travelogue.title = ?1")
  Slice<Travelogue> findByTitleContaining(String keyword, PageRequest pageRequest);
}
