package CUK.CUKBOB.oauth.Controller;

import CUK.CUKBOB.oauth.Dto.Request.NicknameRequest;
import CUK.CUKBOB.oauth.Dto.Response.ApiResponse;
import CUK.CUKBOB.oauth.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /*
    @PostMapping("/nickname")
    public ResponseEntity<ApiResponse<Void>> setNickname(@RequestBody NicknameRequest nicknameRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = Long.parseLong(authentication.getPrincipal().toString());

            userService.setNickname(userId, nicknameRequest.getNickname());

            return ResponseEntity.ok(
                    ApiResponse.success("닉네임이 성공적으로 설정되었습니다.", null)
            );
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(500, "서버 오류가 발생했습니다."));
        }
    }
*/

    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(@RequestBody NicknameRequest nicknameRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = Long.parseLong(authentication.getPrincipal().toString());

            userService.updateNickname(userId, nicknameRequest.getNickname());

            return ResponseEntity.ok(ApiResponse.success("닉네임 변경 완료", null));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(500, "서버 오류가 발생했습니다."));
        }
    }
}