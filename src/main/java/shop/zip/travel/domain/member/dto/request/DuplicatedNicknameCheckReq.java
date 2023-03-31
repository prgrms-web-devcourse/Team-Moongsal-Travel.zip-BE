package shop.zip.travel.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DuplicatedNicknameCheckReq(
    @NotBlank
    @Pattern(regexp = "^[가-힣|a-zA-Z]{2,12}$")
    String nickname
) {

}
