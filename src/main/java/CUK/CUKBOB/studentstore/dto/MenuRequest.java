package CUK.CUKBOB.studentstore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequest {
    private String date;
    private Long restaurantId;
    private Long mealId;
}
