package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.repository.UserRepository;
import kakaopay.moneyDistribute.repository.custom.UserCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;

    // 1. 사용자 저장
    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    // 2. 사용자 찾기
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    // 3. 사용자 이름으로 찾기
    public User findByUserName(String userName){
        return userCustomRepository.findByUserName(userName);
    }

    // 4. 사용자가 특정방에 존재하는지 확인 (true : 해당 방에 있음 , false : 해당 방에 없음)
    public boolean isInRoom(User user, Room room) {
        return userRepository.isInRoom(user, room);
    }

}