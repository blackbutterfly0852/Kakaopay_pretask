package kakaopay.moneyDistribute.repository;

import kakaopay.moneyDistribute.domain.Share;
import kakaopay.moneyDistribute.domain.SharedAmount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SharedAmountRepository {

    private final EntityManager em;

    // 1. 각 분배 건 저장
    public void saveSharedAmount(SharedAmount sharedAmount) {
        em.persist(sharedAmount);
    }

    // 2. 토큰 이름으로 분배건 조회 (테스트 용)
    public List<SharedAmount> findByToken(Share share) {
        return em.createQuery("select sa from SharedAmount sa where sa.share =:share ", SharedAmount.class)
                .setParameter("share", share).getResultList();
    }



}
