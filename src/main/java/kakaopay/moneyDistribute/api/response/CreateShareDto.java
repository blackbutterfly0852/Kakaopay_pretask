package kakaopay.moneyDistribute.api.response;

import lombok.Data;

@Data
public class CreateShareDto {

    private final String newToken;

    public CreateShareDto(String newToken) {
        this.newToken = newToken;
    }
}
