package CUK.CUKBOB.studentstore.domain.repository;

import CUK.CUKBOB.studentstore.domain.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findAllByMeal_Id(Long mealId); // 특정 meal에 대한 메뉴 리스트
}
