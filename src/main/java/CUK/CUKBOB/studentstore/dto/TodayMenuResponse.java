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
        private List<String> menu;
        private List<Integer> price;
        private String restaurant_time;
    }
}
