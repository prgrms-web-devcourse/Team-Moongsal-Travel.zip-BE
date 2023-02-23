package shop.zip.travel.domain.post.travelogue.repository.impl;

import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;
import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.subTravelogue.entity.QSubTravelogue.subTravelogue;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.member.entity.QMember;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchRes;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.QTravelogue;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.querydsl.TravelogueRepositoryQuerydsl;

@Repository
public class TravelogueRepositoryImpl extends QuerydslRepositorySupport implements
    TravelogueRepositoryQuerydsl {

  private static final long LAST_CHECK_NUM = 1L;

  private final JPAQueryFactory jpaQueryFactory;

  public TravelogueRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    super(Travelogue.class);
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Slice<TravelogueSimpleRes> search(String keyword, PageRequest pageRequest) {
    return null;
  }

//  @Override
//  public Slice<TravelogueSimpleRes> search(String keyword, PageRequest pageRequest) {
//    List<TravelogueSimpleRes> travelogues = jpaQueryFactory
//        .select(Projections.fields(TravelogueSimpleRes.class,
//            travelogue.title,
//            travelogue.) travelogue)
//        .from(travelogue)
//        .join(travelogue.member, member).fetchJoin()
//        .where(travelogue.title.contains(keyword))
//        .offset(pageRequest.getOffset())
//        .limit(pageRequest.getPageSize() + LAST_CHECK_NUM)
//        .orderBy(travelogue.createDate.desc())
//        .fetch();
//    return null;
//  }
}
