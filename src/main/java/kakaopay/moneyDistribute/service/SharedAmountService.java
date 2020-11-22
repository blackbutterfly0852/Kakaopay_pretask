package kakaopay.moneyDistribute.service;

import kakaopay.moneyDistribute.domain.Room;
import kakaopay.moneyDistribute.domain.Share;
import kakaopay.moneyDistribute.domain.SharedAmount;
import kakaopay.moneyDistribute.domain.User;
import kakaopay.moneyDistribute.exception.*;
import kakaopay.moneyDistribute.repository.SharedAmountRepository;
import kakaopay.moneyDistribute.repository.UserRepository;
import kakaopay.moneyDistribute.repository.custom.RoomCustomRepository;
import kakaopay.moneyDistribute.repository.custom.ShareCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SharedAmountService {

    private final UserRepository userRepository;
    private final RoomCustomRepository roomCustomRepository;
    private final ShareCustomRepository shareCustomRepository;
    private final SharedAmountRepository sharedAmountRepository;


    // 1. 토큰 이름으로 분배건 조회 (테스트 용)
    public List<SharedAmount> findByToken(Share share) {
        return sharedAmountRepository.findByToken(share);
    }

    // 2. 각 분배 건 받기
    public long saveSharedAmount(Long userId, String roomName, String tokenName) {
        User user = userRepository.findById(userId);
        Room room = roomCustomRepository.findByName(roomName);
        Share share = shareCustomRepository.findByToken(tokenName);

        // 1. 각 분배 건 받기 전 유효성 검사
        // 1) 사용자 활성화 여부
        if (user == null ) {
            throw new NotActivationStatusException();
        }

        // 2) 해당 토큰 존재 여부
        if (share == null) {
            throw new NotExistTokenException();
        }


        // 3) 사용자가 토큰이 발행된 방에 있는지 여부.
        if (!userRepository.isInRoom(user,share.getRoom())){
            throw new NotInTheTokenRoomException();
        }

        // 4) 입력 x-room-id과 입력 토큰이 있는 방과 다른 경우
        if (room.getId() != share.getRoom().getId()) {
            throw new NotTheSameTokenRoomException();
        }


        // 5) 사용자가 해당 토큰의 발행자인지 여부
        if (user.getId() == share.getUser().getId()) {
            throw new SameTokenCreaterException();
        }

        // 6) 뿌리기 10분 경과 여부 (true : 10분 경과,  false : 10분 미경과)
        if (share.isOverTenMinutes()) {
            throw new isOverTenMinutesException();
        }

        // 6) 해당 뿌리기를 받았는지 여부 (true : 이미 받았음,  false : 아직 안 받음)
        if (share.isAlreadyReceive(user.getId())) {
            throw new isAlreadyReceivedException();
        }

        // 2. 분배 하기
        // 1) 남아 있는 분배 건 찾기.
        List<SharedAmount> sharedAmountList = share.getLeftSharedAmount();
        if (sharedAmountList.size() == 0) {
           throw new NotExistSharedAmountException();
        }
        // 2) 분배하기
        sharedAmountList.get(0).receiveAmount(user);
        // 3) 분배 건이 하나 남은 경우 -> share의 ReqEndedTime 같이 Update
        if (sharedAmountList.size() == 1) {
            share.setReqEndedTime(LocalDateTime.now());
        }
        return sharedAmountList.get(0).getRcvAmt();
    }
}
