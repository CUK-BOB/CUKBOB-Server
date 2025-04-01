package CUK.CUKBOB.oauth.Service;

import CUK.CUKBOB.oauth.Dto.KakaoDto;
import CUK.CUKBOB.oauth.Dto.SignInRequest;
import CUK.CUKBOB.oauth.Dto.SignInResponse;
import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.oauth.Domain.SocialType;
import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.oauth.Jwt.JwtTokenProvider;
import CUK.CUKBOB.oauth.Jwt.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

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

    //로그인
    public SignInResponse signIn(String socialAccessToken, SignInRequest request) {
        User user = getUser(socialAccessToken, request);
        user.setAccessToken(socialAccessToken);
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

        System.out.println("저장시도: " + email);
        User newUser = saveUser(socialType, email);
        return userRepository.save(newUser);
    }

    //로그아웃
    public void signOut(long Id) {
        User user = findUser(Id);
        user.resetRefreshToken();
    }

    // 회원탈퇴
    public void withdraw(long id) {
        User user = findUser(id);

        String accessToken = user.getAccessToken();
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("AccessToken is missing. Cannot unlink Kakao account.");
        }

        unlinkFromKakao(accessToken); // Kakao 연결 끊기
        deleteUser(user);             // DB에서 유저 삭제
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
        System.out.println("Saving new user: " + email);

        User newUser = User.builder()
                .email(email)
                .socialType(socialType)
                .build();
        System.out.println("저장완료: " + email);
        return userRepository.save(newUser);
    }


    private KakaoDto getKakaoUserInfo(String socialAccessToken) {
        return kakaoAccessTokenService.getKakaoUserInfo(socialAccessToken);
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
        userRepository.save(user);

        return new SignInResponse(accessToken, refreshToken);
    }

    public String getAccessTokenFromKakao(String code) {
        return kakaoAccessTokenService.getAccessToken(code);
    }

    //카카오 인가코드 -> 엑세스토큰 -> 사용자 정보 받고 -> JWT 발급
    public SignInResponse signInWithAuthorizationCode(String code) {
        String accessToken = kakaoAccessTokenService.getAccessToken(code); //토큰 받기
        KakaoDto userInfo = kakaoAccessTokenService.getKakaoUserInfo(accessToken); //사용자 정보 요청
        String email = userInfo.getKakao_email();

        User user = signUp(SocialType.KAKAO, email); //DB 저장 or 기존 유저 조회
        user.setAccessToken(accessToken); // ✅ accessToken 저장!!
        userRepository.save(user);       // ✅ 저장 꼭 하기!

        return generateToken(user); //JWT 발급
    }

    //유저찾기
    private User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
    private void deleteUser(User user) {
        userRepository.delete(user);
    }

    private void unlinkFromKakao(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                entity,
                String.class
        );
    }



}