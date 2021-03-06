package com.h9.common.common;

import com.alibaba.fastjson.JSONObject;
import com.h9.common.db.bean.RedisBean;
import com.h9.common.db.bean.RedisKey;
import com.h9.common.db.entity.config.GlobalProperty;
import com.h9.common.db.repo.GlobalPropertyRepository;
import com.h9.common.modle.vo.Config;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:获取全局通用配置项
 * ConfigService:刘敏华 shadow.liu@hey900.com
 * Date: 2017/11/9
 * Time: 11:00
 */
@Component
public class ConfigService {

    public static final String BALANCEFLOWTYPE = "balanceFlowType";
    @Resource
    private RedisBean redisBean;

    private Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private GlobalPropertyRepository globalPropertyRepository;

    @Resource
    private MailService mailService;

    /****
     * 获取code 对应的配置 ，
     * @param code
     * @return  @See List<Config> Map<String,String> String
     */
    public Object getConfig(String code) {
        if (code == null) {
            return null;
        }
        //从缓存里面读取
        Object valueRedis1 = getConfigFromCache(code);
        logger.info("getConfigFromCache : "+valueRedis1);
        if (valueRedis1 != null) return valueRedis1;
        return getConfigFromDb(code);
    }

    public void expireConfig(String code) {
       this.redisBean.expire(RedisKey.getConfigValue(code),1, TimeUnit.MILLISECONDS);
    }

    public String getStringConfig(String code) {
        Object config = getConfig(code);
        if (config instanceof String) {

            String configStr = (String) config;
            if (StringUtils.isBlank(configStr)) {
                mailService.sendtMail("参数未配置", "参数未配置 (key) "+code);
            }

            return configStr;
        } else {
            return null;
        }
    }

    public List<String> getStringListConfig(String code) {
        Object config = getConfig(code);
        if (config instanceof List) {
            return (List<String>) config;
        } else {
            return null;
        }
    }

    public List<Double> getDoubleListConfig(String code) {
        Object config = getConfig(code);
        if (config instanceof List) {
            return (List<Double>) config;
        } else {
            return null;
        }
    }

    public Map<String,String> getMapConfig(String code) {
        logger.info("getMapConfig code: "+code);
        Object config = getConfig(code);
        if (config instanceof Map) {
            return (Map) config;
        } else {
            return null;
        }
    }

    public List<Config> getMapListConfig(String code) {
        Map mapConfig = getMapConfig(code);
        if (mapConfig == null) {
            return null;
        }
        List<Config> configs = new ArrayList<>();
        Set<String> set = mapConfig.keySet();
        for(String key:set){
            configs.add(new Config(key, (String)mapConfig.get(key)));
        }
        return configs;
    }

    public String getConfigVal(List<Config> configList, String key) {
        if (configList == null || configList.size() == 0) {
            return null;
        }
        for (int i = 0; i < configList.size(); i++) {
            if (configList.get(i).getKey().equals(key)) {
                return configList.get(i).getVal();
            }
        }
        return null;
    }

    public  String getValueFromMap(String code,String key){
        Map<String,String> mapConfig = getMapConfig(code);

        if(mapConfig != null){
            return mapConfig.get(key);
        }

        return "";
    }

    private Object getConfigFromDb(String code) {
        GlobalProperty globalProperty = globalPropertyRepository.findByCode(code);
        if (globalProperty != null) {
            String val = globalProperty.getVal();
            String type = globalProperty.getType() + "";
            redisBean.setStringValue(RedisKey.getConfigValue(code), val);
            redisBean.setStringValue(RedisKey.getConfigType(code), type);
            logger.info("getConfigFromDb : "+getValue(type, val));
            return getValue(type, val);
        } else {
            logger.info("getConfigFromDb : null");
            return null;
        }
    }

    private Object getConfigFromCache(String code) {
        String typeRedis = redisBean.getStringValue(RedisKey.getConfigType(code));
        String valueRedis = redisBean.getStringValue(RedisKey.getConfigValue(code));
        if (StringUtils.isNotEmpty(valueRedis) && StringUtils.isNotEmpty(typeRedis)) {
            Object map = getValue(typeRedis, valueRedis);
            if (map != null) return map;
        }
        return null;
    }

    private Object getValue(String typeRedis, String valueRedis) {
        if (typeRedis.equals("0")) {
            return valueRedis;
        }
        if (typeRedis.equals("1")) {
            Map map = JSONObject.parseObject(valueRedis, Map.class);
            return map;
        }
        if (typeRedis.equals("2")) {
            List<String> strings = JSONObject.parseArray(valueRedis, String.class);
            return strings;
        }
        return null;
    }


}

