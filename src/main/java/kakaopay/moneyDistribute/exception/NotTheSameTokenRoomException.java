package kakaopay.moneyDistribute.exception;

public class NotTheSameTokenRoomException extends ShareException {
    public NotTheSameTokenRoomException() {
        super("현재 입력된 대화방과 토큰이 생성된 대화방이 다릅니다.");
    }
}
