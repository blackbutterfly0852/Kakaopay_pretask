package kakaopay.moneyDistribute.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
/**
 * SharedAmount : 각 분배 금액 관리 테이블
 * PK : x_shared_amt_id
 * FK : x_share_id
 **/
@Table(name = "SHAREDAMOUNT")
public class SharedAmount {
    @Id
    @GeneratedValue
    @Column(name = "x_shared_amt_id")
    private Long id; // 각 분배 건 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="x_share_id")
    private Share share; // 요청시 고유 토큰 ID
    @Column(name ="x_seq", nullable = false)
    private int seq; // 인원 수에 따른 분배 금액 시퀀스
    @Column(name ="x_rcv_amt", nullable = false)
    private long rcvAmt; // 각 분배 금액

    @Column(name ="x_rcv_id")
    private Long rcvId; // 각 분배 금액 받은 사용자
    @Column(name ="x_rcv_time")
    private LocalDateTime rcvTime; // 각 분배 금액 받은 시각

    // 생성자 접근 제한 -> 생성메소드로 객체 생성
    protected SharedAmount(){

    }

    // 1. 생성 메소드
    public static SharedAmount createSharedAmount(int seq, long rcvAmt, Status status){
        SharedAmount sharedAmount = new SharedAmount();
        sharedAmount.setSeq(seq);
        sharedAmount.setRcvAmt(rcvAmt);
        return sharedAmount;
    }

    // 2. 비즈니스 메소드
    // 1) 뿌리기 받기
    public void receiveAmount(User user){
        this.getShare().setCurrCnt(this.getShare().getCurrCnt()+1);
        this.getShare().setCurrAmt(this.getShare().getCurrAmt()-this.rcvAmt);
        this.rcvId = user.getId();
        this.rcvTime = LocalDateTime.now();
    }

}
