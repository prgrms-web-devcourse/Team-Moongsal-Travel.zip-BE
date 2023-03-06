package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
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
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.fake.FakeTravelogue;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCreateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@ExtendWith(MockitoExtension.class)
@Transactional
class TravelogueServiceTest {

  @InjectMocks
  private TravelogueService travelogueService;

  @Mock
  private TravelogueRepository travelogueRepository;

  @Mock
  private MemberService memberService;

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

		Long memberId = 1L;
		Long countLikes = 1L;
		boolean isLiked = true;

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
				isLiked);

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
		Long memberId = 1L;
		Long countLikes = 1L;
		boolean isLiked = true;

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

		long actualViewCount = 1L;

		assertThat(actualViewCount).isEqualTo(expectedTravelogueDetail.viewCount());
	}

}