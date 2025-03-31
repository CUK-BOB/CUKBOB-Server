package CUK.CUKBOB.oauth.Controller;

import CUK.CUKBOB.oauth.Dto.SignInRequest;
import CUK.CUKBOB.oauth.Dto.SignInResponse;
import CUK.CUKBOB.oauth.Service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class KakaoController {
    private final KakaoService kakaoService;

    // 1. 인가코드 받을 GET 핸들러 ← 브라우저에서 호출됨
    @GetMapping("/kakao/callback")
    public ResponseEntity<SignInResponse> kakaoCallback(@RequestParam String code) {
        // 카카오 인증 코드로 로그인 처리
        SignInResponse signInResponse = kakaoService.signInWithAuthorizationCode(code);
        return ResponseEntity.ok(signInResponse);
    }

    // 2. access token으로 로그인 처리하는 POST 핸들러 ← 클라이언트 앱에서 호출
    @PostMapping("/kakao/callback")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") String socialAccessToken, @RequestBody SignInRequest request) {
        SignInResponse response = kakaoService.signIn(socialAccessToken, request);
        return ResponseEntity.ok(response);
    }

    //로그아웃
    @PostMapping("/kakao/logout")
    public ResponseEntity<Void> signOut(Principal principal) {
        long userId = Long.parseLong(principal.getName());
        kakaoService.signOut(userId);
        return ResponseEntity.ok(null);
    }

    //탈퇴
    @DeleteMapping
    public ResponseEntity<Void> withdrawal(Principal principal) {
        long userId = Long.parseLong(principal.getName());
        kakaoService.withdraw(userId);
        return ResponseEntity.ok(null);
    }
}