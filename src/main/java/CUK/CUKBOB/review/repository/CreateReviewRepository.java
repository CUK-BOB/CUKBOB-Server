package CUK.CUKBOB.review.repository;

import CUK.CUKBOB.review.domain.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
