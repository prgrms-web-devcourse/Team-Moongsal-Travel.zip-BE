package shop.zip.travel.domain.post.travelogue.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.entity.Like;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueLikeRepository;

@Service
@Transactional(readOnly = true)
public class TravelogueLikeService {

  private final MemberService memberService;
  private final TravelogueService travelogueService;
  private final TravelogueLikeRepository travelogueLikeRepository;

  public TravelogueLikeService(MemberService memberService, TravelogueService travelogueService,
      TravelogueLikeRepository travelogueLikeRepository) {
    this.memberService = memberService;
    this.travelogueService = travelogueService;
    this.travelogueLikeRepository = travelogueLikeRepository;
  }

  @Transactional
  public void liking(Long memberId, Long travelogueId) {
    Optional<Long> hasLiked = travelogueLikeRepository.findByMemberAndTravelogue(memberId,
        travelogueId);
    if (hasLiked.isPresent()) {
      cancelLike(hasLiked.get());
      return;
    }
    addLike(memberId, travelogueId);
  }

  private void addLike(Long memberId, Long travelogueId) {
    Member member = memberService.getMember(memberId);
    Travelogue travelogue = travelogueService.getTravelogue(travelogueId);

    travelogueLikeRepository.save(new Like(travelogue, member));
  }

  private void cancelLike(Long likeId) {
    travelogueLikeRepository.deleteById(likeId);
  }
}
