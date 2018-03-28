package com.h9.admin.service;

import com.h9.admin.model.dto.AddBigRichDTO;
import com.h9.admin.model.dto.AddWinnerUserDTO;
import com.h9.admin.model.vo.BigRichListVO;
import com.h9.admin.model.vo.LotteryFlowActivityVO;
import com.h9.common.db.entity.lottery.Lottery;
import com.h9.common.db.entity.lottery.OrdersLotteryActivity;
import com.h9.common.db.entity.lottery.WinnerOptRecord;
import com.h9.common.db.entity.user.User;
import com.h9.common.db.repo.*;
import com.h9.common.modle.dto.LotteryFlowActivityDTO;
import com.h9.common.modle.dto.RewardQueryDTO;
import com.h9.admin.model.vo.RewardVO;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.db.entity.lottery.Reward;
import com.h9.common.utils.DateUtil;
import com.h9.common.utils.MobileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: George
 * @date: 2017/11/7 19:40
 */
@Service
@Transactional
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LotteryFlowRepository lotteryFlowRepository;
    @Autowired
    private LotteryRepository lotteryRepository;
    @Autowired
    private OrdersLotteryActivityRep ordersLotteryActivityRep;
    @Resource
    private UserService userService;
    @Resource
    private WinnerOptRecordRep winnerOptRecordRep;


    public Result<PageResult<RewardVO>> getRewards(RewardQueryDTO rewardQueryDTO) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = this.rewardRepository.pageRequest(rewardQueryDTO.getPageNumber(), rewardQueryDTO.getPageSize(), sort);
        Page<Reward> rewards = this.rewardRepository.findAllReward(rewardQueryDTO, pageRequest);
        PageResult<Reward> pageResult = new PageResult<>(rewards);
        return Result.success(this.toRewardVO(pageResult));
    }

    private PageResult<RewardVO> toRewardVO(PageResult rewards) {
        List<RewardVO> rewardVOList = new ArrayList<>();
        for (Reward reward : (List<Reward>) rewards.getData()) {
            rewardVOList.add(RewardVO.toRewardVO(reward));
        }
        rewards.setData(rewardVOList);
        return rewards;
    }

    public Result<PageResult<LotteryFlowActivityVO>> getLotteryFlows(LotteryFlowActivityDTO lotteryFlowActivityDTO) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<Lottery> lotteryFlows = this.lotteryRepository.findAll(this.lotteryRepository
                .buildActivitySpecification(lotteryFlowActivityDTO), lotteryFlowActivityDTO.toPageRequest(sort));
        PageResult<Lottery> pageResult = new PageResult<>(lotteryFlows);
        return Result.success(pageResult.result2Result(LotteryFlowActivityVO::new));
    }


    /**
     * 新增大富贵期数
     *
     * @param addBigRichDTO
     * @return
     */
    public Result addBigRichActivity(AddBigRichDTO addBigRichDTO) {
        Date endTime = new Date(addBigRichDTO.getEndTime());
        Date startTime = new Date(addBigRichDTO.getStartTime());
        Date startLotteryTime = new Date(addBigRichDTO.getStartLotteryTime());

        if (startLotteryTime.getTime() < startTime.getTime()) {
            return Result.fail("请填写正确的开奖时间");
        }

        if (endTime.getTime() < startTime.getTime()) {
            return Result.fail("请填写正确时间");
        }

        List<OrdersLotteryActivity> byDate1 = ordersLotteryActivityRep.findByDate(startTime);
        List<OrdersLotteryActivity> byDate2 = ordersLotteryActivityRep.findByDate(endTime);

        if (CollectionUtils.isNotEmpty(byDate1) || CollectionUtils.isNotEmpty(byDate2)) {
            return Result.fail("设置的活动区间不能与已有的时间区间重复");
        }

        OrdersLotteryActivity ordersLotteryActivity = getOrdersLotteryActivity(addBigRichDTO);
        ordersLotteryActivityRep.save(ordersLotteryActivity);
        return Result.success();
    }

    public OrdersLotteryActivity getOrdersLotteryActivity(AddBigRichDTO addBigRichDTO) {

        OrdersLotteryActivity ordersLotteryActivity = new OrdersLotteryActivity();
        ordersLotteryActivity.setStartTime(new Date(addBigRichDTO.getStartTime()));
        ordersLotteryActivity.setEndTime(new Date(addBigRichDTO.getEndTime()));
        Long startLotteryTime = addBigRichDTO.getStartLotteryTime();
        String number = DateUtil.formatDate(new Date(startLotteryTime), DateUtil.FormatType.NON_SEPARATOR_DAY);
        ordersLotteryActivity.setNumber(number);
        ordersLotteryActivity.setStatus(addBigRichDTO.getStatus());
        ordersLotteryActivity.setStartLotteryTime(new Date(startLotteryTime));

        return ordersLotteryActivity;
    }

    /**
     * 大富贵活动期数列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Result bigRichList(Integer pageNumber, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = ordersLotteryActivityRep.pageRequest(pageNumber, pageSize, sort);
        Page<OrdersLotteryActivity> page = ordersLotteryActivityRep.findByStatus(1, pageRequest);

        PageResult<BigRichListVO> mapVO = new PageResult<>(page).map(activity -> {
            Map<Long, BigDecimal> userMap = activity.getWinnerUser();
            return new BigRichListVO(activity, getFromMap(userMap));
        });

        return Result.success(mapVO);
    }

    /**
     * 获得map中的第的元素
     *
     * @param userMap
     * @return
     */
    public User getFromMap(Map<Long, BigDecimal> userMap) {

        if (userMap == null) return null;
        Set<Long> keySet = userMap.keySet();
        for (Long id : keySet) {
            User user = userRepository.findOne(id);
            if (user != null) {
                return user;
            }
        }
        return null;
    }

    private Logger logger = Logger.getLogger(this.getClass());

    public Result addWinnerUser(AddWinnerUserDTO addWinnerUserDTO, Long userId) {

        String phone = addWinnerUserDTO.getPhone();
        Long activityId = addWinnerUserDTO.getActivityId();

        if (!MobileUtils.isMobileNO(phone)) return Result.fail("请填写正确的手机号码");
        User user = userRepository.findByPhone(phone);

        if (user == null) {
            user = userService.registerByPhone(phone);
        }

        OrdersLotteryActivity activity = ordersLotteryActivityRep.findOne(activityId);
        if (activity == null) {
            logger.info("id 为 " + activityId + " 的期数不存在");
            return Result.fail("期数不存在");
        }

        Map<Long, BigDecimal> userMap = new HashMap<>();

        BigDecimal money = addWinnerUserDTO.getMoney();

        userMap.put(user.getId(), money.abs());

        ordersLotteryActivityRep.saveAndFlush(activity);
        //记录添加 中奖人 操作日志
        WinnerOptRecord winnerOptRecord = new WinnerOptRecord(null, activity.getId(), user.getId(), userId);
        winnerOptRecordRep.save(winnerOptRecord);

        return Result.success();
    }

}
