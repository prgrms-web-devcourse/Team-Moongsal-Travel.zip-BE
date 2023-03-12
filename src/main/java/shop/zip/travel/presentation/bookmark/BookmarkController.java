package shop.zip.travel.presentation.bookmark;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.zip.travel.domain.bookmark.dto.response.BookmarkResultRes;
import shop.zip.travel.domain.bookmark.service.BookmarkService;
import shop.zip.travel.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/travelogues")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  public BookmarkController(BookmarkService bookmarkService) {
    this.bookmarkService = bookmarkService;
  }

  @PutMapping("/{travelogueId}/bookmarks")
  public ResponseEntity<BookmarkResultRes> markOrCancel(
      @PathVariable Long travelogueId,
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    BookmarkResultRes bookmarkResponseMsg =
        bookmarkService.markOrCancel(userPrincipal.getUserId(), travelogueId);

    return ResponseEntity.ok(bookmarkResponseMsg);
  }

}
