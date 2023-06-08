package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.fake.FakeTravelogue;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueUpdateRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@ExtendWith(MockitoExtension.class)
class TempTravelogueServiceTest {

  @Mock
  private TravelogueRepository travelogueRepository;

  @Mock
  private SubTravelogueRepository subTravelogueRepository;

  @InjectMocks
  private TempTravelogueService tempTravelogueService;

  private Member member;
  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createFakeMember();
    SubTravelogue subTravelogue1 = DummyGenerator.createFakeSubTravelogue(1L, 1);
    SubTravelogue subTravelogue2 = DummyGenerator.createFakeSubTravelogue(2L, 2);
    travelogue = new FakeTravelogue(1L, DummyGenerator.createTravelogueWithSubTravelogues(
        Arrays.asList(subTravelogue1, subTravelogue2), member));
  }

  @Test
  @DisplayName("트래블로그를 수정할 수 있다.")
  void test_update_travelogue() {
    TravelogueUpdateReq travelogueUpdateReq = DummyGenerator.createTravelogueUpdateReq();

    when(travelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(travelogue));

    TravelogueUpdateRes travelogueUpdateRes =
        tempTravelogueService.updateTravelogue(
            travelogue.getId(),
            member.getId(),
            travelogueUpdateReq
        );

    assertThat(travelogueUpdateRes.travelogueId())
        .isEqualTo(travelogue.getId());
  }

  @Test
  @DisplayName("서브 트래블로그를 수정할 수 있다.")
  void test_update_subTravelogue() {
    SubTravelogue subTravelogue = travelogue.getSubTravelogues().get(0);

    SubTravelogueUpdateReq subTravelogueUpdateReq = new SubTravelogueUpdateReq(
        "오사카 1일차",
        "오사카 1일차 여행기입니다.",
        1,
        new ArrayList<>(),
        Set.of(Transportation.SUBWAY),
        new ArrayList<>()
    );

    when(travelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(travelogue));

    when(subTravelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(subTravelogue));

    Long subTravelogueId = 1L;
    SubTravelogueUpdateRes subTravelogueUpdateRes =
        tempTravelogueService.updateSubTravelogue(travelogue.getId(),
            subTravelogueId,
            member.getId(),
            subTravelogueUpdateReq
        );

    assertThat(subTravelogueUpdateRes.subTravelogueId())
        .isEqualTo(subTravelogue.getId());

  }
}