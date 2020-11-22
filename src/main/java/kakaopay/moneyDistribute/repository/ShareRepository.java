package kakaopay.moneyDistribute.repository;


import kakaopay.moneyDistribute.api.response.SearchShareDto;
import kakaopay.moneyDistribute.api.response.SearchSharedAmountDto;
import kakaopay.moneyDistribute.domain.Share;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ShareRepository {
    private final EntityManager em;

    // 1. 뿌리기 저장
    public void saveShare(Share share) {
        em.persist(share);
    }

    // 2. 뿌리기 아이디로 조회
    public Share findById(Long shareId) {
        return em.find(Share.class, shareId);
    }

    // 3. 뿌리기 조회 API
    public SearchShareDto findSearchShareDto(Share share) {
        SearchShareDto result = new SearchShareDto(share);
        result.setSearchSharedAmountDtos(findSharedAmount(share));
        return result;
    }
    private List<SearchSharedAmountDto> findSharedAmount(Share share) {
        List<SearchSharedAmountDto> sharedAmountDtoList =
                em.createQuery("select new kakaopay.moneyDistribute.api.response.SearchSharedAmountDto(sa.rcvAmt,sa.rcvId) " +
                " from SharedAmount sa" +
                " where sa.share =: share" +
                " and sa.rcvId is not null", SearchSharedAmountDto.class).setParameter("share", share).getResultList();
        return sharedAmountDtoList;
    }

}
