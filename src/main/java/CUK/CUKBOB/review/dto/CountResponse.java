package CUK.CUKBOB.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CountResponse {

    private String reviewName;
    private int count;

}
