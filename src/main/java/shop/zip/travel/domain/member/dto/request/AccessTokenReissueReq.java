package shop.zip.travel.domain.member.dto.request;

public record AccessTokenReissueReq(
    String accessToken,
    String refreshToken
) {

}
