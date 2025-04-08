package CUK.CUKBOB.fnb.Repository;

import CUK.CUKBOB.fnb.Domain.Fnb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FnbRepository extends JpaRepository<Fnb, Long> {
    List<Fnb> findAllByCategoryId(Long categoryId);
}