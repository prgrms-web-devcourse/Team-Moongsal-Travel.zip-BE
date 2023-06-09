package shop.zip.travel.domain.post.travelogue.service;

import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchFilter;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCreateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.domain.suggestion.entity.Suggestion;
import shop.zip.travel.domain.suggestion.repository.SuggestionRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class TravelogueService {

  private static final boolean PUBLISH = true;

  private final TravelogueRepository travelogueRepository;
  private final MemberService memberService;
  private final BookmarkRepository bookmarkRepository;
  private final SuggestionRepository suggestionRepository;

  public TravelogueService(TravelogueRepository travelogueRepository, MemberService memberService,
      BookmarkRepository bookmarkRepository, SuggestionRepository suggestionRepository) {
    this.travelogueRepository = travelogueRepository;
    this.memberService = memberService;
    this.bookmarkRepository = bookmarkRepository;
    this.suggestionRepository = suggestionRepository;
  }

  @Transactional
  public TravelogueCreateRes save(TravelogueCreateReq createReq, Long memberId) {
    Member findMember = memberService.getMember(memberId);
    Travelogue travelogue = travelogueRepository.save(createReq.toTravelogue(findMember));
    Long nights = travelogue.getPeriod().getNights();
    return TravelogueCreateRes.toDto(travelogue.getId(), nights);
  }

  public TravelogueCustomSlice<TravelogueSimpleRes> getTravelogues(Pageable pageable) {

    Slice<TravelogueSimple> travelogues =
        travelogueRepository.findAllBySlice(pageable, PUBLISH);

    return TravelogueCustomSlice.toDto(
        travelogues.map(TravelogueSimpleRes::toDto)
    );
  }

  public Travelogue getTravelogue(Long id) {
    return travelogueRepository.findById(id)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
  }

  public TravelogueCustomSlice<TravelogueSimpleRes> search(String keyword, Pageable pageable) {
    return TravelogueCustomSlice.toDto(travelogueRepository.search(keyword, pageable));
  }

  @Transactional
  public TravelogueDetailRes getTravelogueDetail(Long travelogueId, boolean canAddViewCount,
      Long memberId) {
    Long countLikes = travelogueRepository.countLikes(travelogueId);
    boolean isLiked = travelogueRepository.isLiked(memberId, travelogueId);
    boolean isBookmarked = bookmarkRepository.exists(memberId, travelogueId);

    Travelogue travelogue = travelogueRepository.getTravelogueDetail(travelogueId)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));

    updateViewCount(travelogueId, canAddViewCount);

    Suggestion suggestion = new Suggestion(travelogue.getCountry().getName(), memberId);
    suggestionRepository.save(suggestion);

    boolean isWriter = isWriter(travelogue.getMember(), memberId);

    return TravelogueDetailRes.toDto(travelogue, countLikes, isLiked, isBookmarked, isWriter);
  }

  private void updateViewCount(Long travelogueId, boolean canAddViewCount) {
    if (canAddViewCount) {
      getTravelogue(travelogueId).addViewCount();
    }
  }

  public TravelogueCustomSlice<TravelogueSimpleRes> filtering(String keyword, Pageable pageable,
      TravelogueSearchFilter searchFilter) {
    return TravelogueCustomSlice.toDto(
        travelogueRepository.filtering(keyword, pageable, searchFilter));
  }

  private boolean isWriter(Member writer, Long requestMemberId) {
    return Objects.equals(writer.getId(), requestMemberId);
  }
}
