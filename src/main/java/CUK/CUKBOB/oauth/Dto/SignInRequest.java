package CUK.CUKBOB.oauth.Dto;

import CUK.CUKBOB.oauth.Domain.SocialType;
import lombok.NonNull;

public record SignInRequest(
        @NonNull SocialType socialType
) {

    public static SignInRequest of(SocialType socialType) {
        return new SignInRequest(socialType);
    }
}
