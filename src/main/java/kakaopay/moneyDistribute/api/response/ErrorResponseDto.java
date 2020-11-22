package kakaopay.moneyDistribute.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {
    private HttpStatus errorStatus;
    private int errorCode;
    private String errorMessage;

    public ErrorResponseDto( HttpStatus errStatus, int errCode, String errMsg) {
        this.errorStatus = errStatus;
        this.errorCode = errCode;
        this.errorMessage = errMsg;
    }
}
