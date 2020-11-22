package kakaopay.moneyDistribute.exception;

public class NotActivationStatusException extends ShareException {
    public NotActivationStatusException() {
        super("해당 사용자는 존재하지 않습니다.");
    }
}
