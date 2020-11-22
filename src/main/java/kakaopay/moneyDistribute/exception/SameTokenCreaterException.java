package kakaopay.moneyDistribute.exception;

public class SameTokenCreaterException extends ShareException {
    public SameTokenCreaterException() {
        super("해당 뿌리기를 만들었기 때문에 받을 수 없습니다.");
    }
}
