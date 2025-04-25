package CUK.CUKBOB.studentstore.service;

import CUK.CUKBOB.studentstore.domain.MenuEntity;
import CUK.CUKBOB.studentstore.dto.MealResponse;
import CUK.CUKBOB.studentstore.dto.MenuResponse;
import CUK.CUKBOB.studentstore.dto.RestaurantResponse;
import CUK.CUKBOB.studentstore.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<MenuResponse> findByDate(LocalDate date) {
        List<MenuEntity> menus = menuRepository.findAllByDate(date);

        return menus.stream()
                .map(menu -> MenuResponse.builder()
                        .id(menu.getId())
                        .date(menu.getDate().toString())
                        .price(List.of(menu.getPrice().split(",")))
                        .names(List.of(menu.getNames().split(",")))
                        .restaurant(RestaurantResponse.builder()
                                .id(menu.getRestaurant().getId())
                                .name(menu.getRestaurant().getName())
                                .time(menu.getRestaurant().getTime())
                                .location(menu.getRestaurant().getLocation())
                                .build())
                        .meal(MealResponse.builder()
                                .id(menu.getMeal().getId())
                                .type(menu.getMeal().getType())
                                .build())
                        .build())
                .toList();
    }

    public List<MenuResponse> findByWeek(LocalDate date) {
        List<MenuEntity> menus_Week = menuRepository.findAllByDate(date);

        return menus_Week.stream()
                .map(menu -> MenuResponse.builder()
                        .id(menu.getId())
                        .date(menu.getDate().toString())
                        .price(List.of(menu.getPrice().split(",")))
                        .names(List.of(menu.getNames().split(",")))
                        .restaurant(RestaurantResponse.builder()
                                .id(menu.getRestaurant().getId())
                                .name(menu.getRestaurant().getName())
                                .time(menu.getRestaurant().getTime())
                                .location(menu.getRestaurant().getLocation())
                                .build())
                        .meal(MealResponse.builder()
                                .id(menu.getMeal().getId())
                                .type(menu.getMeal().getType())
                                .build())
                        .build())
                .toList();
    }
}
