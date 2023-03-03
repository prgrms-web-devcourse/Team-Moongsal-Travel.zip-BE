package shop.zip.travel.domain.post.travelogue.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.entity.Like;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueLikeRepository;

@ExtendWith(MockitoExtension.class)
class TravelogueLikeServiceTest {

  @Mock
  private MemberService memberService;

  @Mock
  private TravelogueService travelogueService;

  @Mock
  private TravelogueLikeRepository travelogueLikeRepository;

  @InjectMocks
  private TravelogueLikeService travelogueLikeService;

  @Test
  @DisplayName("좋아요를 누를 수 있다.")
  void test_add_like() {
    // given
    when(travelogueLikeRepository.findByMemberAndTravelogue(any(Long.class), any(Long.class)))
        .thenReturn(Optional.empty());

    Long travelogueId = 1L;
    Long memberId = 1L;

    // when
    travelogueLikeService.liking(memberId, travelogueId);

    verify(travelogueLikeRepository, atLeastOnce()).save(any(Like.class));
    verify(travelogueService, atLeastOnce()).findBy(travelogueId);
    verify(memberService, atLeastOnce()).getMember(memberId);
  }

  @Test
  @DisplayName("좋아요를 취소할 수 있다.")
  void test_cancel_like() {
    // given
    Long likeId = 1L;

    when(travelogueLikeRepository.findByMemberAndTravelogue(any(Long.class), any(Long.class)))
        .thenReturn(Optional.of(likeId));

    Long travelogueId = 1L;
    Long memberId = 1L;

    // when
    travelogueLikeService.liking(memberId, travelogueId);

    verify(travelogueLikeRepository, atLeastOnce()).deleteById(likeId);
  }
}