package shop.zip.travel.presentation.member;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.security.JwtTokenProvider;


@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class MemberMyTravelogueControllerTest {

  private static final String tokenName = "AccessToken";

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private MemberRepository memberRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private Member member;
  private Travelogue travelogue;
  private String token;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(DummyGenerator.createMember());
    travelogue = travelogueRepository.save(DummyGenerator.createTravelogue(member));
    travelogueRepository.save(DummyGenerator.createTempTravelogue(member));
    token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());
  }

  @Test
  @DisplayName("내가 작성한 발행된 게시물들을 가져올 수 있다.")
  void getMyTravelogues() throws Exception {

    mockMvc.perform(get("/api/members/my/travelogues")
            .header(tokenName, token)
            .param("size", "2")
            .param("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-my-travelogues",
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("인증 토큰")
            ),
            queryParameters(
                parameterWithName("size").description("한번에 가져올 데이터 갯수, 기본으로 5로 설정되어 있습니다."),
                parameterWithName("page").description("조회할 페이지 넘버, 기본으로 0으로 설정되어 있습니다.(첫페이지)")
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
            )
        ));
  }

  @Test
  @DisplayName("내가 작성한 임시 저장 게시물들을 가져올 수 있다.")
  void getTempAll() throws Exception {

    mockMvc.perform(get("/api/members/my/travelogues/temp")
            .header(tokenName, token)
            .param("size", "2")
            .param("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-my-temp-travelogues",
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("인증 토큰")
            ),
            queryParameters(
                parameterWithName("size").description("한번에 가져올 데이터 갯수, 기본으로 5로 설정되어 있습니다."),
                parameterWithName("page").description("조회할 피이지 넘버, 기본으로 0으로 설정되어 있습니다.(첫페이지)")
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
            )
        ));
  }

  @Test
  @DisplayName("내가 작성했던 하나의 트래블로그 정보를 가져올 수 있다.")
  void getDetailForUpdate() throws Exception {

    mockMvc.perform(get("/api/members/my/travelogues/{travelogueId}", travelogue.getId())
            .header(tokenName, token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-my-one-travelogue",
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("인증 헤더")
            ),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue pk 값")
            ),
            responseFields(
                fieldWithPath("title").type(JsonFieldType.STRING)
                    .description("Travelogue 제목"),
                fieldWithPath("period.startDate").type(JsonFieldType.STRING).description("여행 시작일"),
                fieldWithPath("period.endDate").type(JsonFieldType.STRING).description("여행 종료일"),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("방문한 나라"),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER).description("교통비"),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("숙박비"),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("기타 비용"),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER).description("총 경비"),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING)
                    .description("Travelogue 썸네일"),
                fieldWithPath("subTravelogueIds[]").type(JsonFieldType.ARRAY)
                    .description("서브트레블로그 아이디 리스트")
            )));
  }

  @Test
  @DisplayName("수정을 위해 내가 작성했던 하나의 서브 트래블로그 정보를 가져올 수 있다.")
  void getDetailSubTravelogueForUpdate() throws Exception {

    Long subTravelogueId = travelogue.getSubTravelogues().get(0).getId();

    mockMvc.perform(
            get("/api/members/my/travelogues/{travelogueId}/subTravelogues/{subTravelogueId}",
                travelogue.getId(), subTravelogueId)
                .header(tokenName, token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-my-one-sub-travelogue",
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("인증 헤더")
            ),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue pk 값"),
                parameterWithName("subTravelogueId").description("subTravelogue pk 값")
            ),
            responseFields(
                fieldWithPath("title").type(JsonFieldType.STRING)
                    .description("SubTravelogue 제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("subTravelogue 내용"),
                fieldWithPath("day").type(JsonFieldType.NUMBER).description("subTravelogue 일차 정보"),
                fieldWithPath("addresses[]").type(JsonFieldType.ARRAY).description("방문한 장소 리스트")
                    .optional(),
                fieldWithPath("addresses[].region").type(JsonFieldType.STRING)
                    .description("방문한 장소 정보"),
                fieldWithPath("transportationSet[]").type(JsonFieldType.ARRAY)
                    .description("이용한 교통수단 리스트"),
                fieldWithPath("travelPhotoCreateReqs[]").type(JsonFieldType.ARRAY)
                    .description("이미지 리스트").optional(),
                fieldWithPath("travelPhotoCreateReqs[].url").type(JsonFieldType.STRING)
                    .description("이미지 URL").optional()
            )));
  }

  @Test
  @DisplayName("트래블로그를 수정할 수 있다.")
  void updateTravelogue() throws Exception {
    TravelogueUpdateReq travelogueUpdateReq = DummyGenerator.createTravelogueUpdateReq();

    mockMvc.perform(patch("/api/members/my/travelogues/{travelogueId}", travelogue.getId())
            .header(tokenName, token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(travelogueUpdateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("update-published-travelogue",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("인증 토큰")
            ),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue pk 값")
            ),
            requestFields(
                fieldWithPath("period.startDate").type(JsonFieldType.ARRAY).description("여행 시작일"),
                fieldWithPath("period.endDate").type(JsonFieldType.ARRAY).description("여행 종료일"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("방문한 나라"),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("썸네일 URL"),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER).description("교통비"),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("숙박비"),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("기타 비용"),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER)
                    .description("전체 경비 (전체 경비는 다른 비용의 합보다 작으면 안됩니다.)")
            ),
            responseFields(
                fieldWithPath("travelogueId").type(JsonFieldType.NUMBER)
                    .description("업데이트 된 게시글 PK")
            )
        ));
  }

  @Test
  @DisplayName("서브 트래블로그를 수정할 수 있다.")
  void updateSubTravelogue() throws Exception {
    SubTravelogueUpdateReq subTravelogueUpdateReq = DummyGenerator.createSubTravelogueUpdateReq();

    Long subTravelogueId = travelogue.getSubTravelogues().get(0).getId();

    mockMvc.perform(
            patch("/api/members/my/travelogues/{travelogueId}/subTravelogues/{subTravelogueId}",
                travelogue.getId(), subTravelogueId)
                .header(tokenName, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.registerModule(new JavaTimeModule())
                    .writeValueAsString(subTravelogueUpdateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("update-published-sub-travelogue",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("인증 토큰")
            ),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue pk 값"),
                parameterWithName("subTravelogueId").description("subTravelogue pk 값")
            ),
            requestFields(
                fieldWithPath("title").type(JsonFieldType.STRING).description("소제목"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                fieldWithPath("day").type(JsonFieldType.NUMBER).description("서브 게시물의 일차 정보"),
                fieldWithPath("addresses[].region").type(JsonFieldType.STRING)
                    .description("방문한 장소명"),
                fieldWithPath("transportationSet").type(JsonFieldType.ARRAY)
                    .description("이용한 교통수단"),
                fieldWithPath("travelPhotoCreateReqs[]").type(JsonFieldType.ARRAY)
                    .description("이미지가 없을 경우 반환되는 빈 이미지 리스트").optional(),
                fieldWithPath("travelPhotoCreateReqs[].url").type(JsonFieldType.STRING)
                    .description("업로드 된 이미지 url")
            ),
            responseFields(
                fieldWithPath("subTravelogueId").type(JsonFieldType.NUMBER)
                    .description("업데이트 된 서브 게시글 PK")
            )
        ));
  }
}