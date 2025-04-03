package CUK.CUKBOB.oauth.Dto;

import CUK.CUKBOB.oauth.Vo.Token;
import lombok.Builder;
import lombok.NonNull;

//서버가 로그인 요청을 처리한 후, 클라에게 반환하는 응답 DTO
@Builder
public record SignInResponse(
        @NonNull String accessToken,
        @NonNull String refreshToken
) {

    public static SignInResponse of(Token token, boolean isUserDollExist) {
        return SignInResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}