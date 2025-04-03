package CUK.CUKBOB.oauth.Controller;

import CUK.CUKBOB.oauth.Dto.SignInRequest;
import CUK.CUKBOB.oauth.Dto.SignInResponse;
import CUK.CUKBOB.oauth.Jwt.JwtTokenProvider;
import CUK.CUKBOB.oauth.Service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/kakao")
public class KakaoController {
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;

    // 1. 인가코드 받을 GET 핸들러 ← 브라우저에서 호출됨
    @GetMapping("/callback")
    public ResponseEntity<SignInResponse> kakaoCallback(@RequestParam String code) {
        // 카카오 인증 코드로 로그인 처리
        SignInResponse signInResponse = kakaoService.signInWithAuthorizationCode(code);
        return ResponseEntity.ok(signInResponse);
    }

    // 2. access token으로 로그인 처리하는 POST 핸들러 ← 클라이언트 앱에서 호출
    @PostMapping("/callback")
    public ResponseEntity<SignInResponse> signIn(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody SignInRequest request
    ) {
        String socialAccessToken = authorizationHeader.replace("Bearer ", "").trim();
        SignInResponse response = kakaoService.signIn(socialAccessToken, request);
        return ResponseEntity.ok(response);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> signOut(Principal principal) {
        long userId = Long.parseLong(principal.getName());
        kakaoService.signOut(userId);
        return ResponseEntity.ok("로그아웃 완료"); //여기도 SignIn에 응답 추가
    }

    //회원탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(HttpServletRequest request)    {
        String jwt = extractTokenFromHeader(request); // Authorization 헤더에서 토큰 추출
        Long userId = jwtTokenProvider.getUserFromJwt(jwt); // 토큰에서 유저 ID 파싱

        kakaoService.withdraw(request, userId);
        return ResponseEntity.ok("회원탈퇴 완료"); //여기도 SignIn에 응답 추가
    }

    //유틸함수 나중에 따로 빼기
    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); //Bearer 이후만 자름
        }
        throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
    }
}