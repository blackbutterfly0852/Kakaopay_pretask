package kakaopay.moneyDistribute.api.response;

        import lombok.Data;

@Data
public class ReceiveShareDto {
    private final String code;
    private final String message;
    private final long rcvAmt;

    public ReceiveShareDto(long rcvAmt) {
        this.code = "200";
        this.message = "정상 처리";
        this.rcvAmt = rcvAmt;
    }
}
