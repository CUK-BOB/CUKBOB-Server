package CUK.CUKBOB.fnb.Repository;

import CUK.CUKBOB.fnb.Domain.FnbMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FnbMenuRepository extends JpaRepository<FnbMenu, Long> {
    List<FnbMenu> findAllByFnbId(Long fnbId);
}