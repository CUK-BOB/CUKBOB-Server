package CUK.CUKBOB.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class CreateReviewRequest {
    private Long menuId;
    private Long userId;
    private LocalDate createDate;
    private Map<String, Boolean> reviewList; // 평가 항목들

    // Constructor for Request
    public CreateReviewRequest(Long menuId, Long userId, LocalDate createDate, Map<String, Boolean> reviewList) {
        this.menuId = menuId;
        this.userId = userId;
        this.createDate = createDate;
        this.reviewList = reviewList;
    }
}
