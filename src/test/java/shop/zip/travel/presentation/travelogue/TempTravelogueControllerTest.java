package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
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
class TempTravelogueControllerTest {

  private static final String tokenName = "AccessToken";
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
  private Travelogue travelogue;
  private String token;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(DummyGenerator.createMember());
    travelogue = travelogueRepository.save(DummyGenerator.createTravelogue(member));
    token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());
  }


  @Test
  @DisplayName("트래블로그를 수정할 수 있다.")
  void updateTravelogue() throws Exception {
    TravelogueUpdateReq travelogueUpdateReq = DummyGenerator.createTravelogueUpdateReq();

    mockMvc.perform(put("/api/travelogues/{travelogueId}", travelogue.getId())
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
                fieldWithPath("period.startDate").type(JsonFieldType.ARRAY).description("여행 시작일")
                    .optional(),
                fieldWithPath("period.endDate").type(JsonFieldType.ARRAY).description("여행 종료일")
                    .optional(),
                fieldWithPath("period.nights").type(JsonFieldType.NUMBER).description("총 여행 날짜")
                    .optional(),
                fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional(),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("방문한 나라")
                    .optional(),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("썸네일 URL")
                    .optional(),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER).description("교통비")
                    .optional(),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("숙박비")
                    .optional(),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("기타 비용")
                    .optional(),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER)
                    .description("전체 경비 (전체 경비는 다른 비용의 합보다 작으면 안됩니다.)").optional()
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
            put("/api/travelogues/{travelogueId}/subTravelogues/{subTravelogueId}",
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
                fieldWithPath("addresses[]").type(JsonFieldType.ARRAY)
                    .description("방문한 장소 리스트(장소 리스트가 비어있는 경우 빈 배열로 전달되는 필드입니다.")
                    .optional(),
                fieldWithPath("addresses[].region").type(JsonFieldType.STRING)
                    .description("방문한 장소명").optional(),
                fieldWithPath("transportationSet").type(JsonFieldType.ARRAY)
                    .description("이용한 교통수단"),
                fieldWithPath("travelPhotoCreateReqs[]").type(JsonFieldType.ARRAY)
                    .description("이미지가 없을 경우 반환되는 빈 이미지 리스트").optional(),
                fieldWithPath("travelPhotoCreateReqs[].url").type(JsonFieldType.STRING)
                    .description("업로드 된 이미지 url").optional()
            ),
            responseFields(
                fieldWithPath("subTravelogueId").type(JsonFieldType.NUMBER)
                    .description("업데이트 된 서브 게시글 PK")
            )
        ));
  }

  @Test
  @DisplayName("작성자가 아닌 사용자는 수정 할 수 없다.")
  void test_not_update_travelogue() throws Exception {

    TravelogueUpdateReq travelogueUpdateReq = DummyGenerator.createTravelogueUpdateReq();

    Member notWriter = memberRepository.save(new Member(
        "notwriter@naver.com",
        "password1234@",
        "notwriter",
        "1999",
        "www.naver.com"
    ));

    String notWriterToken = "Bearer " + jwtTokenProvider.createAccessToken(notWriter.getId());

    mockMvc.perform(put("/api/travelogues/{travelogueId}", travelogue.getId())
            .header(tokenName, notWriterToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(travelogueUpdateReq)))
        .andExpect(status().isUnauthorized())
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
                fieldWithPath("period.startDate").type(JsonFieldType.ARRAY).description("여행 시작일")
                    .optional(),
                fieldWithPath("period.endDate").type(JsonFieldType.ARRAY).description("여행 종료일")
                    .optional(),
                fieldWithPath("period.nights").type(JsonFieldType.NUMBER).description("여행 총 날짜")
                    .optional(),
                fieldWithPath("title").type(JsonFieldType.STRING).description("제목")
                    .optional(),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("방문한 나라")
                    .optional(),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("썸네일 URL")
                    .optional(),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER).description("교통비")
                    .optional(),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("숙박비")
                    .optional(),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("기타 비용")
                    .optional(),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER)
                    .description("전체 경비 (전체 경비는 다른 비용의 합보다 작으면 안됩니다.)")
                    .optional()
            ),
            responseFields(
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("요청이 처리되지 않아 발생한 예외 메시지")
            )
        ));
  }

  @Test
  @DisplayName("임시 저장된 게시글을 발행할 수 있다.")
  void test_publish_travelogue() throws Exception {

    Travelogue notPublished = travelogueRepository.save(
        DummyGenerator.createNotPublishedTravelogueWithSubTravelogues(
            List.of(
                DummyGenerator.createSubTravelogue(1),
                DummyGenerator.createSubTravelogue(2)
            ),
            member)
    );

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}/publish", notPublished.getId())
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
                fieldWithPath("travelogueId").description("공개된 게시글 PK")
            )));
  }

}