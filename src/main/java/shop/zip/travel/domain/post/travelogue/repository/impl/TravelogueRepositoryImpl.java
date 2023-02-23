package shop.zip.travel.domain.post.travelogue.repository.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public class TravelogueRepositoryImpl extends QuerydslRepositorySupport implements
    TravelogueRepositoryQuerydsl {

  /**
   * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
   *
   * @param domainClass must not be {@literal null}.
   */
  public TravelogueRepositoryImpl(Class<?> domainClass) {
    super(domainClass);
  }
}
