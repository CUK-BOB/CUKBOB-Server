package CUK.CUKBOB.oauth.Service;

import CUK.CUKBOB.oauth.Domain.User;
import CUK.CUKBOB.oauth.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //닉네임 설정
    public void setNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId) //유저레포지토리에서 JpaRepository를 상속받고 있어서 자동으로 findById 제공!
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //사용자의 닉네임이 이미 있을 때
        if (user.getNickname() != null) {
            throw new IllegalStateException("이미 닉네임이 설정된 사용자입니다.");
        }

        //다른 유저 닉네임과 중복체크
        boolean nicknameExists = userRepository.existsByNickname(nickname);
        if (nicknameExists) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        user.setNickname(nickname);
        userRepository.save(user);
    }

    //닉네임 수정
    public void updateNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //다른 유저 닉네임과 중복체크 (원래 본인 닉네임과 다른닉네임 입력했을 경우에만)
        if (!newNickname.equals(user.getNickname()) &&
                userRepository.existsByNickname(newNickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        user.setNickname(newNickname);
        userRepository.save(user);
    }

    //기본 닉네임 설정
    public String generateDefaultNickname(Long userId) {
        String padded = (userId < 1000)
                ? String.format("%03d", userId)
                : String.valueOf(userId);
        return "쿡밥" + padded;
    }
}