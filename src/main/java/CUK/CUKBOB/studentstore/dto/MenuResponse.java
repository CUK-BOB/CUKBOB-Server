package CUK.CUKBOB.studentstore.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {
    private Long id;
    private String date;
    private List<String> price;
    private List<String> names;
    private RestaurantResponse restaurant;
    private MealResponse meal;
}
