package shop.zip.travel.presentation.member;


import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import shop.zip.travel.domain.member.dto.request.MemberSignupReq;
import shop.zip.travel.domain.member.service.MemberService;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MemberService memberService;

  @Test
  @DisplayName("유저는 회원가입을 성공적으로 할 수 있다")
  public void signup() {
    MemberSignupReq memberSignupReq = new MemberSignupReq("superstring77@gmail.com", "qwe123!@#",
        "Albatross", "1996");

    doNothing().when(memberService).createMember(memberSignupReq);

//    mockMvc.perform(post("/api/"))
  }
}