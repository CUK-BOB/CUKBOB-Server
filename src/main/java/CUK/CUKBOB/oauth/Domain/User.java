package CUK.CUKBOB.oauth.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String nickname;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type")
    private CUK.CUKBOB.oauth.Domain.SocialType socialType;

    @Column(name = "refresh_token")
    private String refreshToken;

    //private String accessToken;

    @Builder
    public User(CUK.CUKBOB.oauth.Domain.SocialType socialType, String email) {
        this.socialType = socialType;
        this.email = email;
        this.nickname = null;
        //this.accessToken = null;
        this.refreshToken = null;
    }

    public void updateRefreshToken(String refreshToken){this.refreshToken = refreshToken;}
}