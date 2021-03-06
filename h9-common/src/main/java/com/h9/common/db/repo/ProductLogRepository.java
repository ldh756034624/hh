package com.h9.common.db.repo;


import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.lottery.ProductLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @ClassName: ProductLogRepository
 * @Description: ProductLog 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface ProductLogRepository extends BaseRepository<ProductLog> {

    @Query("select count(pl.code) from  ProductLog pl where pl.imei = ?1 and pl.createTime>?2  and pl.product = null" )
    long findByUserId(String imei, Date startDate);

    @Query("select count(pl.code) from  ProductLog pl where pl.imei = ?1  and pl.code = ?2" )
    long findByUserId(String imei, String code );

}

