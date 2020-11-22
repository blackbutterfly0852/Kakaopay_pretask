package kakaopay.moneyDistribute.exception;

public class isOverSevenDaysException extends ShareException {
    public isOverSevenDaysException() { super("해당 뿌리기는 7일이 지나서 조회할 수 없습니다.");}
}
