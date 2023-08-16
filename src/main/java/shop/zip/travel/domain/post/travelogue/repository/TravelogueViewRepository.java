package shop.zip.travel.domain.post.travelogue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.entity.Views;

@Repository
public interface TravelogueViewRepository extends JpaRepository<Views, Long> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Views v "
      + "set v.count = v.count + 1 "
      + "where v.id = :id ")
  void increaseCount(@Param("id") Long id);

}

