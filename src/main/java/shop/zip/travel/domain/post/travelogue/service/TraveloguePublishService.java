package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.subTravelogue.service.SubTraveloguePublishService;
import shop.zip.travel.domain.post.travelogue.dto.res.TraveloguePublishRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;
import shop.zip.travel.domain.post.travelogue.exception.TravelogueNotFoundException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
public class TraveloguePublishService {

  private final TravelogueRepository travelogueRepository;
  private final SubTraveloguePublishService subTraveloguePublishService;

  public TraveloguePublishService(TravelogueRepository travelogueRepository,
      SubTraveloguePublishService subTraveloguePublishService) {
    this.travelogueRepository = travelogueRepository;
    this.subTraveloguePublishService = subTraveloguePublishService;
  }

  public TraveloguePublishRes publish(Long travelogueId) {
    Travelogue travelogue = findBy(travelogueId);

    verifyPublish(travelogue);
    travelogue.changePublishStatus();
    return new TraveloguePublishRes(travelogue.getId());
  }

  private Travelogue findBy(Long travelogueId) {
    return travelogueRepository.findById(travelogueId)
        .orElseThrow(() -> {
          throw new TravelogueNotFoundException(ErrorCode.TRAVELOGUE_NOT_FOUND);
        });
  }

  private void verifyPublish(Travelogue travelogue) {
    if (travelogue.cannotPublish()) {
      throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
    }
    subTraveloguePublishService.verifySubTravelogues(travelogue.getSubTravelogues());
  }
}
