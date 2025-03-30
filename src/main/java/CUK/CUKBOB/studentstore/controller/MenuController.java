package CUK.CUKBOB.studentstore.controller;

import CUK.CUKBOB.studentstore.domain.dto.MenuResponse;
import CUK.CUKBOB.studentstore.domain.entity.MenuEntity;
import CUK.CUKBOB.studentstore.domain.repository.MenuRepository;
import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MenuController {
    @GetMapping("/menus/{id}")
    public MenuResponse getMenu(@PathVariable Long id) throws JsonProcessingException {
        MenuEntity menu = MenuRepository.findById(id).orElseThrow();
        List<String> foods = new ObjectMapper().readValue(menu.getNames(), new TypeReference<>() {});
        return new MenuResponse(menu.getId(), menu.getPrice(), menu.getDate(), foods);
    }
}
