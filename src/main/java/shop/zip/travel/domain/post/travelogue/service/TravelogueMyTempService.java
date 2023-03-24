package shop.zip.travel.domain.post.travelogue.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.travelogue.dto.TempTravelogueSimple;
import shop.zip.travel.domain.post.travelogue.dto.res.TempTravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@Service
@Transactional(readOnly = true)
public class TravelogueMyTempService {

  private static final boolean TEMP = false;

  private final TravelogueRepository travelogueRepository;


  public TravelogueMyTempService(TravelogueRepository travelogueRepository) {
    this.travelogueRepository = travelogueRepository;
  }

  public TravelogueCustomSlice<TempTravelogueSimpleRes> getMyTempTravelogues(Long memberId,
      Pageable pageable) {
    Slice<TempTravelogueSimple> travelogues =
        travelogueRepository.getMyTempTravelogues(memberId, pageable, TEMP);

    return TravelogueCustomSlice.toDto(
        travelogues.map(TempTravelogueSimpleRes::toDto)
    );
  }
}
