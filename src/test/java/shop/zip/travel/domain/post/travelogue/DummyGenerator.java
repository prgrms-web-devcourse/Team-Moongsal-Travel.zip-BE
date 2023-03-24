package shop.zip.travel.domain.post.travelogue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.fake.FakeMember;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.data.TempCountry;
import shop.zip.travel.domain.post.fake.FakeSubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.TempAddress;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.data.temp.TempCost;
import shop.zip.travel.domain.post.travelogue.data.temp.TempPeriod;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public class DummyGenerator {

  public static Travelogue createTempTravelogue(Member member) {
    return new Travelogue(
        createPeriod(),
        DefaultValue.STRING.getValue(),
        createCountry(),
        DefaultValue.STRING.getValue(),
        createCost(),
        false,
        member
    );
  }

  public static Travelogue createNotPublishedTravelogue(Member member) {
    ArrayList<SubTravelogue> subTravelogues = new ArrayList<>();
    subTravelogues.add(createSubTravelogue(1));

    return new Travelogue(
        createPeriod(),
        "일본 오사카 다녀왔어요.",
        createCountry(),
        "www.naver.com",
        createCost(),
        false,
        subTravelogues,
        member
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
        member
    );
  }

  public static Travelogue createTravelogueWithSubTravelogues(List<SubTravelogue> subTravelogues,
      Member member) {
    return new Travelogue(
        createPeriod(),
        "일본 오사카 다녀왔어요.",
        createCountry(),
        "www.naver.com",
        createCost(),
        true,
        subTravelogues,
        member
    );
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
        member
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
        member
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
        "오사카 갔는데 또 가고 싶음",
        1,
        List.of(new Address(region)),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  public static Address createAddress() {
    return new Address(
        "일본 오사카 유니버셜 스튜디오"
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

  public static Member createFakeMember() {
    return new FakeMember(
        1L,
        createMember()
    );
  }

  public static TempPeriod createTempPeriod() {
    return new TempPeriod(
        null,
        null
    );
  }

  public static TempCountry createTempCountry() {
    return new TempCountry(
        "일본"
    );
  }

  public static TempAddress createTempAddress() {
    return new TempAddress(
        "일본 오사카 유니버셜 스튜디오"
    );
  }

  public static TempCost createTempCost() {
    return new TempCost(
        0L,
        0L,
        0L,
        10000000L
    );
  }

  public static SubTravelogue createTempSubTravelogue(int dayOfSubTravelogue) {
    return new SubTravelogue(
        "일본 오사카 재밌음",
        DefaultValue.STRING.getValue(),
        dayOfSubTravelogue,
        List.of(createAddress()),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  public static TravelogueSimpleRes createTravelogueSimpleRes(Travelogue travelogue) {
    return TravelogueSimpleRes.toDto(createTravelogueSimple(travelogue));
  }

  public static TravelogueUpdateReq createTravelogueUpdateReq() {
    return new TravelogueUpdateReq(
        new TempPeriod(
            LocalDate.of(2022, 2, 13),
            LocalDate.of(2022, 2, 14)
        ),
        "일본 오사카 방문했어요.",
        createTempCountry(),
        "www.naver.com",
        createTempCost()
    );
  }

  public static SubTravelogueUpdateReq createSubTravelogueUpdateReq() {
    return new SubTravelogueUpdateReq(
        "일본 오사카 1일차입니다.",
        "오사카 1일차인데 눌러앉고 싶네요.",
        1,
        List.of(createTempAddress()),
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
}
