package shop.zip.travel.domain.post.travelogue.dto.res;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.dto.SubTravelogueDetailRes;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TravelogueDetailRes(
    String profileImageUrl,
    String nickname,
    Long id,
    String title,
    String country,
    Long nights,
    Long days,
    Long totalCost,
    List<SubTravelogueDetailRes> subTravelogues,
    Set<Transportation> transportations
) {

    public static TravelogueDetailRes toDto(Travelogue travelogue) {
        long nights = ChronoUnit.DAYS.between(travelogue.getPeriod().getStartDate(),
            travelogue.getPeriod().getEndDate());

        return new TravelogueDetailRes(
            travelogue.getMember().getProfileImageUrl(),
            travelogue.getMember().getNickname(),
            travelogue.getId(),
            travelogue.getTitle(),
            travelogue.getCountry().getName(),
            nights,
            nights + 1,
            travelogue.getCost().getTotal(),
            travelogue.getSubTravelogues()
                .stream()
                .map(SubTravelogueDetailRes::toDto)
                .toList(),
            travelogue.getSubTravelogues()
                .stream()
                .map(SubTravelogue::getTransportationSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
        );
    }
}
