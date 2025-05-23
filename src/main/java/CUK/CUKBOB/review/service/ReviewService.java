package CUK.CUKBOB.review.service;

import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.review.domain.ReviewEntity;
import CUK.CUKBOB.review.dto.*;
import CUK.CUKBOB.review.repository.ReviewRepository;
import CUK.CUKBOB.review.util.ReviewUtil;
import CUK.CUKBOB.studentstore.domain.MenuEntity;
import CUK.CUKBOB.studentstore.repository.MenuRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createReview(CreateReviewRequest request) throws JsonProcessingException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuEntity menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        // reviewList 맵을 List로 변환
        Map<String, Boolean> reviewData = request.getReviewList();

        List<Boolean> reviewList = reviewData.values().stream()
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        String reviewListJson = objectMapper.writeValueAsString(reviewList);

        ReviewEntity review = new ReviewEntity(
                menu,
                user,
                LocalDate.now(),
                reviewListJson
        );
        reviewRepository.save(review);
    }

    public ReviewListResponse getReviewsByMenuAndUser(Long menuId, Long userId) {
        List<ReviewEntity> reviews = reviewRepository.findByMenuId(menuId);

        int[] totalCounts = new int[5]; // [isLarge, isTasty, isClean, isKind, isCheap]

        List<ReviewResponse> reviewDtos = new ArrayList<>();
        MyReviewResponse myReviewResponse = null;

        for (ReviewEntity review : reviews) {
            List<Boolean> list = ReviewUtil.parseReviewListArray(review.getReviewList());

            // 각 항목별 리뷰 개수 증가
            for (int i = 0; i < 5; i++) {
                if (list.get(i)) totalCounts[i]++;
            }

            reviewDtos.add(new ReviewResponse(review.getId(), list));

            if (review.getUser().getId().equals(userId)) {
                myReviewResponse = new MyReviewResponse(review.getUser().getId(), review.getId(), list);
            }
        }

        // 항목별 개수와 이름을 맵핑한 후 내림차순 정렬
        Map<String, Integer> totalCountsMap = new HashMap<>();
        totalCountsMap.put("isLarge", totalCounts[0]);
        totalCountsMap.put("isTasty", totalCounts[1]);
        totalCountsMap.put("isClean", totalCounts[2]);
        totalCountsMap.put("isKind", totalCounts[3]);
        totalCountsMap.put("isCheap", totalCounts[4]);

        List<CountResponse> sortedTotalReview = totalCountsMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .map(entry -> new CountResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ReviewListResponse(sortedTotalReview, myReviewResponse, reviewDtos);
    }

    public void deleteReview(Long reviewId, Long userId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }
}

