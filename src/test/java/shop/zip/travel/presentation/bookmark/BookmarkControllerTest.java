package shop.zip.travel.presentation.bookmark;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.security.JwtTokenProvider;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class BookmarkControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private Member member;
  private Travelogue savedTravelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createMember();

    memberRepository.save(member);
    savedTravelogue = travelogueRepository.save(DummyGenerator.createTravelogue(member));
  }

  @DisplayName("유저는 맘에드는 게시글을 북마크 할 수 있다")
  @Test
  public void add_bookmark() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(put("/api/travelogues/{travelogueId}/bookmarks", savedTravelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("bookmarking",
            pathParameters(
                parameterWithName("travelogueId").description("travelogue id")
            )
        ));
  }


}