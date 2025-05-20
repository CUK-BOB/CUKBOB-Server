package CUK.CUKBOB.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private List<Boolean> review_list;
}
