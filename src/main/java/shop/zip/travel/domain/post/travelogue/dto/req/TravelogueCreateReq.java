package shop.zip.travel.domain.post.travelogue.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TravelogueCreateReq(
    @NotNull
    Period period,
    @NotBlank
    String title,
    @NotNull
    Country country,
    @NotBlank
    String thumbnail,
    @NotNull
    Cost cost
) {

    public Travelogue toTravelogue(Member member) {
        return new Travelogue(
            this.period,
            this.title,
            this.country,
            this.thumbnail,
            this.cost,
            member
        );
    }
}

