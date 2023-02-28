package shop.zip.travel.domain.member.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import shop.zip.travel.domain.member.dto.request.MemberUpdateReq;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class MemberMyPageService {

  private static final String NICK_NAME_PATTERN = "^[가-힣|a-zA-Z]{2,12}$";

  private final TravelogueRepository travelogueRepository;
  private final MemberRepository memberRepository;

  public MemberMyPageService(TravelogueRepository travelogueRepository,
    MemberRepository memberRepository) {
    this.travelogueRepository = travelogueRepository;
    this.memberRepository = memberRepository;
  }

  public MemberInfoRes getInfoBy(Long memberId) {
    return memberRepository.getMemberInfo(memberId);
  }

  public Slice<TravelogueSimpleRes> getTravelogues(Long memberId, Pageable pageable) {
    return new SliceImpl<>(travelogueRepository.getMyTravelogues(memberId, pageable)
      .stream()
      .map(TravelogueSimpleRes::toDto)
      .toList());
  }

  public MemberInfoRes updateMemberProfile(Long memberId, MemberUpdateReq memberUpdateReq) {
    Member member = getMember(memberId);
    updateProfileImageUrl(memberUpdateReq, member);
    updateNickname(memberUpdateReq, member);

    return MemberInfoRes.toDto(memberRepository.save(member));

  }

  private void updateNickname(MemberUpdateReq memberUpdateReq, Member member) {
    if (StringUtils.isNotBlank(memberUpdateReq.nickname())) {
      Assert.isTrue(memberUpdateReq.nickname()
        .matches(NICK_NAME_PATTERN), "닉네임이 형식에 맞지 않습니다");
      member.updateNickname(memberUpdateReq.nickname());
    }
  }

  private void updateProfileImageUrl(MemberUpdateReq memberUpdateReq, Member member) {
    if (StringUtils.isNotBlank(memberUpdateReq.profileImageUrl())) {
      member.updateProfileImageUrl(memberUpdateReq.profileImageUrl());
    }
  }

  private Member getMember(Long id) {
    return memberRepository.findById(id)
      .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
  }

}


