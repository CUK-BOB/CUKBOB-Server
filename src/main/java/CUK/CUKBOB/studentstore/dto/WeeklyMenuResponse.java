package CUK.CUKBOB.studentstore.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyMenuResponse {
    private Long restaurant_id;
    private String restaurant_name;
    private String restaurant_address;

    private MealInfo morning;
    private MealInfo lunch;
    private MealInfo dinner;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealInfo {
        private List<String> menu;
        private List<Integer> price;
        private String restaurant_time;
    }
}