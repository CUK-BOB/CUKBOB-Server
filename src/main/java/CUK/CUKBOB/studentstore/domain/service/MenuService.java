package CUK.CUKBOB.studentstore.domain.service;

import CUK.CUKBOB.studentstore.domain.entity.MenuEntity;
import CUK.CUKBOB.studentstore.domain.repository.MealRepository;
import CUK.CUKBOB.studentstore.domain.repository.MenuRepository;
import CUK.CUKBOB.studentstore.domain.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    // 전체 메뉴 중 특정 날짜에 해당하는 것만 골라주는 메서드
    public List<MenuEntity> getMenusByDate(String date) {
        return menuRepository.findAll().stream()
                .filter(menu -> date.equals(menu.getDate()))
                .toList();
    }
}
