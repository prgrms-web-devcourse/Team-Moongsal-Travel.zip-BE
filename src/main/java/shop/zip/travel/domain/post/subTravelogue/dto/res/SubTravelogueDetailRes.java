package shop.zip.travel.domain.post.subTravelogue.dto.res;

import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

public record SubTravelogueDetailRes(
    String title,
    String content,
    int day,
    List<Address> addresses,
    Set<Transportation> transportationSet,
    List<TravelPhotoCreateReq> travelPhotoCreateReqs
) {

    public static SubTravelogueDetailRes toDto(SubTravelogue subTravelogue) {
        return new SubTravelogueDetailRes(
            subTravelogue.getTitle(),
            subTravelogue.getContent(),
            subTravelogue.getDay(),
            subTravelogue.getAddresses(),
            subTravelogue.getTransportationSet(),
            subTravelogue.getPhotos()
                .stream()
                .map(TravelPhotoCreateReq::toDto)
                .toList()
        );
    }
}
