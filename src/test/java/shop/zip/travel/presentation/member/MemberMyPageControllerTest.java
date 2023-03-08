package shop.zip.travel.presentation.member;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.bookmark.entity.Bookmark;
import shop.zip.travel.domain.bookmark.repository.BookmarkRepository;
import shop.zip.travel.domain.member.dto.request.MemberUpdateReq;
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
@ExtendWith(SpringExtension.class)
class MemberMyPageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private BookmarkRepository bookmarkRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private Member member;

  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = new Member(
        "user@naver.com",
        "password1234!",
        "nickname",
        "2000",
        "ProfileUrlForTest");

    memberRepository.save(member);
    travelogue = travelogueRepository.save(DummyGenerator.createTravelogue(member));
  }

  @DisplayName("유저는 본인의 개인정보를 조회할 수 있다")
  @Test
  public void get_my_page_info() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(get("/api/members/my/info").header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-my-info",
            preprocessResponse(prettyPrint()),
            responseFields(fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("birthYear").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("프로필 이미지 url"))));
  }

  @DisplayName("유저는 본인이 작성한 여행기 목록을 조회할 수 있다")
  @Test
  public void get_my_travelogues() throws Exception {
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(get("/api/members/my/travelogues")
            .header("AccessToken", token)
            .queryParam("size", "2")
            .queryParam("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-my-travelogues",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("size").description("페이지의 Travelogue 수"),
                parameterWithName("page").description("페이지 수")
            ),
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
                fieldWithPath("content[].likeCount").description("게시글 좋아요 수"),
                fieldWithPath("pageable").description(""),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )
        ));
  }

  @DisplayName("유저는 본인의 프로필 사진과 닉네임을 변경할 수 있다")
  @Test
  public void update_my_profile() throws Exception {
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());
    MemberUpdateReq memberUpdateReq = new MemberUpdateReq(
        "test-profile-image-url",
        "testNickname"
    );

    mockMvc.perform(patch("/api/members/my/settings")
            .header("AccessToken", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(memberUpdateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("update-my-profile",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("변경할 프로필 이미지 url"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경할 닉네임")
            )
            ,
            responseFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("birthYear").type(JsonFieldType.STRING).description("생년월일"),
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("프로필 이미지 url")
            )
        ));
  }

  @DisplayName("유저는 본인이 북마크한 여행기목록을 조회할 수 있다")
  @Test
  public void get_my_bookmark_list() throws Exception {
    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());
    Bookmark bookmark = new Bookmark(travelogue, member);
    bookmarkRepository.save(bookmark);

    mockMvc.perform(get("/api/members/my/bookmarks")
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get_my_bookmark_list",
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("[].travelogueId").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                fieldWithPath("[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                fieldWithPath("[].nights").type(JsonFieldType.NUMBER).description("숙박일"),
                fieldWithPath("[].days").type(JsonFieldType.NUMBER).description("여행 전체 일"),
                fieldWithPath("[].totalCost").type(JsonFieldType.NUMBER).description("여행 전체 비용"),
                fieldWithPath("[].country").type(JsonFieldType.STRING).description("여행한 나라"),
                fieldWithPath("[].thumbnail").type(JsonFieldType.STRING).description("게시글 썸네일"),
                fieldWithPath("[].member.nickname").type(JsonFieldType.STRING)
                    .description("작성자 닉네임"),
                fieldWithPath("[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 이미지 링크"),
                fieldWithPath("[].likeCount").type(JsonFieldType.NUMBER)
                    .description("좋아요 갯수")
            )
        ));
  }

  @Test
  @DisplayName("자신이 작성 중이던 임시 저장 글들을 불러올 수 있다.")
  public void test_get_all_temp_travelogue() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/my/travelogues/temp")
            .header("AccessToken", token)
            .queryParam("size", "2")
            .queryParam("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-temp-travelogue",
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("content[]").description("").optional(),
                fieldWithPath("content[].travelogueId").type(JsonFieldType.NUMBER)
                    .description("Travelogue pk"),
                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                    .description("Travelogue 제목"),
                fieldWithPath("content[].nights").type(JsonFieldType.NUMBER).description("숙박 일"),
                fieldWithPath("content[].days").type(JsonFieldType.NUMBER).description("여행 전체 일"),
                fieldWithPath("content[].totalCost").type(JsonFieldType.NUMBER)
                    .description("여행 전체 비용"),
                fieldWithPath("content[].country").type(JsonFieldType.STRING).description("방문한 나라"),
                fieldWithPath("content[].thumbnail").type(JsonFieldType.STRING)
                    .description("썸네일 링크"),
                fieldWithPath("content[].member.nickname").type(JsonFieldType.STRING)
                    .description("작성자 닉네임"),
                fieldWithPath("content[].member.profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 이미지 링크"),
                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                    .description("데이터가 비어있는지에 대한 여부"),
                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                    .description("데이터가 정렬되어있는지에 대한 여부"),
                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                    .description("데이터가 정렬되어 있지 않은지에 대한 여부"),
                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER)
                    .description("페이징 offset"),
                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER)
                    .description("현재 요청한 페이지 넘버"),
                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER)
                    .description("요청한 데이터 갯수"),
                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN)
                    .description("페이징이 된 여부"),
                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN)
                    .description("페이징이 되지 않은 여부"),
                fieldWithPath("size").type(JsonFieldType.NUMBER).description("요청된 페이징 사이즈"),
                fieldWithPath("number").type(JsonFieldType.NUMBER).description("페이지 번호"),
                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                    .description("조회된 데이터 갯수"),
                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("데이터가 없는지의 여부")
            )));
  }
}