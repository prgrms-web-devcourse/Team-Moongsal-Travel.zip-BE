package shop.zip.travel.domain.post.travelogue.repository.impl;

import static org.springframework.util.StringUtils.hasText;
import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public class TravelogueRepositoryImpl extends QuerydslRepositorySupport implements
    TravelogueRepositoryQuerydsl {

  private final JPAQueryFactory jpaQueryFactory;

  public TravelogueRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Travelogue.class);
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public List<TravelogueSimpleRes> search(Long lastTravelogueId, String keyword, String orderType,
      long size) {
    List<Long> ids = jpaQueryFactory
        .select(travelogue.id)
        .from(travelogue)
        .where(
            keywordContains(keyword)
                .and(travelogueIdLt(10L))
        )
        .orderBy(getOrder(orderType))
        .limit(size)
        .fetch();

    if (ids.isEmpty()) {
      return new ArrayList<>();
    }

    List<TravelogueSimple> travelogueSimpleList = jpaQueryFactory
        .select(
            Projections.constructor(
                TravelogueSimple.class,
                travelogue.id,
                travelogue.title,
                travelogue.period,
                travelogue.cost.total,
                travelogue.country.name,
                travelogue.thumbnail,
                travelogue.member.nickname,
                travelogue.member.profileImageUrl
            )
        )
        .from(travelogue)
        .where(travelogue.id.in(ids))
        .leftJoin(travelogue.member, member)
        .orderBy(member.id.desc())
        .fetch();

    List<TravelogueSimpleRes> travelogueSimpleResList = new ArrayList<>();

    travelogueSimpleList.stream().map(travelogueSimple -> travelogueSimpleResList.add(
        TravelogueSimpleRes.toDto(travelogueSimple)));

    return travelogueSimpleResList;
  }

  private BooleanExpression keywordContains(String keyword) {
    return hasText(keyword) ? travelogue.title.contains(keyword) : null;
  }

  private OrderSpecifier<?> getOrder(String orderType) {
    switch (orderType) {
      default -> {
        return travelogue.createDate.desc();
      }
    }
  }

  private BooleanExpression travelogueIdLt(Long lastTravelogueIdLt) {
    return lastTravelogueIdLt != null ? travelogue.id.lt(lastTravelogueIdLt) : null;
  }
}
