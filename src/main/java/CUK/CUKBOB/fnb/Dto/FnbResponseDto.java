package CUK.CUKBOB.fnb.Dto;

import CUK.CUKBOB.fnb.Domain.Fnb;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FnbResponseDto {
    private Long fnbId;
    private Long categoryId;
    private String fnbName;

    @JsonIgnore
    private String time;
    private String fnbLocation;
    private String fnbImg;

    public static FnbResponseDto fromEntity(Fnb fnb) {
        return FnbResponseDto.builder()
                .fnbId(fnb.getId())
                .categoryId(fnb.getCategory().getId())
                .fnbName(fnb.getName())
                .time(fnb.getTime())
                .fnbLocation(fnb.getLocation())
                .fnbImg(fnb.getImg())
                .build();
    }
}
