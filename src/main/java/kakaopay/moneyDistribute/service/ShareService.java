package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.api.response.SearchShareDto;
import kakaopay.moneyDistribute.domain.*;
import kakaopay.moneyDistribute.exception.*;
import kakaopay.moneyDistribute.repository.ShareRepository;
import kakaopay.moneyDistribute.repository.UserRepository;
import kakaopay.moneyDistribute.repository.custom.RoomCustomRepository;
import kakaopay.moneyDistribute.repository.custom.ShareCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kakaopay.moneyDistribute.domain.Share.createShare;
import static kakaopay.moneyDistribute.domain.SharedAmount.createSharedAmount;
import static kakaopay.moneyDistribute.util.Utility.createToken;
import static kakaopay.moneyDistribute.util.Utility.dividedAmount;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ShareService {
    private final UserRepository userRepository;
    private final RoomCustomRepository roomCustomRepository;
    private final ShareRepository shareRepository;
    private final ShareCustomRepository shareCustomRepository;

    //1. 특정 토큰 존재여부 (share : 토큰 존재 , null : 토큰 미존재)
    public Share findByToken(String tokenName) {
        return shareCustomRepository.findByToken(tokenName);

    }

    // 2. 뿌리기 조회
    public Share findById(Long shareId) {
        return shareRepository.findById(shareId);
    }


    // 3. 뿌리기 저장
    @Transactional
    public String saveShare(Long userId, String roomName, long initAmt, int initCnt) {

        User user = userRepository.findById(userId);
        Room room = roomCustomRepository.findByName(roomName);
        // 1. 뿌리기 전 유효성 검사
        // 1) 사용자 활성화 여부
        if (user == null) {
            throw new NotActivationStatusException();
        }

        // 2) 사용자가 특정방에 존재하는지 확인 (true : 해당 방에 있음 , false : 해당 방에 없음)
        if (!userRepository.isInRoom(user, room)) {
            throw new NotInTheRoomException();
        }

        // 3) 1 <= 뿌리기 최초 인원 <= 방의 내부 인원 - 1
        if (!room.isOverNumber(initCnt)) {
            throw new OverRoomCountException();
        }

        // 2. 뿌리기 생성
        // 1) 토큰 생성
        String newToken = "";
        do {
            newToken = createToken();
        } while (findByToken(newToken)!=null); // true : 토큰 존재 false : 토큰 미존재
        log.info("newToken : " + newToken);

        // 2) 각 분배 건 저장
        List<Long> dividedList = dividedAmount(initAmt, initCnt);
        List<SharedAmount> sharedAmountList = new ArrayList<>();
        int seq = 1;
        for (Long dividedAmount : dividedList) {
            SharedAmount dividedSharedAmount = createSharedAmount(seq, dividedAmount, Status.Y);
            sharedAmountList.add(dividedSharedAmount);
            seq++;
        }

        // 3) 뿌리기 저장
        Share share = createShare(newToken, user, room, initAmt, initCnt);
        for (SharedAmount sa : sharedAmountList) {
            share.addSharedAmount(sa);
        }
        shareRepository.saveShare(share);
        return share.getToken();
    }

    // 4. 뿌리기 조회
    @Transactional(readOnly = true)
    public SearchShareDto findShareByToken(long userId, String roomName, String tokenName) {
        User user = userRepository.findById(userId);
        Share share = shareCustomRepository.findByToken(tokenName);

        // 1. 뿌리기 조회전 유효성 검사
        // 1) 사용자 활성화 여부
        if (user == null) {
            throw new NotActivationStatusException();
        }

        // 2) 해당 토큰 존재 여부
        if (share == null) {
            throw new NotExistTokenException();
        }

        // 3) 사용자가 해당 토큰의 발행자인지 여부
        if (user.getId() != share.getUser().getId()) {
            throw new NotTokenCreaterException();
        }

        // 4) 뿌리기 7일 경과 여부 (true : 7일 경과,  false : 7일 미경과)

        if (share.isOverSevenDays()) {
            throw new isOverSevenDaysException();
        }
        return shareRepository.findSearchShareDto(share);
    }
}
