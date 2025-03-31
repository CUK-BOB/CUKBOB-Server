package CUK.CUKBOB.oauth.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}