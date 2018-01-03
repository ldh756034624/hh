package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.hotel.Hotel;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by itservice on 2018/1/2.
 */
public interface HotelRepository  extends BaseRepository<Hotel>{

    @Query("select o from Hotel  o where o.city = ?1 and( o.detailAddress like ?2 or o.hotelName = ?2)")
    List<Hotel> findByCityAndHotelName(String city,String hotelName);
}