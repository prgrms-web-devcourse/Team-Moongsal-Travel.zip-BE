package shop.zip.travel.domain.member.dto.response;

public record MemberLoginRes(
    String accessToken,
    String refreshToken
) {

}
