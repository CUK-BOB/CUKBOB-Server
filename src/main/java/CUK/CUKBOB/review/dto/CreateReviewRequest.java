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
    private Map<String, Boolean> reviewList;
}
