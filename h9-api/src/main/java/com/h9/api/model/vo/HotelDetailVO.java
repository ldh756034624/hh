package com.h9.api.model.vo;

import com.h9.common.db.entity.hotel.Hotel;
import com.h9.common.db.entity.hotel.HotelRoomType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by itservice on 2018/1/2.
 */
@Getter
@Setter
public class HotelDetailVO {
    private Long id;
    private List<String> images = new ArrayList<>();
    private String hotelName;
    private BigDecimal grade = new BigDecimal(0);
    private String detailAddress;
    private String tips;
    private List<RoomListVO> roomList = new ArrayList<>();
    private String hotelInfo;

    public HotelDetailVO() {
    }

    public HotelDetailVO(Hotel hotel,List<HotelRoomType> roomType,String wechatHostUrl) {
        BeanUtils.copyProperties(hotel, this);
        if(roomType != null){
            List<RoomListVO> roomList = roomType.stream().map(el -> new RoomListVO(el)).collect(Collectors.toList());
            this.setRoomList(roomList);
        }
        images = hotel.getImages();
        String url = wechatHostUrl+"/h9-weixin/#/account/articleDetail?id=";
        url += hotel.getId();
        url += "&type=hotel";
        this.setHotelInfo(url);
        String startReserveTimeStr = hotel.getStartReserveTimeStr();
        String endReserveTieStr = hotel.getEndReserveTieStr();
        this.tips = "酒店预定时间为" + startReserveTimeStr + "至" + endReserveTieStr + "其他时间暂不支持预定！";
    }
}
