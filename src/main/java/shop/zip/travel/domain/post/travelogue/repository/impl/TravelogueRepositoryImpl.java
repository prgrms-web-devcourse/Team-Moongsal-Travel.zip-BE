package shop.zip.travel.domain.post.travelogue.repository.impl;

import static shop.zip.travel.domain.member.entity.QMember.member;
import static shop.zip.travel.domain.post.travelogue.entity.QTravelogue.travelogue;
import static shop.zip.travel.domain.post.subTravelogue.entity.QSubTravelogue.subTravelogue;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
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
    List<TravelogueSimple> travelogueSimples = jpaQueryFactory
        .select(Projections.fields(TravelogueSimple.class,
            travelogue.title,
            travelogue.period.startDate,
            travelogue.period.endDate,
            travelogue.cost,
            travelogue.country,
            travelogue.thumbnail,
            travelogue.member.nickname,
            travelogue.member.profileImageUrl))
        .from(travelogue)
        .join(travelogue.member, member).fetchJoin()
        .join(travelogue.subTravelogues, subTravelogue).fetchJoin()
        .where(travelogue.title.contains(keyword)
            .or(travelogue.subTravelogues.any().title.contains(keyword)))
        .offset(pageRequest.getOffset())
        .limit(pageRequest.getPageSize() + LAST_CHECK_NUM)
        .orderBy(travelogue.createDate.desc())
        .fetch();

    List<TravelogueSimpleRes> travelogueSimpleRes = new ArrayList<>();

    for (TravelogueSimple travelogueSimple : travelogueSimples) {
      travelogueSimpleRes.add(TravelogueSimpleRes.toDto(travelogueSimple));
    }

    boolean hasNext = false;
    if (travelogueSimpleRes.size() > pageRequest.getPageSize()) {
      travelogueSimpleRes.remove(pageRequest.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(travelogueSimpleRes, pageRequest, hasNext);
  }
}
