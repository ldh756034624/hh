package com.h9.admin.controller;

import com.h9.admin.interceptor.Secured;
import com.h9.admin.model.dto.PageDTO;
import com.h9.admin.model.dto.activity.ActivityAddDTO;
import com.h9.admin.model.dto.activity.ActivityEditDTO;
import com.h9.admin.model.dto.activity.RewardQueryDTO;
import com.h9.admin.model.vo.RewardVO;
import com.h9.admin.service.ActivityService;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.db.entity.Activity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: George
 * @date: 2017/11/7 19:42
 */
@RestController
@Api
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Secured
    @GetMapping(value = "/lottery/page")
    @ApiOperation("分页获取抢红包")
    public Result<PageResult<RewardVO>> getRewards(RewardQueryDTO rewardQueryDTO){
        return this.activityService.getRewards(rewardQueryDTO);
    }

   /* @Secured
    @GetMapping(value = "/lottery/flow/page")
    @ApiOperation("分页获取抢红包")
    public Result<PageResult<RewardVO>> getRewards(RewardQueryDTO rewardQueryDTO){
        return this.activityService.getRewards(rewardQueryDTO);
    }*/
}