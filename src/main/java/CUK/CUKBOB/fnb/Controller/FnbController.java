package CUK.CUKBOB.fnb.Controller;

import CUK.CUKBOB.fnb.Dto.FnbResponseDto;
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
public class FnbController {
    private final FnbService fnbService;

    @GetMapping("/list/{categoryId}")
    public ResponseEntity<ApiResponse<List<FnbResponseDto>>> getFnbList(@PathVariable Long categoryId) {
        try {
            List<FnbResponseDto> fnbList = fnbService.getFnbListByCategoryId(categoryId);

            String categoryName = fnbService.getCategoryName(categoryId);

            return ResponseEntity.ok(
                    ApiResponse.success( categoryName + "목록 조회 성공", fnbList)
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


    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<FnbResponseDto>>> getAllFnbList(){
        try {
            List<FnbResponseDto> fnbList = fnbService.getAllfnbList();

            return ResponseEntity.ok(
                    ApiResponse.success("카테고리 목록 조회 성공", fnbList)
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
}
