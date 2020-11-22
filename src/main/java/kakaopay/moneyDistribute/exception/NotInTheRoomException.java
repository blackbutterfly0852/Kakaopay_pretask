package kakaopay.moneyDistribute.exception;

public class NotInTheRoomException extends ShareException {
    public NotInTheRoomException() {
        super("해당 대화방에 없는 사용자입니다.");
    }
}
