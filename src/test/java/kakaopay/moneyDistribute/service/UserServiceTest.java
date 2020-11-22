package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.domain.UserRoom;
import kakaopay.moneyDistribute.exception.NotInTheRoomException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Slf4j
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;


    @Test(expected = NotInTheRoomException.class)
    @DisplayName("사용자가 대화방에 있는지 확인")
    //@Rollback(false)
    public void isInTheRoom() throws Exception {
        // given
        User userA = User.createUser("A");
        userService.saveUser(userA);
        User findUserA = userService.findById(userA.getId());
        Room roomA = Room.createRoom(findUserA.getId(), "roomA");
        UserRoom userRoomA = UserRoom.createUserRoom(userA, roomA);
        roomA.addUserRoom(userRoomA);
        roomService.saveRoom(roomA);

        User userB = User.createUser("B");
        userService.saveUser(userB);
        User findUserB = userService.findById(userB.getId());
        Room roomB = Room.createRoom(findUserA.getId(), "roomB");
        UserRoom userRoomB = UserRoom.createUserRoom(userB, roomB);
        roomB.addUserRoom(userRoomB);
        roomService.saveRoom(roomB);

        // when : userA는 roomB에 속하지 않는다.
        if (!userService.isInRoom(findUserA, roomB)) {
            throw new NotInTheRoomException();
        }

        // then
        fail("위에서 에러가 발생해야 합니다.");
    }

}