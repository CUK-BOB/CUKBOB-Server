package CUK.CUKBOB.oauth.Service;

import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.oauth.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void setNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId) //유저레포지토리에서 JpaRepository를 상속받고 있어서 자동으로 findById 제공!
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //사용자의 닉네임이 이미 있을 때
        if (user.getNickname() != null) {
            throw new IllegalStateException("이미 닉네임이 설정된 사용자입니다.");
        }

        //다른 유저가 사용중인 닉네임일때
        boolean nicknameExists = userRepository.existByNickname(nickname);
        if (nicknameExists) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        user.setNickname(nickname);
        userRepository.save(user);
    }
}