package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueUpdateRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.exception.SubTravelogueNotFoundException;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class TempTravelogueService {

  private final TravelogueRepository travelogueRepository;
  private final SubTravelogueRepository subTravelogueRepository;

  public TempTravelogueService(TravelogueRepository travelogueRepository,
      SubTravelogueRepository subTravelogueRepository) {
    this.travelogueRepository = travelogueRepository;
    this.subTravelogueRepository = subTravelogueRepository;
  }


  @Transactional
  public TravelogueUpdateRes updateTravelogue(Long travelogueId, Long memberId,
      TravelogueUpdateReq travelogueUpdateReq) {
    Travelogue travelogue = getMyTravelogue(travelogueId, memberId);

    travelogue.update(travelogueUpdateReq);

    return TravelogueUpdateRes.toDto(travelogue.getId());
  }

  @Transactional
  public SubTravelogueUpdateRes updateSubTravelogue(
      Long travelogueId,
      Long subTravelogueId,
      Long memberId,
      SubTravelogueUpdateReq subTravelogueUpdateReq
  ) {
    Travelogue travelogue = getMyTravelogue(travelogueId, memberId);
    SubTravelogue subTravelogue = getSubTravelogue(subTravelogueId);

    travelogue.isContain(subTravelogue);

    subTravelogue.update(subTravelogueUpdateReq.toSubTravelogueUpdate());
    travelogue.updateSubTravelogues(subTravelogue);

    return SubTravelogueUpdateRes.toDto(subTravelogueId);
  }

  private Travelogue getMyTravelogue(Long travelogueId, Long memberId) {
    Travelogue travelogue = travelogueRepository.findById(travelogueId)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
    travelogue.validWriter(memberId);
    return travelogue;
  }

  private SubTravelogue getSubTravelogue(Long subTravelogueId) {
    return subTravelogueRepository.findById(subTravelogueId)
        .orElseThrow(() -> new SubTravelogueNotFoundException(ErrorCode.SUB_TRAVELOGUE_NOT_FOUND));
  }
}
