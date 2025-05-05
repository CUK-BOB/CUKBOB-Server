package CUK.CUKBOB.review.service;

import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.oauth.Repository.UserRepository;
import CUK.CUKBOB.review.domain.ReviewEntity;
import CUK.CUKBOB.review.dto.CreateReviewRequest;
import CUK.CUKBOB.review.repository.CreateReviewRepository;
import CUK.CUKBOB.studentstore.domain.MenuEntity;
import CUK.CUKBOB.studentstore.repository.MenuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CreateReviewRepository createReviewRepository;
    private final MenuRepository menuRepository; // MenuRepository 필요
    private final UserRepository userRepository; // UserRepository 필요
    private final ObjectMapper objectMapper; // JSON을 String으로 변환할 때 사용

    @Transactional
    public ReviewEntity createReview(CreateReviewRequest createReviewRequest) throws IOException {
        // 메뉴와 사용자 정보 확인
        MenuEntity menu = menuRepository.findById(createReviewRequest.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        User user = userRepository.findById(createReviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 리뷰 리스트를 JSON 형태로 변환
        String reviewListJson = objectMapper.writeValueAsString(createReviewRequest.getReviewList());

        // ReviewEntity 생성 및 저장
        ReviewEntity reviewEntity = new ReviewEntity(menu, user, createReviewRequest.getCreateDate(), reviewListJson);
        return createReviewRepository.save(reviewEntity);
    }
}
