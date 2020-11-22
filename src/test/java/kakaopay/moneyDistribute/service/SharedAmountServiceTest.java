package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.api.response.SearchShareDto;
import kakaopay.moneyDistribute.api.response.SearchSharedAmountDto;
import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.Share;
import kakaopay.moneyDistribute.domain.SharedAmount;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.exception.*;
import kakaopay.moneyDistribute.repository.ShareRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
@Slf4j
/**
 *  initDB.java 활성화 필요
 **/
public class SharedAmountServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    ShareService shareService;
    @Autowired
    SharedAmountService sharedAmountService;
    @Autowired
    ShareRepository shareRepository;

    @Test
    @DisplayName("뿌리기 건이 없으면 오류 발생")
    //@Rollback(false)
    public void test_05() throws Exception {
        // given
        String newToken = createShareMockDate();
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

        // when
        List<SharedAmount> leftSharedAmountE = share.getLeftSharedAmount();

        if (leftSharedAmountE.size() == 0) {
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new NotExistSharedAmountException();
            });

        }

    }

    @Test
    @DisplayName("뿌리기 분배 건이 잘 받아지는지 확인")
    //@Rollback(false)
    public void test_06() throws Exception {
        // given
        String newToken = createShareMockDate();
        User userB = userService.findByUserName("B");
        User userC = userService.findByUserName("C");
        Share share = shareService.findByToken(newToken);

        // when
        List<SharedAmount> leftSharedAmountB = share.getLeftSharedAmount();
        leftSharedAmountB.get(0).receiveAmount(userB);
        List<SharedAmount> leftSharedAmountC = share.getLeftSharedAmount();
        leftSharedAmountC.get(0).receiveAmount(userC);

        // then
        log.info("분배되었으면 해당 뿌릴건의 받은 숫자를 늘려야 한다.");
        assertEquals(2, share.getCurrCnt());
        long expectedAmt = share.getInitAmt() - leftSharedAmountB.get(0).getRcvAmt() - leftSharedAmountC.get(0).getRcvAmt();
        log.info("분배되었으면 해당 뿌릴건의 남은 금액과 동일해야 한다.");
        assertEquals(expectedAmt, share.getCurrAmt());
        for (SharedAmount sa : share.getSharedAmountList()) {
            if (sa.getRcvId() == userB.getId()) {
                log.info("만약 금액을 받았으면 받은자로 변경되어야 한다.");
                assertEquals(userB.getId(), sa.getRcvId());
                log.info("만약 금액을 받았으면 받은날짜로 변경되어야 한다.");
                assertEquals(true, sa.getRcvTime() != null);
            } else if (sa.getRcvId() == userC.getId()) {
                log.info("만약 금액을 받았으면 받은자로 변경되어야 한다.");
                assertEquals(userC.getId(), sa.getRcvId());
                log.info("만약 금액을 받았으면 받은자로 변경되어야 한다.");
                assertEquals(true, sa.getRcvId() != null);
            } else {
                // log.info("분배건이 아닌 경우 : " + sa.toString());
            }

        }
    }

    @Test
    @DisplayName("해당 뿌리기건을 이미 받았는지 확인")
    //@Rollback(false)
    public void test_07() throws Exception {
        // given
        String newToken = createShareMockDate();
        User userB = userService.findByUserName("B");
        Share share = shareService.findByToken(newToken);
        List<SharedAmount> leftSharedAmountB = share.getLeftSharedAmount();
        leftSharedAmountB.get(0).receiveAmount(userB);

        // when
        if (share.isAlreadyReceive(userB.getId())) {
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new isAlreadyReceivedException();
            });

        }

    }

    @Test
    @DisplayName("사용자와 토큰 발행자 동일 -> 금액 받기 불가능")
    //@Rollback(false)
    public void test_08() throws Exception {
        // given
        String newToken = createShareMockDate();
        User findUser = userService.findByUserName("A");
        Share share = shareService.findByToken(newToken);
        // when
        if (findUser.getId() == share.getUser().getId()) {
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new SameTokenCreaterException();
            });

        }

    }

    @Test
    @DisplayName("다른 대화방의 뿌리기 건을 접근 제한")
    //@Rollback(false)
    public void test_09() throws Exception {
        // given
        String newToken = createShareMockDate();
        Room findRoom = roomService.findByName("roomB");
        Share share = shareService.findByToken(newToken);
        // when
        if (findRoom.getId() != share.getRoom().getId()) {
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new NotInTheTokenRoomException();
            });

        }

    }

    @Test
    @DisplayName("토큰 발행 후 10분 경과 금액 받기 불가능")
    //@Rollback(false)
    // !!! Test시 createShare()의 setReqCreatedTime 수정 필요
    public void test_10() throws Exception {
        // given
        String newToken = createShareMockDate();
        Share share = shareService.findByToken(newToken);
        // when
        if (share.isOverTenMinutes()) {
            for (SharedAmount sa : share.getSharedAmountList()) {
                log.info("sa.getRcvId is expected null : " + sa.getRcvId());
                assertEquals(null, sa.getRcvId());
            }
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new isOverTenMinutesException();
            });

        }

        //fail("위에서 오류가 발생해야 합니다.");

    }

    @Test
    @DisplayName("뿌리기건이 잘 조회 되는지 확인")
    //@Rollback(false)
    public void test_11() throws Exception {
        // given
        String newToken = createShareMockDate();
        User userB = userService.findByUserName("B");
        User userC = userService.findByUserName("C");
        Share share = shareService.findByToken(newToken);

        // when
        List<SharedAmount> leftSharedAmountB = share.getLeftSharedAmount();
        leftSharedAmountB.get(0).receiveAmount(userB);
        List<SharedAmount> leftSharedAmountC = share.getLeftSharedAmount();
        leftSharedAmountC.get(0).receiveAmount(userC);

        SearchShareDto sharedAmountList = shareRepository.findSearchShareDto(share);

        // then
        log.info("뿌린 시각이 동일해야 한다.");
        assertEquals(share.getReqCreatedTime(), sharedAmountList.getReqCreatedTime());
        log.info("뿌린 금액이 동일해야 한다.");
        assertEquals(share.getInitAmt(), sharedAmountList.getInitAmt());
        log.info("받기 완료된 금액이 동일해야 한다.");
        assertEquals(share.getInitAmt() - share.getCurrAmt(), sharedAmountList.getTotalRcvAMt());


        SearchSharedAmountDto s1 = sharedAmountList.getSearchSharedAmountDtos().get(0);
        log.info("받은 금액이 동일해야 한다.");
        log.info("받은 사용자 아이디가 동일해야 한다.");
        assertEquals(leftSharedAmountB.get(0).getRcvAmt(), s1.getRcvAmt());
        assertEquals(userB.getId(), s1.getRcvId());
        SearchSharedAmountDto s2 = sharedAmountList.getSearchSharedAmountDtos().get(1);
        assertEquals(leftSharedAmountC.get(0).getRcvAmt(), s2.getRcvAmt());
        assertEquals(userC.getId(), s2.getRcvId());


    }

    @Test
    @DisplayName("뿌린 사람만 조회가가능한지 확인")
    //@Rollback(false)
    public void test_12() throws Exception {
        // given
        String newToken = createShareMockDate();
        User userB = userService.findByUserName("B");
        Share share = shareService.findByToken(newToken);

        // when
        // 사용자가 해당 토큰의 발행자인지 여부
        if (userB.getId() != share.getUser().getId()) {
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new NotTokenCreaterException();
            });

        }


    }


    @Test
    @DisplayName("토큰 존재 여부 확인")
    //@Rollback(false)
    public void test_13() throws Exception {
        // given
        String newToken = createShareMockDate();
        String findToken = "DBC";
        // when
        Share findShare = shareService.findByToken(findToken);
        if (findShare == null) {
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new NotExistTokenException();
            });

        }

    }


    @Test
    @DisplayName("토큰 발행 후 7일 경과 조회 불가능")
    // !!! Test시 createShare()의 setReqCreatedTime 수정 필요"
    public void test_14() throws Exception {
        // given
        String newToken = createShareMockDate();
        Share share = shareService.findByToken(newToken);
        // when
        if (share.isOverSevenDays()) {
            for (SharedAmount sa : share.getSharedAmountList()) {
                log.info("sa.getRcvId is expected null : " + sa.getRcvId());
                assertEquals(null, sa.getRcvId());
            }
            // then
            Assertions.assertThrows(Exception.class, () -> {
                throw new isOverSevenDaysException();
            });

        }
        //fail("위에서 오류가 발생해야 합니다.");

    }


    public String createShareMockDate() {
        User user = userService.findByUserName("A");
        Room room = roomService.findByName("roomA");
        String newToken = shareService.saveShare(user.getId(), room.getName(), 10000, 3);
        return newToken;
    }
}