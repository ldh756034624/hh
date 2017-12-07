package com.h9.common.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:全局配置获取类
 * ConfigHanler:刘敏华 shadow.liu@hey900.com
 * Date: 2017/11/15
 * Time: 14:15
 */
@Service
public class ConfigHanler {
    @Resource
    private ConfigService configService;

    public int getSmsOneDayCount() {
        int defaultValue = 10;
        String dayMaxotteryCount = configService.getStringConfig("sms:OneDay:CountCount");
        try {
            if(StringUtils.isEmpty(dayMaxotteryCount)) defaultValue = Integer.parseInt(dayMaxotteryCount);
        } catch (NumberFormatException e) {
        }
        return defaultValue;
    }
}
