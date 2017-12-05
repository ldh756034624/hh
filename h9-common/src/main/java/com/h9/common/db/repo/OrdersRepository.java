package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.base.PageResult;
import com.h9.common.db.entity.Orders;
import com.h9.common.modle.dto.transaction.OrderDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by itservice on 2017/11/1.
 */
public interface OrdersRepository extends BaseRepository<Orders> {
    @Query("SELECT o from Orders o where o.user.id=?1 order by o.id desc")
    Page<Orders> findByUser(Long userId, Pageable pageable);

    @Query("SELECT o from Orders o where o.user.id=?1 and o.orderFrom = ?2 order by o.id desc")
    Page<Orders> findByUser(Long userId,Integer orderFrom , Pageable pageable);


    @Query("SELECT o from Orders o where o.user.id=?1 and o.goodsType = ?2 order by o.id desc")
    Page<Orders> findDiDiCardByUser(Long userId,String goodsTypeCode, Pageable pageable);

    default PageResult<Orders> findByUser(Long userId, int page, int limit){
        Page<Orders> byUser = findByUser(userId, pageRequest(page, limit));
        return new PageResult(byUser);
    }

    @Async
    @Query("select o from Orders o")
    Future<List<Orders>> findAllAsy();

    default Specification<Orders> buildSpecification(OrderDTO orderDTO) {
        return new Specification<Orders>() {
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotBlank(orderDTO.getNo())) {
                    predicateList.add(criteriaBuilder.equal(root.get("no").as(String.class),orderDTO.getNo()));
                }
                if (StringUtils.isNotBlank(orderDTO.getPhone())) {
                    predicateList.add(criteriaBuilder.equal(root.get("phone").as(String.class),orderDTO.getPhone()));
                }
                if (orderDTO.getStartTime() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("startTime").as(Date.class),orderDTO.getStartTime()));
                }
                if (orderDTO.getEndTime() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("endTime").as(Date.class),orderDTO.getEndTime()));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status").as(Integer.class),orderDTO.getStatus()));
                Predicate[] pre = new Predicate[predicateList.size()];
                return criteriaQuery.where(predicateList.toArray(pre)).getRestriction();
            }
        };
    }


}
