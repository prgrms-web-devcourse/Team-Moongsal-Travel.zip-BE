package shop.zip.travel.domain.bookmark.repository.impl;


import static com.querydsl.core.types.ExpressionUtils.count;
import static shop.zip.travel.domain.bookmark.entity.QBookmark.bookmark;
import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.travelogue.entity.QLike.like;
import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.querydsl.BookmarkRepositoryQuerydsl;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;

public class BookmarkRepositoryImpl extends QuerydslRepositorySupport
    implements BookmarkRepositoryQuerydsl {

  private final JPAQueryFactory jpaQueryFactory;

  public BookmarkRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Bookmark.class);
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Boolean exists(Long memberId, Long travelogueId) {
    Integer bookmarked = jpaQueryFactory
        .selectOne()
        .from(bookmark)
        .where(bookmark.travelogue.id.eq(travelogueId)
            .and(bookmark.member.id.eq(memberId)))
        .fetchFirst();

    return bookmarked != null;
  }

  @Override
  public Slice<TravelogueSimpleRes> getBookmarkedList(Long memberId, Pageable pageable) {
    List<Long> travelogueIds = jpaQueryFactory.select(bookmark.travelogue.id)
        .from(bookmark)
        .leftJoin(bookmark.member, member)
        .where(bookmark.member.id.eq(memberId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    if (travelogueIds.isEmpty()) {
      return new SliceImpl<>(Collections.emptyList());
    }

    List<TravelogueSimple> travelogueSimpleList = jpaQueryFactory.select(
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
        ).from(travelogue)
        .leftJoin(bookmark)
        .on(bookmark.travelogue.id.eq(travelogue.id))
        .leftJoin(travelogue.member, member)
        .leftJoin(like)
        .on(like.travelogue.id.eq(travelogue.id))
        .where(travelogue.id.in(travelogueIds))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .orderBy(bookmark.createDate.asc())
        .groupBy(travelogue.id)
        .fetch();

    List<TravelogueSimple> results = new ArrayList<>();
    results.addAll(travelogueSimpleList);

    return checkLastPage(pageable, results);
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

}
