package shop.zip.travel.domain.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@SpringBootTest
public class DummyTest {

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Test
  @DisplayName("Period 객체를 생성할 수 있다.")
  public void test_create_period() {
    Period period = new Period(
        LocalDate.of(2023, 2, 2),
        LocalDate.of(2023, 2, 3)
    );
  }

  @Test
  @DisplayName("country 객체를 생성할 수 있다.")
  public void test_create_country() {
    Country country = new Country(
        "일본"
    );
  }

  @Test
  @DisplayName("cost 객체를 생성할 수 있다.")
  public void test_create_cost() {
    Cost cost = new Cost(
        500000L
    );
  }

  @Test
  @DisplayName("member 객체를 생성할 수 있다.")
  public void test_create_member() {
    Member member = new Member(
        "user@naver.com",
        "password1234!",
        "nickname",
        1998
    );
  }

  @Test
  @DisplayName("address 객체를 생성할 수 있다.")
  public void test_create_address() {
    Address address = new Address(
        new Country("일본"),
        "오사카",
        "유니버셜 스튜디오"
    );
  }

  @Test
  @DisplayName("subTravelogue 객체를 생성할 수 있다.")
  public void test_create_subTravelogue() {
    SubTravelogue subTravelogue = new SubTravelogue(
        "일본 오사카 재밌음",
        "오사카 갔는데 또 가고 싶음",
        List.of(new Address(new Country("일본"), "오사카", "유니버셜")),
        Set.of(Transportation.BUS),
        new ArrayList<>()
    );
  }

  @Test
  @DisplayName("travelogue 객체를 생성할 수 있다.")
  public void test_create_Travelogue() {
    Travelogue travelogue = new Travelogue(
        new Period(
            LocalDate.of(2022, 2, 3),
            LocalDate.of(2022, 2, 4)
        ),
        "일본 오사카 재밌음",
        new Country("일본"),
        "www.naver.com",
        new Cost(300000L),
        List.of(new SubTravelogue(
            "일본 오사카 재밌음",
            "오사카 갔는데 또 가고 싶음",
            List.of(new Address(new Country("일본"), "오사카", "유니버셜")),
            Set.of(Transportation.BUS),
            new ArrayList<>()
        )),
        new Member(
            "user@naver.com",
            "password1234!",
            "nickname",
            1998)
    );
  }

}
