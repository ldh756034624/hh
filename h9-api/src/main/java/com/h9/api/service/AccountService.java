package com.h9.api.service;

import com.h9.api.model.vo.BalanceFlowVO;
import com.h9.api.model.vo.MyCouponsVO;
import com.h9.api.model.vo.UserAccountInfoVO;
import com.h9.api.model.vo.VbconvertVO;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.common.CommonService;
import com.h9.common.common.ConfigService;
import com.h9.common.common.MailService;
import com.h9.common.db.entity.*;
import com.h9.common.db.repo.*;
import com.h9.common.utils.DateUtil;
import com.h9.common.utils.MoneyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:账号
 * BalanceService:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/30
 * Time: 16:50
 */
@Component
public class AccountService {
    @Resource
    private BalanceFlowRepository balanceFlowRepository;
    @Resource
    private VCoinsFlowRepository vCoinsFlowRepository;
    @Resource
    private UserAccountRepository userAccountRepository;
    @Resource
    private OrderItemReposiroty orderItemReposiroty;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserBankRepository userBankRepository;
    @Resource
    private OrdersRepository ordersReposiroty;
    @Resource
    private GlobalPropertyRepository globalPropertyRepository;
    @Resource
    private ConfigService configService;
    @Resource
    private MailService mailService;
    @Resource
    private CommonService commonService;
    @Resource
    private OrderService orderService;
    private Logger logger = Logger.getLogger(this.getClass());


    public Result getBalanceFlow(Long userId, int page, int limit) {

        PageRequest pageRequest = balanceFlowRepository.pageRequest(page, limit);
        Page<BalanceFlow> balanceFlows = balanceFlowRepository.findByBalance(userId, pageRequest);
        PageResult<BalanceFlow> flowPageResult = new PageResult<>(balanceFlows);

//        GlobalProperty val = globalPropertyRepository.findByCode("balanceFlowImg");
//        Map iconMap = JSONObject.parseObject(val.getVal(), Map.class);

        Map iconMap = configService.getMapConfig("balanceFlowImg");

        return Result.success(flowPageResult.result2Result(bf -> new BalanceFlowVO(bf, iconMap)));

    }

    public Result getVCoinsFlow(Long userId, int page, int limit) {
        PageRequest pageRequest = balanceFlowRepository.pageRequest(page, limit);
        Page<VCoinsFlow> balanceFlows = vCoinsFlowRepository.findByBalance(userId, pageRequest);
        PageResult<VCoinsFlow> flowPageResult = new PageResult<>(balanceFlows);

        Map iconMap = configService.getMapConfig("balanceFlowImg");

        return Result.success(flowPageResult.result2Result(bc -> new BalanceFlowVO(bc, iconMap)));
    }

    public BigDecimal getAccountBalance(Long userId) {
        UserAccount userAccount = userAccountRepository.findOne(userId);
        if (userAccount == null) return new BigDecimal(0);

        return userAccount.getBalance();
    }

    public Result accountInfo(Long userId) {
        UserAccount userAccount = userAccountRepository.findByUserId(userId);
        User user = userRepository.findOne(userId);
        Object cardCount = orderItemReposiroty.findCardCount(userId);
        String max = configService.getStringConfig("withdrawMax");
        if (StringUtils.isBlank(max)) {
            max = "100";
            logger.error("没有找到提现最大值参数，默认为: " + max);
        }
        UserAccountInfoVO userAccountInfoVO = new UserAccountInfoVO(user, userAccount, cardCount + "", user.getPhone(), max);
        return Result.success(userAccountInfoVO);
    }

    public Result couponeList(Long userId, int page, int limit) {

        PageRequest pageRequest = orderItemReposiroty.pageRequest(page, limit);
        Page<Orders> orders = ordersReposiroty.findDiDiCardByUser(userId, pageRequest);

        return Result.success(new PageResult<>(orders).result2Result(ord -> {

            List<OrderItems> list = ord.getOrderItems();
            if (!CollectionUtils.isEmpty(list)) {
                OrderItems orderItems = list.get(0);
                MyCouponsVO myCouponsVO = new MyCouponsVO(orderItems);
                return myCouponsVO;
            }
            return null;
        }));

    }

    public Result convertInfo(Long userId) {

        UserAccount userAccount = userAccountRepository.findByUserId(userId);

        String endTime = configService.getStringConfig("h9:api:vb:endTime");

        BigDecimal vbCount = userAccount.getvCoins();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = null;
        try {
            endDate = dataFormat.parse(endTime);
        } catch (ParseException e) {
            logger.info(e.getMessage(), e);
        }

        String rateStr = configService.getStringConfig("h9:api:vb2JiuYuan");

        BigDecimal JiuYuan = vbCount.multiply(new BigDecimal(rateStr));

        VbconvertVO vo = new VbconvertVO()
                .setEndTimeTip(DateUtil.formatDate(endDate, DateUtil.FormatType.MINUTE))
                .setJiuYuan(MoneyUtils.formatMoney(JiuYuan))
                .setVb(MoneyUtils.formatMoney(vbCount) + "")
                .setJiuYuanIcon("");

        return Result.success(vo);
    }

    @Transactional
    public Result vbConvert(Long userId) {

        UserAccount userAcount = userAccountRepository.findByUserId(userId);
        User user = userRepository.findOne(userId);
        BigDecimal vbCount = userAcount.getvCoins();

        if(vbCount.compareTo(new BigDecimal(0)) <= 0){
            return Result.fail("您的v币余额已不足");
        }

        String rateStr = configService.getStringConfig("h9:api:vb2JiuYuan");
        BigDecimal money = vbCount.multiply(new BigDecimal(rateStr));
        Orders order = orderService.initOrder( money, user.getPhone(), Orders.orderTypeEnum.VIRTUAL_GOODS.getCode()+"", "徽酒",user);
        ordersReposiroty.saveAndFlush(order);

        Result result = commonService.setBalance(userId, money, 11L, order.getId(), "", "");
        if (result.getCode() == 1) {
            throw new RuntimeException("转换酒元异常");
        }
        //vb流水
        VCoinsFlow vCoinsFlow = generateVBflowObj(userId, new BigDecimal(0), vbCount, order.getId(),11L);
        vCoinsFlowRepository.save(vCoinsFlow);
        userAcount.setvCoins(new BigDecimal(0));
        return Result.success("兑换成功");

    }

    public VCoinsFlow generateVBflowObj(Long userId, BigDecimal balance, BigDecimal money, Long orderId,Long flowType){
        VCoinsFlow vCoinsFlow = new VCoinsFlow();
        vCoinsFlow.setBalance(balance);
        vCoinsFlow.setMoney(money);
        vCoinsFlow.setOrderId(orderId);
        vCoinsFlow.setUserId(userId);
        vCoinsFlow.setRemarks("vb转酒元");
        vCoinsFlow.setOrderNo(orderId+"");
        vCoinsFlow.setvCoinsflowType(flowType);
        return vCoinsFlow;
    }

}
