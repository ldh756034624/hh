package com.h9.common.db.repo;


import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.lottery.Reward;
import com.h9.common.modle.dto.RewardQueryDTO;
import com.h9.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: RewardRepository
 * @Description: Reward 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface RewardRepository extends BaseRepository<Reward> {
    @Query("select r from Reward r where r.code =?1")
    Reward findByCode(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reward r where r.id =?1")
    Reward findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reward r where r.code =?1")
    Reward findByCode4Update(String code);

    default  Page<Reward> findAllReward(RewardQueryDTO rewardQueryDTO, Pageable page){
        return findAll(buildRewardSpecification(rewardQueryDTO),page);
    }

    default Specification<Reward> buildRewardSpecification(RewardQueryDTO rewardQueryDTO){
        return new Specification<Reward>(){
            @Override
            public Predicate toPredicate(Root<Reward> root, CriteriaQuery<?> query, CriteriaBuilder cb){
                List<Predicate> predicates = new ArrayList<>();
                if(!StringUtils.isEmpty(rewardQueryDTO.getCode())){
                    predicates.add(cb.equal(root.get("code").as(String.class),rewardQueryDTO.getCode()));
                }
                if(rewardQueryDTO.getStartTime()!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class),rewardQueryDTO.getStartTime()));
                }
                if(rewardQueryDTO.getEndTime()!=null){
                    predicates.add(cb.lessThan(root.get("createTime").as(Date.class), DateUtil.addDays(rewardQueryDTO.getEndTime(),1)));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
    }

    /**
     * description: 查询结束时间少于当前时间,并且状态等于 1 或 2 的
     */
    @Query("select o from Reward  o where (o.status = 1 or o.status = 2) and o.finishTime  <= ?1")
    List<Reward> findByEndTimeAndStatus(Date date);


    @Query("select o.code from Reward  o where o.status = 1 ")
    List<String> findByStatus();

    @Query("select o.partakeCount from Reward  o where o.id = ?1 ")
   int findByPartakeCount(long rewardId);

    @Transactional
    @Modifying
    @Query("update Reward r set r.finishTime = ?2,r.partakeCount=r.partakeCount+1 where r.id=?1")
    public int updateReward(long rewardId, Date endDate);

    @Transactional
    @Modifying
    @Query("update Reward r set r.finishTime = ?2,r.userId=?3,r.partakeCount=r.partakeCount+1 where r.id=?1")
    public void updateReward(long rewardId,Date endDate,Long userId);

    @Query("SELECT r from Reward r where r.status = 3 and r.factoryStatus = -1 and r.createTime>'2018-01-15 00:00:00'")
    public List<Reward> findFactoryStatus();

    @Transactional
    @Modifying
    @Query("update Reward r set r.status=?2 where r.status<>?2 and r.id=?1")
    public int updateRewardStatus(long rewardId,int status);

}
