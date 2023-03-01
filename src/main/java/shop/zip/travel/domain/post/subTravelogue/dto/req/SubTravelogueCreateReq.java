package shop.zip.travel.domain.post.subTravelogue.dto.req;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.TempAddress;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

public record SubTravelogueCreateReq(
    String title,
    String content,
    List<TempAddress> addresses,
    Set<Transportation> transportationSet,
    @Valid
    List<TravelPhotoCreateReq> travelPhotoCreateReqs
) {

    public SubTravelogue toSubTravelogue() {
        return new SubTravelogue(
            (title == null) ? DefaultValue.STRING.getValue() : title,
            (content == null) ? DefaultValue.STRING.getValue() : content,
            toAddresses(),
            (transportationSet == null) ? new HashSet<>() : transportationSet,
            toTravelPhotos()
        );
    }

    private List<Address> toAddresses() {
        if (addresses == null) {
            return new ArrayList<>();
        }

        return addresses.stream()
            .map(TempAddress::toAddress)
            .toList();
    }

    private List<TravelPhoto> toTravelPhotos() {
        if (travelPhotoCreateReqs == null) {
            return new ArrayList<>();
        }

        return travelPhotoCreateReqs.stream()
            .map(TravelPhotoCreateReq::toEntity)
            .toList();
    }
}
