package shop.zip.travel.domain.bookmark.service;

import org.springframework.stereotype.Service;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@Service
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
  
}
