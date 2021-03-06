package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.base.PageResult;
import com.h9.common.db.entity.bigrich.OrdersLotteryActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * <p>Title:h9-parent</p>
 * <p>Desription:</p>
 *
 * @author LiYuan
 * @Date 2018/3/28
 */
public interface OrdersLotteryActivityRepository extends BaseRepository<OrdersLotteryActivity> {
    @Query("select r from OrdersLotteryActivity r where r.status = ?1 and r.winnerUserId is not null  order by r.id DESC ")
    Page<OrdersLotteryActivity> findAllDetail(Integer status, Pageable pageRequest);

    default PageResult<OrdersLotteryActivity> findAllDetail(Integer page, Integer limit) {
        Page<OrdersLotteryActivity> List = findAllDetail(OrdersLotteryActivity.statusEnum.FINISH.getCode(), pageRequest(page, limit));
        return new PageResult(List);
    }


    @Query("select o from OrdersLotteryActivity o where o.id = ?1")
    OrdersLotteryActivity findOneById(Long id);

    @Query(value = "SELECT * From orders_lottery  where start_time < ?1 and end_time > ?1 and status = 1 ORDER BY start_time desc limit 1", nativeQuery = true)
    OrdersLotteryActivity findAllTime(Date createTime);
}
