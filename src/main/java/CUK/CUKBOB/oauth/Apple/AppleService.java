package CUK.CUKBOB.oauth.Apple;

import CUK.CUKBOB.oauth.Domain.SocialType;
import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.oauth.Dto.Response.SignInResponse;
import CUK.CUKBOB.oauth.Jwt.JwtTokenProvider;
import CUK.CUKBOB.oauth.Jwt.UserAuthentication;
import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.oauth.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AppleService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String appleClientId;

    @Value("${apple.team-id}")
    private String appleTeamId;

    @Value("${apple.key-id}")
    private String appleKeyId;

    @Value("${apple.private-key}")
    private String applePrivateKeyString;

    private static final int ACCESS_TOKEN_EXPIRATION = 7200000;
    private static final int REFRESH_TOKEN_EXPIRATION = 1209600000;

    public SignInResponse signIn(String code) {
        // 1. Apple 서버로부터 Access Token 및 ID Token 받아오기
        AppleTokenResponse tokenResponse = getAppleToken(code);

        // 2. ID Token에서 사용자 정보(이메일, sub) 추출
        String email = getEmailFromIdToken(tokenResponse.getIdToken());

        // 3. 로그인 또는 회원가입 처리
        User user = userRepository.findBySocialTypeAndEmail(SocialType.APPLE, email)
                .orElseGet(() -> signUp(email));

        // 4. 자체 JWT 토큰 발급
        return generateToken(user);
    }

    private User signUp(String email) {
        User newUser = User.builder()
                .email(email)
                .socialType(SocialType.APPLE)
                .build();
        newUser = userRepository.saveAndFlush(newUser);

        // 기본 닉네임 설정
        String defaultNick = userService.generateDefaultNickname(newUser.getId());
        newUser.setNickname(defaultNick);

        return userRepository.save(newUser);
    }

    private AppleTokenResponse getAppleToken(String code) {
        String clientSecret = createAppleClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleClientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<AppleTokenResponse> response = restTemplate.postForEntity(
                    "https://appleid.apple.com/auth/token", request, AppleTokenResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new IllegalStateException("Apple 토큰 요청 실패", e);
        }
    }

    // .p8 키 파일을 로드해서 JWT(Client Secret) 생성
    private String createAppleClientSecret() {
        try {

            String realPrivateKey = applePrivateKeyString.replace("\\n", "\n");

            Reader pemReader = new StringReader(realPrivateKey);
            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKey privateKey = converter.getPrivateKey((PrivateKeyInfo) pemParser.readObject());

            return Jwts.builder()
                    .setHeaderParam("kid", appleKeyId)
                    .setHeaderParam("alg", "ES256")
                    .setIssuer(appleTeamId)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(appleClientId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5))) // 5분 유효
                    .signWith(privateKey, SignatureAlgorithm.ES256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Apple Client Secret 생성 실패", e);
        }
    }

    // id_token 디코딩하여 email 추출 (간편 검증)
    private String getEmailFromIdToken(String idToken) {
        try {
            // JWT는 "header.payload.signature" 구조. payload만 뜯어서 확인
            String[] parts = idToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            Map<String, Object> claims = objectMapper.readValue(payload, Map.class);
            return (String) claims.get("email");
        } catch (Exception e) {
            throw new RuntimeException("Apple ID Token 파싱 실패", e);
        }
    }

    // JWT 토큰 생성 (기존 로직 재사용)
    private SignInResponse generateToken(User user) {
        Authentication authentication = new UserAuthentication(user.getId(), null, null);
        String accessToken = jwtTokenProvider.generateToken(authentication, ACCESS_TOKEN_EXPIRATION);
        String refreshToken = jwtTokenProvider.generateToken(authentication, REFRESH_TOKEN_EXPIRATION);
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
        return new SignInResponse(user.getId(), accessToken);
    }
}