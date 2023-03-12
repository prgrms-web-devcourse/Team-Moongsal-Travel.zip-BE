package shop.zip.travel.domain.post.subTravelogue.dto;

import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;

public record SubTravelogueUpdate(
    String title,
    String content,
    List<Address> addresses,
    Set<Transportation> transportationSet,
    List<TravelPhoto> travelPhotoCreateReqs
) {

}
