package kakaopay.moneyDistribute.exception;

public class NotExistSharedAmountException extends ShareException {
    public NotExistSharedAmountException() {
        super("해당 뿌리기건이 종료되어 뿌리기 건이 존재 하지 않습니다.");
    }
}
