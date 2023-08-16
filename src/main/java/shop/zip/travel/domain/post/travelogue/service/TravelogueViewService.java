package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueViewRepository;

@Service
public class TravelogueViewService {

  private final TravelogueViewRepository travelogueViewRepository;

  public TravelogueViewService(TravelogueViewRepository travelogueViewRepository) {
    this.travelogueViewRepository = travelogueViewRepository;
  }

  @Transactional
  public void increase(Long id, boolean canAddViewCount) {
    if (canAddViewCount) {
      travelogueViewRepository.increaseCount(id);
    }
  }
}
