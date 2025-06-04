package CUK.CUKBOB.review.domain;


import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.studentstore.domain.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity(name = "review")
@Getter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 생성
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate createDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reviewList;

    public Review(Menu menu, User user, LocalDate createDate, String reviewList) {
        this.menu = menu;
        this.user = user;
        this.createDate = createDate;
        this.reviewList = reviewList;
    }
}
