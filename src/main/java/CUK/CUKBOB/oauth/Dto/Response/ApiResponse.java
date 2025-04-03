package CUK.CUKBOB.oauth.Dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) //데이터값이 null인 경우 응답에서 안나오도록
    private final T data;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //로그인은 데이터 값 필요
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    //로그아웃이랑 회원탈퇴는 data값 없도록
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    //fail 응답도 data 값 불필요
    public static ApiResponse<Void> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
