package shop.zip.travel.presentation.storage;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
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
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.storage.document.Place;
import shop.zip.travel.domain.storage.document.Storage;
import shop.zip.travel.domain.storage.dto.req.ScrapCreateReq;
import shop.zip.travel.domain.storage.dto.req.StorageCreateReq;
import shop.zip.travel.domain.storage.repository.StorageRepository;
import shop.zip.travel.global.security.JwtTokenProvider;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class StorageControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  StorageRepository storageRepository;

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  ObjectMapper objectMapper = new ObjectMapper();

  Member member;
  String accessToken;
  Storage storage;

  @BeforeEach
  void setup() {
    member = new Member("user1@gamil.com", "qwe123!@#", "Ones", "1996");
    memberRepository.save(member);
    accessToken = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    Storage savedStorage = storageRepository.save(new Storage(member.getId(), "Florence travel"));
    savedStorage.getContents().add(new Place("Duomo Cathedral", 1L));
    storage = storageRepository.save(savedStorage);
  }

  @AfterEach
  void clear() {
    memberRepository.deleteById(member.getId());
    storageRepository.deleteAll();
  }

  @Test
  @DisplayName("유저는 스크랩 문서를 생성할 수 있다")
  void createScrapDocument() throws Exception {
    StorageCreateReq storageCreateReq = new StorageCreateReq("Paris Travel");

    mockMvc.perform(post("/api/storage")
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(storageCreateReq)))
        .andExpect(status().isOk())
        .andDo(document("scrap/create-storage",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("Access Token")
            ),
            requestFields(
                fieldWithPath("title").type(JsonFieldType.STRING).description("문서 제목")
            )
        ));
  }

  @Test
  @DisplayName("유저는 여행기를 보며 스크랩을 할 수 있다")
  void createScrap_success() throws Exception {
    System.out.println(storage.getId());
    ScrapCreateReq scrapCreateReq = new ScrapCreateReq(storage.getId().toString(), "Duomo Cathedral",
        1L);

    mockMvc.perform(post("/api/storage/scrap")
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scrapCreateReq)))

        .andExpect(status().isOk())
        .andDo(document("scrap/create-scrap",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("Access Token")
            ),
            requestFields(
                fieldWithPath("storageObjectId").type(JsonFieldType.STRING)
                    .description("문서 object id"),
                fieldWithPath("content").type(JsonFieldType.STRING)
                    .description("스크랩 내용"),
                fieldWithPath("postId").type(JsonFieldType.NUMBER)
                    .description("게시글 id")
            )));
  }

  @Test
  @DisplayName("유저는 생성한 스크랩 문서 목록을 조회할 수 있다")
  void getAllScrapDocument() throws Exception {
    mockMvc.perform(get("/api/storage")
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andDo(document("scrap/get-all-storage",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("Access Token")
            ),
            responseFields(
                fieldWithPath("list[].storageObjectId").type(JsonFieldType.STRING)
                    .description("문서 object id"),
                fieldWithPath("list[].title").type(JsonFieldType.STRING).description("문서 제목")
            )));
  }

  @Test
  @DisplayName("유저는 생성한 문서를 상세 조회할 수 있다")
  void getOneScrapDocument() throws Exception {
    ObjectId storageObjectId = storage.getId();
    mockMvc.perform(get("/api/storage/{storageObjectId}", storageObjectId)
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andDo(document("scrap/get-one-storage",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("Access Token")
            ),
            pathParameters(
                parameterWithName("storageObjectId").description("문서 object id")
            ),
            responseFields(
                fieldWithPath("title").type(JsonFieldType.STRING).description("문서 제목"),
                fieldWithPath("contents[].scrapObjectId").type(JsonFieldType.STRING)
                    .description("스크랩 object id"),
                fieldWithPath("contents[].placeName").type(JsonFieldType.STRING)
                    .description("scrap 장소 이름"),
                fieldWithPath("contents[].postId").type(JsonFieldType.NUMBER).description("게시글 id")

            )));
  }

  @Test
  @DisplayName("유저는 특정 스크랩을 삭제할 수 있다")
  void deleteScrap() throws Exception {
    ObjectId storageObjectId = storage.getId();
    String scrapObjectId = storage.getContents().get(0).getScrapObjectId();

    mockMvc.perform(delete("/api/storage/{storageObjectId}/scrap/{scrapObjectId}", storageObjectId,
            new ObjectId(scrapObjectId))
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andDo(document("scrap/delete-one-scrap",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("Access Token")
            ),
            pathParameters(
                parameterWithName("storageObjectId").description("문서 object id"),
                parameterWithName("scrapObjectId").description("스크랩 object id")
            )));
  }

  @Test
  @DisplayName("유저는 스크랩 문서를 삭제할 수 있다")
  void deleteStorage() throws Exception {
    ObjectId storageObjectId = storage.getId();

    mockMvc.perform(delete("/api/storage/{storageObjectId}", storageObjectId)
            .header("AccessToken", accessToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())

        .andDo(document("scrap/delete-one-storage",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("AccessToken").description("Access Token")
            ),
            pathParameters(
                parameterWithName("storageObjectId").description("문서 object id")
            )));
  }

}
