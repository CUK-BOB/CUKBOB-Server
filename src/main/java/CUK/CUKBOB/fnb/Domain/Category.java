package CUK.CUKBOB.fnb.Domain;

import CUK.CUKBOB.oauth.Domain.SocialType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_type")
    private String type;

    @Builder
    public Category(String type) {
        this.type = type;
    }

}
