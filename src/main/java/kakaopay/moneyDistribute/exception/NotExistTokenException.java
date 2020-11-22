package kakaopay.moneyDistribute.exception;

public class NotExistTokenException extends ShareException {
    public NotExistTokenException() {super("해당 토큰이 존재하지 않습니다.");
    }
}
