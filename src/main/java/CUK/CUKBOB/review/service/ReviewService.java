package CUK.CUKBOB.review.service;

import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.review.domain.ReviewEntity;
import CUK.CUKBOB.review.dto.CreateReviewRequest;
import CUK.CUKBOB.review.repository.CreateReviewRepository;
import CUK.CUKBOB.studentstore.domain.MenuEntity;
import CUK.CUKBOB.studentstore.repository.MenuRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CreateReviewRepository createReviewRepository;
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
        createReviewRepository.save(review);
    }
}

