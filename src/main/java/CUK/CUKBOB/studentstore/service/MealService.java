package CUK.CUKBOB.studentstore.service;

import CUK.CUKBOB.studentstore.dto.MealResponse;
import CUK.CUKBOB.studentstore.domain.Meal;
import CUK.CUKBOB.studentstore.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;

    //특정 mealId에 해당하는 메뉴들 조회
    public List<MealResponse> getAllByMeals() {
        return mealRepository.findAll().stream()
                .map(meal -> new MealResponse(meal.getId(), meal.getType()))
                .toList();
    }

    // 특정 id의 meal 반환
    public Meal getMeal(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
    }
}
