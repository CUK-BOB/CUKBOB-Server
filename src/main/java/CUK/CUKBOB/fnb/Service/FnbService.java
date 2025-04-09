package CUK.CUKBOB.fnb.Service;

import CUK.CUKBOB.fnb.Domain.Fnb;
import CUK.CUKBOB.fnb.Dto.Response.FnbResponseDto;
import CUK.CUKBOB.fnb.Repository.FnbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FnbService {
    private final FnbRepository fnbRepository;

    //카테고리별 fnb 목록 조회
    public List<FnbResponseDto> getFnbListByCategoryId(Long categoryId) {
        List<Fnb> fnbList = fnbRepository.findAllByCategoryId(categoryId);
        return fnbList.stream()
                .map(FnbResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    //전체 fnb 목록 조회
    public List<FnbResponseDto> getAllfnbList() {
        List<Fnb> fnbList = fnbRepository.findAll();
        return fnbList.stream()
                .map(FnbResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    //카테고리별 이름 반환
    public String getCategoryName(Long categoryId){
        switch (categoryId.intValue()) {
            case 1:
                return "카페";
            case 2:
                return "샐러드";
            case 3:
                return "음식점";
            default:
                return "카테고리";
        }
    }
}
