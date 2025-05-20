package CUK.CUKBOB.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewListResponse {
    private List<Integer> total_review;
    private MyReviewResponse my_review;
    private List<ReviewResponse> reviews;
}


