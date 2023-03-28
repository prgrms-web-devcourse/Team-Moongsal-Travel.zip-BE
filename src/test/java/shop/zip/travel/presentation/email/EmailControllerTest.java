package shop.zip.travel.presentation.email;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import shop.zip.travel.domain.email.dto.request.CodeValidateReq;
import shop.zip.travel.domain.email.dto.request.EmailValidateReq;
import shop.zip.travel.global.util.RedisUtil;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class EmailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RedisUtil redisUtil;

  @Test
  @DisplayName("유저는 이메일 인증을 위해 이메일을 받을 수 있다")
  public void sendEmail_success() throws Exception {
    EmailValidateReq emailValidateReq = new EmailValidateReq("user123@gmail.com");

    mockMvc.perform(post("/api/emails")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(emailValidateReq)))
        .andExpect(status().isOk())
        .andDo(document("email/valid",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("인증하려는 이메일")
            )
        ));
  }

  @Test
  @DisplayName("유저는 인증 코드를 통해 인증할 수 있다")
  public void verifyWithVerificationCode() throws Exception {
    redisUtil.setDataWithExpire("user123@gmail.com", "123456", 3L);
    CodeValidateReq codeValidateReq = new CodeValidateReq("user123@gmail.com", "123456");

    mockMvc.perform(post("/api/emails/code")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(codeValidateReq)))
        .andExpect(status().isOk())
        .andDo(document("email/code",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("인증하려는 이메일"),
                fieldWithPath("code").type(JsonFieldType.STRING).description("인증 코드")
            )
        ));
  }
}