package shop.zip.travel.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.fake.FakeTravelogue;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueUpdateRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailForUpdateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@ExtendWith(MockitoExtension.class)
class MemberMyTravelogueServiceTest {

  @Mock
  private TravelogueRepository travelogueRepository;

  @Mock
  private SubTravelogueRepository subTravelogueRepository;

  @InjectMocks
  private MemberMyTravelogueService memberMyTravelogueService;

  private Member member;
  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createFakeMember();
    travelogue = new FakeTravelogue(1L, DummyGenerator.createTravelogueWithSubTravelogues(
        List.of(
            DummyGenerator.createFakeSubTravelogue(1L, 1),
            DummyGenerator.createFakeSubTravelogue(2L, 2)
        ),
        member
    ));
  }

  @Test
  @DisplayName("자신이 작성한 게시물들을 가져올 수 있다.")
  void test_get_travelogues() {
    boolean isPublished = true;

    Member member = DummyGenerator.createMember();
    Travelogue travelogue = DummyGenerator.createTravelogue(member);

    PageRequest pageRequest = PageRequest.of(0, 2);

    SliceImpl<TravelogueSimple> travelogues = new SliceImpl<>(
        List.of(DummyGenerator.createTravelogueSimple(travelogue),
            DummyGenerator.createTravelogueSimple(travelogue)),
        pageRequest,
        pageRequest.next().isPaged()
    );

    when(travelogueRepository.getMyTravelogues(any(Long.class), any(Pageable.class),
        any(Boolean.class)))
        .thenReturn(travelogues);

    // when
    Long memberId = 1L;
    TravelogueCustomSlice<TravelogueSimpleRes> expect =
        memberMyTravelogueService.getTravelogues(memberId, pageRequest);

    int traveloguesSize = travelogues.getSize();
    int expectSize = expect.content().size();

    assertThat(traveloguesSize).isEqualTo(expectSize);

    verify(travelogueRepository, atLeastOnce())
        .getMyTravelogues(any(Long.class), any(Pageable.class), any(Boolean.class));

  }

  @Test
  @DisplayName("수정을 위한 하나의 트래블로그 정보와 서브 트래블로그 id 들을 가져올 수 있다.")
  void test_get_travelogue_for_update() {
    when(travelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(travelogue));

    List<Long> subTraveloguesId = travelogue.getSubTravelogues()
        .stream()
        .map(SubTravelogue::getId)
        .toList();

    TravelogueDetailForUpdateRes travelogueDetailForUpdateRes =
        memberMyTravelogueService.getTravelogueForUpdate(member.getId(), travelogue.getId());

    assertThat(travelogueDetailForUpdateRes.subTravelogueIds())
        .usingRecursiveComparison()
        .isEqualTo(subTraveloguesId);

    verify(travelogueRepository, atLeastOnce())
        .findById(any(Long.class));
  }

  @Test
  @DisplayName("트래블로그를 수정할 수 있다.")
  void test_update_travelogue() {
    TravelogueUpdateReq travelogueUpdateReq = new TravelogueUpdateReq(
        DummyGenerator.createTempPeriod(),
        "오사카 여행기",
        DummyGenerator.createTempCountry(),
        "www.naver.com",
        DummyGenerator.createTempCost()
    );

    when(travelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(travelogue));

    TravelogueUpdateRes travelogueUpdateRes =
        memberMyTravelogueService.updateTravelogue(
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
        List.of(DummyGenerator.createTempAddress()),
        Set.of(Transportation.SUBWAY),
        new ArrayList<>()
    );

    when(travelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(travelogue));

    when(subTravelogueRepository.findById(any(Long.class)))
        .thenReturn(Optional.of(subTravelogue));

    SubTravelogueUpdateRes subTravelogueUpdateRes =
        memberMyTravelogueService.updateSubTravelogue(travelogue.getId(),
            subTravelogue.getId(),
            member.getId(),
            subTravelogueUpdateReq
        );

    assertThat(subTravelogueUpdateRes.subTravelogueId())
        .isEqualTo(subTravelogue.getId());

  }
}