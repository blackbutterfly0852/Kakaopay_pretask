package kakaopay.moneyDistribute.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
/**
 * Share : 뿌리기 관리 테이블
 * PK : x_share_id (문자열인 x_token 대신 사용)
 * FK : x_room_id
 **/
public class Share {
    @Id
    @GeneratedValue
    @Column(name = "x_share_id")
    private Long id; // PK
    @Column(name = "x_token", unique = true)
    private String token; // 요청시 고유 토큰

    @ManyToOne
    @JoinColumn(name = "x_user_id")
    private User user; // 뿌리는 사용자 ID

    @ManyToOne
    @JoinColumn(name = "x_room_id")
    private Room room; // 뿌리는 대화방


    @Column(name = "x_init_amt", nullable = false)
    private long initAmt; // 최초 뿌릴 금액
    @Column(name = "x_curr_amt", nullable = false)
    private long currAmt; // 현재 남은 금액 (default : x_init_amt)

    @Column(name = "x_init_cnt", nullable = false)
    private int initCnt; // 최초 뿌릴 사람 수
    @Column(name = "x_curr_cnt", nullable = false)
    private int currCnt; // 현재까지 뿌린 사람 수 (default : 0)

    @Column(name = "x_req_created_time", nullable = false)
    private LocalDateTime reqCreatedTime; // 뿌리기 생성 시각
    @Column(name = "x_req_ended_time")
    private LocalDateTime reqEndedTime; // 뿌리기 종료 시각

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL)
    private List<SharedAmount> sharedAmountList = new ArrayList<>();

    // 생성자 접근 제한 -> 생성메소드로 객체 생성
    protected Share() {

    }

    // 1. 생성 메소드
    public static Share createShare(String token, User user, Room room, long initAmt, int initCnt) {
        Share share = new Share();
        share.setToken(token);
        share.setUser(user);
        share.setRoom(room);
        share.setInitAmt(initAmt);
        share.setCurrAmt(initAmt);
        share.setInitCnt(initCnt);
        share.setCurrCnt(0);
        share.setReqCreatedTime(LocalDateTime.now());
        //share.setReqCreatedTime(LocalDateTime.now().minusDays(11)); // 10분초과 7일초과 TEST용
        return share;
    }

    // 2. 연관관계 메소드
    public void addSharedAmount(SharedAmount sharedAmount) {
        sharedAmount.setShare(this);
        this.sharedAmountList.add(sharedAmount);
    }


    // 3. 비즈니스 메소드
    // 1) 뿌리기 10분 경과 여부 (true : 10분 경과,  false : 10분 미경과)
    public boolean isOverTenMinutes() {
        if (LocalDateTime.now().isAfter(this.getReqCreatedTime().plusMinutes(10))) {
            return true;
        }
        return false;
    }

    // 2) 뿌리기 7일 경과 여부 (true : 7일 경과,  false : 7일 미경과)
    public boolean isOverSevenDays() {
        if (LocalDateTime.now().isAfter(this.getReqCreatedTime().plusDays(7))) {
            return true;
        }
        return false;
    }

    // 3) 해당 뿌리기를 받았는지 여부 (true : 이미 받았음,  false : 아직 안 받음)
    public boolean isAlreadyReceive(Long userId) {
        for (SharedAmount sa : this.sharedAmountList) {
            if (sa.getRcvId() == userId) {
                return true;
            }
        }
        return false;
    }

    // 4) 남아있는 분배 건 찾기
    public List<SharedAmount> getLeftSharedAmount() {
        List<SharedAmount> sharedAmountList = new ArrayList<>();
        for (SharedAmount sa : this.sharedAmountList) {
            if (sa.getRcvId() == null) {
                sharedAmountList.add(sa);
            }
        }
        return sharedAmountList;
    }

}
