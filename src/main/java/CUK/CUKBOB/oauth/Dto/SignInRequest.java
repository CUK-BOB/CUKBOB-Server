package CUK.CUKBOB.oauth.Dto;

import CUK.CUKBOB.oauth.Domain.SocialType;
import lombok.NonNull;

//클라이언트가 로그인 요청을 보낼 때 사용하는 DTO
public record SignInRequest(
        @NonNull SocialType socialType
) {

    public static SignInRequest of(SocialType socialType) {
        return new SignInRequest(socialType);
    }
}
