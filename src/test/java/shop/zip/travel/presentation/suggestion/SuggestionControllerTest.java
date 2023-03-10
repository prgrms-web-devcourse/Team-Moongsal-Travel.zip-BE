package shop.zip.travel.presentation.suggestion;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.security.JwtTokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class SuggestionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private MemberRepository memberRepository;

  private Member member;

  private Travelogue travelogue;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @BeforeEach
  void setUp() {
    member = new Member("user@naver.com", "password1234!", "nickname", "1999");
    memberRepository.save(member);

    travelogue = travelogueRepository.save(DummyGenerator.createTravelogue(member));
  }

  @Test
  @DisplayName("???????????? ?????? ???????????? ?????? ??? ??????!")
  void suggestion() throws Exception {
    String accessToken = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(
            get("/api/suggestions")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("AccessToken", accessToken))
        .andExpect(status().isOk())
        .andDo(
            document(
                "suggestion",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("page").description("?????????")
                ),
                requestHeaders(
                    headerWithName("AccessToken").description("??????")
                ),
                responseFields(
                    fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("??????"),
                    fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                        .description("Travelogue id").optional(),
                    fieldWithPath("content[].title").type(JsonFieldType.STRING)
                        .description("Travelogue ??????").optional(),
                    fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("?????????").optional(),
                    fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("?????????").optional(),
                    fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                        .description("?????? ??? ??????").optional(),
                    fieldWithPath("content[].country").type(JsonFieldType.STRING).description("????????? ??????").optional(),
                    fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                        .description("Travelogue ?????????").optional(),
                    fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                        .description("????????? ?????????").optional(),
                    fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                        .description("????????? ????????? ????????? URL").optional(),
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
}