package shop.zip.travel.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.dto.request.MemberUpdateReq;
import shop.zip.travel.domain.member.dto.response.MemberInfoRes;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.fake.FakeTravelogue;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

@ExtendWith(MockitoExtension.class)
class MemberMyPageServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private BookmarkRepository bookmarkRepository;

  @InjectMocks
  private MemberMyPageService memberMyPageService;

  private Member member;
  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createFakeMember();
    travelogue = new FakeTravelogue(1L, DummyGenerator.createTravelogueWithSubTravelogues(
        List.of(
            DummyGenerator.createFakeSubTravelogue(1L, 1),
            DummyGenerator.createFakeSubTravelogue(2L, 2)
        ),
        member
    ));
  }

  @DisplayName("유저는 프로필 정보(닉네임과 프로필 이미지)를 수정할 수 있다")
  @Test
  void test_update_member_profile() {
    //Given
    final String nickname = "변경된닉네임";
    final String profileUrl = "www.changeUrl.com";
    final Long memberId = member.getId();
    MemberUpdateReq memberUpdateReq = new MemberUpdateReq(profileUrl, nickname);

    when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(member));
    member.update(nickname, profileUrl);
    when(memberRepository.save(any())).thenReturn(member);

    MemberInfoRes expectedMemberInfoRes = new MemberInfoRes(
        member.getEmail(),
        member.getNickname(),
        member.getBirthYear(),
        member.getProfileImageUrl()
    );
    //When
    MemberInfoRes actualMemberInfoRes =
        memberMyPageService.updateMemberProfile(memberId, memberUpdateReq);
    //Then
    assertThat(actualMemberInfoRes).isEqualTo(expectedMemberInfoRes);
  }

  @DisplayName("유저는 북마크한 게시물들을 목록 조회 할 수 있다")
  @Test
  void test_get_my_bookmarked_list() {
    //Given
    final Pageable pageable = PageRequest.of(0, 5);

    TravelogueSimpleRes travelogueSimpleRes =
        DummyGenerator.createTravelogueSimpleRes(travelogue);

    SliceImpl<TravelogueSimpleRes> travelogueSimpleResList =
        new SliceImpl<>(List.of(travelogueSimpleRes));

    TravelogueCustomSlice<TravelogueSimpleRes> expectedResult =
        TravelogueCustomSlice.toDto(travelogueSimpleResList);

    when(bookmarkRepository.getBookmarkedList(any(), any())).thenReturn(travelogueSimpleResList);

    //When
    TravelogueCustomSlice<TravelogueSimpleRes> actualResult =
        memberMyPageService.getMyBookmarkedList(member.getId(), pageable);

    //Then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

}