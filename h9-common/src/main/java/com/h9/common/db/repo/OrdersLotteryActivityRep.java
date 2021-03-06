package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.bigrich.OrdersLotteryActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;

/**
 * Created by Ln on 2018/3/28.
 */
public interface OrdersLotteryActivityRep extends BaseRepository<OrdersLotteryActivity> {

    @Query("select o from OrdersLotteryActivity o where ?1>= o.startTime and ?1 < o.endTime and o.status = 1 and  o.id is not null and id<>?2")
    List<OrdersLotteryActivity> findByDateId(Date date, Long id);

    @Query("select o from OrdersLotteryActivity o where ?1< o.startTime and ?2 > o.startTime and o.status = 1 and  o.id is not null and id<>?3")
    List<OrdersLotteryActivity> findByDateId2(Date date1, Date date2, Long id);

    @Query("select o from OrdersLotteryActivity o where ?1<= o.endTime and ?2 > o.endTime and o.status = 1 and  o.id is not null and id<>?3")
    List<OrdersLotteryActivity> findByDateId3(Date date1, Date date2, Long id);

    @Query("select o from OrdersLotteryActivity o where ?1>= o.startTime and ?1 < o.endTime and o.status = 1 and o.id <> id")
    List<OrdersLotteryActivity> findByDate(Date date, Long id);

    @Query("select o from OrdersLotteryActivity o where o.status =?1")
    Page<OrdersLotteryActivity> findByStatus(int status, Pageable pageable);

    @Query("select o from OrdersLotteryActivity o where  o.status = 1 and o.startLotteryTime <= ?1 ")
    List<OrdersLotteryActivity> findByLotteryDate(Date date);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "false")})
    @Query("select o from OrdersLotteryActivity o where id = ?1")
    OrdersLotteryActivity findByIdFromDB(Long id);

    @Query("select o from OrdersLotteryActivity  o where o.delFlag = ?1")
    Page<OrdersLotteryActivity> findPageByDelFlag(int delFlag, Pageable pageable);


}
