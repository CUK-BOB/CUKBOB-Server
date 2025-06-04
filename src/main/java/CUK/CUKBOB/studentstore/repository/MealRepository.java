package CUK.CUKBOB.studentstore.repository;

import CUK.CUKBOB.studentstore.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
}
