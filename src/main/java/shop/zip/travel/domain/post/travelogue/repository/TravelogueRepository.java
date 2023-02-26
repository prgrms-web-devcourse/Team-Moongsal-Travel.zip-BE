package shop.zip.travel.domain.post.travelogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long>,
    TravelogueRepositoryQuerydsl {

    @Query(value = "select t "
        + "from Travelogue t "
        + "left join fetch t.member "
        + "where t.id = :travelogueId")
    Travelogue getTravelogueDetail(Long travelogueId);

}
