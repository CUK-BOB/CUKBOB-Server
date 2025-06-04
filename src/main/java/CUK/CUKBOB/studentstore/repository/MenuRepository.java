package CUK.CUKBOB.studentstore.repository;

import CUK.CUKBOB.studentstore.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 날짜의 메뉴 조회
    List<Menu> findAllByDate(LocalDate date);

    // 날짜 범위 내 메뉴 조회 (주간 조회용)
    List<Menu> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
