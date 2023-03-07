package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
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
  private SubTravelogueRepository subTravelogueRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private Travelogue travelogue;
  private Member member;

  @BeforeEach
  void setUp() {
    member = new Member("user@gmail.com", "password123!", "nickname", "1998");
    memberRepository.save(member);
    travelogue = DummyGenerator.createNotPublishedTravelogue(member);
    travelogueRepository.saveAll(
        List.of(
            travelogue,
            DummyGenerator.createTravelogue(member),
            DummyGenerator.createTravelogue(member))
    );

    SubTravelogue subTravelogue = subTravelogueRepository.save(
        DummyGenerator.createSubTravelogue()
    );

    travelogue.addSubTravelogue(subTravelogue);
  }

  @Test
  @DisplayName("저장이 완료된 전체 게시물 리스트를 페이지별로 가져올 수 있다.")
  void test_get_all_travelogue() throws Exception {

    mockMvc.perform(get("/api/travelogues")
            .queryParam("size", "2")
            .queryParam("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-travelogue",
            responseFields(
                fieldWithPath("content[].travelogueId").description("Travelogue 아이디"),
                fieldWithPath("content[].title").description("Travelogue 제목"),
                fieldWithPath("content[].nights").description("숙박 일"),
                fieldWithPath("content[].days").description("여행 전체 일"),
                fieldWithPath("content[].totalCost").description("여행 전체 비용"),
                fieldWithPath("content[].country").description("방문한 나라"),
                fieldWithPath("content[].thumbnail").description("썸네일 링크"),
                fieldWithPath("content[].member.nickname").description("작성자 닉네임"),
                fieldWithPath("content[].member.profileImageUrl").description("작성자 프로필 이미지 링크"),
                fieldWithPath("content[].likeCount").description("좋아요 수"),
                fieldWithPath("pageable.sort.empty").description("데이터가 비어있는지에 대한 여부"),
                fieldWithPath("pageable.sort.sorted").description("데이터가 정렬되어있는지에 대한 여부"),
                fieldWithPath("pageable.sort.unsorted").description("데이터가 정렬되어 있지 않은지에 대한 여부"),
                fieldWithPath("pageable.offset").description("페이징 offset"),
                fieldWithPath("pageable.pageNumber").description("현재 요청한 페이지 넘버"),
                fieldWithPath("pageable.pageSize").description("요청한 데이터 갯수"),
                fieldWithPath("pageable.paged").description("페이징이 된 여부"),
                fieldWithPath("pageable.unpaged").description("페이징이 되지 않은 여부"),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )));
  }

  @Test
  @DisplayName("임시 저장된 게시글을 발행할 수 있다.")
  void test_publish_travelogue() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}/publish", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("publish-travelogue-success",
            responseFields(
                fieldWithPath("travelogueId").description("공개된 게시글 PK")
            )));
  }

  @Test
  @DisplayName("작성이 완료되지 않은 게시글은 발행할 수 없다.")
  void test_fail_publish_travelogue() throws Exception {

    Travelogue cannotPublishTravelogue =
        travelogueRepository.save(DummyGenerator.createNotPublishedTravelogue(member));

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(
            patch("/api/travelogues/{travelogueId}/publish", cannotPublishTravelogue.getId())
                .header("AccessToken", token))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("publish-travelogue-fail",
            responseFields(
                fieldWithPath("message").description("예외 메시지")
            )));
  }

  @Test
  @DisplayName("메인 게시글을 작성 혹은 임시 작성 할 수 있다.")
  void test_temp_save_travelogue() throws Exception {
    // given
    TravelogueCreateReq travelogueCreateReq = new TravelogueCreateReq(
        DummyGenerator.createTempPeriod(),
        null,
        DummyGenerator.createTempCountry(),
        "www.naver.com",
        DummyGenerator.createTempCost()
    );

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(post("/api/travelogues")
            .header("AccessToken", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(travelogueCreateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("save-temp-travelogue",
            requestFields(
                fieldWithPath("period.startDate").type(JsonFieldType.ARRAY).description("여행 시작 날짜")
                    .optional(),
                fieldWithPath("period.endDate").type(JsonFieldType.ARRAY).description("여행 종료 날짜")
                    .optional(),
                fieldWithPath("period.nights").type(JsonFieldType.NUMBER).description("여행 숙박 횟수")
                    .optional(),
                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목").optional(),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("여행한 나라 이름")
                    .optional(),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("게시물 썸네일 URL")
                    .optional(),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER)
                    .description("이동 수단 경비").optional(),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("숙박 비용")
                    .optional(),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("기타 비용")
                    .optional(),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER).description("전체 경비")
                    .optional()
            ),
            responseFields(
                fieldWithPath("id").description("임시 저장된 게시물의 pk 값"),
                fieldWithPath("nights").description("n박"),
                fieldWithPath("days").description("n일")
            )));
  }

  @Test
  @DisplayName("게시글의 상세정보를 조회할 수 있다.")
  void test_get_one_travelogue() throws Exception {

    travelogue.changePublishStatus();
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-one-detail-travelogue",
            responseFields(
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 이미지"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Travelogue id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("Travelogue 제목"),
                fieldWithPath("country").type(JsonFieldType.STRING)
                    .description("Travelogue 방문한 나라"),
                fieldWithPath("nights").type(JsonFieldType.NUMBER)
                    .description("Travelogue 여행 기간 중 숙박 날짜"),
                fieldWithPath("days").type(JsonFieldType.NUMBER)
                    .description("Travelogue 여행 기간 중 전체 날짜"),
                fieldWithPath("totalCost").type(JsonFieldType.NUMBER)
                    .description("Travelogue 여행 전체 경비"),
                fieldWithPath("countLikes").type(JsonFieldType.NUMBER)
                    .description("좋아요 갯수"),
                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN)
                    .description("좋아요 여부"),
                fieldWithPath("subTravelogues[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue 리스트"),
                fieldWithPath("subTravelogues[].title").type(JsonFieldType.STRING)
                    .description("SubTravelogue의 제목"),
                fieldWithPath("subTravelogues[].content").type(JsonFieldType.STRING)
                    .description("SubTravelogue의 내용"),
                fieldWithPath("subTravelogues[].addresses[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue의 방문한 장소 리스트"),
                fieldWithPath("subTravelogues[].addresses[].region").type(JsonFieldType.STRING)
                    .description("SubTravelogue의 방문한 장소명"),
                fieldWithPath("subTravelogues[].transportationSet[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue 에서 이용한 이동 수단 리스트"),
                fieldWithPath("subTravelogues[].travelPhotoCreateReqs[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue의 이미지 리스트").optional(),
                fieldWithPath("subTravelogues[].travelPhotoCreateReqs[].url").type(
                    JsonFieldType.STRING).description("SubTravelogue의 이미지 url").optional(),
                fieldWithPath("transportations").type(JsonFieldType.ARRAY)
                    .description("Travelogue에서의 이용한 이동 수단"),
                fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("조회수")
            )));
  }

  @DisplayName("유저는 나라이름, 게시글 제목, 게시글 내용, 장소로 게시물을 검색할 수 있다")
  @ParameterizedTest
  @ValueSource(strings = {"일본 오사카 다녀왔어요.", "일본", "또 가고 싶음", "유니버셜"})
  void test_search(String keyword) throws Exception {

    travelogue.changePublishStatus();
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(get("/api/travelogues/search")
            .header("AccessToken", token)
            .param("keyword", keyword))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-travelogues-searched",
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("keyword").description("검색 키워드")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue 제목"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("숙박일"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("전체일"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("여행 총 경비"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("여행한 나라"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue 썸네일"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("작성자 닉네임"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 이미지 URL"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("게시글 좋아요 수"),
                fieldWithPath("pageable").description("페이징 객체"),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )));
  }

  @DisplayName("유저는 기간으로 필터링하여 검색 할 수 있다")
  @ParameterizedTest
  @ValueSource(strings = {"일본 오사카 다녀왔어요.", "일본", "또 가고 싶음"})
  void test_search_with_filter_period(String keyword) throws Exception {

    travelogue.changePublishStatus();
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    MultiValueMap<String, String> period = new LinkedMultiValueMap<>();
    period.add("minDays", "0");
    period.add("maxDays", "3");

    mockMvc.perform(get("/api/travelogues/search/filters")
            .header("AccessToken", token)
            .params(period)
            .param("keyword", keyword))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-travelogues-filtered-period",
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("keyword").description("검색 키워드"),
                parameterWithName("minDays").description("최소 기간"),
                parameterWithName("maxDays").description("최대 기간")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue 제목"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("숙박일"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("전체일"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("여행 총 경비"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("여행한 나라"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue 썸네일"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("작성자 닉네임"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 사진"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("게시물 좋아요 수"),
                fieldWithPath("pageable").description("페이징 객체"),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )));
  }

  @DisplayName("유저는 가격범위로 필터링하여 검색 할 수 있다")
  @ParameterizedTest
  @ValueSource(strings = {"일본 오사카 다녀왔어요.", "일본", "또 가고 싶음"})
  void test_search_with_filter_cost(String keyword) throws Exception {

    travelogue.changePublishStatus();
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    MultiValueMap<String, String> cost = new LinkedMultiValueMap<>();
    cost.add("minCost", "0");
    cost.add("maxCost", String.valueOf(travelogue.getCost().getTotal()));

    mockMvc.perform(get("/api/travelogues/search/filters")
            .header("AccessToken", token)
            .params(cost)
            .param("keyword", keyword))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-travelogues-filtered-cost",
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("keyword").description("검색 키워드"),
                parameterWithName("minCost").description("최저가"),
                parameterWithName("maxCost").description("최고가")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue 제목"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("숙박일"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("전체일"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("여행 총 경비"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("여행한 나라"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue 썸네일"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("작성자 닉네임"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 사진"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("게시물 좋아요 수").optional(),
                fieldWithPath("pageable").description("페이징 객체"),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )));
  }

  @DisplayName("유저는 가격과 기간으로 필터링 검색을 할 수 있다")
  @ParameterizedTest
  @ValueSource(strings = {"일본 오사카 다녀왔어요.", "일본", "또 가고 싶음"})
  void test_search_with_filter(String keyword) throws Exception {

    travelogue.changePublishStatus();
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    MultiValueMap<String, String> period = new LinkedMultiValueMap<>();
    period.add("minDays", "0");
    period.add("maxDays", "3");

    MultiValueMap<String, String> cost = new LinkedMultiValueMap<>();
    cost.add("minCost", "0");
    cost.add("maxCost", String.valueOf(travelogue.getCost().getTotal()));

    mockMvc.perform(get("/api/travelogues/search/filters")
            .header("AccessToken", token)
            .params(period)
            .params(cost)
            .param("keyword", keyword))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-travelogues-filtered",
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("keyword").description("검색 키워드"),
                parameterWithName("minDays").description("최소 기간"),
                parameterWithName("maxDays").description("최대 기간"),
                parameterWithName("minCost").description("최저가"),
                parameterWithName("maxCost").description("최고가")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue 제목"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("숙박일"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("전체일"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("여행 총 경비"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("여행한 나라"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue 썸네일"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("작성자 닉네임"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 사진"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("게시물 좋아요 수").optional(),
                fieldWithPath("pageable").description("페이징 객체"),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )));
  }

}