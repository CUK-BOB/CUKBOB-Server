package CUK.CUKBOB.studentstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String date;
    private Long price;
    private List<String> names;
    private RestaurantResponse restaurant;

}
