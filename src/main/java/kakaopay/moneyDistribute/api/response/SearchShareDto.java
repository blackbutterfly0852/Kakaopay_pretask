package kakaopay.moneyDistribute.api.response;

import kakaopay.moneyDistribute.domain.Share;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchShareDto {
    private final LocalDateTime reqCreatedTime; // 뿌린 시각
    private final long initAmt; // 뿌린 금액
    private final long totalRcvAMt; // 받기 완료된 금액
    private List<SearchSharedAmountDto> searchSharedAmountDtos; // 받기 완료된 정보

    public SearchShareDto(Share share) {
        this.reqCreatedTime = share.getReqCreatedTime();
        this.initAmt = share.getInitAmt();
        this.totalRcvAMt = share.getInitAmt() - share.getCurrAmt();
    }

}
