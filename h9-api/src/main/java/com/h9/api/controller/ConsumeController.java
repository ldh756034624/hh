package com.h9.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.h9.api.interceptor.Secured;
import com.h9.api.model.dto.DidiCardDTO;
import com.h9.api.model.dto.DidiCardVerifyDTO;
import com.h9.api.model.dto.MobileRechargeDTO;
import com.h9.api.model.dto.MobileRechargeVerifyDTO;
import com.h9.api.provider.model.SuNingResult;
import com.h9.api.service.ConsumeService;
import com.h9.common.base.Result;
import io.swagger.annotations.Api;
import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * description: 充值、消费接口
 */
@RestController
@RequestMapping("/consume")
@Api(value = "充值接口", description = "充值接口")
public class ConsumeController {

    @Resource
    private ConsumeService consumeService;

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * description: 手机充值
     */
    @Secured(bindPhone = true)
    @PostMapping("/mobile/recharge")
    public Result mobileRecharge(
            @SessionAttribute("curUserId") Long userId,
            @Valid @RequestBody MobileRechargeDTO mobileRechargeDTO) {
            return consumeService.recharge(userId, mobileRechargeDTO);

    }

    /**
     * description: 手机充值校验
     */
    @Secured(bindPhone = true)
    @PostMapping("/mobile/verify/recharge")
    public Result mobileRechargeVerify(
            @SessionAttribute("curUserId") Long userId,
            @Valid @RequestBody MobileRechargeVerifyDTO mobileRechargeVerifyDTO) {
            return consumeService.rechargeVerify(userId, mobileRechargeVerifyDTO);
    }

    /**
     * description: 获取可充值的面额
     */
    @Secured()
    @GetMapping("/mobile/denomination")
    public Result rechargeDenomination(@SessionAttribute("curUserId") Long userId) {

        return consumeService.rechargeDenomination(userId);
    }

    /**
     * description: 滴滴卡劵列表
     */
    @Secured(bindPhone = true)
    @GetMapping("/didiCards")
    public Result didiCardList() {

        return consumeService.didiCardList();
    }

    /**
     * description: 兑换滴滴卡
     */
    @Secured(bindPhone = true)
    @PutMapping("/didiCard/convert")
    public Result didiCardConvert(@RequestBody @Valid DidiCardDTO didiCardDTO, @SessionAttribute("curUserId") Long userId) {
        return consumeService.didiCardConvert(didiCardDTO, userId);
    }

    /**
     * description: 兑换卡劵校验接口
     */
    @Secured(bindPhone = true)
    @PutMapping("/coupons/convert/verify")
    public Result verifyCoupons(@RequestBody @Valid DidiCardVerifyDTO didiCardDTO, @SessionAttribute("curUserId") Long userId){
        return consumeService.verifyConvertCoupons(didiCardDTO, userId);
    }

    /**
     * description: 提现
     */
    @Secured(bindPhone = true)
    @PostMapping("/withdraw/{bankId}/{code}")
    public Result bankWithdraw(@SessionAttribute("curUserId") Long userId
            , @PathVariable Long bankId
            , @PathVariable String code
            , @RequestParam(required = false, defaultValue = "0") double longitude
            , @RequestParam(required = false, defaultValue = "0") double latitude, HttpServletRequest request) {

        return consumeService.bankWithDraw(userId, bankId, code, longitude, latitude, request);
    }

    /**
     * description: 提现
     */

    @PostMapping(value = "/withdraw/callback",consumes ={"application/x-www-form-urlencoded"}, produces = {"text/html"})
    public String bankWithdraw(SuNingResult suNingResult) {
        logger.debugv(JSONObject.toJSONString(suNingResult));
        return consumeService.callback(suNingResult);
    }

    /**
     * description: 提现校验
     */
    @Secured(bindPhone = true)
    @PostMapping("/withdraw/verify/{bankId}")
    public Result bankWithdrawVerify(@SessionAttribute("curUserId") Long userId
            , @PathVariable Long bankId
            , @RequestParam(required = false, defaultValue = "0") double longitude
            , @RequestParam(required = false, defaultValue = "0") double latitude, HttpServletRequest request) {

        return consumeService.bankWithDrawVerify(userId, bankId, longitude, latitude, request);
    }

    /**
     * description: 余额充值/test 使用
     */
    @GetMapping("/cz/{userId}")
    public Result cz(@PathVariable Long userId) {
        return consumeService.cz(userId);
    }


    /**
     * description: 提现展示信息
     */
    @Secured
    @GetMapping("/withdraw/info")
    public Result withdraInfo(@SessionAttribute("curUserId") Long userId) {
        return consumeService.withdraInfo(userId);
    }

}
