package shop.zip.travel.domain.post.subTravelogue.dto.req;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

public record SubTravelogueCreateReq(
    String title,
    String content,
    int day,
    @NotNull
    List<Address> addresses,
    Set<Transportation> transportationSet,
    List<TravelPhotoCreateReq> travelPhotoCreateReqs
) {

    public SubTravelogue toSubTravelogue() {
        return new SubTravelogue(
            title,
            content,
            day,
            addresses,
            transportationSet,
            toTravelPhotos()
        );
    }

    public List<TravelPhoto> toTravelPhotos() {
        if (Objects.isNull(travelPhotoCreateReqs)) {
            return new ArrayList<>();
        }

        return travelPhotoCreateReqs.stream()
            .map(TravelPhotoCreateReq::toEntity)
            .collect(Collectors.toList());
    }
}
