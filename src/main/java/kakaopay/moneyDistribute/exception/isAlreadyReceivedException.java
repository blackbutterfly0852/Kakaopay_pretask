package kakaopay.moneyDistribute.exception;

public class isAlreadyReceivedException extends ShareException {
    public isAlreadyReceivedException() {
        super("해당 뿌리기 건에서 이미 금액을 받았습니다.");
    }
}
