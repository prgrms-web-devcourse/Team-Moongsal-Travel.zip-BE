package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.fake.FakeTravelogue;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSearchFilter;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCreateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.domain.suggestion.repository.SuggestionRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class TravelogueServiceTest {

  @InjectMocks
  private TravelogueService travelogueService;

  @Mock
  private TravelogueRepository travelogueRepository;

  @Mock
  private BookmarkRepository bookmarkRepository;

  @Mock
  private SuggestionRepository suggestionRepository;

  @Mock
  private MemberService memberService;

  private final Long memberId = 1L;
  private final Long countLikes = 1L;
  private final boolean isLiked = true;
  private final boolean isBookmarked = false;

  private static final Member member = new Member("user@gmail.com", "password1!", "nickname",
      "1998");

  @Test
  @DisplayName("페이지로 가져온 게시글 목록을 TravelogueSimpleRes로 변경해서 전달할 수 있다.")
  public void test_get_all() {
    // given
    Travelogue travelogue = DummyGenerator.createTravelogue(member);

    List<TravelogueSimple> travelogueSimpleList = List.of(
        DummyGenerator.createTravelogueSimple(travelogue),
        DummyGenerator.createTravelogueSimple(travelogue)
    );
    PageRequest pageRequest = PageRequest.of(
        0,
        2,
        Sort.by(Sort.Direction.DESC, "createDate")
    );

    Slice<TravelogueSimple> travelogueSimples = new SliceImpl<>(
        travelogueSimpleList,
        pageRequest,
        pageRequest.next().isPaged()
    );

    when(travelogueRepository.findAllBySlice(pageRequest, true))
        .thenReturn(travelogueSimples);

    // when
    TravelogueCustomSlice<TravelogueSimpleRes> travelogueSimpleRes =
        travelogueService.getTravelogues(pageRequest);

    // then
    int expectedNights = 1;
    int expectedDays = 2;

    assertThat(travelogueSimpleRes.content().get(0).nights())
        .isEqualTo(expectedNights);

    assertThat(travelogueSimpleRes.content().get(0).days())
        .isEqualTo(expectedDays);
  }

  @Test
  @DisplayName("게시글을 작성 혹은 임시 작성 할 수 있다.")
  void test_temp_save() {
    // given
    TravelogueCreateReq tempTravelogueCreateReq = new TravelogueCreateReq(
        DummyGenerator.createTempPeriod(),
        "일본 여행은 2박 3일은 짧아요.",
        DummyGenerator.createTempCountry(),
        "www.google.com",
        DummyGenerator.createTempCost()
    );

    Travelogue travelogue = new FakeTravelogue(
        1L,
        tempTravelogueCreateReq.toTravelogue(member)
    );

    when(memberService.getMember(member.getId()))
        .thenReturn(member);

    when(travelogueRepository.save(any(Travelogue.class)))
        .thenReturn(travelogue);

    TravelogueCreateRes travelogueCreateRes = travelogueService.save(tempTravelogueCreateReq,
        member.getId());

    long actualId = travelogue.getId();
    long expected = travelogueCreateRes.id();

    assertThat(actualId).isEqualTo(expected);
  }

  @Test
  @DisplayName("메인 게시물을 상세조회 할 수 있다")
  void test_get_detail() {
    Member member = DummyGenerator.createMember();

    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogue(member)
    );

    when(travelogueRepository.findById(travelogue.getId())).thenReturn(Optional.of(travelogue));
    when(travelogueRepository.getTravelogueDetail(travelogue.getId())).thenReturn(
        Optional.of(travelogue));
    when(travelogueRepository.isLiked(travelogue.getId(), memberId))
        .thenReturn(isLiked);
    when(travelogueRepository.countLikes(travelogue.getId())).thenReturn(countLikes);

    TravelogueDetailRes expectedTravelogueDetail = travelogueService.getTravelogueDetail(
        travelogue.getId(),
        true,
        memberId);
    TravelogueDetailRes actualTravelogueDetail = TravelogueDetailRes.toDto(travelogue, countLikes,
        isLiked, isBookmarked);

    assertThat(actualTravelogueDetail).isEqualTo(expectedTravelogueDetail);
  }

  @Test
  @DisplayName("게시물을 상세조회 할 경우 조회수가 올라간다")
  void test_view_count() {
    Member member = DummyGenerator.createMember();

    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogue(member)
    );

    when(travelogueRepository.findById(travelogue.getId())).thenReturn(Optional.of(travelogue));
    when(travelogueRepository.getTravelogueDetail(travelogue.getId())).thenReturn(
        Optional.of(travelogue));
    when(bookmarkRepository.exists(any(), any())).thenReturn(isBookmarked);

		TravelogueDetailRes expectedTravelogueDetail = travelogueService.getTravelogueDetail(
				travelogue.getId(),
				true,
				memberId);

    long actualViewCount = 1L;

    assertThat(actualViewCount).isEqualTo(expectedTravelogueDetail.viewCount());
  }

  @Test
  @DisplayName("게시물을 제목으로 검색할 수 있다.")
  void test_search_by_title() {
    //given
    Member member = DummyGenerator.createMember();
    String title = "일본 갔다온 썰 푼다";
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogueWithTitle(title, member)
    );

    Pageable pageable = PageRequest.of(0, 1);
    SliceImpl<TravelogueSimpleRes> actualSearchRes = new SliceImpl<>(
        List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));
    when(travelogueRepository.search(title, pageable)).thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes = travelogueService.search(title,
        pageable);
    verify(travelogueRepository).search(title, pageable);

    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }

  @Test
  @DisplayName("게시물을 나라명으로 검색할 수 있다.")
  void test_search_by_country() {
    //given
    Member member = DummyGenerator.createMember();
    String country = "일본";
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogueWithCountry(country, member)
    );

    Pageable pageable = PageRequest.of(0, 1);
    SliceImpl<TravelogueSimpleRes> actualSearchRes = new SliceImpl<>(
        List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));
    when(travelogueRepository.search(country, pageable)).thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes = travelogueService.search(country,
        pageable);
    verify(travelogueRepository).search(country, pageable);
    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }

  @Test
  @DisplayName("게시물을 subTravelogue 의 내용으로 검색할 수 있다.")
  void test_search_by_content() {
    //given
    Member member = DummyGenerator.createMember();
    String content = "일본에가면 가장 먹고싶은건 오꼬노미야끼이다.";
    SubTravelogue subTravelogue = DummyGenerator.createSubTravelogueWithContent(content);
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogueWithSubTravelogues(List.of(subTravelogue), member)
    );

    Pageable pageable = PageRequest.of(0, 1);
    SliceImpl<TravelogueSimpleRes> actualSearchRes = new SliceImpl<>(
        List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));
    when(travelogueRepository.search(content, pageable)).thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes = travelogueService.search(content,
        pageable);
    verify(travelogueRepository).search(content, pageable);
    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }

  @Test
  @DisplayName("게시물을 subTravelogue 의 장소로 검색할 수 있다.")
  void test_search_by_region() {
    //given
    Member member = DummyGenerator.createMember();
    String region = "디즈니랜드";
    SubTravelogue subTravelogue = DummyGenerator.createSubTravelogueWithRegion(region);
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogueWithSubTravelogues(List.of(subTravelogue), member)
    );

    Pageable pageable = PageRequest.of(0, 1);
    SliceImpl<TravelogueSimpleRes> actualSearchRes = new SliceImpl<>(
        List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));
    when(travelogueRepository.search(region, pageable)).thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes = travelogueService.search(region,
        pageable);
    verify(travelogueRepository).search(region, pageable);

    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }

  @Test
  @DisplayName("게시물을 여행기간으로 필터링하여 검색할 수 있다")
  void test_search_by_period() {
    //given
    Member member = DummyGenerator.createMember();
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogue(member)
    );
    String searchKeyword = travelogue.getTitle();
    Pageable pageable = PageRequest.of(0, 1);
    Long minDays = 1L;
    Long maxDays = 3L;

    TravelogueSearchFilter searchFilter =
        new TravelogueSearchFilter(minDays, maxDays, null, null);

    SliceImpl<TravelogueSimpleRes> actualSearchRes =
        new SliceImpl<>(List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));

    when(travelogueRepository.filtering(searchKeyword, pageable, searchFilter))
        .thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes =
        travelogueService.filtering(searchKeyword, pageable, searchFilter);
    verify(travelogueRepository).filtering(searchKeyword, pageable, searchFilter);

    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }

  @Test
  @DisplayName("게시물을 금액범위로 필터링하여 검색할 수 있다")
  void test_search_by_cost() {
    //given
    Member member = DummyGenerator.createMember();
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogue(member)
    );
    String searchKeyword = travelogue.getTitle();
    Pageable pageable = PageRequest.of(0, 1);
    Long minCost = 0L;
    Long maxCost = travelogue.getCost().getTotal();

    TravelogueSearchFilter searchFilter =
        new TravelogueSearchFilter(null, null, minCost, maxCost);

    SliceImpl<TravelogueSimpleRes> actualSearchRes =
        new SliceImpl<>(List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));

    when(travelogueRepository.filtering(searchKeyword, pageable, searchFilter))
        .thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes =
        travelogueService.filtering(searchKeyword, pageable, searchFilter);
    verify(travelogueRepository).filtering(searchKeyword, pageable, searchFilter);

    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }

  @Test
  @DisplayName("게시물을 금액범위와 여행기간으로 필터링하여 검색할 수 있다")
  void test_search_by_filter() {
    //given
    Member member = DummyGenerator.createMember();
    Travelogue travelogue = new FakeTravelogue(
        1L,
        DummyGenerator.createTravelogue(member)
    );
    String searchKeyword = travelogue.getTitle();
    Pageable pageable = PageRequest.of(0, 1);
    Long lowest = 0L;
    Long maximum = travelogue.getCost().getTotal();
    Long minDays = 0L;
    Long maxDays = 2L;

    TravelogueSearchFilter searchFilter =
        new TravelogueSearchFilter(minDays, maxDays, lowest, maximum);

    SliceImpl<TravelogueSimpleRes> actualSearchRes =
        new SliceImpl<>(List.of(DummyGenerator.createTravelogueSimpleRes(travelogue)));

    when(travelogueRepository.filtering(searchKeyword, pageable, searchFilter))
        .thenReturn(actualSearchRes);

    //when
    TravelogueCustomSlice<TravelogueSimpleRes> expectedSearchRes =
        travelogueService.filtering(searchKeyword, pageable, searchFilter);
    verify(travelogueRepository).filtering(searchKeyword, pageable, searchFilter);

    //then
    assertThat(TravelogueCustomSlice.toDto(actualSearchRes)).isEqualTo(expectedSearchRes);
  }


}