package kakaopay.moneyDistribute.repository.custom;

import kakaopay.moneyDistribute.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCustomRepository extends JpaRepository<Room, Long> {
    // 1. 방 이름으로 찾기
    public Room findByName(String roomName);
}
