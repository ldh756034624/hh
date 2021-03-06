package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.config.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by itservice on 2017/10/30.
 */
public interface BannerRepository extends BaseRepository<Banner> {

    /**
     * description: 查询当前生效的banner
     */
    @Query(value = "select * from banner,banner_type where banner.banner_type_id = banner_type.id " +
            "and banner.start_time < ?1 and banner.end_time >?1 and banner.enable = 1 " +
            "and banner_type.enable = 1 and banner_type.location =?2 order by banner.sort desc,banner.id desc"
            ,nativeQuery = true)
    List<Banner> findActiviBanner( Date date,Integer location);

    /**
     * description: 查询当前生效的banner
     */
    @Query(value = "select * from banner,banner_type where banner.banner_type_id = banner_type.id " +
            "and banner.start_time < ?2 and banner.end_time >?2 and banner.enable = 1 " +
            "and banner_type.enable = 1 and banner_type.location =?1 order by banner.sort desc,banner.id desc"
            ,nativeQuery = true)
    Stream<Banner> findActiviBanner(Integer location,Date date);

    Banner findByTitle(String title);

    Banner findByIdNotAndTitle(long id,String title);

    @Query("select o from Banner o where o.bannerType.id = ?1  order by o.enable desc , o.sort desc , o.id desc ")
    Page<Banner> findAllByBannerType_Id(long banner_type_id,Pageable page);

    @Query("select o from Banner o where o.bannerType.id = ?1")
    List<Banner> findAllByBannerTypeId(long banner_type_id);

    @Query("select o from Banner o where o.bannerType.id = ?1 order by o.sort")
    List<Banner> findAllByBannerTypeIdOrder(long banner_type_id);

    @Query("select o from Banner o where o.bannerType.id = ?1 and o.enable = 1 order by o.sort desc ,o.id desc")
    List<Banner> findAllByBanner(long banner_type_id);
}
