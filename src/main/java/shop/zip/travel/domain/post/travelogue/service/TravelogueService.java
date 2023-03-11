package shop.zip.travel.domain.post.travelogue.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger log = LoggerFactory.getLogger(TravelogueService.class);

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

    setViewCount(travelogueId, canAddViewCount);
    Long countLikes = travelogueRepository.countLikes(travelogueId);
    boolean isLiked = travelogueRepository.isLiked(memberId, travelogueId);
    Boolean isBookmarked = bookmarkRepository.exists(memberId, travelogueId);

    Travelogue travelogue = travelogueRepository.getTravelogueDetail(travelogueId)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));

    Suggestion suggestion = new Suggestion(travelogue, memberId);
    suggestionRepository.save(suggestion);


    return TravelogueDetailRes.toDto(travelogue, countLikes, isLiked, isBookmarked);
  }

  private void setViewCount(Long travelogueId, boolean canAddViewCount) {
    if (canAddViewCount) {
      Travelogue findTravelogue = getTravelogue(travelogueId);
      findTravelogue.addViewCount();
    }
  }

  public TravelogueCustomSlice<TravelogueSimpleRes> filtering(String keyword, Pageable pageable,
      TravelogueSearchFilter searchFilter) {
    return TravelogueCustomSlice.toDto(
        travelogueRepository.filtering(keyword, pageable, searchFilter));
  }
}