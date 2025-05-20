package CUK.CUKBOB.studentstore.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayMenuResponse {
    private MealGroup morning;
    private MealGroup lunch;
    private MealGroup dinner;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MealGroup {
        private List<RestaurantMenu> restaurant;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RestaurantMenu {
        private Long restaurant_id;
        private String restaurant_name;
        // 8글자 이하 메뉴
        private List<String> menu;
        private List<Integer> price;

        // 8글자 초과 메뉴
        private List<String> menuLong;
        private List<Integer> priceLong;

        private String restaurant_time;
    }
}
