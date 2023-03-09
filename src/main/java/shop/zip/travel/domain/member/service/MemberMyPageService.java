package shop.zip.travel.domain.member.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.dto.request.MemberUpdateReq;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.global.error.ErrorCode;


@Service
@Transactional(readOnly = true)
public class MemberMyPageService {

  private final MemberRepository memberRepository;
  private final BookmarkRepository bookmarkRepository;

  public MemberMyPageService(MemberRepository memberRepository,
      BookmarkRepository bookmarkRepository) {
    this.memberRepository = memberRepository;
    this.bookmarkRepository = bookmarkRepository;
  }

  public MemberInfoRes getInfoBy(Long memberId) {
    return memberRepository.getMemberInfo(memberId);
  }

  @Transactional
  public MemberInfoRes updateMemberProfile(Long memberId, MemberUpdateReq memberUpdateReq) {
    Member member = getMember(memberId);
    member.updateProfileImageUrl(memberUpdateReq.profileImageUrl());
    member.updateNickname(memberUpdateReq.nickname());

    return MemberInfoRes.toDto(memberRepository.save(member));
  }

  private Member getMember(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
  }

  public List<TravelogueSimpleRes> getMyBookmarkedList(Long memberId, Pageable pageable) {
    return bookmarkRepository.getBookmarkedList(memberId, pageable);
  }


}


