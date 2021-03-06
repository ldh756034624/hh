package com.h9.admin.model.vo;

import com.alibaba.fastjson.JSONObject;
import com.h9.common.db.entity.hotel.Hotel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by itservice on 2018/1/4.
 */
@Data
@Accessors(chain = true)
@ApiModel(value="酒店列表")
public class HotelListVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("图片")
    private List<String> images;

    @ApiModelProperty("酒店名")
    private String hotelName;

    @ApiModelProperty("房间数")
    private Integer roomCount;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("评分")
    private BigDecimal grade;

    @ApiModelProperty("酒店预订电话")
    private String hotelPhone;
    @ApiModelProperty("酒店城市")
    private String city;
    @ApiModelProperty("酒店预订开始时间")
    private String  startReserveTime;
    @ApiModelProperty("酒店预订结束时间")
    private String endReserveTime;
    @ApiModelProperty("酒店介绍")
    private String hotelInfo;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("状态描述")
    private String statusDesc;



    public HotelListVO(){}

    public HotelListVO(Hotel hotel,Integer roomCount){

        BeanUtils.copyProperties(hotel, this);
//        List<String> imagesFromDb = hotel.getImages();
//        if(!CollectionUtils.isEmpty(imagesFromDb)){
//            this.images = imagesFromDb.get(0);
//        }
        this.setRoomCount(roomCount);
        this.setStartReserveTime(hotel.getStartReserveTimeStr());
        this.setEndReserveTime(hotel.getEndReserveTieStr());
        Integer status = hotel.getStatus();
        if(status == null){
            System.out.println();
        }
        Hotel.Status findStatus = Hotel.Status.findByCode(status);
        if(findStatus == null){
            this.status = -1;
            this.setStatusDesc("未知状态");
        }else{
            this.setStatus(status);
            this.setStatusDesc(findStatus.getDesc());
        }

    }

}
