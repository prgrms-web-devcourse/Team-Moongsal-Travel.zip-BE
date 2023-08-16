package shop.zip.travel.domain.post.travelogue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.fake.FakeMember;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.fake.FakeSubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimpleDetail;
import shop.zip.travel.domain.post.travelogue.dto.req.CostCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.req.CountryCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.req.PeriodCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.entity.Views;

public class DummyGenerator {

  public static Travelogue createNotPublishedTravelogueWithSubTravelogues(
      List<SubTravelogue> subTravelogues, Member member
  ) {
    return new Travelogue(
        createPeriod(),
        "일본 오사카 다녀왔어요.",
        createCountry(),
        "www.naver.com",
        createCost(),
        false,
        subTravelogues,
        member,
        new Views(0L)
    );
  }

  public static Travelogue createTravelogue(Member member) {
    ArrayList<SubTravelogue> subTravelogues = new ArrayList<>();
    subTravelogues.add(createSubTravelogue(1));
    subTravelogues.add(createSubTravelogue(2));

    return new Travelogue(
        createPeriod(),
        "일본 오사카 다녀왔어요.",
        createCountry(),
        "www.naver.com",
        createCost(),
        true,
        subTravelogues,
        member,
        new Views(0L)
    );
  }

  public static Travelogue createTravelogueWithSubTravelogues(List<SubTravelogue> subTravelogues,
      Member member) {
    Travelogue travelogue1 = new Travelogue(
        createPeriod(),
        "일본 오사카 다녀왔어요.",
        createCountry(),
        "www.naver.com",
        createCost(),
        true,
        member
    );

    subTravelogues.forEach(i-> i.updateTravelogue(travelogue1));

    return travelogue1;
  }


  public static Travelogue createTravelogueWithTitle(String title, Member member) {
    ArrayList<SubTravelogue> subTravelogues = new ArrayList<>();
    subTravelogues.add(createSubTravelogue(1));

    return new Travelogue(
        createPeriod(),
        title,
        createCountry(),
        "www.naver.com",
        createCost(),
        true,
        subTravelogues,
        member,
        new Views(0L)
    );
  }

  public static Travelogue createTravelogueWithCountry(String country, Member member) {
    ArrayList<SubTravelogue> subTravelogues = new ArrayList<>();
    subTravelogues.add(createSubTravelogue(1));

    return new Travelogue(
        createPeriod(),
        "어디 다녀왔게",
        new Country(country),
        "www.naver.com",
        createCost(),
        true,
        subTravelogues,
        member,
        new Views(0L)
    );
  }

  public static SubTravelogue createSubTravelogue(int day) {
    return new SubTravelogue(
        "일본 오사카 재밌음",
        "오사카 갔는데 또 가고 싶음",
        day,
        List.of(createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  public static SubTravelogue createSubTravelogueWithContent(String content) {
    return new SubTravelogue(
        "일본 오사카 재밌음",
        content,
        1,
        List.of(createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  public static SubTravelogue createSubTravelogueWithRegion(String region) {
    return new SubTravelogue(
        "일본 오사카 재밌음",
        "또 가고 싶음",
        1,
        List.of(new Address(region)),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  public static Address createAddress() {
    return new Address(
        "유니버셜"
    );
  }

  public static Period createPeriod() {
    return new Period(
        LocalDate.of(2023, 2, 2),
        LocalDate.of(2023, 2, 3)
    );
  }

  public static Country createCountry() {
    return new Country(
        "대한민국"
    );
  }

  public static Cost createCost() {
    return new Cost(
        500000L
    );
  }

  public static TravelogueSimple createTravelogueSimple(Travelogue travelogue) {
    return new TravelogueSimple(
        travelogue.getId(),
        travelogue.getTitle(),
        travelogue.getPeriod(),
        travelogue.getCost().getTotal(),
        travelogue.getCountry().getName(),
        travelogue.getThumbnail(),
        travelogue.getMember().getNickname(),
        travelogue.getMember().getProfileImageUrl(),
        256L
    );
  }

  public static Member createMember() {
    return new Member(
        "user@naver.com",
        "password1234!",
        "nickname",
        "1998",
        "www.naver.com"
    );
  }

  public static PeriodCreateReq createPeriodCreateReq() {
    return new PeriodCreateReq(
        LocalDate.of(2022, 2, 10),
        LocalDate.of(2022, 2, 11)
    );
  }

  public static CostCreateReq createCostCreateReq() {
    return new CostCreateReq(
        null,
        null,
        null,
        100000L
    );
  }

  public static Member createFakeMember() {
    return new FakeMember(
        1L,
        createMember()
    );
  }

  public static TravelogueUpdateReq createTravelogueUpdateReq() {
    return new TravelogueUpdateReq(
        new PeriodCreateReq(
            null,
            null
        ),
        "게시글 제목",
        new CountryCreateReq(
            "일본"
        ),
        "www.google.com",
        new CostCreateReq(
            0L,
            0L,
            0L,
            10000000L
        )
    );
  }

  public static TravelogueSimpleRes createTravelogueSimpleRes(Travelogue travelogue) {
    return TravelogueSimpleRes.toDto(createTravelogueSimple(travelogue));
  }

  public static SubTravelogueUpdateReq createSubTravelogueUpdateReq() {
    return new SubTravelogueUpdateReq(
        "일본 오사카 1일차입니다.",
        "오사카 1일차인데 눌러앉고 싶네요.",
        1,
        new ArrayList<>(),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  public static SubTravelogue createFakeSubTravelogue(Long id, int day) {
    return new FakeSubTravelogue(id, createSubTravelogue(day));
  }

  public static TempTravelogueSimple createTempTravelogueSimple(Travelogue travelogue) {
    return new TempTravelogueSimple(travelogue, 256L);
  }

  public static TravelogueSimpleDetail createTravelogueSimpleDetail(Travelogue travelogue, Member member) {
    return new TravelogueSimpleDetail(
        travelogue,
        member.getId(),
        member.getProfileImageUrl(),
        member.getNickname(),
        0L,
        false,
        false,
        1L,
        List.of(createSubTravelogue(2))
    );
  }
}
