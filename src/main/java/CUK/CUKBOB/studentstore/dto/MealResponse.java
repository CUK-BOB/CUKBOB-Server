package CUK.CUKBOB.studentstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MealResponse {
    private Long id;
    private String type;
}
