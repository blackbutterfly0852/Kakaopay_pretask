package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.Share;
import kakaopay.moneyDistribute.domain.SharedAmount;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Slf4j
public class SharedAmountServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    ShareService shareService;
    @Autowired
    SharedAmountService sharedAmountService;


    static String newToken = "";

    @Before
    public void testData() {
        // 뿌리기 생성
        User user = userService.findByUserName("A");
        Room room = roomService.findByName("roomA");
        newToken = shareService.saveShare(user.getId(), room.getName(), 10000, 4);
    }

    @Test(expected = NotExistTokenException.class)
    @DisplayName("토큰 존재 여부 확인")
    //@Rollback(false)
    public void isExistToken() throws Exception {
        // given
        String findToken = "DBC";
        // when
        Share findShare = shareService.findByToken(findToken);
        if (findShare == null) {
            throw new NotExistTokenException();
        }
        // then
        fail("위에서 예외가 발생해야 합니다.");
    }

    @Test(expected = NotInTheTokenRoomException.class)
    @DisplayName("사용자가 토큰이 발행된 대화방에 있는지 확인")
    //@Rollback(false)
    public void isInTheTokenRoom() throws Exception {
        // given
        // User findUser = userCustomRepository.findByUserName("B");
        Room findRoom = roomService.findByName("roomB");
        Share share = shareService.findByToken(newToken);
        // when
        if (findRoom.getId() != share.getRoom().getId()) {
            throw new NotInTheTokenRoomException();
        }
        // then
        fail("위에서 예외가 발생해야 합니다.");
    }

    @Test(expected = SameTokenCreaterException.class)
    @DisplayName("사용자 == 토큰 발행자 이면 금액 받기 불가능한지 확인")
    //@Rollback(false)
    public void isSameTheTokenCreater() throws Exception {
        // given
        User findUser = userService.findByUserName("A");
        Share share = shareService.findByToken(newToken);
        // when
        if (findUser.getId() == share.getUser().getId()) {
            throw new SameTokenCreaterException();
        }
        // then
        fail("위에서 예외가 발생해야 합니다.");
    }

    @Test(expected = isOverTenMinutesException.class)
    @DisplayName("토큰 발행 후 10분 경과하면 뿌리기 금액 받지 못함 확인, Test시 createShare()의 setReqCreatedTime 수정 필요")
    //@Rollback(false)
    public void isOverTenMinutes() throws Exception {
        // given
        Share share = shareService.findByToken(newToken);
        // when
        if (share.isOverTenMinutes()) {
            for (SharedAmount sa : share.getSharedAmountList()) {
                log.info("sa.getRcvId is expected null : " + sa.getRcvId());
                assertEquals("유효성 검사 전에는 1:다 관계로 SharedAmount의 rcvI가 null이어야 한다.", null, sa.getRcvId());
            }
            throw new isOverTenMinutesException();
        }

        // then
        fail("위에서 예외가 발생해야 합니다.");
    }

    @Test(expected = isOverSevenDaysException.class)
    @DisplayName("7일 이후에 조회 불가능 확인, Test시 createShare()의 setReqCreatedTime 수정 필요")
    public void isImpossibleSearch() throws Exception{
        // given
        Share share = shareService.findByToken(newToken);
        // when
        if (share.isOverSevenDays()) {
            for (SharedAmount sa : share.getSharedAmountList()) {
                log.info("sa.getRcvId is expected null : " + sa.getRcvId());
                assertEquals("유효성 검사 전에는 1:다 관계로 SharedAmount의 rcvId가 null이어야 한다.", null, sa.getRcvId());
            }
            throw new isOverSevenDaysException();
        }

        // then
        fail("위에서 예외가 발생해야 합니다.");
    }

    @Test
    @DisplayName("뿌리기 분배 건이 잘 받아지는지 확인")
    //@Rollback(false)
    public void isGoodSharedAmount() throws Exception {
        // given
        User userB = userService.findByUserName("B");
        User userC = userService.findByUserName("C");
        Share share = shareService.findByToken(newToken);

        // when
        List<SharedAmount> leftSharedAmountB = share.getLeftSharedAmount();
        leftSharedAmountB.get(0).receiveAmount(userB);
        List<SharedAmount> leftSharedAmountC = share.getLeftSharedAmount();
        leftSharedAmountC.get(0).receiveAmount(userC);

        // then
        assertEquals("분배되었으면 해당 뿌릴건의 받은 숫자를 늘려야 한다.", 2, share.getCurrCnt());
        long expectedAmt = share.getInitAmt() - leftSharedAmountB.get(0).getRcvAmt() - leftSharedAmountC.get(0).getRcvAmt();
        assertEquals("분배되었으면 해당 뿌릴건의 남은 금액과 동일해야 한다.", expectedAmt , share.getCurrAmt());
        for (SharedAmount sa : share.getSharedAmountList()) {
            if (sa.getRcvId() == userB.getId()) {
                assertEquals("만약 금액을 받았으면 받은자로 변경되어야 한다.", userB.getId(), sa.getRcvId());
                assertEquals("만약 금액을 받았으면 받은날짜로 변경되어야 한다.", true, sa.getRcvTime() != null);
            } else if (sa.getRcvId() == userC.getId()) {
                assertEquals("만약 금액을 받았으면 받은자로 변경되어야 한다.", userC.getId(), sa.getRcvId());
                assertEquals("만약 금액을 받았으면 받은자로 변경되어야 한다.", true, sa.getRcvId() != null);
            } else {
                // log.info("분배건이 아닌 경우 : " + sa.toString());
            }

        }
    }

    @Test(expected = isAlreadyReceivedException.class)
    @DisplayName("해당 뿌리기건을 이미 받았는지 확인")
    //@Rollback(false)
    public void isAlreadyReceive() throws Exception {
        // given
        User userB = userService.findByUserName("B");
        Share share = shareService.findByToken(newToken);
        List<SharedAmount> leftSharedAmountB = share.getLeftSharedAmount();
        leftSharedAmountB.get(0).receiveAmount(userB);

        // when
        if (share.isAlreadyReceive(userB.getId())) {
            throw new isAlreadyReceivedException();
        }
        // then
        fail("위에서 예외가 발생해야 합니다.");
    }

    @Test
    //@Rollback(false)
    public void findSharedAmountByShare() throws Exception {
        // given
        User userB = userService.findByUserName("B");
        User userC = userService.findByUserName("C");
        User userD = userService.findByUserName("D");
        User userE = userService.findByUserName("E");
        Share share = shareService.findByToken(newToken);

        // when
        List<SharedAmount> leftSharedAmountB = share.getLeftSharedAmount();
        leftSharedAmountB.get(0).receiveAmount(userB);
        List<SharedAmount> leftSharedAmountC = share.getLeftSharedAmount();
        leftSharedAmountC.get(0).receiveAmount(userC);
        List<SharedAmount> leftSharedAmountD = share.getLeftSharedAmount();
        leftSharedAmountD.get(0).receiveAmount(userD);
        //SharedAmount leftSharedAmountE = share.getLeftSharedAmount();
        //leftSharedAmountE.receiveAmount(userE);

        // when
        int resultCnt = 0;
        List<SharedAmount> sharedAmountList = sharedAmountService.findByToken(share);
        for(SharedAmount sa : sharedAmountList){
            if(sa.getRcvId() != null){
                resultCnt ++;
            }
        }
        // then
        assertEquals("크기가 3개나와야 한다.", 3, resultCnt);
        for (SharedAmount sa : sharedAmountList) {
            log.info(sa.getRcvId() + " " + sa.getRcvAmt());
        }
    }
}