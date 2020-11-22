package kakaopay.moneyDistribute.api.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto {
    private final HttpStatus errorStatus;
    private final int errorCode;
    private final String errorMessage;

    public ErrorResponseDto(HttpStatus status, int errCode, String errMsg) {
        this.errorStatus = status;
        this.errorCode = errCode;
        this.errorMessage = errMsg;
    }
}
