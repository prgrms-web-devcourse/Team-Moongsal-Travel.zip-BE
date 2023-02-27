package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.config.QuerydslConfig;
import shop.zip.travel.global.security.JwtTokenProvider;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@Import(QuerydslConfig.class)
class TravelogueControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TravelogueRepository travelogueRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  private Member member;

  @BeforeEach
  void setUp() {
    member = new Member("user@gmail.com", "password123!", "nickname", "1998");
    memberRepository.save(member);
    travelogueRepository.save(DummyGenerator.createTravelogue(member));
    travelogueRepository.save(DummyGenerator.createTravelogue(member));
    travelogueRepository.save(DummyGenerator.createTravelogue(member));
    travelogueRepository.save(DummyGenerator.createTravelogue(member));
  }

  @Test
  @DisplayName("전체 게시물 리스트를 페이지별로 가져올 수 있다.")
  public void test_get_all_travelogue() throws Exception {
    String token = "Bearer " + jwtTokenProvider.createToken(member.getId());

    mockMvc.perform(get("/api/travelogues")
            .header("AccessToken", token)
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

  @Test
  @DisplayName("메인 게시물을 저장할 수 있다.")
  public void test_save_travelogue() throws Exception {
    // given
    TravelogueCreateReq travelogueCreateReq = new TravelogueCreateReq(
        DummyGenerator.createPeriod(),
        "메인 게시물 제목",
        DummyGenerator.createCountry(),
        "www.naver.com",
        DummyGenerator.createCost()
    );

    String token = "Bearer " + jwtTokenProvider.createToken(member.getId());

    mockMvc.perform(post("/api/travelogues")
            .header("AccessToken", token)
            .queryParam("memberId", member.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(travelogueCreateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("save-travelogue",
            requestFields(
                fieldWithPath("period.startDate").description("여행 시작 날짜"),
                fieldWithPath("period.endDate").description("여행 종료 날짜"),
                fieldWithPath("period.nights").description("여행 숙박 횟수"),
                fieldWithPath("title").description("게시물 제목"),
                fieldWithPath("country.name").description("여행한 나라 이름"),
                fieldWithPath("thumbnail").description("게시물 썸네일 URL"),
                fieldWithPath("cost.transportation").description("이동 수단 경비"),
                fieldWithPath("cost.lodge").description("숙박 비용"),
                fieldWithPath("cost.etc").description("기타 비용"),
                fieldWithPath("cost.total").description("전체 경비")
            ),
            responseFields(
                fieldWithPath("id").description("생성된 게시물의 pk 값")
            )));
  }
}