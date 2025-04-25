package CUK.CUKBOB.studentstore.service;

import CUK.CUKBOB.studentstore.dto.RestaurantResponse;
import CUK.CUKBOB.studentstore.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(r -> new RestaurantResponse(
                        r.getId(),
                        r.getName(),
                        r.getTime(),
                        r.getLocation()
                ))
                .toList();
    }
}
