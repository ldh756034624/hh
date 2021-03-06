package com.h9.api.controller;


import com.h9.api.provider.WeChatProvider;
import com.h9.common.base.Result;
import com.h9.common.db.bean.JedisTool;
import com.h9.common.db.entity.user.User;
import com.h9.common.db.entity.user.UserAccount;
import com.h9.common.db.repo.UserAccountRepository;
import com.h9.common.db.repo.UserRepository;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * TestController:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/31
 * Time: 10:26
 */
@RestController
@Api(value = "测试相关接口", description = "测试相关接口")
public class TestController {

    /**
     * description: 手机号登录
     */
    @GetMapping("/test/hello")
    public Result phoneLogin() {
        return Result.success();
    }


    @Resource
    private UserRepository userRepository;
    @Resource
    private UserAccountRepository userAccountRepository;

    @Value("${h9.current.envir}")
    private String envir;

    @GetMapping("/test/addvb")
    public Result addvb(@RequestParam String tel, @RequestParam String money) {
        if (!envir.equals("product")) {
            return Result.fail("不支持");
        }
        try {
            User user = userRepository.findByPhone(tel);
            UserAccount userAccount = userAccountRepository.findByUserId(user.getId());
            userAccount.setvCoins(new BigDecimal(money));
            userAccountRepository.save(userAccount);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @Resource
    private WeChatProvider weChatProvider;

    @GetMapping("/test/ast")
    public String getast() {

        String weChatAccessToken = weChatProvider.getWeChatAccessToken();
        return weChatAccessToken;
    }

    @GetMapping("/test/red")
    public void geRed(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.sendRedirect("https://weixin-dev-h9.thy360.com/h9-weixin/#/account/hongbao/result?id=1");

    }

    @Resource
    private JedisTool jedisTool;

    @GetMapping("/lock")
    public String testLock(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long time = System.currentTimeMillis();
        boolean b = jedisTool.tryGetDistributedLock("name", time+"", 2000);
        if (b) {
            boolean b1 = jedisTool.releaseDistributedLock("name", time+"");
            return b + " , " + b1;
        }
        return b+"";
    }
}
