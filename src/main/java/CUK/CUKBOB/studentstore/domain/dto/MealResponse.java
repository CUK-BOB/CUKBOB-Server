package CUK.CUKBOB.studentstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MealResponse {
    private Long id;
    private String type;
    private List<MenuResponse> menus;
}
