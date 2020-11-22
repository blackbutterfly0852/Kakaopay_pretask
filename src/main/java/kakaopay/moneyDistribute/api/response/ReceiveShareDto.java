package kakaopay.moneyDistribute.api.response;

import lombok.Data;

@Data
public class ReceiveShareDto {

    private final long rcvAmt;
    public ReceiveShareDto(long rcvAmt) {
        this.rcvAmt = rcvAmt;
    }
}
