package shop.zip.travel.domain.post.subTravelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.SubTravelogueCreateReq;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;

@Service
@Transactional(readOnly = true)
public class SubTravelogueService {

    private final SubTravelogueRepository subTravelogueRepository;
    private final TravelogueService travelogueService;

    public SubTravelogueService(SubTravelogueRepository subTravelogueRepository,
        TravelogueService travelogueService) {
        this.subTravelogueRepository = subTravelogueRepository;
        this.travelogueService = travelogueService;
    }

    @Transactional
    public Long save(SubTravelogueCreateReq createReq, Long travelogueId) {

        Travelogue travelogue = travelogueService.findBy(travelogueId);
        SubTravelogue subTravelogue = subTravelogueRepository.save(createReq.toEntity());
        subTravelogue.getPhotos()
            .addAll(createReq.travelPhotoCreateReqs()
                .stream()
                .map(TravelPhotoCreateReq::toEntity)
                .toList());
        travelogue.add(subTravelogue);

        return subTravelogue.getId();
    }
}
