package CUK.CUKBOB.studentstore.controller;

import CUK.CUKBOB.studentstore.dto.ApiResponse;
import CUK.CUKBOB.studentstore.dto.TodayMenuResponse;
import CUK.CUKBOB.studentstore.dto.WeeklyMenuResponse;
import CUK.CUKBOB.studentstore.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/today")
    public ApiResponse<TodayMenuResponse> getTodayMenus(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return menuService.getTodayMenus(date);
    }

    @GetMapping("/week")
    public ApiResponse<List<WeeklyMenuResponse>> getWeeklyMenus(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return menuService.getWeeklyMenus(from, to);
    }
}
