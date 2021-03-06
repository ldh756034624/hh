package com.h9.api.model.vo.community;

import java.math.BigDecimal;

import lombok.Data;

import static com.h9.common.constant.ParamConstant.JIUYUAN_ICON;

/**
 * Created by 李圆 on 2018/1/11
 */
@Data
public class StickRewardMoneyVO {

   private String icon;

   private String type;

   private BigDecimal rewardMoney;

   private BigDecimal balance = new BigDecimal(0);

   private String words;

   private String title;

   public StickRewardMoneyVO(){

   }
}
