package CUK.CUKBOB.studentstore.repository;

import CUK.CUKBOB.studentstore.domain.MenuEntity;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findAllByDate(LocalDate date);
}
