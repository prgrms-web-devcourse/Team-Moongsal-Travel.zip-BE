package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.List;
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
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
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

	@Test
	@DisplayName("페이지로 가져온 게시글 목록을 TravelogueSimpleRes로 변경해서 전달할 수 있다.")
	public void test_get_all() {
		// given
		Member member = new Member("user@gmail.com", "password1!", "nickname", "1998");

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

		when(travelogueRepository.findAllBySlice(pageRequest))
			.thenReturn(travelogueSimples);

		// when
		String sortField = "createDate";
		TravelogueCustomSlice<TravelogueSimpleRes> travelogueSimpleRes = travelogueService.getTravelogues(
			pageRequest.getPageNumber(), pageRequest.getPageSize(), sortField
		);

		// then
		int expectedNights = 1;
		int expectedDays = 2;

		assertThat(travelogueSimpleRes.content().get(0).nights())
			.isEqualTo(expectedNights);

		assertThat(travelogueSimpleRes.content().get(0).days())
			.isEqualTo(expectedDays);
	}

	@Test
	@DisplayName("메인 게시물을 저장할 수 있다.")
	void test_save_travelogue() {
		// given
		TravelogueCreateReq travelogueCreateReq = new TravelogueCreateReq(
			DummyGenerator.createPeriod(),
			"메인 게시물 제목",
			DummyGenerator.createCountry(),
			"www.naver.com",
			DummyGenerator.createCost()
		);

		Member member = DummyGenerator.createMember();
		Travelogue travelogue = new FakeTravelogue(
			1L,
			DummyGenerator.createTravelogue(member),
			member
		);

		when(memberService.getMember(1L))
			.thenReturn(member);

		when(travelogueRepository.save(any(Travelogue.class)))
			.thenReturn(travelogue);

		// when
		long expectedId = travelogueService.save(travelogueCreateReq, 1L)
			.id();

		// then
		long actualId = travelogue.getId();
		assertThat(actualId).isEqualTo(expectedId);
	}
}