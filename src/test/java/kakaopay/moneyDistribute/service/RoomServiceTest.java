package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.exception.OverRoomCountException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Slf4j
public class RoomServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    @Autowired
    EntityManager em;


    @Test(expected = OverRoomCountException.class)
    @DisplayName("뿌리기 최초 인원 오류 테스트")
    public void  initCntException() throws Exception {
        // given
        int initCnt = 10;
        User userA = User.createUser("A");
        userService.saveUser(userA);
        User findUserA = userService.findById(userA.getId());
        Room roomA = Room.createRoom(findUserA.getId(), "roomA");
        roomService.saveRoom(roomA);
        // when
        if(!roomA.isOverNumber(initCnt)){
            throw new OverRoomCountException();
        }
        // then
        fail("위에서 예외가 발생해야 합니다.");
    }
}