package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.order.China;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 李圆 on 2017/11/30
 */

public interface ChinaRepository extends BaseRepository<China> {

    /**
     * 查询对应省的id
     * @param name
     * @return
     */
    @Query("select p.parentCode from  China p where p.name = ?1")
    String  findPid(String name);

    /**
     * 查询对应子区域的id
     * @param parentCode
     * @param name
     * @return id
     */
    @Query("select c.id from China c where c.parentCode = ?1 and c.name = ?2 and status = 1")
    String findCid(String parentCode,String name);

    /**
     * 所有省
     * @return
     */
    @Query("select c from  China c where c.level = 1")
    List<China> findAllProvinces();


    @Query("select c.name from  China c where c.id = ?1")
    String  findName(Long id);

}
