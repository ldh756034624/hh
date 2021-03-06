package com.h9.common.db.repo;


import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.community.StickLike;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: StickLikeRepository
 * @Description: StickLike 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface StickLikeRepository extends BaseRepository<StickLike> {


    @Query("select s from StickLike s where s.userId = ?1 and s.stickId = ?2")
    StickLike findByUserIdAndStickId(long userId, long id);
}
