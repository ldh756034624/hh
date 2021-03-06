package com.h9.common.db.repo;


import com.h9.common.base.BaseRepository;
import com.h9.common.base.PageResult;
import com.h9.common.db.entity.lottery.LotteryFlow;
import com.h9.common.db.entity.lottery.Reward;
import com.h9.common.db.entity.user.User;
import com.h9.common.modle.dto.LotteryFlowActivityDTO;
import com.h9.common.modle.dto.LotteryFlowFinanceDTO;
import com.h9.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: LotteryFlowRepository
 * @Description: LotteryFlow 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface LotteryFlowRepository extends BaseRepository<LotteryFlow> {

    @Query("select l from LotteryFlow l where l.reward = ?1 order by  l.createTime  ")
    List<LotteryFlow> findByReward(Reward reward);


    @Query("select l from LotteryFlow l where l.reward = ?1 and l.user.id = ?2 order by l.createTime desc ")
    LotteryFlow findByReward(Reward reward,Long userId);


    LotteryFlow findTop1ByRewardOrderByMoneyDesc(Reward reward);




    @Query("select l from LotteryFlow l where l.user.id = ?1 order by l.createTime desc ")
    Page<LotteryFlow> findByReward(Long userId, Pageable pageable);

    default Specification<LotteryFlow> buildActivitySpecification(LotteryFlowActivityDTO lotteryFlowActivityDTO){
        return  new Specification<LotteryFlow>() {
            @Override
            public Predicate toPredicate(Root<LotteryFlow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(lotteryFlowActivityDTO.getPhone())){
                    Join<LotteryFlow,User> join = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(join.get("phone").as(String.class), lotteryFlowActivityDTO.getPhone()));
                }
                if(!StringUtils.isEmpty(lotteryFlowActivityDTO.getCode())){
                    predicates.add(cb.equal(root.get("reward").get("code").as(String.class), lotteryFlowActivityDTO.getCode()));
                }
                if(lotteryFlowActivityDTO.getStatus()!=null&& lotteryFlowActivityDTO.getStatus()!=0){
                    if(lotteryFlowActivityDTO.getStatus()==2){
                        predicates.add(cb.lessThanOrEqualTo(root.get("money").as(BigDecimal.class), BigDecimal.ZERO));
                    }else{
                        predicates.add(cb.greaterThan(root.get("money").as(BigDecimal.class), BigDecimal.ZERO));
                    }
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };

    }

    default Specification<LotteryFlow> buildFinanceSpecification(LotteryFlowFinanceDTO lotteryFlowFinanceDTO){
        return  new Specification<LotteryFlow>() {
            @Override
            public Predicate toPredicate(Root<LotteryFlow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(lotteryFlowFinanceDTO.getPhone())){
                    predicates.add(cb.equal(root.get("user").get("phone").as(String.class), lotteryFlowFinanceDTO.getPhone()));
                }
                if(lotteryFlowFinanceDTO.getCode()!=null){
                    predicates.add(cb.equal(root.get("reward").get("code").as(String.class), lotteryFlowFinanceDTO.getCode()));
                }
                if(lotteryFlowFinanceDTO.getStartTime()!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), lotteryFlowFinanceDTO.getStartTime()));
                }
                if(lotteryFlowFinanceDTO.getEndTime()!=null){
                    predicates.add(cb.lessThan(root.get("createTime").as(Date.class), DateUtil.addDays(lotteryFlowFinanceDTO.getEndTime(),1)));
                }
                predicates.add(cb.equal(root.get("transferStatus").as(Integer.class),LotteryFlow.TransferStatusEnum.NON_TRANSFERRED.getId()));
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };

    }

   /* @Query(value = "select ob.* from (select o.* from lottery_flow o, user u,reward r "+
            " where o.user_id = u.id and o.reward_id = r.id" +
            " and (?1 is null or u.phone = ?1)" +
            " and (?2 is null or r.code = ?2)" +
            " and (?3 is null or o.create_time >= ?3)" +
            " and (?4 is null or o.create_time < ?4)) as ob" +
            " left join lottery_flow_record l" +
            " on ob.id = l.lottery_flow_id" +
            " where l.id is null" +
            " order by ob.id desc \n#pageable\n", nativeQuery = true)
    Page<Object> findByFinance(String phone, String code, Date startTime, Date endTime, Pageable pageable);*/

   default PageResult<LotteryFlow> findByReward(Long userId, int page , int limit){
       PageRequest pageRequest = pageRequest(page, limit);
       Page<LotteryFlow> lotteryFlows = findByReward(userId, pageRequest);
       return new PageResult(lotteryFlows);
   }

   @Query("select sum(l.money) from LotteryFlow l")
   BigDecimal getLotteryCount();

   @Transactional
   @Query("select o from LotteryFlow o where o.id=?1")
   @Lock(LockModeType.PESSIMISTIC_WRITE)
    LotteryFlow findByLockId(long id);
}
