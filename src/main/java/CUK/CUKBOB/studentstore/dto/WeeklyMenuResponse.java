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
        private List<String> menu;       // 8글자 이하 메뉴
        private List<Integer> price;

        private List<String> menuLong;   // 8글자 초과 메뉴
        private List<Integer> priceLong;

        private String restaurant_time;
    }
}