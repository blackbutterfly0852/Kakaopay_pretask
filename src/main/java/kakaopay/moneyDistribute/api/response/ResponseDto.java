package kakaopay.moneyDistribute.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto {
    private String code;
    private String message;
    private Object body;
    public ResponseDto( Object object ) {
        this.code = "200";
        this.message = "정상 처리";
        this.body = object;
    }
}
