package CUK.CUKBOB.fnb.Dto.Response;

import CUK.CUKBOB.fnb.Domain.Fnb;
import CUK.CUKBOB.fnb.Domain.FnbMenu;
import CUK.CUKBOB.fnb.Dto.FnbMenuDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FnbMenuResponseDto {
    private String fnbName;
    private String fnbTime;
    private String fnbLocation;
    private String fnbImg;
    private List<FnbMenuDto> fnbMenuList;

    public static FnbMenuResponseDto fromEntity(Fnb fnb, List<FnbMenu> fnbMenuList) {
        //메뉴 리스트로 변환
        List<FnbMenuDto> menuDtos = fnbMenuList.stream()
                .map(menu -> FnbMenuDto.builder()
                        .fnbMenuName(menu.getName())
                        .fnbMenuPrice(menu.getPrice())
                        .build())
                .collect(Collectors.toList());


        return FnbMenuResponseDto.builder()
                .fnbName(fnb.getName())
                .fnbTime(fnb.getTime())
                .fnbLocation(fnb.getLocation())
                .fnbImg(fnb.getImg())
                .fnbMenuList(menuDtos)
                .build();
    }
}