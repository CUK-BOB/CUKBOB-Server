package CUK.CUKBOB.oauth.Service;

import CUK.CUKBOB.oauth.Dto.KakaoDto;
import CUK.CUKBOB.oauth.Dto.Request.SignInRequest;
import CUK.CUKBOB.oauth.Dto.Response.SignInResponse;
import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.oauth.Domain.SocialType;
import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.oauth.Jwt.JwtTokenProvider;
import CUK.CUKBOB.oauth.Jwt.UserAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoService {

    private static final int ACCESS_TOKEN_EXPIRATION = 7200000;
    private static final int REFRESH_TOKEN_EXPIRATION = 1209600000;

    private final KakaoAccessTokenService kakaoAccessTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String kakaoClientSecret;

    //로그인
    public SignInResponse signIn(String socialAccessToken, SignInRequest request) {
        User user = getUser(socialAccessToken, request);
        return generateToken(user);
    }

    // 회원가입
    private User signUp(SocialType socialType, String email) {
        System.out.println("이메일: " + email);
        Optional<User> existingUser = userRepository.findBySocialTypeAndEmail(socialType, email);

        if (existingUser.isPresent()) {
            System.out.println("User already exists: " + email);

            return existingUser.get();
        }
        User newUser = saveUser(socialType, email);
        return userRepository.save(newUser);
    }

    //로그아웃
    public void signOut(long Id) {
        User user = findUser(Id);
        user.setRefreshToken(null);
    }

    // 회원탈퇴
    public void withdraw(HttpServletRequest request, long id) {
        User user = findUser(id);

        String kakaoAccessToken = request.getHeader("Kakao-Access-Token");
        if (kakaoAccessToken == null || kakaoAccessToken.isBlank()) {
            throw new IllegalStateException("카카오 엑세스토큰이 없습니다. 회원탈퇴를 할 수 없습니다.");
        }

        unlinkFromKakao(kakaoAccessToken); //unlink할때는 카카오 엑세스토큰 필요
        deleteUser(user);
    }

    //유저정보삭제
    private void deleteUser(User user) {
        userRepository.delete(user);
    }

    //연결끊기 (카카오 api호출이므로 카카오 엑세스토큰)
    private void unlinkFromKakao(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v1/user/unlink",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("카카오 unlink 실패: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 에러: " + e.getMessage());
        }
    }
    //유저정보받기
    private User getUser(String socialAccessToken, SignInRequest request) {
        SocialType socialType = request.socialType();
        KakaoDto userInfo = getKakaoUserInfo(socialAccessToken);
        String email = userInfo.getKakao_email();
        return signUp(socialType, email);
    }

    //유저정보 DB 저장
    @Transactional(rollbackFor = Exception.class)  // 모든 예외에 대해 롤백
    public User saveUser(SocialType socialType, String email) {
        User newUser = User.builder()
                .email(email)
                .socialType(socialType)
                .build();
        return userRepository.save(newUser);
    }


    private KakaoDto getKakaoUserInfo(String socialAccessToken) {
        return kakaoAccessTokenService.getKakaoUserInfo(socialAccessToken);
    }

    //인가코드 받아서 카카오엑세스토큰 받아오기
    public String getAccessTokenFromKakao(String code) {
        return kakaoAccessTokenService.getAccessToken(code);
    }

    //이메일 받아오기
    /*
    // private Map<String, String> getEmail(String socialAccessToken, SocialType socialType) {
        return switch (socialType) {
            case KAKAO -> kakaoAccessTokenService.getKakaoUserInfo(socialAccessToken); // 오류 수정
        };
    }*/

    //JWT 토큰 생성
    private SignInResponse generateToken(User user) {
        Authentication authentication = new UserAuthentication(user.getId(), null, null);

        String accessToken = jwtTokenProvider.generateToken(authentication, ACCESS_TOKEN_EXPIRATION);
        String refreshToken = jwtTokenProvider.generateToken(authentication, REFRESH_TOKEN_EXPIRATION);

        user.updateRefreshToken(refreshToken);
        //user.updateAccessToken(accessToken);
        userRepository.save(user);

        return new SignInResponse(user.getId(), accessToken);
    }

    //카카오 인가코드 -> 엑세스토큰 -> 사용자 정보 받고 -> JWT 발급
    public SignInResponse signInWithAuthorizationCode(String code) {
        String accessToken = kakaoAccessTokenService.getAccessToken(code); //토큰 받기
        KakaoDto userInfo = kakaoAccessTokenService.getKakaoUserInfo(accessToken); //사용자 정보 요청
        String email = userInfo.getKakao_email();

        User user = signUp(SocialType.KAKAO, email);
        //user.setAccessToken(accessToken);
        userRepository.save(user);

        return generateToken(user); //JWT 발급
    }

    //유저찾기
    private User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
}