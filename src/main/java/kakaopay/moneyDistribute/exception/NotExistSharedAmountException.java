package kakaopay.moneyDistribute.exception;

public class NotExistSharedAmountException extends ShareException {
    public NotExistSharedAmountException() {
        super("뿌리기 건이 존재 하지 않습니다.");
    }
}
