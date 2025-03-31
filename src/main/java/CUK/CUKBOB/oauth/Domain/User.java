package CUK.CUKBOB.oauth.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
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
    private SocialType socialType;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public User(SocialType socialType, String email) {
        this.socialType = socialType;
        this.email = email;
        this.nickname = null;
        this.refreshToken = null;
    }

    public void updateRefreshToken(String refreshToken){this.refreshToken = refreshToken;}

    public void resetRefreshToken() {this.refreshToken = null; }
}
