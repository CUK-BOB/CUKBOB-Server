package CUK.CUKBOB.studentstore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String names;
    private String price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")  // Menu 테이블에 생길 FK 컬럼 이름
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private MealEntity meal;
}
