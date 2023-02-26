package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.config.QuerydslConfig;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@Import(QuerydslConfig.class)
class TravelogueControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TravelogueRepository travelogueRepository;

	@Autowired
	private MemberRepository memberRepository;


	@BeforeEach
	void setUp(){
		Member member = new Member("user@gmail.com", "password123!", "nickname", 1998);
		memberRepository.save(member);
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
	}

	@Test
	@DisplayName("전체 게시물 리스트를 페이지별로 가져올 수 있다.")
	public void test_get_all_travelogue() throws Exception {
		mockMvc.perform(get("/api/travelogues")
				.queryParam("size", "2")
				.queryParam("page", "0"))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("get-all-travelogue",
				responseFields(
					fieldWithPath("content[].title").description("Travelogue 제목"),
					fieldWithPath("content[].nights").description("몇박 몇일 중 몇박에 해당하는 값"),
					fieldWithPath("content[].days").description("몇박 몇일 중 몇일에 해당하는 값"),
					fieldWithPath("content[].totalCost").description("여행 전체 비용"),
					fieldWithPath("content[].country").description("방문한 나라"),
					fieldWithPath("content[].thumbnail").description("썸네일 링크"),
					fieldWithPath("content[].member.nickname").description("작성자 닉네임"),
					fieldWithPath("content[].member.profileImageUrl").description("작성자 프로필 이미지 링크"),
					fieldWithPath("pageable.sort.empty").description("데이터가 비어있는지에 대한 여부"),
					fieldWithPath("pageable.sort.sorted").description("데이터가 정렬되어있는지에 대한 여부"),
					fieldWithPath("pageable.sort.unsorted").description("데이터가 정렬되어 있지 않은지에 대한 여부"),
					fieldWithPath("pageable.offset").description("사용자가 마지막으로 본 위치?"),
					fieldWithPath("pageable.pageNumber").description("현재 요청한 페이지 넘버"),
					fieldWithPath("pageable.pageSize").description("요청한 데이터 갯수"),
					fieldWithPath("pageable.paged").description("페이징이 된 여부?"),
					fieldWithPath("pageable.unpaged").description("페이징이 되지 않은 여부?"),
					fieldWithPath("size").description("객체의 사이즈?"),
					fieldWithPath("number").description("????"),
					fieldWithPath("numberOfElements").description("게시물 갯수"),
					fieldWithPath("first").description("첫번째 페이지인지의 여부"),
					fieldWithPath("last").description("마지막 페이지인지의 여부"),
					fieldWithPath("empty").description("데이터가 없는지의 여부")
				)));
	}
}