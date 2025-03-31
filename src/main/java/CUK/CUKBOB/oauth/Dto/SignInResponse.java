package CUK.CUKBOB.oauth.Dto;

import CUK.CUKBOB.oauth.Vo.Token;
import lombok.Builder;
import lombok.NonNull;

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