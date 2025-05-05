package CUK.CUKBOB.review.domain;

import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.studentstore.domain.MenuEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuEntity menu; // 메뉴와 연관된 엔티티

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자와 연관된 엔티티

    private LocalDate createDate;

    @Lob
    @Column(columnDefinition = "JSON")
    private String reviewList; // JSON 형태로 저장된 리뷰 리스트

    public ReviewEntity(MenuEntity menu, User user, LocalDate createDate, String reviewList) {
        this.menu = menu;
        this.user = user;
        this.createDate = createDate;
        this.reviewList = reviewList;
    }
}
