package CUK.CUKBOB.oauth.Controller;

import CUK.CUKBOB.oauth.Dto.Request.NicknameRequest;
import CUK.CUKBOB.oauth.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/nickname")
    public ResponseEntity<String> setNickname(@RequestBody NicknameRequest nicknameRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        userService.setNickname(userId, nicknameRequest.getNickname());
        return ResponseEntity.ok("닉네임이 성공적으로 설정되었습니다");
    }

}
