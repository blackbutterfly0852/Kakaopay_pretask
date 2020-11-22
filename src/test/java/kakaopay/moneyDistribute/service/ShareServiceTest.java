package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.*;
import kakaopay.moneyDistribute.repository.ShareRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kakaopay.moneyDistribute.domain.SharedAmount.createSharedAmount;
import static kakaopay.moneyDistribute.util.Utility.createToken;
import static kakaopay.moneyDistribute.util.Utility.dividedAmount;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Slf4j
/**
 *  initDB.java 비활성화 필요
 **/
public class ShareServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    ShareService shareService;
    @Autowired
    ShareRepository shareRepository;
    @Autowired
    SharedAmountService sharedAmountService;


    @Test
    @DisplayName("토큰 존재확인")
    //@Rollback(false)
    public void isExistToken() throws Exception {
        // given
        User userA = User.createUser("A");
        userService.saveUser(userA);
        User findUserA = userService.findById(userA.getId());
        Room roomA = Room.createRoom(userA.getId(), "roomA");
        UserRoom userRoomA = UserRoom.createUserRoom(findUserA, roomA);
        roomA.addUserRoom(userRoomA);
        roomService.saveRoom(roomA);
        String token = "ABC";
        Share share = Share.createShare(token, findUserA, roomA, 100010L, 4);
        shareRepository.saveShare(share);

        // when
        String newToken = "ABC";
        Share findShare = shareService.findByToken(newToken);

        // then : 토큰이 이미 존재(true) 해야 한다.
        assertEquals(true, findShare!=null);

    }

    @Test
    @DisplayName("토큰 자동생성 확인")
    //@Rollback(false)
    public void isGenerateToken() throws Exception {
        // given
        User userA = User.createUser("A");
        userService.saveUser(userA);
        User findUserA = userService.findById(userA.getId());
        Room roomA = Room.createRoom(findUserA.getId(), "roomA");
        UserRoom userRoomA = UserRoom.createUserRoom(findUserA, roomA);
        roomA.addUserRoom(userRoomA);
        roomService.saveRoom(roomA);
        String tokenA = "ABC";
        log.info("tokenA : " + tokenA);
        Share shareA = Share.createShare(tokenA, findUserA, roomA, 100010L, 4);
        shareRepository.saveShare(shareA);

        // when
        User userB = User.createUser("B");
        userService.saveUser(userB);
        User findUserB = userService.findById(userB.getId());
        Room roomB = Room.createRoom(findUserB.getId(), "roomB");
        UserRoom userRoomB = UserRoom.createUserRoom(findUserB, roomB);
        roomB.addUserRoom(userRoomB);
        roomService.saveRoom(roomB);
        String tokenB = "";
        do {
            tokenB = createToken();
        } while (shareService.findByToken(tokenB)!=null); // true : 토큰 존재 false : 토큰 미존재
        log.info("tokenB : " + tokenB);
        Share shareB = Share.createShare(tokenB, findUserB, roomB, 200010L, 3);
        shareRepository.saveShare(shareB);

        //then : 토큰은 서로 달라야 한다.
        assertEquals(true, shareA.getToken() != shareB.getToken());

    }

    @Test
    @DisplayName("금액 분배 테스트")
    // @Rollback(false)
    public void isGoodDivided() throws Exception {
        // given
        long initAmt = 129384921;
        int initCnt = 10;

        // when
        List<Long> dividedList = dividedAmount(initAmt, initCnt);
        List<SharedAmount> sharedAmountList = new ArrayList<>();

        int seq = 1;
        for (Long dividedMoney : dividedList) {
            SharedAmount dividedSharedAmount = createSharedAmount(seq, dividedMoney, Status.Y);
            sharedAmountList.add(dividedSharedAmount);
            seq++;
        }

        // then : 금액과 시퀀스가 동일해야 한다.
        long totalAmt = 0;
        for (int i = 0; i < sharedAmountList.size(); i++) {
            log.info("seq 확인 : " + (i + 1) + " " + sharedAmountList.get(i).getSeq());
            //assertEquals((i + 1), sharedAmountList.get(i).getSeq());
            log.info("분배금액 확인 : " + sharedAmountList.get(i).getRcvAmt());
            totalAmt+=sharedAmountList.get(i).getRcvAmt();
        }
        assertEquals(initAmt, totalAmt);

    }



    @Test
    @DisplayName("뿌리기 분배건 저장 확인")
    //@Rollback(false)
    public void isGoodSaveSharedAmount() throws Exception {
        // given
        User userA = User.createUser("A");
        userService.saveUser(userA);
        User findUserA = userService.findById(userA.getId());
        Room roomA = Room.createRoom(findUserA.getId(), "roomA");
        UserRoom userRoomA = UserRoom.createUserRoom(findUserA, roomA);
        roomA.addUserRoom(userRoomA);
        roomService.saveRoom(roomA);
        long initAmt = 50000008L;
        int initCnt = 10;
        String token = "ABC";
        Share share = Share.createShare(token, findUserA, roomA, initAmt, initCnt);

        List<Long> dividedList = dividedAmount(initAmt, initCnt);
        List<SharedAmount> sharedAmountList = new ArrayList<>();
        int seq = 1;
        for (Long dividedMoney : dividedList) {
            SharedAmount dividedSharedAmount = createSharedAmount(seq, dividedMoney, Status.Y);
            sharedAmountList.add(dividedSharedAmount);
            seq++;
        }

        // when
         for(SharedAmount sa : sharedAmountList){
            share.addSharedAmount(sa);
        }
        shareRepository.saveShare(share);
        Share findShare = shareRepository.findById(share.getId());
        List<SharedAmount> findSharedAmountList = findShare.getSharedAmountList();

        // then
        assertEquals("분배되서 저장된 크기가 10이어야 합니다.", 10, findSharedAmountList.size());

    }



}