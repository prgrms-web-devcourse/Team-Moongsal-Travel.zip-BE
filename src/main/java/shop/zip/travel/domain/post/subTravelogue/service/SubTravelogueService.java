package shop.zip.travel.domain.post.subTravelogue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.req.SubTravelogueCreateReq;
import shop.zip.travel.domain.post.subTravelogue.dto.res.SubTravelogueCreateRes;
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
    public SubTravelogueCreateRes save(SubTravelogueCreateReq createReq, Long travelogueId) {

        Travelogue travelogue = travelogueService.getTravelogue(travelogueId);
        SubTravelogue subTravelogue = subTravelogueRepository.save(createReq.toSubTravelogue());

        subTravelogue.updateTravelogue(travelogue);
        subTravelogue.addTravelPhotos(createReq.toTravelPhotos());

        return new SubTravelogueCreateRes(subTravelogue.getId());
    }

//    private void addPhotosTo(
//        SubTravelogue subTravelogue,
//        SubTravelogueCreateReq subTravelogueCreateReq
//    ) {
//        if (subTravelogue.getPhotos().isEmpty()) {
//            return;
//        }
//
//
//        subTravelogue.getPhotos()
//            .addAll(subTravelogueCreateReq.travelPhotoCreateReqs()
//                .stream()
//                .map(TravelPhotoCreateReq::toEntity)
//                .toList());
//    }
}
