package shop.zip.travel.domain.member.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.dto.request.MemberUpdateReq;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.exception.MemberNotFoundException;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailForUpdateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;


@Service
@Transactional(readOnly = true)
public class MemberMyPageService {

  private final TravelogueRepository travelogueRepository;
  private final MemberRepository memberRepository;
  private final BookmarkRepository bookmarkRepository;

  public MemberMyPageService(TravelogueRepository travelogueRepository,
      MemberRepository memberRepository, BookmarkRepository bookmarkRepository) {
    this.travelogueRepository = travelogueRepository;
    this.memberRepository = memberRepository;
    this.bookmarkRepository = bookmarkRepository;
  }

  public MemberInfoRes getInfoBy(Long memberId) {
    return memberRepository.getMemberInfo(memberId);
  }

  public TravelogueCustomSlice<TravelogueSimpleRes> getTravelogues(Long memberId,
      Pageable pageable) {
    Slice<TravelogueSimpleRes> travelogueSimpleRes = new SliceImpl<>(
        travelogueRepository.getMyTravelogues(memberId, pageable)
            .stream()
            .map(TravelogueSimpleRes::toDto)
            .toList());

    return TravelogueCustomSlice.toDto(travelogueSimpleRes);
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

  public TravelogueDetailForUpdateRes getTravelogueForUpdate(Long memberId, Long travelogueId) {
    Travelogue travelogue = getTravelogue(travelogueId);

    return TravelogueDetailForUpdateRes.toDto(travelogue);
  }

  private Travelogue getTravelogue(Long travelogueId) {
    return travelogueRepository.findById(travelogueId)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
  }

}


