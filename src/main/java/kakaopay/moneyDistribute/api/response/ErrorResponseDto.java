package kakaopay.moneyDistribute.api.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto {
    private final HttpStatus status;
    private final int errCode;
    private final String errMsg;

    public ErrorResponseDto(HttpStatus status, int errCode, String errMsg) {
        this.status = status;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
