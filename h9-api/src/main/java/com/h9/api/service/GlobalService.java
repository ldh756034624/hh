package com.h9.api.service;

import com.h9.common.base.Result;
import com.h9.common.db.entity.config.GlobalProperty;
import com.h9.common.db.repo.GlobalPropertyRepository;

import org.springframework.stereotype.Service;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by 李圆 on 2017/12/23
 */
@Service
public class GlobalService {


    @Resource
    GlobalPropertyRepository globalPropertyRepository;

    @Transactional
    public Result version(Integer client) {
        HashMap map = new HashMap();
        // 安卓
        if (client == 1) {
            GlobalProperty globalProperty = globalPropertyRepository.findByCode("androidDownload");
            System.out.println(globalProperty.getVal());
            map.put("redirect",globalProperty.getVal());
            return Result.success(map);
        }
        // IOS
        if (client == 2) {
            GlobalProperty globalProperty1 = globalPropertyRepository.findByCode("iosDownload");
            map.put("redirect",globalProperty1.getVal());
            return Result.success(map);
        }
        return Result.fail("请求失败，接口调用出错");
    }
}
