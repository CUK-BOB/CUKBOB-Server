package CUK.CUKBOB.studentstore.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequest {
    private String date;
    private List<String> menus;
    private Long price;
    private Long restaurantId;
    private Long mealId;
}
