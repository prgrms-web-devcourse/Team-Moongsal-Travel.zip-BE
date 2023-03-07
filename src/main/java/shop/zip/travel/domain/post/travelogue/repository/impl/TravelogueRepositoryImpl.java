package shop.zip.travel.domain.post.travelogue.repository.impl;

import static org.springframework.util.StringUtils.hasText;
import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.subTravelogue.data.QAddress.address;
import static shop.zip.travel.domain.post.subTravelogue.entity.QSubTravelogue.subTravelogue;
import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchFilter;
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
  public Slice<TravelogueSimpleRes> search(String keyword, Pageable pageable) {
    List<Long> subTravelogueIds = getSubTravelogueIds(keyword, pageable);
    List<Long> travelogueIds = getTravelogueIds(keyword, pageable, subTravelogueIds);

    if (travelogueIds.isEmpty()) {
      return new SliceImpl<>(Collections.emptyList());
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
        .where(travelogue.id.in(travelogueIds).and(travelogue.isPublished.isTrue()))
        .leftJoin(travelogue.member, member)
        .orderBy(travelogue.createDate.desc())
        .fetch();

    return new SliceImpl<>(travelogueSimpleList.stream()
        .map(TravelogueSimpleRes::toDto)
        .toList());
  }

  public Slice<TravelogueSimpleRes> filtering(String keyword, Pageable pageable,
      TravelogueSearchFilter searchFilter) {

    List<Long> subTravelogueIds = getSubTravelogueIds(keyword, pageable);
    List<Long> travelogueIds = getTravelogueIds(keyword, pageable, subTravelogueIds);

    if (travelogueIds.isEmpty()) {
      return new SliceImpl<>(Collections.emptyList());
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
        .where(travelogue.id.in(travelogueIds),
            TraveloguePeriodDaysBetween(searchFilter.minDays(), searchFilter.maxDays()),
            totalCostBetween(searchFilter.minCost(), searchFilter.maxCost()),
            travelogue.isPublished.isTrue()
        )
        .leftJoin(travelogue.member, member)
        .orderBy(getOrder(pageable.getSort()))
        .fetch();

    return new SliceImpl<>(travelogueSimpleList.stream()
        .map(TravelogueSimpleRes::toDto)
        .toList());

  }


  private List<Long> getTravelogueIds(String keyword, Pageable pageable,
      List<Long> subTravelogueIds) {

    return jpaQueryFactory
        .select(travelogue.id)
        .from(travelogue)
        .innerJoin(travelogue.subTravelogues, subTravelogue)
        .where(
            countryContains(keyword)
                .or(titleContains(keyword))
                .or(subTravelogue.id.in(subTravelogueIds))
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private List<Long> getSubTravelogueIds(String keyword, Pageable pageable) {
    return jpaQueryFactory
        .select(subTravelogue.id)
        .from(subTravelogue)
        .innerJoin(subTravelogue.addresses, address)
        .where(
            spotContains(keyword)
                .or(contentContains(keyword))

        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private BooleanExpression titleContains(String keyword) {
    return hasText(keyword) ? travelogue.title.contains(keyword) : null;
  }

  private BooleanExpression countryContains(String keyword) {
    return hasText(keyword) ? travelogue.country.name.contains(keyword) : null;
  }

  private BooleanExpression contentContains(String keyword) {
    return hasText(keyword) ? subTravelogue.content.contains(keyword) : null;
  }

  private BooleanExpression spotContains(String keyword) {
    return hasText(keyword) ?
        address.region.contains(keyword) : null;
  }

  private OrderSpecifier<?> getOrder(Sort sort) {

    if (!sort.isEmpty()) {
      for (Sort.Order order : sort) {
        return switch (order.getProperty()) {
          case "createDate" -> travelogue.createDate.desc();
          default -> travelogue.createDate.desc();
        };
      }
    }
    return travelogue.createDate.desc();
  }


  private NumberTemplate<Integer> getDays() {
    return Expressions.numberTemplate(Integer.class, "datediff(DAY,{0},{1})",
        travelogue.period.startDate, travelogue.period.endDate);
  }

  private BooleanExpression TraveloguePeriodDaysBetween(Long minDays, Long maxDays) {
    if (Objects.nonNull(minDays) && Objects.nonNull(maxDays)) {
      return getDays().between(minDays, maxDays);
    }
    return null;
  }

  private BooleanExpression lowestCostGoe(Long lowest) {
    return Objects.nonNull(lowest) ? travelogue.cost.total.goe(lowest) : null;
  }

  private BooleanExpression maximumCostLoe(Long maximum) {
    return Objects.nonNull(maximum) ? travelogue.cost.total.loe(maximum) : null;
  }

  private BooleanExpression totalCostBetween(Long lowest, Long maximum) {
    if (isAllNonNull(maximumCostLoe(maximum), lowestCostGoe(lowest))) {
      return maximumCostLoe(maximum).and(lowestCostGoe(lowest));
    }
    return null;
  }

  private boolean isAllNonNull(BooleanExpression booleanExpression1,
      BooleanExpression booleanExpression2) {
    return Objects.nonNull(booleanExpression1) && Objects.nonNull(booleanExpression2);
  }


}

