package CUK.CUKBOB.fnb.Controller;

import CUK.CUKBOB.fnb.Dto.Response.FnbMenuResponseDto;
import CUK.CUKBOB.fnb.Service.FnbMenuService;
import CUK.CUKBOB.fnb.Service.FnbService;
import CUK.CUKBOB.oauth.Dto.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fnb")
public class FnbMenuController {
    private final FnbMenuService fnbMenuService;

    @GetMapping("/menu/list/{fnbId}")
    public ResponseEntity<ApiResponse<List<FnbMenuResponseDto>>> getFnbMenuList(@PathVariable Long fnbId) {
        try {
            // fnbId로 메뉴 목록 조회
            List<FnbMenuResponseDto> fnbMenuResponseDtoList = fnbMenuService.getFnbMenuListByFnbId(fnbId);
            return ResponseEntity.ok(
                    ApiResponse.success("메뉴 조회 성공", fnbMenuResponseDtoList)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(500, "서버 오류가 발생했습니다."));
        }
    }
}