package kakaopay.moneyDistribute.service;


import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.repository.RoomRepository;
import kakaopay.moneyDistribute.repository.custom.RoomCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomCustomRepository roomCustomRepository;


    // 1. 방 저장
    public long saveRoom(Room room) {
        roomRepository.saveRoom(room);
        return room.getId();
    }

    // 2. 방 이름으로 찾기
    public Room findByName(String roomName) {
        Room room = roomCustomRepository.findByName(roomName);
        return room;
    }

}
