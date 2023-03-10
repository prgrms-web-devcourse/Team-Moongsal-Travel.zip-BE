package shop.zip.travel.domain.post.subTravelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.fake.FakeSubTravelogue;
import shop.zip.travel.domain.post.fake.FakeTravelogue;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueCreateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueCreateRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@ExtendWith(MockitoExtension.class)
@Transactional
class SubTravelogueServiceTest {

  @Mock
  private SubTravelogueRepository subTravelogueRepository;

  @Mock
  private TravelogueService travelogueService;

  @InjectMocks
  private SubTravelogueService subTravelogueService;

  private static final int day = 1;

  @Test
  @DisplayName("요청된 데이터를 통해 하나의 SubTravelogue를 저장할 수 있다.")
  void test_save_subTravelogue() {
    // given
    SubTravelogueCreateReq subTravelogueCreateReq = new SubTravelogueCreateReq(
        "유니버셜 스튜디오 다녀옴.",
        "유니버셜 스튜디오에서는 해리포터 테마가 필수임.",
        day,
        List.of(DummyGenerator.createTempAddress()),
        Set.of(Transportation.SUBWAY),
        List.of(new TravelPhotoCreateReq("www.naver.com"))
    );

    Member member = DummyGenerator.createMember();

    Travelogue travelogue = new FakeTravelogue(1L, DummyGenerator.createTempTravelogue(member));

    SubTravelogue actual = new FakeSubTravelogue(1L, subTravelogueCreateReq.toSubTravelogue());

    when(travelogueService.getTravelogue(travelogue.getId()))
        .thenReturn(travelogue);

    when(subTravelogueRepository.save(any(SubTravelogue.class)))
        .thenReturn(actual);

    // when
    SubTravelogueCreateRes expected = subTravelogueService.save(subTravelogueCreateReq,
        travelogue.getId());

    // then
    assertThat(expected.id()).isEqualTo(actual.getId());
  }
}