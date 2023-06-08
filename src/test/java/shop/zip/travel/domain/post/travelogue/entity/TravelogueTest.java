package shop.zip.travel.domain.post.travelogue.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.req.CostCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.req.CountryCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.req.PeriodCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;

class TravelogueTest {

  private Member member;
  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createMember();
    travelogue = DummyGenerator.createTravelogue(member);
  }

  @Test
  @DisplayName("하나의 트래블로그를 수정할 수 있다.")
  void test_update_travelogue() {
    TravelogueUpdateReq travelogueUpdate = new TravelogueUpdateReq(
        new PeriodCreateReq(
            LocalDate.of(2021, 2, 3),
            LocalDate.of(2021, 2, 4)),
        "수정 제목",
        new CountryCreateReq("미국"),
        "www.google.com",
        new CostCreateReq(
            0L, 0L, 0L, 10000000L
        )
    );

    travelogue.update(travelogueUpdate);

    assertThat(travelogue.getPeriod()).usingRecursiveComparison()
        .isEqualTo(travelogueUpdate.getPeriod());
    assertThat(travelogue.getTitle())
        .isEqualTo(travelogueUpdate.getTitle());
    assertThat(travelogue.getCountry()).usingRecursiveComparison()
        .isEqualTo(travelogueUpdate.getCountry());
    assertThat(travelogue.getCost()).usingRecursiveComparison()
        .isEqualTo(travelogueUpdate.getCost());
    assertThat(travelogue.getThumbnail())
        .isEqualTo(travelogueUpdate.getThumbnail());

    assertThat(travelogue.getIsPublished()).isFalse();
  }

  @Test
  @DisplayName("하나의 서브 트래블로그를 수정할 수 있다.")
  void test_update_subTravelogue() {
    SubTravelogue newSubTravelogue = new SubTravelogue(
        "수정 제목",
        "수정 내용",
        1,
        new ArrayList<>(),
        Set.of(Transportation.CAR),
        new ArrayList<>()
    );

    travelogue.updateSubTravelogues(newSubTravelogue);

    SubTravelogue afterUpdate = travelogue.getSubTravelogues().get(0);

    assertThat(afterUpdate).usingRecursiveComparison()
        .isEqualTo(newSubTravelogue);
  }

}