package CUK.CUKBOB.fnb.Service;

import CUK.CUKBOB.fnb.Domain.Fnb;
import CUK.CUKBOB.fnb.Dto.FnbResponseDto;
import CUK.CUKBOB.fnb.Repository.FnbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FnbService {
    private final FnbRepository fnbRepository;

    public List<FnbResponseDto> getFnbListByCategoryId(Long categoryId) {
        List<Fnb> fnbList = fnbRepository.findAllByCategoryId(categoryId);
        return fnbList.stream()
                .map(FnbResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
