package shop.zip.travel.domain.bookmark.repository.impl;


import static shop.zip.travel.domain.bookmark.entity.QBookmark.bookmark;
import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
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
  public List<TravelogueSimpleRes> getBookmarkedList(Long memberId, Pageable pageable) {
    List<Long> travelogueIds = jpaQueryFactory.select(bookmark.travelogue.id)
        .from(bookmark)
        .leftJoin(bookmark.member, member)
        .where(bookmark.member.id.eq(memberId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    if (travelogueIds.isEmpty()) {
      return new ArrayList<>();
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
                travelogue.member.profileImageUrl
            )
        ).from(travelogue)
        .leftJoin(bookmark)
        .on(bookmark.travelogue.id.eq(travelogue.id))
        .leftJoin(travelogue.member, member)
        .where(travelogue.id.in(travelogueIds))
        .orderBy(bookmark.createDate.asc())
        .fetch();

    return travelogueSimpleList.stream()
        .map(TravelogueSimpleRes::toDto)
        .toList();
  }

}
