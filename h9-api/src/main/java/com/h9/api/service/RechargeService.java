package com.h9.api.service;

import com.h9.api.model.dto.OrderDTO;
import com.h9.api.model.vo.OrderVo;
import com.h9.api.model.vo.PayVO;
import com.h9.api.provider.PayProvider;
import com.h9.common.base.Result;
import com.h9.common.db.entity.PayInfo;
import com.h9.common.db.entity.RechargeOrder;
import com.h9.common.db.entity.User;
import com.h9.common.db.repo.PayInfoRepository;
import com.h9.common.db.repo.RechargeOrderRepository;
import com.h9.common.db.repo.UserRepository;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * RechargeService:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/11
 * Time: 17:53
 */
@Component
public class RechargeService {

     Logger logger = Logger.getLogger(RechargeService.class);

    @Resource
    private RechargeOrderRepository rechargeOrderRepository;

    @Resource
    private PayInfoRepository payInfoRepository;

    @Resource
    private PayProvider payProvider;

    @Resource
    private UserRepository userRepository;

    @Transactional
    public Result recharge(Long userId, BigDecimal money){
        if(money == null){
            return Result.fail("请填入要充值的金额");
        }
        if(money.compareTo(new BigDecimal(0))<=0){
            return Result.fail("请填入正确的充值金额");
        }

        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setUser_id(userId);
        rechargeOrder.setMoney(money);
        rechargeOrder = rechargeOrderRepository.saveAndFlush(rechargeOrder);


        PayInfo payInfo = new PayInfo();
        payInfo.setMoney(rechargeOrder.getMoney());
        payInfo.setOrderId(rechargeOrder.getId());
        payInfo.setOrderType(PayInfo.OrderTypeEnum.Recharge.getId());
        payInfo = payInfoRepository.save(payInfo);

        User user = userRepository.findOne(userId);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBusinessOrderId(payInfo.getId());
        orderDTO.setTotalAmount(money);
        orderDTO.setOpenId(user.getOpenId());
        logger.debugv("开始支付");
        Result<OrderVo> result = payProvider.initOrder(orderDTO);
        if(!result.isSuccess()){
            return result;
        }

        OrderVo orderVo = result.getData();
        String pay = payProvider.goPay(orderVo.getPayOrderId());
        PayVO payVO = new PayVO();
        payVO.setPayOrderId(orderVo.getPayOrderId());
        payVO.setPayUrl(pay);
        return Result.success();
    }


}
