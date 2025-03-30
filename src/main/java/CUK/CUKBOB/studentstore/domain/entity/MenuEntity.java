package CUK.CUKBOB.studentstore.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String names;
    private Long price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")  // Menu 테이블에 생길 FK 컬럼 이름
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private MealEntity meal;
}
