package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Like;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueLikeRepository;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.security.JwtTokenProvider;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class TravelogueLikeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private TravelogueLikeRepository travelogueLikeRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Member member;

  private Travelogue travelogue;

  private Travelogue notLikeTravelogue;

  private Like like;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(
        DummyGenerator.createMember());
    travelogue = travelogueRepository.save(
        DummyGenerator.createTravelogue(member));
    notLikeTravelogue = travelogueRepository.save(
        DummyGenerator.createTravelogue(member));
    like = travelogueLikeRepository.save(new Like(travelogue, member));
  }

  @Test
  @DisplayName("???????????? ????????? ?????? ???????????? ?????? ???????????? ?????? ??? ??????.")
  void test_add_like() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(put("/api/travelogues/{travelogueId}/likes", notLikeTravelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("add-like-travelogue",
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("?????? ????????? ????????? ???????????????.")
            ),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue pk ???")
            ),
            responseFields(
                fieldWithPath("isAdded").description("???????????? ??????????????? ?????? ?????? - ???????????? ????????? ????????? true ??????"),
                fieldWithPath("isCanceled").description("???????????? ??????????????? ?????? ?????? - ???????????? ????????? ????????? true ??????")
            )));
  }

  @Test
  @DisplayName("???????????? ?????? ???????????? ???????????? ??? ????????? ????????????.")
  void test_cancel_like() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(put("/api/travelogues/{travelogueId}/likes", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("cancel-like-travelogue",
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("?????? ????????? ????????? ???????????????.")
            ),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue pk ???")
            ),
            responseFields(
                fieldWithPath("isAdded").description("???????????? ??????????????? ?????? ?????? - ???????????? ????????? ????????? true ??????"),
                fieldWithPath("isCanceled").description("???????????? ??????????????? ?????? ?????? - ???????????? ????????? ????????? true ??????")
            )));
  }
}