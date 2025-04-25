package CUK.CUKBOB.studentstore.controller;

import CUK.CUKBOB.studentstore.domain.MenuEntity;
import CUK.CUKBOB.studentstore.dto.MenuResponse;
import CUK.CUKBOB.studentstore.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/api/restaurant/today")
    public List<MenuResponse> getMenusDate(@RequestParam LocalDate date) {
        return menuService.findByDate(date);
    }

}
