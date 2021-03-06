package com.h9.common.db.entity.hotel;



import com.alibaba.fastjson.JSONObject;
import com.h9.common.base.BaseEntity;
import com.h9.common.utils.DateUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jboss.logging.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description: 酒店
 */
@Data
@Entity
@Table(name = "hotel")
@Accessors(chain = true)
public class Hotel extends BaseEntity{
    @Transient
    Logger logger = Logger.getLogger(Hotel.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "images",columnDefinition = "varchar(2000) COMMENT '酒店图片'")
    private String images;

    @Column(name = "grade",columnDefinition = "DECIMAL(10,2) default 0.00 COMMENT '酒店评分'")
    private BigDecimal grade = new BigDecimal(0);

    @Column(name = "detail_address",columnDefinition = "varchar(255) comment '酒店地址'")
    private String detailAddress;

    @Column(name = "tips",columnDefinition = "varchar(255) comment '预定提示'")
    private String tips;

    @Column(name = "hotel_info",columnDefinition = "text comment '酒店介绍 html'")
    private String hotelInfo;

    @Column(name= "discount",columnDefinition = "float comment '折扣'")
    private Float discount;

    @Column(name = "min_consumer",columnDefinition = "decimal(10,2) comment '最低消费'")
    private BigDecimal minConsumer;

    @Column(name = "city",columnDefinition = "varchar(255) comment '城市'")
    private String city;

    @Column(name = "hotel_name",columnDefinition = "varchar(255) comment '酒店名'")
    @NotNull
    private String hotelName;

    @Column(name = "hotel_phone",columnDefinition = "varchar(200) comment '订房电话'")
    @NotNull
    private String hotelPhone;

    @Column(name = "start_reserve_time",columnDefinition = "varchar(200) comment '开始预约时间'")
    private String  startReserveTime;

    @Column(name = "end_reserve_time",columnDefinition = "varchar(200) comment '结束预约时间'")
    private String endReserveTime;

    /**
     * description:
     * @see  Status
     */
    @Column(name = "status",columnDefinition = "int default 1 comment '状态 1正常，0禁用'")
    @NotNull
    private Integer status;

    public void setImages(List<String> images) {
        if (images == null || images.isEmpty()) {
            images = new ArrayList<>();
        }
        this.images = JSONObject.toJSONString(images);
    }

    public List<String> getImages(){
        try {
            return JSONObject.parseArray(images, String.class);
        }catch (Exception e){
            logger.debugv("图片解析出错，请检查数据"+getId());
        }
        return new ArrayList<>();
    }

    public Long getId() {
        return id;
    }


    @Getter
    public static enum Status{
        NORMAL(1, "正常"),
        BAN(0,"禁用");
        public int code;
        public String desc;

        Status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static Status findByCode(int code) {
            Status[] values = values();
            for (Status elEnum : values) {
                if (code == elEnum.getCode()) {
                    return elEnum;
                }
            }
            return null;
        }
    }


    public Date getStartReserveTime() {
        Date startDate = DateUtil.formatDate(DateUtil.formatDate(new Date(), DateUtil.FormatType.DAY) +" "+ startReserveTime, DateUtil.FormatType.MINUTE);
        return startDate;
    }

    public Date getEndReserveTime() {
        Date endDate = DateUtil.formatDate(DateUtil.formatDate(new Date(), DateUtil.FormatType.DAY) +" "+ endReserveTime, DateUtil.FormatType.MINUTE);
        return endDate;
    }

    public String getStartReserveTimeStr(){
        return startReserveTime;
    }

    public String getEndReserveTieStr(){
        return endReserveTime;
    }
}
