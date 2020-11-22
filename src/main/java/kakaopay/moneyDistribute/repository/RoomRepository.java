package kakaopay.moneyDistribute.repository;


import kakaopay.moneyDistribute.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;
    // 1. 방 저장
    public void saveRoom(Room room) {
        em.persist(room);
    }


}
