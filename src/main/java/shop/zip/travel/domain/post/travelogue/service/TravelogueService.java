package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.CustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

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
    public Long save(TravelogueCreateReq createReq, Long memberId) {
        Member findMember = memberService.getMember(memberId);
        return travelogueRepository.save(createReq.toEntity(findMember))
            .getId();
    }

	public CustomSlice<TravelogueSimpleRes> getTravelogues(int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));

		Slice<TravelogueSimple> travelogues = travelogueRepository.findAllBySlice(pageRequest);

		return CustomSlice.toDto(
			travelogues.map(TravelogueSimpleRes::toDto)
		);
	}

    public Travelogue findBy(Long id) {
        return travelogueRepository.findById(id)
            .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
    }

}

