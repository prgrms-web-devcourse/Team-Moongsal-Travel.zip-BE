package shop.zip.travel.domain.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final TravelogueService travelogueService;
  private final MemberService memberService;

  public BookmarkService(BookmarkRepository bookmarkRepository, TravelogueService travelogueService,
      MemberService memberService) {
    this.bookmarkRepository = bookmarkRepository;
    this.travelogueService = travelogueService;
    this.memberService = memberService;
  }

  @Transactional
  public void bookmarking(Long memberId, Long travelogueId) {
    if (bookmarkRepository.existsBy(memberId, travelogueId)) {
      cancelBookmark(memberId, travelogueId);

      return;
    }
    addBookmark(memberId, travelogueId);
  }

  private void addBookmark(Long memberId, Long travelogueId) {
    Member member = memberService.getMember(memberId);
    Travelogue travelogue = travelogueService.getTravelogue(travelogueId);

    bookmarkRepository.save(new Bookmark(travelogue, member));
  }

  private void cancelBookmark(Long memberId, Long travelogueId) {
    Long bookmarkId = bookmarkRepository.getBookmarkId(memberId, travelogueId);
    bookmarkRepository.deleteById(bookmarkId);
  }

}
