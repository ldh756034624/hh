package com.h9.lottery.controller;

import com.h9.common.annotations.PrintReqResLog;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.common.ServiceException;
import com.h9.lottery.config.LotteryConstantConfig;
import com.h9.lottery.interceptor.Secured;
import com.h9.lottery.model.dto.LotteryFlowDTO;
import com.h9.lottery.model.dto.LotteryResult;
import com.h9.lottery.model.vo.LotteryDto;
import com.h9.lottery.service.LotteryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;
import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * LotteryContorller:刘敏华 shadow.liu@hey900.com
 * Date: 2017/11/2
 * Time: 15:18
 */
@RestController
@Api(value = "抽奖活动", description = "抽奖活动",position = 0,basePath = "/h9/lottery")
public class LotteryContorller {

     Logger logger = Logger.getLogger(LotteryContorller.class);

    @Resource
    private LotteryService lotteryService;
    @Resource
    private Redisson redisson;


    @Secured(bindPhone = false)
    @GetMapping("/qr")
    @ApiOperation(value = "扫码抽奖")
    public Result appCode(@ApiParam(value = "用户token" ,name = "token",required = true,type="header")
                              @SessionAttribute("curUserId") long userId,
                          @ModelAttribute LotteryDto lotteryVo, HttpServletRequest request) throws ServiceException {
        String code = LotteryConstantConfig.path2Code(lotteryVo.getCode());

        RLock lock = redisson.getLock("lock:" + code);
        Result result;
        try {
            logger.debugv("lock start" + userId);
            lock.lock(30, TimeUnit.SECONDS);
            result = lotteryService.appCode(userId, lotteryVo, request);
        } finally {
            lock.unlock();
            logger.debugv("lock end"+ userId);
        }
        return result;
    }


    @Secured(bindPhone = false)
    @GetMapping("/start")
    @ApiOperation(value = "开始抽奖")
    public Result startCode(@ApiParam(value = "用户token" ,name = "token",required = true,type="header")
                          @SessionAttribute("curUserId") long userId

                            ,@RequestParam("code") String code){
        code = LotteryConstantConfig.path2Code(code);
        RLock lock = redisson.getLock("lock:start:" + code);
        Result lottery;
        try {
            lock.lock(30, TimeUnit.SECONDS);
            logger.debugv("start userId {0} code {1}" ,userId, code);
            lottery = lotteryService.lottery(userId, code);
        } finally {
            lock.unlock();
        }
        return lottery;
    }


    @Secured(bindPhone = false)
    @GetMapping("/room")
    @ApiOperation(value = "奖励房间")
    public Result<LotteryResult> getLotteryRoom(
            @ApiParam(value = "用户token" ,name = "token",required = true,type="header")
            @SessionAttribute("curUserId") long userId,@RequestParam("code") String code){
        logger.debugv("room userId {0} code {1}" ,userId, code);
        code = LotteryConstantConfig.path2Code(code);
        RLock lock = redisson.getLock("lock:start:" + code);
        Result lottery;
        try {
            lock.lock(30, TimeUnit.SECONDS);
            logger.debugv("start userId {0} code {1}" ,userId, code);
            lottery = lotteryService.getLotteryRoom(userId,code);
        } finally {
            lock.unlock();
        }
        return lottery;
    }




    @Secured(bindPhone = false)
    @GetMapping("/history")
    @ApiOperation(value = "抽奖记录")
    public Result<PageResult<LotteryFlowDTO>>  getLotteryRoom(
            @ApiParam(value = "用户token" ,name = "token",required = true,type="header")
            @SessionAttribute("curUserId") long userId,
            @RequestParam(value = "page",required = false) int page,
            @RequestParam(value = "limit",required = false) int limit
    ){
        return lotteryService.history(userId,page,limit);
    }


    @GetMapping("/forward/{code}")
    @ApiOperation(value = "扫码抽奖")
    public void forward(@PathVariable("code") String code, HttpServletResponse response) throws IOException {
        String forward = lotteryService.forward(code);
        logger.debugv("forward path:{0}",forward);
        response.sendRedirect(forward);
    }


    /**
     * description: 兼容 以前 数据
     */
    @GetMapping("/forward//GRP/Index")
    public void forwardOld(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String forward = lotteryService.forward(code);
        logger.debugv("forward path:{0}",forward);
        response.sendRedirect(forward);
    }


}
