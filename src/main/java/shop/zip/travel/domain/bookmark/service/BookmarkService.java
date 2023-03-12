package shop.zip.travel.domain.bookmark.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.dto.response.BookmarkResultRes;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

  private static final boolean ADD = true;
  private static final boolean NOT_ADD = false;
  private static final boolean CANCEL = true;
  private static final boolean NOT_CANCEL = false;

  private final BookmarkRepository bookmarkRepository;
  private final TravelogueService travelogueService;
  private final MemberService memberService;

  public BookmarkService(
      BookmarkRepository bookmarkRepository,
      TravelogueService travelogueService,
      MemberService memberService
  ) {
    this.bookmarkRepository = bookmarkRepository;
    this.travelogueService = travelogueService;
    this.memberService = memberService;
  }

  @Transactional
  public BookmarkResultRes markOrCancel(Long memberId, Long travelogueId) {
    Optional<Long> bookmarkId = bookmarkRepository.getBookmarkId(memberId, travelogueId);
    if (bookmarkId.isPresent()) {
      cancelBookmark(bookmarkId.get());

      return BookmarkResultRes.toDto(NOT_ADD, CANCEL);
    }
    addBookmark(memberId, travelogueId);

    return BookmarkResultRes.toDto(ADD, NOT_CANCEL);
  }

  private void addBookmark(Long memberId, Long travelogueId) {
    Member member = memberService.getMember(memberId);
    Travelogue travelogue = travelogueService.getTravelogue(travelogueId);
    bookmarkRepository.save(new Bookmark(travelogue, member));
  }

  private void cancelBookmark(Long bookmarkId) {
    bookmarkRepository.deleteById(bookmarkId);
  }

}
