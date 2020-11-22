package kakaopay.moneyDistribute.api.response;

import lombok.Data;

@Data
public class SearchSharedAmountDto {

    private final long rcvAmt; // 받은 금액
    private final Long rcvId; // 받은 사용자 아이디

    public SearchSharedAmountDto(long rcvAmt, Long rcvId) {
        this.rcvAmt = rcvAmt;
        this.rcvId = rcvId;
    }

}
