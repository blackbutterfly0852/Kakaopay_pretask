package kakaopay.moneyDistribute.exception;

public class isOverTenMinutesException extends ShareException {
    public isOverTenMinutesException() {
        super("해당 뿌리기는 시작한지 10분이 지나 금액을 받을 수 없습니다.");
    }
}
