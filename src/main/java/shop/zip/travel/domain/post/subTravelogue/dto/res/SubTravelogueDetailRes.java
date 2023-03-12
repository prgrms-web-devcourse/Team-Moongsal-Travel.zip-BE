package shop.zip.travel.domain.post.subTravelogue.dto.res;

import static shop.zip.travel.domain.post.data.DefaultValue.orGetStringReturnValue;

import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

public record SubTravelogueDetailRes(
    String title,
    String content,
    int day,
    List<AddressRes> addresses,
    Set<Transportation> transportationSet,
    List<TravelPhotoCreateReq> travelPhotoCreateReqs
) {

    public static SubTravelogueDetailRes toDto(SubTravelogue subTravelogue) {
        return new SubTravelogueDetailRes(
            orGetStringReturnValue(subTravelogue.getTitle()),
            orGetStringReturnValue(subTravelogue.getContent()),
            subTravelogue.getDay(),
            subTravelogue.getAddresses()
                .stream()
                .map(AddressRes::toDto)
                .toList(),
            subTravelogue.getTransportationSet(),
            subTravelogue.getPhotos()
                .stream()
                .map(TravelPhotoCreateReq::toDto)
                .toList()
        );
    }
}
