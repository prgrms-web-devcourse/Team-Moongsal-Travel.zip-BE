package shop.zip.travel.domain.member.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
public class MemberMyPageService {

  private final TravelogueRepository travelogueRepository;
  private final MemberRepository memberRepository;

  public MemberMyPageService(TravelogueRepository travelogueRepository,
    MemberRepository memberRepository) {
    this.travelogueRepository = travelogueRepository;
    this.memberRepository = memberRepository;
  }

  private void checkMemberExist(Long id) {
    if (memberRepository.existsById(id)) {
      return;
    }
    throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
  }

  public MemberInfoRes getInfoBy(Long memberId) {
    checkMemberExist(memberId);
    return memberRepository.getMemberInfo(memberId);
  }

  public Slice<TravelogueSimpleRes> getTravelogues(Long memberId, Pageable pageable) {
    return new SliceImpl<>(travelogueRepository.getMyTravelogues(memberId, pageable)
      .stream()
      .map(TravelogueSimpleRes::toDto)
      .toList());
  }

}


