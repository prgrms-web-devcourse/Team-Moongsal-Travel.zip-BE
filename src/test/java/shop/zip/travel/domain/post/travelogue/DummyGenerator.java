package shop.zip.travel.domain.post.travelogue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.data.TempCountry;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.data.temp.TempCost;
import shop.zip.travel.domain.post.travelogue.data.temp.TempPeriod;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TempTravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public class DummyGenerator {

	public static TempTravelogueCreateReq createTempTravelogueReq(Member member) {
		return new TempTravelogueCreateReq(
			createTempPeriod(),
			null,
			createTempCountry(),
			null,
			new TempCost(null, null, null, null)
		);
	}

	public static Travelogue createTravelogue(Member member) {
		ArrayList<SubTravelogue> subTravelogues = new ArrayList<>();
		subTravelogues.add(createSubTravelogue());

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

	public static SubTravelogue createSubTravelogue(){
		return new SubTravelogue(
			"일본 오사카 재밌음",
			"오사카 갔는데 또 가고 싶음",
			List.of(createAddress()),
			Set.of(Transportation.BUS),
			new ArrayList<>()
		);
	}

	public static Address createAddress(){
		return new Address(
			createCountry(),
			"오사카",
			"유니버셜 스튜디오"
		);
	}

	public static Period createPeriod(){
		return new Period(
			LocalDate.of(2023, 2, 2),
			LocalDate.of(2023, 2, 3)
		);
	}

	public static Country createCountry(){
		return new Country(
			"일본"
		);
	}

	public static Cost createCost(){
		return new Cost(
			500000L
		);
	}

	public static TravelogueSimple createTravelogueSimple(Travelogue travelogue){
		return new TravelogueSimple(
			travelogue.getTitle(),
			travelogue.getPeriod(),
			travelogue.getCost().getTotal(),
			travelogue.getCountry().getName(),
			travelogue.getThumbnail(),
			travelogue.getMember().getNickname(),
			travelogue.getMember().getProfileImageUrl()
		);
	}

	public static Member createMember() {
		return new Member(
			"user@naver.com",
			"password1234!",
			"nickname",
			"1998"
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
}
