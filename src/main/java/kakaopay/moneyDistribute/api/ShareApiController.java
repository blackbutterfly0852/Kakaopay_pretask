package kakaopay.moneyDistribute.api;

import kakaopay.moneyDistribute.api.response.CreateShareDto;
import kakaopay.moneyDistribute.api.response.ReceiveShareDto;
import kakaopay.moneyDistribute.api.response.SearchShareDto;
import kakaopay.moneyDistribute.service.ShareService;
import kakaopay.moneyDistribute.service.SharedAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class ShareApiController {

    private final ShareService shareService;
    private final SharedAmountService sharedAmountService;

    // 1. 뿌리기 생성 API
    @PostMapping("/v1")
    public CreateShareDto createShare(@RequestHeader(value = "X-USER-ID") Long userId,
                                      @RequestHeader(value = "X-ROOM-ID") String roomName,
                                      @RequestParam(value = "initAmt"   ) long initAmt,
                                      @RequestParam(value = "initCnt"   ) int initCnt
    ) {
        String newToken = shareService.saveShare(userId, roomName, initAmt, initCnt);
        return new CreateShareDto(newToken);
    }

    // 2. 뿌리기 받기 API
    @PutMapping("/v2")
    public ReceiveShareDto receiveShare(@RequestHeader(value = "X-USER-ID") Long userId,
                                        @RequestHeader(value = "X-ROOM-ID") String roomName,
                                        @RequestParam(value = "token") String tokenName

    ) {
        long rcvAmt = sharedAmountService.saveSharedAmount(userId, roomName, tokenName);
        return new ReceiveShareDto(rcvAmt);
    }

    // 3. 뿌리기 조회 API
    @GetMapping("/v3")
    public SearchShareDto findShares(@RequestHeader(value = "X-USER-ID") Long userId,
                                     @RequestHeader(value = "X-ROOM-ID") String roomName,
                                     @RequestParam(value = "token") String tokenName
    ) {

        return shareService.findShareByToken(userId, roomName, tokenName);
    }


}
