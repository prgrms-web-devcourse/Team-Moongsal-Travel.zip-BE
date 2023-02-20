package shop.zip.travel.domain.travelog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.travelog.dto.response.TravelogueFindAllResponseDto;
import shop.zip.travel.domain.travelog.entity.Travelogue;
import shop.zip.travel.domain.travelog.repository.TravelogueRepository;

@Service
@Transactional(readOnly = true)
public class TravelogueService {

  private final TravelogueRepository travelogueRepository;

  public TravelogueService(TravelogueRepository travelogueRepository) {
    this.travelogueRepository = travelogueRepository;
  }

  public Slice<TravelogueFindAllResponseDto> findAll(Pageable pageable) {
    Slice<Travelogue> travelogues = travelogueRepository.findSliceBy(pageable);

    return travelogues.map(
        travelogue -> new TravelogueFindAllResponseDto(travelogue.getThumbnail(),
            travelogue.getTitle()));
  }
}
