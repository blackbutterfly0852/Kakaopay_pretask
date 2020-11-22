package kakaopay.moneyDistribute;


import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.domain.UserRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 테스트용 데이터
 * 방이름 : 방에 속한 사용자
 * roomA : A, B, C, E
 * roomB : B, D
 * roomC : A, C, D
 * roomD : B, C, D, E
 * roomE : C, E
 **/

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.createUsersAndRooms();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor

    static class InitService{
        private final EntityManager em;
        public void createUsersAndRooms(){
            // 1. 사용자 생성과 방 생성
            User userA = User.createUser("A");
            em.persist(userA);
            Room roomA = Room.createRoom(userA.getId(),"roomA");
            roomA.addUserRoom(UserRoom.createUserRoom(userA,roomA));
            em.persist(roomA);

            User userB = User.createUser("B");
            em.persist(userB);
            Room roomB = Room.createRoom(userB.getId(),"roomB");
            roomB.addUserRoom(UserRoom.createUserRoom(userB,roomB));
            em.persist(roomB);

            User userC = User.createUser("C");
            em.persist(userC);
            Room roomC = Room.createRoom(userC.getId(),"roomC");
            roomC.addUserRoom(UserRoom.createUserRoom(userC,roomC));
            em.persist(roomC);

            User userD = User.createUser("D");
            em.persist(userD);
            Room roomD = Room.createRoom(userD.getId(),"roomD");
            roomD.addUserRoom(UserRoom.createUserRoom(userD,roomD));
            em.persist(roomD);

            User userE = User.createUser("E");
            em.persist(userE);
            Room roomE = Room.createRoom(userD.getId(),"roomE");
            roomE.addUserRoom(UserRoom.createUserRoom(userE,roomE));
            em.persist(roomE);

            // 2. 방마다 사용자 분배
            userA.addUserRoom(UserRoom.createUserRoom(userA,roomC));
            userB.addUserRoom(UserRoom.createUserRoom(userB,roomA));
            userB.addUserRoom(UserRoom.createUserRoom(userB,roomD));
            userC.addUserRoom(UserRoom.createUserRoom(userC,roomA));
            userC.addUserRoom(UserRoom.createUserRoom(userC,roomD));
            userC.addUserRoom(UserRoom.createUserRoom(userC,roomE));
            userD.addUserRoom(UserRoom.createUserRoom(userD,roomA));
            userD.addUserRoom(UserRoom.createUserRoom(userD,roomB));
            userD.addUserRoom(UserRoom.createUserRoom(userD,roomC));
            userE.addUserRoom(UserRoom.createUserRoom(userE,roomA));
            userE.addUserRoom(UserRoom.createUserRoom(userE,roomD));

            em.persist(userA);
            em.persist(userB);
            em.persist(userC);
            em.persist(userD);
            em.persist(userE);

           }

    }
}
