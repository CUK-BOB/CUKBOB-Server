package CUK.CUKBOB.oauth.Apple;

import CUK.CUKBOB.oauth.Dto.Response.ApiResponse;
import CUK.CUKBOB.oauth.Dto.Response.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/apple")
public class AppleController {

    private final AppleService appleService;

    // 프론트(iOS앱)나 웹에서 받은 인가코드(code)로 로그인 요청
    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<SignInResponse>> appleLogin(@RequestParam("code") String code) {
        try {
            SignInResponse response = appleService.signIn(code);
            return ResponseEntity.ok(ApiResponse.success("애플 로그인 성공", response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail(400, "애플 로그인 실패: " + e.getMessage()));
        }
    }
}