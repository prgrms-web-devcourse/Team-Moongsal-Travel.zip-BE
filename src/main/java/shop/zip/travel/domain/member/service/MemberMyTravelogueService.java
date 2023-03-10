package shop.zip.travel.domain.member.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueUpdateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueDetailRes;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueUpdateRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.exception.SubTravelogueNotFoundException;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueUpdate;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueDetailForUpdateRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueUpdateRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class MemberMyTravelogueService {

  private static final boolean PUBLISHED = true;

  private final TravelogueRepository travelogueRepository;
  private final SubTravelogueRepository subTravelogueRepository;

  public MemberMyTravelogueService(TravelogueRepository travelogueRepository,
      SubTravelogueRepository subTravelogueRepository) {
    this.travelogueRepository = travelogueRepository;
    this.subTravelogueRepository = subTravelogueRepository;
  }

  public TravelogueCustomSlice<TravelogueSimpleRes> getTravelogues(Long memberId,
      Pageable pageable) {

    Slice<TravelogueSimple> myTravelogues = travelogueRepository.getMyTravelogues(memberId,
        pageable, PUBLISHED);

    return TravelogueCustomSlice.toDto(myTravelogues.map(TravelogueSimpleRes::toDto));
  }

  public TravelogueDetailForUpdateRes getTravelogueForUpdate(Long memberId, Long travelogueId) {
    Travelogue travelogue = getMyTravelogue(travelogueId, memberId);

    return TravelogueDetailForUpdateRes.toDto(travelogue);
  }

  @Transactional
  public TravelogueUpdateRes updateTravelogue(Long travelogueId, Long memberId,
      TravelogueUpdateReq travelogueUpdateReq) {
    Travelogue travelogue = getMyTravelogue(travelogueId, memberId);
    TravelogueUpdate travelogueUpdate = travelogueUpdateReq.toTravelogueUpdate();

    travelogue.update(travelogueUpdate);

    return TravelogueUpdateRes.toDto(travelogue.getId());
  }

  public SubTravelogueDetailRes getSubTravelogueForUpdate(Long memberId, Long travelogueId,
      Long subTravelogueId) {
    Travelogue travelogue = getMyTravelogue(travelogueId, memberId);
    SubTravelogue subTravelogue = getSubTravelogue(subTravelogueId);
    travelogue.isContain(subTravelogue);

    return SubTravelogueDetailRes.toDto(subTravelogue);
  }

  private Travelogue getMyTravelogue(Long travelogueId, Long memberId) {
    Travelogue travelogue = travelogueRepository.findById(travelogueId)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
    travelogue.validateWriter(memberId);
    return travelogue;
  }

  private SubTravelogue getSubTravelogue(Long subTravelogueId) {
    return subTravelogueRepository.findById(subTravelogueId)
        .orElseThrow(() -> new SubTravelogueNotFoundException(ErrorCode.SUB_TRAVELOGUE_NOT_FOUND));
  }

  @Transactional
  public SubTravelogueUpdateRes updateSubTravelogue(
      Long travelogueId,
      Long subTravelogueId,
      Long memberId,
      SubTravelogueUpdateReq subTravelogueUpdateReq) {
    Travelogue travelogue = getMyTravelogue(travelogueId, memberId);
    SubTravelogue subTravelogue = getSubTravelogue(subTravelogueId);
    travelogue.isContain(subTravelogue);

    subTravelogue.update(subTravelogueUpdateReq.toSubTravelogueUpdate());

    travelogue.updateSubTravelogues(subTravelogue);

    return SubTravelogueUpdateRes.toDto(subTravelogue.getId());
  }
}
