package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.exception.OverRoomCountException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
@SpringBootTest
@Transactional
@Slf4j
class RoomServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("뿌리기 최초 인원 오류 테스트")
    public void  test_00() throws Exception {
        // given
        int initCnt = 10;
        User userA = User.createUser("A");
        userService.saveUser(userA);
        User findUserA = userService.findById(userA.getId());
        Room roomA = Room.createRoom(findUserA.getId(), "roomA");
        roomService.saveRoom(roomA);
        // when
        if(!roomA.isOverNumber(initCnt)){
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new OverRoomCountException();
            });
        }
    }


}