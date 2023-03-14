package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.TempTravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@ExtendWith(MockitoExtension.class)
class TravelogueMyTempServiceTest {

  @Mock
  private TravelogueRepository travelogueRepository;

  @InjectMocks
  private TravelogueMyTempService travelogueTempService;

  @Test
  @DisplayName("자신이 작성 중이던 임시 저장 글들을 불러올 수 있다.")
  void test_get_all_my_travelogue() {
    // given
    Member member = DummyGenerator.createMember();
    Travelogue travelogue = DummyGenerator.createTravelogue(member);

    List<TempTravelogueSimple> travelogues = List.of(
        DummyGenerator.createTempTravelogueSimple(travelogue),
        DummyGenerator.createTempTravelogueSimple(travelogue)
    );

    PageRequest pageRequest = PageRequest.of(
        0,
        2,
        Sort.by(Direction.DESC, "createDate")
    );

    Slice<TempTravelogueSimple> actual = new SliceImpl<>(
        travelogues,
        pageRequest,
        pageRequest.next().isPaged()
    );

    when(travelogueRepository.getMyTempTravelogues(any(Long.class), any(Pageable.class),
        any(Boolean.class)))
        .thenReturn(actual);

    // when
    Long memberId = 1L;
    TravelogueCustomSlice<TempTravelogueSimpleRes> result =
        travelogueTempService.getMyTempTravelogues(memberId, pageRequest);

    // then
    int actualContentSize = actual.getContent().size();
    int resultContentSize = result.content().size();
    assertThat(actualContentSize).isEqualTo(resultContentSize);

    verify(travelogueRepository)
        .getMyTempTravelogues(any(Long.class), any(Pageable.class), any(Boolean.class));
  }
}