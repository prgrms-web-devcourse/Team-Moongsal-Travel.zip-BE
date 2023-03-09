package shop.zip.travel.domain.post.travelogue.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.dto.res.LikeResultRes;
import shop.zip.travel.domain.post.travelogue.entity.Like;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueLikeRepository;

@Service
@Transactional(readOnly = true)
public class TravelogueLikeService {

  private static final boolean ADD = true;
  private static final boolean NOT_ADD = false;
  private static final boolean CANCEL = true;
  private static final boolean NOT_CANCEL = false;

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
  public LikeResultRes liking(Long memberId, Long travelogueId) {
    Optional<Long> hasLiked = travelogueLikeRepository.findByMemberAndTravelogue(memberId,
        travelogueId);
    if (hasLiked.isPresent()) {
      cancelLike(hasLiked.get());
      return LikeResultRes.toDto(NOT_ADD, CANCEL);
    }
    addLike(memberId, travelogueId);
    return LikeResultRes.toDto(ADD, NOT_CANCEL);
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
