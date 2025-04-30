package CUK.CUKBOB.fnb.Service;

import CUK.CUKBOB.fnb.Domain.Fnb;
import CUK.CUKBOB.fnb.Domain.FnbMenu;
import CUK.CUKBOB.fnb.Dto.Response.FnbMenuResponseDto;
import CUK.CUKBOB.fnb.Repository.FnbMenuRepository;
import CUK.CUKBOB.fnb.Repository.FnbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FnbMenuService {
    private final FnbMenuRepository fnbMenuRepository;
    private final FnbRepository fnbRepository;

    // fnb menu 목록 조회
    public List<FnbMenuResponseDto> getFnbMenuListByFnbId(Long fnbId) {
        List<FnbMenu> fnbMenuList = fnbMenuRepository.findAllByFnbId(fnbId);

        Fnb fnb = fnbRepository.findById(fnbId)
                .orElseThrow(() -> new IllegalArgumentException("Fnb not found for id: " + fnbId));

        return List.of(FnbMenuResponseDto.fromEntity(fnb, fnbMenuList));
    }
}