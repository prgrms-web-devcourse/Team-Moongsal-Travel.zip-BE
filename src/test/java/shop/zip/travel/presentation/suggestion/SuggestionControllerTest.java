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

//  @Test
//  @DisplayName("사용자는 추천 게시물을 받을 수 있다!")
//  void suggestion() throws Exception {
//    String accessToken = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());
//
//    mockMvc.perform(
//            get("/api/suggestions")
//                .param("page", "0")
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .header("AccessToken", accessToken))
//        .andExpect(status().isOk())
//        .andDo(
//            document(
//                "suggestion",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                queryParameters(
//                    parameterWithName("page").description("페이지")
//                ),
//                requestHeaders(
//                    headerWithName("AccessToken").description("토큰")
//                ),
//                responseFields(
//                    fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("배열"),
//                    fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
//                        .description("Travelogue id").optional(),
//                    fieldWithPath("content[].title").type(JsonFieldType.STRING)
//                        .description("Travelogue 제목").optional(),
//                    fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("숙박일").optional(),
//                    fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("전체일").optional(),
//                    fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
//                        .description("여행 총 경비").optional(),
//                    fieldWithPath("content[].country").type(JsonFieldType.STRING).description("여행한 나라").optional(),
//                    fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
//                        .description("Travelogue 썸네일").optional(),
//                    fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
//                        .description("작성자 닉네임").optional(),
//                    fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
//                        .description("작성자 프로필 이미지 URL").optional(),
//                    fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
//                        .description("게시글 좋아요 수").optional(),
//                    fieldWithPath("pageable.sort.empty").description("데이터가 비어있는지에 대한 여부"),
//                    fieldWithPath("pageable.sort.sorted").description("데이터가 정렬되어있는지에 대한 여부"),
//                    fieldWithPath("pageable.sort.unsorted").description("데이터가 정렬되어 있지 않은지에 대한 여부"),
//                    fieldWithPath("pageable.offset").description("페이징 offset"),
//                    fieldWithPath("pageable.pageNumber").description("현재 요청한 페이지 넘버"),
//                    fieldWithPath("pageable.pageSize").description("요청한 데이터 갯수"),
//                    fieldWithPath("pageable.paged").description("페이징이 된 여부"),
//                    fieldWithPath("pageable.unpaged").description("페이징이 되지 않은 여부"),
//                    fieldWithPath("size").description("요청된 페이징 사이즈"),
//                    fieldWithPath("number").description("페이지 번호"),
//                    fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
//                    fieldWithPath("first").description("첫번째 페이지인지의 여부"),
//                    fieldWithPath("last").description("마지막 페이지인지의 여부"),
//                    fieldWithPath("empty").description("데이터가 없는지의 여부")
//                )));
//  }
}