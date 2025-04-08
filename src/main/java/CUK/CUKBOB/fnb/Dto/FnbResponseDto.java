package CUK.CUKBOB.fnb.Dto;

import CUK.CUKBOB.fnb.Domain.Fnb;
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
    private String time;
    private String location;
    private String restImg;

    public static FnbResponseDto fromEntity(Fnb fnb) {
        return FnbResponseDto.builder()
                .fnbId(fnb.getId())
                .categoryId(fnb.getCategory().getId())
                .fnbName(fnb.getName())
                .time(fnb.getTime())
                .location(fnb.getLocation())
                .restImg(fnb.getImg())
                .build();
    }
}
