package CUK.CUKBOB.review.controller;

import CUK.CUKBOB.review.dto.CreateReviewRequest;
import CUK.CUKBOB.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{userId}/{menuId}")
    public ResponseEntity<String> createReview(
            @PathVariable Long userId,
            @PathVariable Long menuId,
            @RequestBody CreateReviewRequest createReviewRequest) throws IOException {

        // userId와 menuId를 ReviewService에 전달
        createReviewRequest.setUserId(userId);
        createReviewRequest.setMenuId(menuId);

        // 리뷰 생성
        reviewService.createReview(createReviewRequest);

        // 응답 메시지
        return ResponseEntity.status(HttpStatus.CREATED).body("Review created successfully");
    }
}