package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.travelogue.dto.res.TraveloguePublishRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class TraveloguePublishService {

  private final TravelogueRepository travelogueRepository;

  public TraveloguePublishService(TravelogueRepository travelogueRepository) {
    this.travelogueRepository = travelogueRepository;
  }

  public TraveloguePublishRes publish(Long travelogueId) {
    Travelogue travelogue = findBy(travelogueId);

    travelogue.changePublishStatus();
    return new TraveloguePublishRes(travelogue.getId());
  }

  private Travelogue findBy(Long travelogueId) {
    return travelogueRepository.findById(travelogueId)
        .orElseThrow(() -> new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND));
  }
}
