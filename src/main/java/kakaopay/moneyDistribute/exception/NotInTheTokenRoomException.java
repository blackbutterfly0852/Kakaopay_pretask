package kakaopay.moneyDistribute.exception;

public class NotInTheTokenRoomException extends ShareException {
    public NotInTheTokenRoomException() {
        super("다른 대화방의 뿌리기 건을 받을 수 없습니다.");
    }
}
