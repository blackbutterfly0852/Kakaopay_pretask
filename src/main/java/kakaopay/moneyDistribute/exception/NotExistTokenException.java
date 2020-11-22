package kakaopay.moneyDistribute.exception;

public class NotExistTokenException extends ShareException {
    public NotExistTokenException() {super("해당 토큰은 존재하지 않습니다.");
    }
}
