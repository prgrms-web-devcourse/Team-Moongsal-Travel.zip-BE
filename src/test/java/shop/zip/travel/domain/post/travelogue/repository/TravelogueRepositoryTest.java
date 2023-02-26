package shop.zip.travel.domain.post.travelogue.repository;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class TravelogueRepositoryTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  TravelogueRepository travelogueRepository;

  @Test
  void search() throws Exception {
    // Period생성
    Period period = new Period(LocalDateTime.now().minusDays(2L), LocalDateTime.now());
    // Country 생성
    Country country = new Country("가나");
    // Cost 생성
    Cost cost = new Cost(1000L, 1000L, 1000L, 3000L);

    List<Address> addressList = new ArrayList<>();
    addressList.add(new Address(country, "도시임", "랜드마크임"));

    Set<Transportation> transportationList = new HashSet<>();
    transportationList.add(Transportation.BUS);
    SubTravelogue subTravelogue = new SubTravelogue("서브 트레블 제목임", "가나", addressList,
        transportationList, null);

    List<SubTravelogue> subTravelogueList = new ArrayList<>();
    subTravelogueList.add(subTravelogue);

    List<SubTravelogue> subTravelogueList2 = new ArrayList<>();
    subTravelogueList.add(subTravelogue);

    List<SubTravelogue> subTravelogueList3 = new ArrayList<>();
    subTravelogueList.add(subTravelogue);

    Member member = new Member("cloudwi@naver.com", "qwe123!@#", "cloudwi");
    memberRepository.save(member);

    Travelogue travelogue = new Travelogue(period, "제목", country, "ㅇ차퍼ㅗ마오ㅓㅏㅇㄴㅎ촞앟초ㅓㅏㄴㅁㅎ", cost,
        subTravelogueList, member);
    Travelogue travelogue2 = new Travelogue(period, "제목", country, "ㅇ차퍼ㅗ마오ㅓㅏㅇㄴㅎ촞앟초ㅓㅏㄴㅁㅎ", cost,
        subTravelogueList2, member);
    Travelogue travelogue3 = new Travelogue(period, "제목", country, "ㅇ차퍼ㅗ마오ㅓㅏㅇㄴㅎ촞앟초ㅓㅏㄴㅁㅎ", cost,
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
                    fieldWithPath("[].title").type(JsonFieldType.STRING).description("여행기 제목").optional(),
                    fieldWithPath("[].nights").type(JsonFieldType.NUMBER).description("여행 몇박").optional(),
                    fieldWithPath("[].days").type(JsonFieldType.NUMBER).description("여행 몇일").optional(),
                    fieldWithPath("[].totalCost").type(JsonFieldType.NUMBER).description("여행 총 비용").optional(),
                    fieldWithPath("[].country").type(JsonFieldType.STRING).description("여행 나라").optional(),
                    fieldWithPath("[].thumbnail").type(JsonFieldType.STRING)
                        .description("여행기 썸네일 이미지 url").optional(),
                    fieldWithPath("[].memberSimpleRes.nickname").type(JsonFieldType.STRING)
                        .description("여행기 작성자 닉네임").optional(),
                    fieldWithPath("[].memberSimpleRes.profileImageUrl").type(JsonFieldType.STRING)
                        .description("여행기 작성자 프로필 이미지").optional()
                )
            )
        );
  }
}