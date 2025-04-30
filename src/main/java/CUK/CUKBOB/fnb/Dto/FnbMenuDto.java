package CUK.CUKBOB.fnb.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FnbMenuDto {
    private String fnbMenuName;
    private Long fnbMenuPrice;
}
