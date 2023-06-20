package shop.zip.travel.domain.post.travelogue.repository.impl;

import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.StringUtils.hasText;
import static shop.zip.travel.domain.bookmark.entity.QBookmark.bookmark;
import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.subTravelogue.data.QAddress.address;
import static shop.zip.travel.domain.post.subTravelogue.entity.QSubTravelogue.subTravelogue;
import static shop.zip.travel.domain.post.travelogue.entity.QLike.like;
import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchFilter;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimpleDetail;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.QLike;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public class TravelogueRepositoryImpl extends QuerydslRepositorySupport implements
    TravelogueRepositoryQuerydsl {

  private static final int SPARE_PAGE = 1;
  private static final int SPARE_DAY = 1;
  private static final String POPULAR = "popular";

  private final JPAQueryFactory jpaQueryFactory;

  public TravelogueRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Travelogue.class);
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Slice<TravelogueSimpleRes> search(String keyword, Pageable pageable) {
    List<Long> subTravelogueIds = getSubTravelogueIds(keyword);
    List<Long> travelogueIds = getTravelogueIds(keyword, subTravelogueIds);

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
                travelogue.member.profileImageUrl,
                count(like)
            )
        )
        .from(travelogue)
        .where(travelogue.id.in(travelogueIds).and(publishedIsTrue()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + SPARE_PAGE)
        .leftJoin(travelogue.member, member)
        .leftJoin(like)
        .on(like.travelogue.id.eq(travelogue.id))
        .orderBy(travelogue.createDate.desc())
        .groupBy(travelogue.id)
        .fetch();

    List<TravelogueSimple> results = new ArrayList<>(travelogueSimpleList);

    return checkLastPage(pageable, results);
  }

  public Slice<TravelogueSimpleRes> filtering(String keyword, Pageable pageable,
      TravelogueSearchFilter searchFilter) {

    List<Long> subTravelogueIds = getSubTravelogueIds(keyword);
    List<Long> travelogueIds = getTravelogueIds(keyword, subTravelogueIds);

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
                travelogue.member.profileImageUrl,
                count(like)
            )
        )
        .from(travelogue)
        .where(travelogue.id.in(travelogueIds),
            TraveloguePeriodDaysBetween(searchFilter.minDays(), searchFilter.maxDays()),
            totalCostBetween(searchFilter.minCost(), searchFilter.maxCost()),
            travelogue.isPublished.isTrue()
        )
        .leftJoin(travelogue.member, member)
        .leftJoin(like)
        .on(like.travelogue.id.eq(travelogue.id))
        .orderBy(getOrder(pageable.getSort()), travelogue.createDate.desc())
        .groupBy(travelogue.id)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + SPARE_PAGE)
        .fetch();

    List<TravelogueSimple> results = new ArrayList<>(travelogueSimpleList);

    return checkLastPage(pageable, results);
  }

  private static BooleanExpression publishedIsTrue() {
    return travelogue.isPublished.isTrue();
  }

  private List<Long> getTravelogueIds(String keyword, List<Long> subTravelogueIds) {
    return Stream.concat(getTravelogueIds_containsSubTravelogues(subTravelogueIds).stream(),
            Stream.concat(getTravelogueIds_searchCountryName(keyword).stream(),
                getTravelogueIds_containsTitleName(keyword).stream()))
        .distinct()
        .toList();
  }

  private List<Long> getTravelogueIds_containsSubTravelogues(
      List<Long> subTravelogueIds
  ) {
    return jpaQueryFactory
        .select(travelogue.id)
        .from(travelogue)
        .leftJoin(travelogue.subTravelogues, subTravelogue)
        .where(
            publishedIsTrue(),
            subTravelogue.id.in(subTravelogueIds)
        )
        .fetch();
  }

  private List<Long> getTravelogueIds_containsTitleName(String keyword) {
    return jpaQueryFactory
        .select(travelogue.id)
        .from(travelogue)
        .where(
            publishedIsTrue(),
            titleContains(keyword)
        )
        .fetch();
  }

  private List<Long> getTravelogueIds_searchCountryName(
      String keyword
  ) {
    return jpaQueryFactory
        .select(travelogue.id)
        .from(travelogue)
        .where(
            publishedIsTrue(),
            countryContains(keyword)
        )
        .fetch();
  }

  private List<Long> getSubTravelogueIds(String keyword) {
    return Stream.concat(getSubTravelogueIds_containsContent(keyword).stream(),
            getSubTravelogueIds_containsSpot(keyword).stream())
        .distinct()
        .toList();
  }

  private List<Long> getSubTravelogueIds_containsSpot(String keyword) {
    return jpaQueryFactory
        .select(subTravelogue.id)
        .from(subTravelogue)
        .innerJoin(subTravelogue.addresses, address)
        .where(
            spotContains(keyword)
        )
        .fetch();
  }

  private List<Long> getSubTravelogueIds_containsContent(String keyword) {
    return jpaQueryFactory
        .select(subTravelogue.id)
        .from(subTravelogue)
        .innerJoin(subTravelogue.addresses, address)
        .where(
            contentContains(keyword)
        )
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
        return POPULAR.equals(order.getProperty()) ?
            like.count().desc() : travelogue.createDate.desc();
      }
    }

    return travelogue.createDate.desc();
  }

  private NumberTemplate<Integer> getDays() {
    return Expressions.numberTemplate(Integer.class, "datediff({0}, {1})",
        travelogue.period.endDate, travelogue.period.startDate);
  }

  private BooleanExpression TraveloguePeriodDaysBetween(Long minDays, Long maxDays) {
    if (Objects.nonNull(minDays) && Objects.nonNull(maxDays)) {
      return getDays().goe(minDays - SPARE_DAY)
          .and(getDays().loe(maxDays - SPARE_DAY));
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

  public boolean isLiked(Long travelogueId, Long memberId) {
    Integer countLike = jpaQueryFactory.selectOne()
        .from(QLike.like)
        .where(QLike.like.travelogue.id.eq(travelogueId)
            .and(QLike.like.member.id.eq(memberId)))
        .fetchFirst();

    return countLike != null;
  }

  public Long countLikes(Long travelogueId) {
    Long countLikes = jpaQueryFactory.select(count(like))
        .from(like)
        .where(like.travelogue.id.eq(travelogueId))
        .groupBy(like.travelogue.id)
        .fetchOne();

    return (countLikes == null) ? 0 : countLikes;
  }

  private Slice<TravelogueSimpleRes> checkLastPage(Pageable pageable,
      List<TravelogueSimple> results) {

    boolean hasNext = false;

    if (results.size() > pageable.getPageSize()) {
      hasNext = true;
      results.remove(pageable.getPageSize());
    }

    return new SliceImpl<>(results.stream()
        .map(TravelogueSimpleRes::toDto)
        .toList(), pageable, hasNext);
  }

  public Optional<TravelogueSimpleDetail> getTravelogueDetail(Long travelogueId, Long memberId) {
    return Optional.ofNullable(jpaQueryFactory.select(
            Projections.constructor(
                TravelogueSimpleDetail.class,
                travelogue,
                travelogue.member.id,
                travelogue.member.profileImageUrl,
                travelogue.member.nickname,
                ExpressionUtils.as(
                    JPAExpressions.select(count(like))
                        .from(like)
                        .where(like.travelogue.id.eq(travelogueId))
                        .groupBy(like.travelogue.id), "countLikes"),
                new CaseBuilder()
                    .when(like.member.id.eq(memberId)).then(true).otherwise(false)
                    .as("isLiked"),
                new CaseBuilder()
                    .when(bookmark.member.id.eq(memberId)).then(true).otherwise(false)
                    .as("isBookmarked")
            ))
        .from(travelogue)
        .leftJoin(travelogue.member, member)
        .leftJoin(like)
        .on(like.travelogue.id.eq(travelogue.id))
        .leftJoin(bookmark)
        .on(bookmark.travelogue.id.eq(travelogue.id))
        .where(travelogue.id.eq(travelogueId))
        .fetchOne());
  }

}

