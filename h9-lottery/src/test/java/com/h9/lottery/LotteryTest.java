package com.h9.lottery;

import com.alibaba.fastjson.JSONObject;
import com.h9.common.common.ConfigService;
import com.h9.common.db.entity.lottery.Activity;
import com.h9.common.db.entity.lottery.Product;
import com.h9.common.db.entity.lottery.Reward;
import com.h9.common.db.repo.*;
import com.h9.common.utils.MD5Util;
import com.h9.lottery.provider.FactoryProvider;
import com.h9.lottery.utils.CodeUtil;
import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

import static org.jboss.logging.Logger.getLogger;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * LotteryTest:刘敏华 shadow.liu@hey900.com
 * Date: 2017/11/2
 * Time: 14:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TransactionConfiguration(defaultRollback=false)
public class LotteryTest {
     Logger logger = getLogger(LotteryTest.class);

    @Resource
    private RewardRepository rewardRepository;
    @Resource
    private ActivityRepository activityRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private ConfigService configService;
    
    @Resource
    private FactoryProvider factoryProvider;

    @Resource
    private ProductLogRepository productLogRepository;

//
////    @Test
//    @Transactional
//    public void testContextLoads() {
//        Date date = DateUtil.formatDate("2017-11-15 14:57:12", DateUtil.FormatType.SECOND);
//        long byUserId = productLogRepository.findByUserId(28L, date);
//        logger.debugv("byUserId"+byUserId);
//    }
    


////    @Test
//    @Transactional
//    public void contextLoads() {
//
////        LotteryModel pr = factoryProvider.findByLotteryModel("6123456973");
////        logger.debugv(JSONObject.toJSONString(pr));
//
//        generateCode();
//
////        List<String> lotteryRemark = configService.getStringListConfig(ParamConstant.LOTTERY_REMARK);
////        logger.debugv(JSONObject.toJSONString(lotteryRemark));
////        Map profileJob = configService.getMapConfig("profileJob");
////        logger.debugv(JSONObject.toJSONString(profileJob));
////        List<Config> profileJob1 = configService.getMapListConfig("profileJob");
////        logger.debugv(JSONObject.toJSONString(profileJob1));
////
////        String withdrawMax = configService.getStringConfig("withdrawMax");
////        logger.debugv(JSONObject.toJSONString(withdrawMax));
//    }

    @Test
    @Transactional
    public void generateCode() {
        Activity one = activityRepository.findOne(8L);
        Product product = productRepository.findOne(1L);
        for(int i=0;i<=50;i++) {
            try {
                Reward reward = new Reward();
                reward.setMoney(new BigDecimal(18));
                String uuid = UUID.randomUUID().toString();
                String shortUrl = CodeUtil.shortUrl(uuid, CodeUtil.genRandomStrCode(8));
                reward.setCode(shortUrl);
                logger.debugv(shortUrl);
                reward.setActivityId(1L);
                reward.setMd5Code(MD5Util.getMD5(shortUrl));
                Reward reward1 = rewardRepository.saveAndFlush(reward);
                logger.debugv(shortUrl+ " " + JSONObject.toJSONString(reward1));
            } catch (Exception e) {
                logger.debug(e.getMessage(),e);;
            } finally {
            }
        }
        logger.debugv("完成");
    }

//    @Resource
//    private ProductTypeRepository productTypeRepository;
//
//    @Test
//    public void productType(){
//        List<Product> products = productRepository.findAll();
//        for (Product product : products) {
//            ProductType productType = productTypeRepository.findOrNew(product.getName());
//            product.setProductType(productType);
//            productRepository.saveAndFlush(product);
//        }
//    }


}
