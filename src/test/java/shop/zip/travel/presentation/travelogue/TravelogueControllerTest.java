package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
import shop.zip.travel.domain.post.travelogue.service.TravelogueLikeService;
import shop.zip.travel.global.config.QuerydslConfig;
import shop.zip.travel.global.security.JwtTokenProvider;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
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
  private TravelogueLikeService likeService;
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
        DummyGenerator.createSubTravelogue(2)
    );

    travelogue.addSubTravelogue(subTravelogue);
  }

  @Test
  @DisplayName("????????? ????????? ?????? ????????? ???????????? ??????????????? ????????? ??? ??????.")
  void test_get_all_travelogue() throws Exception {

    mockMvc.perform(get("/api/travelogues")
            .queryParam("size", "2")
            .queryParam("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-travelogue",
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("size").description("???????????? Travelogue ???"),
                parameterWithName("page").description("????????? ???")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").description("Travelogue ?????????"),
                fieldWithPath("content[].title").description("Travelogue ??????"),
                fieldWithPath("content[].nights").description("?????? ???"),
                fieldWithPath("content[].days").description("?????? ?????? ???"),
                fieldWithPath("content[].totalCost").description("?????? ?????? ??????"),
                fieldWithPath("content[].country").description("????????? ??????"),
                fieldWithPath("content[].thumbnail").description("????????? ??????"),
                fieldWithPath("content[].member.nickname").description("????????? ?????????"),
                fieldWithPath("content[].member.profileImageUrl").description("????????? ????????? ????????? ??????"),
                fieldWithPath("content[].likeCount").description("????????? ???"),
                fieldWithPath("pageable.sort.empty").description("???????????? ?????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.sorted").description("???????????? ???????????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.unsorted").description("???????????? ???????????? ?????? ???????????? ?????? ??????"),
                fieldWithPath("pageable.offset").description("????????? offset"),
                fieldWithPath("pageable.pageNumber").description("?????? ????????? ????????? ??????"),
                fieldWithPath("pageable.pageSize").description("????????? ????????? ??????"),
                fieldWithPath("pageable.paged").description("???????????? ??? ??????"),
                fieldWithPath("pageable.unpaged").description("???????????? ?????? ?????? ??????"),
                fieldWithPath("size").description("????????? ????????? ?????????"),
                fieldWithPath("number").description("????????? ??????"),
                fieldWithPath("numberOfElements").description("????????? ????????? ??????"),
                fieldWithPath("first").description("????????? ?????????????????? ??????"),
                fieldWithPath("last").description("????????? ?????????????????? ??????"),
                fieldWithPath("empty").description("???????????? ???????????? ??????")
            )));
  }

  @Test
  @DisplayName("?????? ????????? ???????????? ????????? ??? ??????.")
  void test_publish_travelogue() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}/publish", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("publish-travelogue-success",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue id")
            ),
            responseFields(
                fieldWithPath("travelogueId").description("????????? ????????? PK")
            )));
  }

  @Test
  @DisplayName("????????? ???????????? ?????? ???????????? ????????? ??? ??????.")
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
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("message").description("?????? ?????????")
            )));
  }

  @Test
  @DisplayName("?????? ???????????? ?????? ?????? ?????? ?????? ??? ??? ??????.")
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
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("period.startDate").type(JsonFieldType.ARRAY).description("?????? ?????? ??????")
                    .optional(),
                fieldWithPath("period.endDate").type(JsonFieldType.ARRAY).description("?????? ?????? ??????")
                    .optional(),
                fieldWithPath("period.nights").type(JsonFieldType.NUMBER).description("?????? ?????? ??????")
                    .optional(),
                fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????").optional(),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("????????? ?????? ??????")
                    .optional(),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("????????? ????????? URL")
                    .optional(),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER)
                    .description("?????? ?????? ??????").optional(),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("?????? ??????")
                    .optional(),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("?????? ??????")
                    .optional(),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER).description("?????? ??????")
                    .optional()
            ),
            responseFields(
                fieldWithPath("id").description("?????? ????????? ???????????? pk ???"),
                fieldWithPath("nights").description("n???"),
                fieldWithPath("days").description("n???")
            )));
  }

  @Test
  @DisplayName("???????????? ??????????????? ????????? ??? ??????.")
  void test_get_one_travelogue() throws Exception {

    travelogue.changePublishStatus();
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-one-detail-travelogue",
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("????????? ????????? ?????????"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Travelogue id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("Travelogue ??????"),
                fieldWithPath("country").type(JsonFieldType.STRING)
                    .description("Travelogue ????????? ??????"),
                fieldWithPath("nights").type(JsonFieldType.NUMBER)
                    .description("Travelogue ?????? ?????? ??? ?????? ??????"),
                fieldWithPath("days").type(JsonFieldType.NUMBER)
                    .description("Travelogue ?????? ?????? ??? ?????? ??????"),
                fieldWithPath("totalCost").type(JsonFieldType.NUMBER)
                    .description("Travelogue ?????? ?????? ??????"),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue ????????? ????????? URL"),
                fieldWithPath("countLikes").type(JsonFieldType.NUMBER)
                    .description("????????? ??????"),
                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN)
                    .description("????????? ??????"),
                fieldWithPath("bookmarked").type(JsonFieldType.BOOLEAN)
                    .description("????????? ??????"),
                fieldWithPath("subTravelogues[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue ?????????"),
                fieldWithPath("subTravelogues[].title").type(JsonFieldType.STRING)
                    .description("SubTravelogue??? ??????"),
                fieldWithPath("subTravelogues[].content").type(JsonFieldType.STRING)
                    .description("SubTravelogue??? ??????"),
                fieldWithPath("subTravelogues[].day").type(JsonFieldType.NUMBER)
                    .description("SubTravelogue??? ??????"),
                fieldWithPath("subTravelogues[].addresses[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue??? ????????? ?????? ?????????"),
                fieldWithPath("subTravelogues[].addresses[].region").type(JsonFieldType.STRING)
                    .description("SubTravelogue??? ????????? ?????????"),
                fieldWithPath("subTravelogues[].transportationSet[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue ?????? ????????? ?????? ?????? ?????????"),
                fieldWithPath("subTravelogues[].travelPhotoCreateReqs[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue??? ????????? ?????????").optional(),
                fieldWithPath("subTravelogues[].travelPhotoCreateReqs[].url").type(
                    JsonFieldType.STRING).description("SubTravelogue??? ????????? url").optional(),
                fieldWithPath("transportations").type(JsonFieldType.ARRAY)
                    .description("Travelogue????????? ????????? ?????? ??????"),
                fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("?????????")
            )));
  }

  @DisplayName("????????? ????????????, ????????? ??????, ????????? ??????, ????????? ???????????? ????????? ??? ??????")
  @ParameterizedTest
  @ValueSource(strings = {"?????? ????????? ???????????????.", "??????", "??? ?????? ??????", "????????????"})
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
                parameterWithName("keyword").description("?????? ?????????")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue ??????"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("?????? ??? ??????"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue ?????????"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("????????? ?????????"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("????????? ????????? ????????? URL"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("????????? ????????? ???"),
                fieldWithPath("pageable.sort.empty").description("???????????? ?????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.sorted").description("???????????? ???????????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.unsorted").description("???????????? ???????????? ?????? ???????????? ?????? ??????"),
                fieldWithPath("pageable.offset").description("????????? offset"),
                fieldWithPath("pageable.pageNumber").description("?????? ????????? ????????? ??????"),
                fieldWithPath("pageable.pageSize").description("????????? ????????? ??????"),
                fieldWithPath("pageable.paged").description("???????????? ??? ??????"),
                fieldWithPath("pageable.unpaged").description("???????????? ?????? ?????? ??????"),
                fieldWithPath("size").description("????????? ????????? ?????????"),
                fieldWithPath("number").description("????????? ??????"),
                fieldWithPath("numberOfElements").description("????????? ????????? ??????"),
                fieldWithPath("first").description("????????? ?????????????????? ??????"),
                fieldWithPath("last").description("????????? ?????????????????? ??????"),
                fieldWithPath("empty").description("???????????? ???????????? ??????")
            )));
  }

  @DisplayName("????????? ???????????? ??????????????? ?????? ??? ??? ??????")
  @ParameterizedTest
  @ValueSource(strings = {"?????? ????????? ???????????????.", "??????", "??? ?????? ??????"})
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
                parameterWithName("keyword").description("?????? ?????????"),
                parameterWithName("minDays").description("?????? ??????"),
                parameterWithName("maxDays").description("?????? ??????")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue ??????"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("?????? ??? ??????"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue ?????????"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("????????? ?????????"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("????????? ????????? ??????"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("????????? ????????? ???"),
                fieldWithPath("pageable.sort.empty").description("???????????? ?????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.sorted").description("???????????? ???????????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.unsorted").description("???????????? ???????????? ?????? ???????????? ?????? ??????"),
                fieldWithPath("pageable.offset").description("????????? offset"),
                fieldWithPath("pageable.pageNumber").description("?????? ????????? ????????? ??????"),
                fieldWithPath("pageable.pageSize").description("????????? ????????? ??????"),
                fieldWithPath("pageable.paged").description("???????????? ??? ??????"),
                fieldWithPath("pageable.unpaged").description("???????????? ?????? ?????? ??????"),
                fieldWithPath("size").description("????????? ????????? ?????????"),
                fieldWithPath("number").description("????????? ??????"),
                fieldWithPath("numberOfElements").description("????????? ????????? ??????"),
                fieldWithPath("first").description("????????? ?????????????????? ??????"),
                fieldWithPath("last").description("????????? ?????????????????? ??????"),
                fieldWithPath("empty").description("???????????? ???????????? ??????")
            )));
  }

  @DisplayName("????????? ??????????????? ??????????????? ?????? ??? ??? ??????")
  @ParameterizedTest
  @ValueSource(strings = {"?????? ????????? ???????????????.", "??????", "??? ?????? ??????"})
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
                parameterWithName("keyword").description("?????? ?????????"),
                parameterWithName("minCost").description("?????????"),
                parameterWithName("maxCost").description("?????????")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue ??????"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("?????? ??? ??????"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue ?????????"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("????????? ?????????"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("????????? ????????? ??????"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("????????? ????????? ???").optional(),
                fieldWithPath("pageable.sort.empty").description("???????????? ?????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.sorted").description("???????????? ???????????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.unsorted").description("???????????? ???????????? ?????? ???????????? ?????? ??????"),
                fieldWithPath("pageable.offset").description("????????? offset"),
                fieldWithPath("pageable.pageNumber").description("?????? ????????? ????????? ??????"),
                fieldWithPath("pageable.pageSize").description("????????? ????????? ??????"),
                fieldWithPath("pageable.paged").description("???????????? ??? ??????"),
                fieldWithPath("pageable.unpaged").description("???????????? ?????? ?????? ??????"),
                fieldWithPath("size").description("????????? ????????? ?????????"),
                fieldWithPath("number").description("????????? ??????"),
                fieldWithPath("numberOfElements").description("????????? ????????? ??????"),
                fieldWithPath("first").description("????????? ?????????????????? ??????"),
                fieldWithPath("last").description("????????? ?????????????????? ??????"),
                fieldWithPath("empty").description("???????????? ???????????? ??????")
            )));
  }

  @DisplayName("????????? ????????? ???????????? ????????? ????????? ??? ??? ??????")
  @ParameterizedTest
  @ValueSource(strings = {"?????? ????????? ???????????????.", "??????", "??? ?????? ??????"})
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
                parameterWithName("keyword").description("?????? ?????????"),
                parameterWithName("minDays").description("?????? ??????"),
                parameterWithName("maxDays").description("?????? ??????"),
                parameterWithName("minCost").description("?????????"),
                parameterWithName("maxCost").description("?????????")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue ??????"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("?????? ??? ??????"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue ?????????"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("????????? ?????????"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("????????? ????????? ??????"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("????????? ????????? ???").optional(),
                fieldWithPath("pageable.sort.empty").description("???????????? ?????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.sorted").description("???????????? ???????????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.unsorted").description("???????????? ???????????? ?????? ???????????? ?????? ??????"),
                fieldWithPath("pageable.offset").description("????????? offset"),
                fieldWithPath("pageable.pageNumber").description("?????? ????????? ????????? ??????"),
                fieldWithPath("pageable.pageSize").description("????????? ????????? ??????"),
                fieldWithPath("pageable.paged").description("???????????? ??? ??????"),
                fieldWithPath("pageable.unpaged").description("???????????? ?????? ?????? ??????"),
                fieldWithPath("size").description("????????? ????????? ?????????"),
                fieldWithPath("number").description("????????? ??????"),
                fieldWithPath("numberOfElements").description("????????? ????????? ??????"),
                fieldWithPath("first").description("????????? ?????????????????? ??????"),
                fieldWithPath("last").description("????????? ?????????????????? ??????"),
                fieldWithPath("empty").description("???????????? ???????????? ??????")
            )));
  }

  @Test
  @DisplayName("????????? ??????????????? ??????????????? ????????? ??? ??????")
  void get_travelogues_sort_by_like_count() throws Exception {

    Travelogue travelogue2 = DummyGenerator.createTravelogue(member);
    Travelogue travelogue3 = DummyGenerator.createTravelogue(member);
    travelogueRepository.saveAll(
        List.of(
            travelogue2,
            travelogue3));

    changePublishStatus(travelogue, travelogue2, travelogue3);
    likeService.liking(member.getId(), travelogue.getId());

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    MultiValueMap<String, String> filterParam = new LinkedMultiValueMap<>();
    filterParam.add("keyword", "??????");
    filterParam.add("sort", "popular");

    mockMvc.perform(get("/api/travelogues/search/filters")
            .header("AccessToken", token)
            .params(filterParam))
        .andExpect(status().isOk())
        .andExpect(jsonPath("content[0].travelogueId").value(travelogue.getId()))
        .andDo(print())
        .andDo(document("get-travelogues-filtered-with-popular-sort",
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("keyword").description("?????? ?????????"),
                parameterWithName("sort").description("??????")
            ),
            responseFields(
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue id"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue ??????"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("?????? ??? ??????"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue ?????????"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("????????? ?????????"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("????????? ????????? ??????"),
                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                    .description("????????? ????????? ???").optional(),
                fieldWithPath("pageable.sort.empty").description("???????????? ?????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.sorted").description("???????????? ???????????????????????? ?????? ??????"),
                fieldWithPath("pageable.sort.unsorted").description("???????????? ???????????? ?????? ???????????? ?????? ??????"),
                fieldWithPath("pageable.offset").description("????????? offset"),
                fieldWithPath("pageable.pageNumber").description("?????? ????????? ????????? ??????"),
                fieldWithPath("pageable.pageSize").description("????????? ????????? ??????"),
                fieldWithPath("pageable.paged").description("???????????? ??? ??????"),
                fieldWithPath("pageable.unpaged").description("???????????? ?????? ?????? ??????"),
                fieldWithPath("size").description("????????? ????????? ?????????"),
                fieldWithPath("number").description("????????? ??????"),
                fieldWithPath("numberOfElements").description("????????? ????????? ??????"),
                fieldWithPath("first").description("????????? ?????????????????? ??????"),
                fieldWithPath("last").description("????????? ?????????????????? ??????"),
                fieldWithPath("empty").description("???????????? ???????????? ??????")
            )));
  }

  private void changePublishStatus(Travelogue... args) {
    for (Travelogue travelogue : args) {
      travelogue.changePublishStatus();
    }
  }

}