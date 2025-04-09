package CUK.CUKBOB.fnb.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "fnb_menu")
public class FnbMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fnb_menu_id")
    private Long id;

    @Column(name = "fnb_menu_name")
    private String name;

    @Column(name = "fnb_menu_price")
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fnb_id")
    private Fnb fnb;

    @Builder
    public FnbMenu(String name, Long price, Fnb fnb, String location, String img, Category category) {
        this.name = name;
        this.price = price;
        this.fnb = fnb;
    }
}
