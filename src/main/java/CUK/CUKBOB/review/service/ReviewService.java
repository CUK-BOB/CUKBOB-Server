package CUK.CUKBOB.review.service;

import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.review.domain.ReviewEntity;
import CUK.CUKBOB.review.dto.CreateReviewRequest;
import CUK.CUKBOB.review.dto.MyReviewDto;
import CUK.CUKBOB.review.dto.ReviewDto;
import CUK.CUKBOB.review.dto.ReviewListResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        List<List<Boolean>> reviewLists = new ArrayList<>();

        int[] totalCounts = new int[5]; // [isLarge, isTasty, isClean, isKind, isCheap]

        List<ReviewDto> reviewDtos = new ArrayList<>();
        MyReviewDto myReviewDto = null;

        for (ReviewEntity review : reviews) {
            List<Boolean> list = ReviewUtil.parseReviewListArray(review.getReviewList());

            for (int i = 0; i < 5; i++) {
                if (list.get(i)) totalCounts[i]++;
            }

            reviewDtos.add(new ReviewDto(review.getId(), list));

            if (review.getUser().getId().equals(userId)) {
                myReviewDto = new MyReviewDto(review.getUser().getId(), review.getId(), list);
            }
        }

        return new ReviewListResponse(
                Arrays.stream(totalCounts).boxed().collect(Collectors.toList()),
                myReviewDto,
                reviewDtos
        );
    }
}

