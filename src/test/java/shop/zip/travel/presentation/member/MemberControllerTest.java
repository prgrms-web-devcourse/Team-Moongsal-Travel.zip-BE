package shop.zip.travel.presentation.member;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.dto.request.AccessTokenReissueReq;
import shop.zip.travel.domain.member.dto.request.DuplicatedNicknameCheckReq;
import shop.zip.travel.domain.member.dto.request.MemberLoginReq;
import shop.zip.travel.domain.member.dto.request.MemberRegisterReq;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.security.JwtTokenProvider;
import shop.zip.travel.global.util.RedisUtil;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@SpringBootTest
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  Long memberId;

  @BeforeEach
  public void setup() {
    Member member = new Member("user123@gmail.com", passwordEncoder.encode("qwe123!@#"), "Nick",
        "1994");
    Member savedMember = memberRepository.save(member);
    memberId = savedMember.getId();
  }

  @Test
  @DisplayName("유저는 닉네임 중복확인을 할 수 있다")
  public void checkDuplicatedNickname_success() throws Exception {
    DuplicatedNicknameCheckReq duplicatedNicknameCheckReq = new DuplicatedNicknameCheckReq("User");

    mockMvc.perform(post("/api/members/check/nickname")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(duplicatedNicknameCheckReq)))
        .andExpect(status().isOk())
        .andDo(document("member/check-nickname",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
            ),
            responseFields(
                fieldWithPath("isDuplicated").type(JsonFieldType.BOOLEAN).description("중복 되었는지 여부")
            )
        ));
  }

  @Test
  @DisplayName("유저는 회원가입 할 수 있다")
  public void register_success() throws Exception {
    MemberRegisterReq memberRegisterReq = new MemberRegisterReq("superstring77@gmail.com",
        "qwe123!@#",
        "Albatross", "1996");

    mockMvc.perform(post("/api/members/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(memberRegisterReq)))
        .andExpect(status().isOk())
        .andDo(document("member/register",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("birthYear").type(JsonFieldType.STRING).description("탄생년도")
            )
        ));
  }


  @Test
  @DisplayName("유저는 로그인할 수 있다")
  public void login_success() throws Exception {
    MemberLoginReq memberLoginReq = new MemberLoginReq("user123@gmail.com", "qwe123!@#");

    mockMvc.perform(post("/api/members/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(memberLoginReq)))
        .andExpect(status().isOk())
        .andDo(document("member/login",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("발급된 액세스 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("발급된 리프레시 토큰")
            )
        ));
  }

  @Test
  @DisplayName("유저는 AccessToken 과 RefreshToken 을 재발급 받을 수 있다")
  public void reissue_success() throws Exception {
    String accessToken = jwtTokenProvider.createAccessToken(1L);
    String refreshToken = jwtTokenProvider.createRefreshToken();
    redisUtil.setDataWithExpire("1", refreshToken, 1L);

    AccessTokenReissueReq accessTokenReissueReq = new AccessTokenReissueReq(accessToken,
        refreshToken);

    mockMvc.perform(post("/api/members/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(accessTokenReissueReq)))
        .andExpect(status().isOk())
        .andDo(document("member/reissue",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("재발급 된 액세스 토큰"),
                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                    .description("재발급 된 리프레시 토큰")
            )
        ));
  }

  @Test
  @DisplayName("유저는 정상적으로 로그아웃 할 수 있다")
  public void logout_success() throws Exception {
    String accessToken = "Bearer " + jwtTokenProvider.createAccessToken(memberId);
    String refreshToken = jwtTokenProvider.createRefreshToken();
    redisUtil.setDataWithExpire(String.valueOf(memberId), refreshToken, 1L);

    mockMvc.perform(delete("/api/members/logout")
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("member/logout",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("액세스 토큰")
            )
        ));
  }
}