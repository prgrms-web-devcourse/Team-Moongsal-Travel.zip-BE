package shop.zip.travel.domain.post.travelogue.service;

import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public class FakeTravelogue extends Travelogue {

	private final Long id;

	public FakeTravelogue(Long id, Travelogue travelogue, Member member) {
		super(
			travelogue.getPeriod(),
			travelogue.getTitle(),
			travelogue.getCountry(),
			travelogue.getThumbnail(),
			travelogue.getCost(),
			travelogue.getSubTravelogues(),
			member
		);
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}
}
