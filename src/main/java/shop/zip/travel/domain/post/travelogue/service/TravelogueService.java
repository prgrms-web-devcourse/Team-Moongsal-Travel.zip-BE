package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@Service
@Transactional(readOnly = true)
public class TravelogueService {

	private final TravelogueRepository travelogueRepository;
	private final MemberService memberService;

	public TravelogueService(TravelogueRepository travelogueRepository,
		MemberService memberService) {
		this.travelogueRepository = travelogueRepository;
		this.memberService = memberService;
	}

	@Transactional
	public Long createTravelogue(TravelogueCreateReq createReq, Long memberId) {
		Member findMember = memberService.getMember(memberId);
		return travelogueRepository.save(createReq.toEntity(findMember))
			.getId();
	}

}
