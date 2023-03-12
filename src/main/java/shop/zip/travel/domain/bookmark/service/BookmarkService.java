package shop.zip.travel.domain.bookmark.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.dto.response.BookmarkRes;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

  private static final String CANCEL = "북마크가 취소되었습니다";
  private static final String ADD = "북마크에 추가되었습니다";

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
  public BookmarkRes bookmarking(Long memberId, Long travelogueId) {
    Optional<Long> bookmarkId = bookmarkRepository.getBookmarkId(memberId, travelogueId);
    String message = bookmarkId.isPresent() ?
        cancelBookmark(bookmarkId.get()) : addBookmark(memberId, travelogueId);

    return new BookmarkRes(message);
  }

  private String addBookmark(Long memberId, Long travelogueId) {
    Member member = memberService.getMember(memberId);
    Travelogue travelogue = travelogueService.getTravelogue(travelogueId);
    bookmarkRepository.save(new Bookmark(travelogue, member));

    return ADD;
  }

  private String cancelBookmark(Long bookmarkId) {
    bookmarkRepository.deleteById(bookmarkId);

    return CANCEL;
  }

}
