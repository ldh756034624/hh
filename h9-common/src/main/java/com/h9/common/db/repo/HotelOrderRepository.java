package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.hotel.HotelOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by itservice on 2018/1/3.
 */
public interface HotelOrderRepository extends BaseRepository<HotelOrder> {

    @Query("select ho from HotelOrder ho where ho.userId =?1 and ho.orderStatus in ?2 order by ho.createTime desc")
    Page<HotelOrder> findAllBy(long userId,Collection status, Pageable pageRequest);

    @Query("select ho from HotelOrder ho where ho.userId =?1 order by ho.createTime desc")
    Page<HotelOrder> findAllByUserId(long userId, Pageable pageRequest);


    @Query("select o from HotelOrder o where o.orderStatus in(?1,?2)")
    List<HotelOrder> findByOrderStatus(Integer status,Integer status2);
}
