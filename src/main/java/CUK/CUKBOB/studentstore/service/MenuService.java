package CUK.CUKBOB.studentstore.service;

import CUK.CUKBOB.studentstore.domain.MenuEntity;
import CUK.CUKBOB.studentstore.dto.ApiResponse;
import CUK.CUKBOB.studentstore.dto.TodayMenuResponse;
import CUK.CUKBOB.studentstore.dto.WeeklyMenuResponse;
import CUK.CUKBOB.studentstore.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public ApiResponse<TodayMenuResponse> getTodayMenus(LocalDate date) {
        List<MenuEntity> menus = menuRepository.findAllByDate(date);
        Map<String, List<TodayMenuResponse.RestaurantMenu>> grouped = new LinkedHashMap<>();

        for (MenuEntity menu : menus) {
            String mealTypeKor = menu.getMeal().getType();
            String mealTypeEng = switch (mealTypeKor) {
                case "조식" -> "morning";
                case "중식" -> "lunch";
                case "석식" -> "dinner";
                default -> null;
            };
            if (mealTypeEng == null) continue;

            String time = switch (mealTypeKor) {
                case "조식" -> menu.getRestaurant().getBreakfastTime();
                case "중식" -> menu.getRestaurant().getLunchTime();
                case "석식" -> menu.getRestaurant().getDinnerTime();
                default -> null;
            };

            List<String> menuList = List.of(menu.getNames().split(","));
            List<Integer> priceList = Arrays.stream(menu.getPrice().split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();

            if ("카페 멘사".equals(menu.getRestaurant().getName())) {
                List<String> shortMenu = new ArrayList<>();
                List<Integer> shortPrice = new ArrayList<>();
                List<String> longMenu = new ArrayList<>();
                List<Integer> longPrice = new ArrayList<>();

                for (int i = 0; i < menuList.size(); i++) {
                    String m = menuList.get(i).trim();
                    int p = priceList.get(i);
                    if (m.length() <= 10) {
                        shortMenu.add(m);
                        shortPrice.add(p);
                    } else {
                        longMenu.add(m);
                        longPrice.add(p);
                    }
                }

                TodayMenuResponse.RestaurantMenu dto = TodayMenuResponse.RestaurantMenu.builder()
                        .restaurant_id(menu.getRestaurant().getId())
                        .restaurant_name(menu.getRestaurant().getName())
                        .menu(shortMenu)
                        .price(shortPrice)
                        .menuLong(longMenu)
                        .priceLong(longPrice)
                        .restaurant_time(time)
                        .build();

                grouped.computeIfAbsent(mealTypeEng, k -> new ArrayList<>()).add(dto);

            } else {
                TodayMenuResponse.RestaurantMenu dto = TodayMenuResponse.RestaurantMenu.builder()
                        .restaurant_id(menu.getRestaurant().getId())
                        .restaurant_name(menu.getRestaurant().getName())
                        .menu(menuList)
                        .price(priceList)
                        .restaurant_time(time)
                        .build();

                grouped.computeIfAbsent(mealTypeEng, k -> new ArrayList<>()).add(dto);
            }
        }

        TodayMenuResponse response = TodayMenuResponse.builder()
                .morning(grouped.get("morning") == null ? null : TodayMenuResponse.MealGroup.builder().restaurant(grouped.get("morning")).build())
                .lunch(grouped.get("lunch") == null ? null : TodayMenuResponse.MealGroup.builder().restaurant(grouped.get("lunch")).build())
                .dinner(grouped.get("dinner") == null ? null : TodayMenuResponse.MealGroup.builder().restaurant(grouped.get("dinner")).build())
                .build();

        return ApiResponse.success("오늘 학식 조회 성공", response);
    }


    public ApiResponse<List<WeeklyMenuResponse>> getWeeklyMenus(LocalDate fromDate, LocalDate toDate) {
        List<MenuEntity> menus = menuRepository.findAllByDateBetween(fromDate, toDate);
        Map<Long, WeeklyMenuResponse> result = new HashMap<>();

        for (MenuEntity menu : menus) {
            Long restaurantId = menu.getRestaurant().getId();
            String mealTypeKor = menu.getMeal().getType();

            WeeklyMenuResponse response = result.computeIfAbsent(restaurantId, id -> WeeklyMenuResponse.builder()
                    .restaurant_id(id)
                    .restaurant_name(menu.getRestaurant().getName())
                    .restaurant_address(menu.getRestaurant().getLocation())
                    .build()
            );

            String time = switch (mealTypeKor) {
                case "조식" -> menu.getRestaurant().getBreakfastTime();
                case "중식" -> menu.getRestaurant().getLunchTime();
                case "석식" -> menu.getRestaurant().getDinnerTime();
                default -> null;
            };

            List<String> menuList = List.of(menu.getNames().split(","));
            List<Integer> priceList = Arrays.stream(menu.getPrice().split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();

            if ("카페 멘사".equals(menu.getRestaurant().getName())) {
                List<String> shortMenu = new ArrayList<>();
                List<Integer> shortPrice = new ArrayList<>();
                List<String> longMenu = new ArrayList<>();
                List<Integer> longPrice = new ArrayList<>();

                for (int i = 0; i < menuList.size(); i++) {
                    String m = menuList.get(i).trim();
                    int p = priceList.get(i);
                    if (m.length() <= 10) {
                        shortMenu.add(m);
                        shortPrice.add(p);
                    } else {
                        longMenu.add(m);
                        longPrice.add(p);
                    }
                }

                WeeklyMenuResponse.MealInfo info = WeeklyMenuResponse.MealInfo.builder()
                        .menu(shortMenu)
                        .price(shortPrice)
                        .menuLong(longMenu)
                        .priceLong(longPrice)
                        .restaurant_time(time)
                        .build();

                switch (mealTypeKor) {
                    case "조식" -> response.setMorning(info);
                    case "중식" -> response.setLunch(info);
                    case "석식" -> response.setDinner(info);
                }

            } else {
                WeeklyMenuResponse.MealInfo info = WeeklyMenuResponse.MealInfo.builder()
                        .menu(menuList)
                        .price(priceList)
                        .restaurant_time(time)
                        .build();

                switch (mealTypeKor) {
                    case "조식" -> response.setMorning(info);
                    case "중식" -> response.setLunch(info);
                    case "석식" -> response.setDinner(info);
                }
            }
        }

        return ApiResponse.success("주간 학식 조회 성공", new ArrayList<>(result.values()));
    }

}
