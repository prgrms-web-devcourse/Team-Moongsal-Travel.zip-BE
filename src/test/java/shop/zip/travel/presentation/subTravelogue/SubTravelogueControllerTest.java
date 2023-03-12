package shop.zip.travel.presentation.subTravelogue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.security.JwtTokenProvider;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class SubTravelogueControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private Travelogue travelogue;

  private Member member;

  private static final int day = 1;

  @BeforeEach
  void setUp() {
    member = new Member("user@naver.com", "password1234!", "nickname", "1999");
    memberRepository.save(member);

    travelogue = travelogueRepository.save(DummyGenerator.createTempTravelogue(member));
  }

  @Test
  @DisplayName("일차 게시물을 작성할 수 있다.")
  void test_create_subTravelogue() throws Exception {
    SubTravelogueCreateReq subTravelogueCreateReq = new SubTravelogueCreateReq(
        "일본 다녀왔습니다.",
        "일본은 가까워서 좋고, 맛있는게 많아서 좋습니다. 일단 일본 다녀오면 3kg이 찝니다. 주의하세요.",
        day,
        List.of(DummyGenerator.createTempAddress()),
        Set.of(Transportation.BUS),
        List.of(new TravelPhotoCreateReq("www.google.com"))
    );

    Travelogue tempTravelogue = travelogueRepository.save(
        DummyGenerator.createTempTravelogue(member));

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(post("/api/travelogues/{travelogueId}/subTravelogues", tempTravelogue.getId())
            .header("AccessToken", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(subTravelogueCreateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("create-subTravelogue",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("travelogueId").description("travelogue id")
            ),
            requestFields(
                fieldWithPath("title").description("서브 게시물 제목"),
                fieldWithPath("content").description("서브 게시물 내용"),
                fieldWithPath("day").description("서브 게시물 일차 정보"),
                fieldWithPath("addresses[]").description("방문한 장소 리스트"),
                fieldWithPath("addresses[].region").description("방문한 장소"),
                fieldWithPath("transportationSet[]").description("이용한 이동수단 리스트"),
                fieldWithPath("travelPhotoCreateReqs[].url").description("게시물에 들어가는 이미지 URL")
            ),
            responseFields(
                fieldWithPath("id").description("생성된 서브 게시물 PK")
            )
        ));
  }
}