package shop.zip.travel.domain.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.zip.travel.domain.bookmark.dto.response.BookmarkResultRes;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

  @Mock
  private BookmarkRepository bookmarkRepository;

  @Mock
  private TravelogueService travelogueService;

  @Mock
  private MemberService memberService;

  @InjectMocks
  private BookmarkService bookmarkService;

  private Member member;
  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createFakeMember();
    travelogue = DummyGenerator.createTravelogue(member);
  }

  @DisplayName("유저는 북마크를 추가할 수 있다")
  @Test
  void test_add_bookmark() {
    //Given
    final boolean isAdded = true;
    final boolean isCanceled = false;
    when(bookmarkRepository.getBookmarkId(any(), any())).thenReturn(Optional.empty());
    when(memberService.getMember(any())).thenReturn(member);
    when(travelogueService.getTravelogue(any())).thenReturn(travelogue);

    BookmarkResultRes expectedResult = new BookmarkResultRes(isAdded, isCanceled);
    //When
    BookmarkResultRes actualResult =
        bookmarkService.markOrCancel(member.getId(), travelogue.getId());
    //Then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @DisplayName("유저는 북마크를 취소할 수 있다")
  @Test
  void test_cancel_bookmark() {
    //Given
    final boolean isAdded = false;
    final boolean isCanceled = true;
    final long bookmarkId = 1L;

    when(bookmarkRepository.getBookmarkId(any(), any())).thenReturn(Optional.of(bookmarkId));

    BookmarkResultRes expectedResult = new BookmarkResultRes(isAdded, isCanceled);
    //When
    BookmarkResultRes actualResult =
        bookmarkService.markOrCancel(member.getId(), travelogue.getId());
    //Then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

}