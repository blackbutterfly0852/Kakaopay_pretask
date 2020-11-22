package kakaopay.moneyDistribute.repository;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.domain.UserRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    // 1. 사용자 저장
    public void saveUser(User user) {
        em.persist(user);
    }

    // 2. 사용자 ID로 찾기
    public User findById(Long userId) {
        return em.find(User.class, userId);
    }

    // 3. 사용자가 특정방에 존재하는지 확인 (true : 해당 방에 있음 , false : 해당 방에 없음)
    public boolean isInRoom(User user, Room room) {
        List<UserRoom> userRoomList = em.createQuery(" select ur from UserRoom ur where ur.user =:user and ur.room =:room", UserRoom.class)
                .setParameter("user", user).setParameter("room",room).getResultList();
        if(userRoomList.size()>0){
            return true;
        }
        return false;
    }

}
