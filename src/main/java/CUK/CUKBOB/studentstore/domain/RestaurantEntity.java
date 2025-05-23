package CUK.CUKBOB.studentstore.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "restaurant")
@Getter
@Setter
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 기존 전체 시간
    private String time;

    // 식사별 고정 운영시간
    private String breakfastTime;
    private String lunchTime;
    private String dinnerTime;

    private String location;
}
