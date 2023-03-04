package shop.zip.travel.presentation.travelogue;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.config.QuerydslConfig;
import shop.zip.travel.global.security.JwtTokenProvider;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@Import(QuerydslConfig.class)
class TravelogueControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TravelogueRepository travelogueRepository;
  @Autowired
  private SubTravelogueRepository subTravelogueRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private Travelogue travelogue;
  private Member member;

  @BeforeEach
  void setUp() {
    member = new Member("user@gmail.com", "password123!", "nickname", "1998");
    memberRepository.save(member);
    travelogue = DummyGenerator.createNotPublishedTravelogue(member);
    travelogueRepository.saveAll(
        List.of(
            travelogue,
            DummyGenerator.createTravelogue(member),
            DummyGenerator.createTravelogue(member))
    );

    SubTravelogue subTravelogue = subTravelogueRepository.save(
        DummyGenerator.createSubTravelogue()
    );

    travelogue.addSubTravelogue(subTravelogue);
  }

  @Test
  @DisplayName("저장이 완료된 전체 게시물 리스트를 페이지별로 가져올 수 있다.")
  void test_get_all_travelogue() throws Exception {

    mockMvc.perform(get("/api/travelogues")
            .queryParam("size", "2")
            .queryParam("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-travelogue",
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
                fieldWithPath("pageable.sort.empty").description("데이터가 비어있는지에 대한 여부"),
                fieldWithPath("pageable.sort.sorted").description("데이터가 정렬되어있는지에 대한 여부"),
                fieldWithPath("pageable.sort.unsorted").description("데이터가 정렬되어 있지 않은지에 대한 여부"),
                fieldWithPath("pageable.offset").description("페이징 offset"),
                fieldWithPath("pageable.pageNumber").description("현재 요청한 페이지 넘버"),
                fieldWithPath("pageable.pageSize").description("요청한 데이터 갯수"),
                fieldWithPath("pageable.paged").description("페이징이 된 여부"),
                fieldWithPath("pageable.unpaged").description("페이징이 되지 않은 여부"),
                fieldWithPath("size").description("요청된 페이징 사이즈"),
                fieldWithPath("number").description("페이지 번호"),
                fieldWithPath("numberOfElements").description("조회된 데이터 갯수"),
                fieldWithPath("first").description("첫번째 페이지인지의 여부"),
                fieldWithPath("last").description("마지막 페이지인지의 여부"),
                fieldWithPath("empty").description("데이터가 없는지의 여부")
            )));
  }

  @Test
  @DisplayName("자신이 작성 중이던 임시 저장 글들을 불러올 수 있다.")
  public void test_get_all_temp_travelogue() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(get("/api/travelogues/temp")
            .header("AccessToken", token)
            .queryParam("size", "2")
            .queryParam("page", "0"))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-all-temp-travelogue",
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

  @Test
  @DisplayName("메인 게시물을 저장할 수 있다.")
  void test_save_travelogue() throws Exception {
    // given
    TravelogueCreateReq travelogueCreateReq = new TravelogueCreateReq(
        DummyGenerator.createPeriod(),
        "메인 게시물 제목",
        DummyGenerator.createCountry(),
        "www.naver.com",
        DummyGenerator.createCost()
    );

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(post("/api/travelogues")
            .header("AccessToken", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(travelogueCreateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("save-travelogue",
            requestFields(
                fieldWithPath("period.startDate").description("여행 시작 날짜"),
                fieldWithPath("period.endDate").description("여행 종료 날짜"),
                fieldWithPath("period.nights").description("여행 숙박 횟수"),
                fieldWithPath("title").description("게시물 제목"),
                fieldWithPath("country.name").description("여행한 나라 이름"),
                fieldWithPath("thumbnail").description("게시물 썸네일 URL"),
                fieldWithPath("cost.transportation").description("이동 수단 경비"),
                fieldWithPath("cost.lodge").description("숙박 비용"),
                fieldWithPath("cost.etc").description("기타 비용"),
                fieldWithPath("cost.total").description("전체 경비")
            ),
            responseFields(
                fieldWithPath("id").description("생성된 게시물의 pk 값"),
                fieldWithPath("nights").description("n박"),
                fieldWithPath("days").description("n일")

            )));
  }

  @Test
  @DisplayName("임시 저장된 게시글을 발행할 수 있다.")
  void test_publish_travelogue() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("publish-travelogue-success",
            responseFields(
                fieldWithPath("travelogueId").description("공개된 게시글 PK")
            )));
  }

  @Test
  @DisplayName("작성이 완료되지 않은 게시글은 발행할 수 없다.")
  void test_fail_publish_travelogue() throws Exception {

    Travelogue cannotPublishTravelogue =
        travelogueRepository.save(DummyGenerator.createNotPublishedTravelogue(member));

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(patch("/api/travelogues/{travelogueId}", cannotPublishTravelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("publish-travelogue-fail",
            responseFields(
                fieldWithPath("message").description("예외 메시지")
            )));
  }

  @Test
  @DisplayName("값들이 비어있는 게시물을 임시 저장할 수 있다.")
  void test_temp_save_travelogue() throws Exception {
    // given
    TravelogueCreateReq travelogueCreateReq = new TravelogueCreateReq(
        DummyGenerator.createPeriod(),
        null,
        DummyGenerator.createCountry(),
        "www.naver.com",
        DummyGenerator.createCost()
    );

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(post("/api/travelogues/temp")
            .header("AccessToken", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.registerModule(new JavaTimeModule())
                .writeValueAsString(travelogueCreateReq)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("save-temp-travelogue",
            requestFields(
                fieldWithPath("period.startDate").type(JsonFieldType.ARRAY).description("여행 시작 날짜")
                    .optional(),
                fieldWithPath("period.endDate").type(JsonFieldType.ARRAY).description("여행 종료 날짜")
                    .optional(),
                fieldWithPath("period.nights").type(JsonFieldType.NUMBER).description("여행 숙박 횟수")
                    .optional(),
                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목").optional(),
                fieldWithPath("country.name").type(JsonFieldType.STRING).description("여행한 나라 이름")
                    .optional(),
                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("게시물 썸네일 URL")
                    .optional(),
                fieldWithPath("cost.transportation").type(JsonFieldType.NUMBER)
                    .description("이동 수단 경비").optional(),
                fieldWithPath("cost.lodge").type(JsonFieldType.NUMBER).description("숙박 비용")
                    .optional(),
                fieldWithPath("cost.etc").type(JsonFieldType.NUMBER).description("기타 비용")
                    .optional(),
                fieldWithPath("cost.total").type(JsonFieldType.NUMBER).description("전체 경비")
                    .optional()
            ),
            responseFields(
                fieldWithPath("id").description("임시 저장된 게시물의 pk 값"),
                fieldWithPath("nights").description("n박"),
                fieldWithPath("days").description("n일")
            )));
  }

  @Test
  @DisplayName("게시글의 상세정보를 조회할 수 있다.")
  void test_get_one_travelogue() throws Exception {

    String token = "Bearer " + jwtTokenProvider.createAccessToken(member.getId());

    mockMvc.perform(get("/api/travelogues/{travelogueId}", travelogue.getId())
            .header("AccessToken", token))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-one-detail-travelogue",
            responseFields(
                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                    .description("작성자 프로필 이미지"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("Travelogue id"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("Travelogue 제목"),
                fieldWithPath("country").type(JsonFieldType.STRING)
                    .description("Travelogue 방문한 나라"),
                fieldWithPath("nights").type(JsonFieldType.NUMBER)
                    .description("Travelogue 여행 기간 중 숙박 날짜"),
                fieldWithPath("days").type(JsonFieldType.NUMBER)
                    .description("Travelogue 여행 기간 중 전체 날짜"),
                fieldWithPath("totalCost").type(JsonFieldType.NUMBER)
                    .description("Travelogue 여행 전체 경비"),
                fieldWithPath("subTravelogues[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue 리스트"),
                fieldWithPath("subTravelogues[].title").type(JsonFieldType.STRING)
                    .description("SubTravelogue의 제목"),
                fieldWithPath("subTravelogues[].content").type(JsonFieldType.STRING)
                    .description("SubTravelogue의 내용"),
                fieldWithPath("subTravelogues[].addresses[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue의 방문한 장소 리스트"),
                fieldWithPath("subTravelogues[].addresses[].region").type(JsonFieldType.STRING)
                    .description("SubTravelogue의 방문한 장소명"),
                fieldWithPath("subTravelogues[].transportationSet[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue 에서 이용한 이동 수단 리스트"),
                fieldWithPath("subTravelogues[].travelPhotoCreateReqs[]").type(JsonFieldType.ARRAY)
                    .description("SubTravelogue의 이미지 리스트").optional(),
                fieldWithPath("subTravelogues[].travelPhotoCreateReqs[].url").type(
                    JsonFieldType.STRING).description("SubTravelogue의 이미지 url").optional(),
                fieldWithPath("transportations").type(JsonFieldType.ARRAY)
                    .description("Travelogue에서의 이용한 이동 수단")
            )));
  }

  @Test
  @DisplayName("검색 Rest docs 테스트 입니다.")
  void search() throws Exception {
    Period period = DummyGenerator.createPeriod();
    Country country = DummyGenerator.createCountry();
    Cost cost = DummyGenerator.createCost();

    List<Address> addressList = new ArrayList<>();
    addressList.add(DummyGenerator.createAddress());

    Set<Transportation> transportationList = new HashSet<>();
    transportationList.add(Transportation.BUS);
    SubTravelogue subTravelogue = DummyGenerator.createSubTravelogue();

    List<SubTravelogue> subTravelogueList = new ArrayList<>();
    subTravelogueList.add(subTravelogue);

    List<SubTravelogue> subTravelogueList2 = new ArrayList<>();
    subTravelogueList.add(subTravelogue);

    List<SubTravelogue> subTravelogueList3 = new ArrayList<>();
    subTravelogueList.add(subTravelogue);

    Member member = new Member("cloudwi@naver.com", "qwe123!@#", "cloudwi", "1998");
    memberRepository.save(member);

    Travelogue travelogue = new Travelogue(period, "제목", country, "ㅇ차퍼ㅗ마오ㅓㅏㅇㄴㅎ촞앟초ㅓㅏㄴㅁㅎ", cost, true,
        subTravelogueList, member);
    Travelogue travelogue2 = new Travelogue(period, "제목", country, "ㅇ차퍼ㅗ마오ㅓㅏㅇㄴㅎ촞앟초ㅓㅏㄴㅁㅎ", cost,
        true,
        subTravelogueList2, member);
    Travelogue travelogue3 = new Travelogue(period, "제목", country, "ㅇ차퍼ㅗ마오ㅓㅏㅇㄴㅎ촞앟초ㅓㅏㄴㅁㅎ", cost,
        true,
        subTravelogueList3, member);

    travelogueRepository.save(travelogue);
    travelogueRepository.save(travelogue2);
    travelogueRepository.save(travelogue3);

    mockMvc.perform(
            get("/api/travelogues/search")
                .param("keyword", "제목")
                .param("lastTravelogue", "0")
                .param("orderType", "나중에 추가")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        .andExpect(status().isOk())
        .andDo(
            document(
                "TravelogueController/search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("keyword").description("검색 키워드").optional(),
                    parameterWithName("lastTravelogue").description("마지막 조회 된 travelogusId")
                        .optional(),
                    parameterWithName("orderType").description("조회 옵션").optional(),
                    parameterWithName("size").description("검색 사이즈").optional()
                ),
                responseFields(
                    fieldWithPath("[].travelogueId").type(JsonFieldType.NUMBER)
                        .description("여행기 아이디").optional(),
                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("여행기 제목")
                        .optional(),
                    fieldWithPath("[].nights").type(JsonFieldType.NUMBER).description("여행 몇박")
                        .optional(),
                    fieldWithPath("[].days").type(JsonFieldType.NUMBER).description("여행 몇일")
                        .optional(),
                    fieldWithPath("[].totalCost").type(JsonFieldType.NUMBER).description("여행 총 비용")
                        .optional(),
                    fieldWithPath("[].country").type(JsonFieldType.STRING).description("여행 나라")
                        .optional(),
                    fieldWithPath("[].thumbnail").type(JsonFieldType.STRING)
                        .description("여행기 썸네일 이미지 url").optional(),
                    fieldWithPath("[].member.nickname").type(JsonFieldType.STRING)
                        .description("여행기 작성자 닉네임").optional(),
                    fieldWithPath("[].member.profileImageUrl").type(JsonFieldType.STRING)
                        .description("여행기 작성자 프로필 이미지").optional()
                )
            )
        );
  }

}