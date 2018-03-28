package com.h9.api.controller;

import com.h9.api.interceptor.Secured;
import com.h9.api.model.vo.BigRichVO;
import com.h9.api.service.BigRichService;
import com.h9.common.base.Result;
import org.hibernate.annotations.GeneratorType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>Title:h9-parent</p>
 * <p>Desription:一号大富贵活动</p>
 *
 * @author LiYuan
 * @Date 2018/3/28
 */
@RestController
public class BigRichController {


    @Resource
    private BigRichService bigRichService;

    @Secured
    @GetMapping("/bigrich/record")
    public Result getRecord(@SessionAttribute("curUserId")long userId,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "20") Integer limit){
        return bigRichService.getRecord(1,page,limit);
    }

    @GetMapping("/bigrich/userRecord")
    public  Result getUserRecord(@SessionAttribute("curUserId")long userId,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit){
        return bigRichService.getUserRecord(userId,page,limit);
    }

}
