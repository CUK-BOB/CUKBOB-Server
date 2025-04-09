package CUK.CUKBOB.fnb.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "fnb")
public class Fnb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fnb_id")
    private Long id;

    @Column(name = "fnb_name")
    private String name;

    @Column(name = "fnb_time")
    private String time;

    @Column(name = "fnb_location")
    private String location;

    @Column(name = "fnb_img")
    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Fnb(String name, String time, String location, String img, Category category) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.img = null;
        this.category = category;
    }
}
