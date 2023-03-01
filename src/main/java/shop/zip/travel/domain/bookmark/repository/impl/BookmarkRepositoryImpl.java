package shop.zip.travel.domain.bookmark.repository.impl;


import static shop.zip.travel.domain.bookmark.entity.QBookmark.bookmark;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.querydsl.BookmarkRepositoryQuerydsl;

public class BookmarkRepositoryImpl extends QuerydslRepositorySupport
  implements BookmarkRepositoryQuerydsl {

  private final JPAQueryFactory jpaQueryFactory;

  public BookmarkRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Bookmark.class);
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public boolean existsBy(Long memberId, Long travelogueId) {
    Integer fetchOne = jpaQueryFactory.selectOne()
      .from(bookmark)
      .where(bookmark.travelogue.id.eq(travelogueId)
        .and(bookmark.member.id.eq(memberId)))
      .fetchFirst();

    return fetchOne != null;
  }
}
