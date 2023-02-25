package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.service.MemberService;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueCreateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailRes;
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

    public Travelogue findBy(Long id) {
        return travelogueRepository.findById(id)
            .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
    }

    public TravelogueDetailRes getTravelogueDetail(Long id) {
        return TravelogueDetailRes.toDto(travelogueRepository.getTravelogueDetail(id));
    }

}

