package kakaopay.moneyDistribute.repository.custom;

import kakaopay.moneyDistribute.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareCustomRepository extends JpaRepository<Share, Long> {

    //1. 특정 토큰 존재여부 (share : 토큰 존재 , null : 토큰 미존재)
    public Share findByToken(String tokenName);
}
