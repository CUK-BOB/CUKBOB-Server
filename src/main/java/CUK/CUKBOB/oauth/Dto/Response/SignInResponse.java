package CUK.CUKBOB.oauth.Dto.Response;

import CUK.CUKBOB.oauth.Vo.Token;
import lombok.Builder;
import lombok.NonNull;

//서버가 로그인 요청을 처리한 후, 클라에게 반환하는 응답 DTO
@Builder
public record SignInResponse(
        Long userId,
        String accessToken
) {

    public static SignInResponse of(Long userId, Token token) {
        return SignInResponse.builder()
                .userId(userId)
                .accessToken(token.getAccessToken())
                .build();
    }
}