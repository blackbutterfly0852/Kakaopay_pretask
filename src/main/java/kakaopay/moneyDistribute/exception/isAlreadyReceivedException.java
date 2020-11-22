package kakaopay.moneyDistribute.exception;

public class isAlreadyReceivedException extends ShareException {
    public isAlreadyReceivedException() {
        super("이미 금액을 받았습니다.");
    }
}
