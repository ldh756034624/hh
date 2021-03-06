package com.h9.api.controller;

import com.h9.api.interceptor.Secured;
import com.h9.api.service.RechargeService;
import com.h9.common.base.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * RecharageController:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/11
 * Time: 16:29
 */
@RestController
@RequestMapping("/recharge")
public class RecharageController {

    @Resource
    private RechargeService rechargeService;


    @Secured
    @GetMapping("/order")
    public Result recharge(@SessionAttribute("curUserId") Long userId, @RequestParam BigDecimal money){
        return rechargeService.recharge(userId,money,false);
    }


    @Secured
    @GetMapping("/app/order")
    public Result appRecharge(@SessionAttribute("curUserId") Long userId, @RequestParam BigDecimal money){
        return rechargeService.appRecharge(userId,money);
    }


    @Secured
    @GetMapping("/order/{id}")
    public Result recharge(@PathVariable(name="id") Long orderId){
        return rechargeService.getOrder(orderId);
    }




}
