package CUK.CUKBOB.studentstore.domain.service;

import CUK.CUKBOB.studentstore.domain.entity.MealEntity;
import CUK.CUKBOB.studentstore.domain.entity.MenuEntity;
import CUK.CUKBOB.studentstore.domain.repository.MealRepository;
import CUK.CUKBOB.studentstore.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final MenuRepository menuRepository;

    //특정 mealId에 해당하는 메뉴들 조회
    public List<MenuEntity> getMenusByMeal(Long mealId) {
        return menuRepository.findAllByMeal_Id(mealId); //MenuEntity에서 meal의 id로 필터링한 결과
    }
    
//    DB에 있는 모든 Meal 목록을 가져오는 매서드
//    public List<MealEntity> getAllMeals() {
//        return mealRepository.findAll();
//    }

    // 특정 id의 meal 반환
    public MealEntity getMeal(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found"));
    }
}
