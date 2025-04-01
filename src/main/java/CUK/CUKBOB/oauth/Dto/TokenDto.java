package CUK.CUKBOB.oauth.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    /*
    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

     */
}