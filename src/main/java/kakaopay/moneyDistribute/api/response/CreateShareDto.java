package kakaopay.moneyDistribute.api.response;

import lombok.Data;

@Data
public class CreateShareDto {
    private final String code;
    private final String message;
    private final String newToken;
    public CreateShareDto(String newToken){
        this.code = "200";
        this.message = "정상 처리";
        this.newToken = newToken;
    }
}
