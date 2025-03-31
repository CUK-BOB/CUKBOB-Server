package CUK.CUKBOB.oauth.Service;

import CUK.CUKBOB.oauth.Dto.KakaoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAccessTokenService {

    private final RestTemplate restTemplate;

    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String kakaoClientSecret;

    public KakaoDto getKakaoUserInfo(String socialAccessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + socialAccessToken);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            //카카오 API에 사용자 정보 요청
            ResponseEntity<Map> responseData = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    httpEntity,
                    Map.class
            );

            Map<String, Object> responseBody = responseData.getBody();

            if (responseBody == null) {
                throw new IllegalStateException("Response body is null");
            }

            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");

            if (kakaoAccount == null) {
                throw new IllegalStateException("Kakao account not found");
            }

            //이메일 정보 추출
            String email = (String) kakaoAccount.get("email");

            //프로필 정보 추출
            /*
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            String nickname = (profile != null) ? (String) profile.get("nickname") : null;
            */

            KakaoDto userInfo = new KakaoDto();
            userInfo.setKakao_email(email);

            //결과 맵 생성
            /*
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("email", email);
            //userInfo.put("nickname", nickname);
            */

            return userInfo;

        } catch (Exception exception) {
            throw new IllegalStateException("Failed to get Kakao user info", exception);
        }

    }

    //AccessToken 받아오기
    public String getAccessToken(String code) {
        String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("client_secret", kakaoClientSecret);
        body.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenRequestUrl, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null || responseBody.get("access_token") == null) {
            throw new IllegalStateException("Access token is null");
        }

        return responseBody.get("access_token").toString();
    }
}