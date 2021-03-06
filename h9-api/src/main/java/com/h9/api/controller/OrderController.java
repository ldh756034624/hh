package com.h9.api.controller;

import com.h9.api.interceptor.Secured;
import com.h9.api.service.OrderService;
import com.h9.common.base.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by itservice on 2017/11/1.
 */

@RestController
@Api(value = "订单相关接口",description = "订单相关接口")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * description: 订单列表
     */
    @Secured
    @GetMapping("/orders")
    public Result orderList(@SessionAttribute("curUserId")long userId,
                            @RequestParam(defaultValue = "-1",required = false) Integer status,
                            @RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer limit) {
        return orderService.orderList(status,userId,page,limit);
    }

    /**
     * description: 订单详情
     */
    @Secured
    @GetMapping("/orders/{orderId}")
    public Result orderDetail(@PathVariable Long orderId,@SessionAttribute("curUserId")long userId){
        return orderService.orderDetail(orderId,userId);
    }



}
