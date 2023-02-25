package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.CustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@ExtendWith(MockitoExtension.class)
class TravelogueServiceTest {

	@InjectMocks
	private TravelogueService travelogueService;

	@Mock
	private TravelogueRepository travelogueRepository;

	@Test
	@DisplayName("페이지로 가져온 게시글 목록을 TravelogueSimpleRes로 변경해서 전달할 수 있다.")
	public void test_get_all() {
		// given
		Member member = new Member("user@gmail.com", "password1!", "nickname");

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

		doReturn(travelogueSimples).when(travelogueRepository)
			.findAllBySlice(pageRequest);

		// when
		CustomSlice<TravelogueSimpleRes> travelogueSimpleRes = travelogueService.getTravelogues(
			pageRequest.getPageNumber(), pageRequest.getPageSize()
		);

		// then
		int expectedNights = 1;
		int expectedDays = 2;

		assertThat(travelogueSimpleRes.content().get(0).nights())
			.isEqualTo(expectedNights);

		assertThat(travelogueSimpleRes.content().get(0).days())
			.isEqualTo(expectedDays);
	}
}