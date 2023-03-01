package shop.zip.travel.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberUpdateReq(
    @NotNull
    String profileImageUrl,
    @NotBlank
    String nickname
) {

}
