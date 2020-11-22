package kakaopay.moneyDistribute.repository.custom;

import kakaopay.moneyDistribute.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCustomRepository extends JpaRepository<User, Long> {
    // 1. 사용자 이름으로 찾기 -> 테스트 용도로 단건으로 가정.
    public User findByUserName(String userName);
}