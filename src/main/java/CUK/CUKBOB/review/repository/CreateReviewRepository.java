package CUK.CUKBOB.review.repository;

import CUK.CUKBOB.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateReviewRepository extends JpaRepository<ReviewEntity, Long> {
    // 추가적인 쿼리 메소드 필요시 여기에 추가 가능
}
