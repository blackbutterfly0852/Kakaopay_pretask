package kakaopay.moneyDistribute.exception;

import kakaopay.moneyDistribute.api.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j

/**
 * GlobalExceptionController.java
 * 오류 핸들링
 * */
public class GlobalExceptionController {
    /**
     * 입력 값 오류
     **/
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(ConstraintViolationException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "Header를 확인해주세요.");
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(MissingServletRequestParameterException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "파라미터 명을 확인해 주세요.");
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(MethodArgumentTypeMismatchException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "파라미터 값이 유효하지 않습니다.");
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }


    /**
     * 뿌리기 생성 API 시 오류
     **/
    // 최초 뿌릴 인원 수 오류__500
    @ExceptionHandler(OverRoomCountException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(OverRoomCountException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 대화방에 존재 하지 않는 사용자__500
    @ExceptionHandler(NotInTheRoomException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotInTheRoomException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 비활성화 상태__404
    @ExceptionHandler(NotActivationStatusException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotActivationStatusException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }



    /**
     * 뿌리기 받기 API 시 오류
     **/
    // 이미 뿌리기 건 받았는지__500
    @ExceptionHandler(isAlreadyReceivedException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(isAlreadyReceivedException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 토큰 미존재__404
    @ExceptionHandler(NotExistTokenException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotExistTokenException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 다른 대화방의 뿌리기 건을 요청한 사용자_500
    @ExceptionHandler(NotInTheTokenRoomException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotInTheTokenRoomException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 현재 입력된 대화방과 토큰이 생성된 대화방이 다를 경우_500
    @ExceptionHandler(NotTheSameTokenRoomException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotTheSameTokenRoomException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 자기가 만든 뿌리기 건을 받는 경우_500
    @ExceptionHandler(SameTokenCreaterException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(SameTokenCreaterException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 뿌리기 건 10분 경과_500
    @ExceptionHandler(isOverTenMinutesException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(isOverTenMinutesException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    // 뿌리기 건 미존재_500
    @ExceptionHandler(NotExistSharedAmountException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotExistSharedAmountException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    /**
     * 뿌리기 조회 API 시 오류
     **/
    // 자신이 만들지 않은 다른 뿌리기 건을 조회하는 사용자_500
    @ExceptionHandler(NotTokenCreaterException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(NotTokenCreaterException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

    //  뿌리기 건 7일 경과_500
    @ExceptionHandler(isOverSevenDaysException.class)
    protected ResponseEntity<ErrorResponseDto> ErrorHandler(isOverSevenDaysException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorStatus());
    }

}
