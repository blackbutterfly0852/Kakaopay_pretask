package kakaopay.moneyDistribute.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Utility {

    // 1. 토큰 생성 메소드
    public static String createToken() {
        return RandomStringUtils.randomAlphabetic(3);
    }

    // 2. 뿌리기 금액 생성 메소드
    public static List<Long> dividedAmount(long initAmt, int initCnt) {
        long leftMoney = initAmt;
        int leftCnt = initCnt+1;
        long minValue = 0;
        long maxValue = initAmt / initCnt * 2;

        List<Long> dividedList = new ArrayList<>();
        for (int i = 0; i < initCnt - 1; i++) {
            int currInt = (int) (Math.random() * leftCnt) + 1;
            long dividedMoney = Math.max(minValue, Math.min(maxValue, leftMoney / currInt));
            dividedList.add(dividedMoney);
            leftMoney -= dividedMoney;
        }
        dividedList.add(leftMoney);
        return dividedList;
    }

}
