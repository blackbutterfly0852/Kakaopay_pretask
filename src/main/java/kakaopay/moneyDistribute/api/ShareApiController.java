package kakaopay.moneyDistribute.api;

import kakaopay.moneyDistribute.api.response.CreateShareDto;
import kakaopay.moneyDistribute.api.response.ReceiveShareDto;
import kakaopay.moneyDistribute.api.response.ResponseDto;
import kakaopay.moneyDistribute.api.response.SearchShareDto;
import kakaopay.moneyDistribute.service.ShareService;
import kakaopay.moneyDistribute.service.SharedAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ShareApiController {

    private final ShareService shareService;
    private final SharedAmountService sharedAmountService;

    // 1. 뿌리기 생성 API
    @PostMapping
    public ResponseDto createShare(@RequestHeader(value = "X-USER-ID") Long userId,
                                   @RequestHeader(value = "X-ROOM-ID") String roomName,
                                   @RequestParam(value = "initAmt") long initAmt,
                                   @RequestParam(value = "initCnt") int initCnt
    ) {
        String newToken = shareService.saveShare(userId, roomName, initAmt, initCnt);
        return new ResponseDto(new CreateShareDto(newToken));
    }

    // 2. 뿌리기 받기 API
    @PutMapping
    public ResponseDto receiveShare(@RequestHeader(value = "X-USER-ID") Long userId,
                                    @RequestHeader(value = "X-ROOM-ID") String roomName,
                                    @RequestParam(value = "token") String tokenName

    ) {
        long rcvAmt = sharedAmountService.saveSharedAmount(userId, roomName, tokenName);

        return new ResponseDto(new ReceiveShareDto(rcvAmt));
    }

    // 3. 뿌리기 조회 API
    @GetMapping
    public ResponseDto findShares(@RequestHeader(value = "X-USER-ID") Long userId,
                                  @RequestHeader(value = "X-ROOM-ID") String roomName,
                                  @RequestParam(value = "token") String tokenName
    ) {
        SearchShareDto searchShareDto = shareService.findShareByToken(userId, roomName, tokenName);
        return new ResponseDto(searchShareDto);
    }


}
