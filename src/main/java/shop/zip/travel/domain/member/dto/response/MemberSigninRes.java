package shop.zip.travel.domain.member.dto.response;

public record MemberSigninRes(
    String accessToken,
    String refreshToken
) {

}
