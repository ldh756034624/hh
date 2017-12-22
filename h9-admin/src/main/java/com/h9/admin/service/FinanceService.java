package com.h9.admin.service;

import com.h9.admin.model.dto.finance.WithdrawRecordQueryDTO;
import com.h9.admin.model.vo.LotteryFlowFinanceVO;
import com.h9.common.modle.vo.admin.finance.LotteryFlowRecordVO;
import com.h9.common.db.entity.*;
import com.h9.common.db.repo.*;
import com.h9.common.modle.dto.PageDTO;
import com.h9.common.modle.vo.admin.finance.WithdrawRecordVO;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.common.CommonService;
import com.h9.common.common.ConfigService;
import com.h9.common.constant.Constants;
import com.h9.common.db.basis.JpaRepository;
import com.h9.common.modle.dto.LotteryFLowRecordDTO;
import com.h9.common.modle.dto.LotteryFlowFinanceDTO;
import com.h9.common.utils.DateUtil;
import com.h9.common.utils.HttpUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author: George
 * @date: 2017/11/9 14:38
 */
@Service
@Transactional
public class FinanceService {
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private JpaRepository jpaRepository;
    @Autowired
    private WithdrawalsRecordRepository withdrawalsRecordRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private LotteryFlowRepository lotteryFlowRepository;
    @Autowired
    private LotteryFlowRecordRepository lotteryFlowRecordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfigService configService;
    @Autowired
    private VB2MoneyRepository vb2MoneyRepository;

    public Result<PageResult<WithdrawRecordVO>> getWithdrawRecords(WithdrawRecordQueryDTO withdrawRecordQueryDTO) throws InvocationTargetException, IllegalAccessException {
        /*PageRequest pageRequest = new PageRequest(withdrawRecordQueryDTO.getPageNumber(),withdrawRecordQueryDTO.getPageSize());
        String sql = "select o.* from withdrawals_record o";
        Page<List<Map>> records = this.withdrawalsRecordRepository.findByCondtion(sql,pageRequest);*/
        String sql = this.buildWithdrawRecordQueryString(withdrawRecordQueryDTO);
        List<Map> maps = this.jpaRepository.createNativeQuery(sql,withdrawRecordQueryDTO.getStartIndex(),withdrawRecordQueryDTO.getPageSize());
        long total = this.jpaRepository.nativeCount(sql);
        //解决Apache的BeanUtils对日期的支持不是很好的问题
        ConvertUtils.register(new DateConverter(null),java.util.Date.class);
        List<WithdrawRecordVO> withdrawRecordVOS = WithdrawRecordVO.toWithdrawRecordVOs(maps);
        PageResult<WithdrawRecordVO> pageResult = new PageResult<>(withdrawRecordQueryDTO.getPageNumber(),withdrawRecordQueryDTO.getPageSize(),total,withdrawRecordVOS);
        return  Result.success(pageResult);
    }

    private String buildWithdrawRecordQueryString(WithdrawRecordQueryDTO withdrawRecordQueryDTO){
        StringBuilder sql = new StringBuilder(
                "select w.id,w.order_id as orderId,w.user_id as userId,w.money,w.create_time as createTime,w.finish_time as finishTime,w.status,u.phone" +
                        ",ub.name,ub.no as bankCardNo,ub.provice,ub.city,ut.bank_name as bankName" +
                        " from withdrawals_record w,user u,user_bank ub,bank_type ut,user_record ur where w.user_id=u.id and w.user_bank_id = ub.id and ub.bank_type_id = ut.id and w.user_record_id=ur.id "
        );
        if(!StringUtils.isEmpty(withdrawRecordQueryDTO.getPhone())){
            sql.append(" and u.phone=").append(withdrawRecordQueryDTO.getPhone());
        }
        if(!StringUtils.isEmpty(withdrawRecordQueryDTO.getBankCardNo())){
            sql.append(" and ub.no=").append(withdrawRecordQueryDTO.getBankCardNo());
        }
        if(withdrawRecordQueryDTO.getStatus()!=null&&withdrawRecordQueryDTO.getStatus()!=0){
            sql.append(" and w.status=").append(withdrawRecordQueryDTO.getStatus());
        }
        if(withdrawRecordQueryDTO.getUserId()!=null){
            sql.append(" and w.user_id=").append(withdrawRecordQueryDTO.getUserId());
        }
        sql.append(" order by w.id desc");
        return sql.toString();
    }

    public Result<WithdrawalsRecord> updateWithdrawRecordStatus(long id){
        this.logger.infov("提现退回，订单id:{0}",id);
        WithdrawalsRecord withdrawalsRecord = this.withdrawalsRecordRepository.findByLockId(id);
        if(withdrawalsRecord.getStatus()!=WithdrawalsRecord.statusEnum.WITHDRA_EXPCETION.getCode()){
            return Result.fail("该订单不是提现异常订单");
        }
        Result result = this.commonService.setBalance(withdrawalsRecord.getUserId(),withdrawalsRecord.getMoney(), BalanceFlow.BalanceFlowTypeEnum.RETURN.getId(),
                withdrawalsRecord.getId(),withdrawalsRecord.getOrderNo(),"银联退回");
        if(result.getCode()==Result.FAILED_CODE){
            this.logger.errorf("改变用户余额失败,msg:{0}",result.getMsg());
            return Result.fail("改变用户余额失败");
        }
        withdrawalsRecord.setStatus(WithdrawalsRecord.statusEnum.CANCEL.getCode());
//        this.withdrawalsRecordRepository.save(withdrawalsRecord);
        return Result.success(this.withdrawalsRecordRepository.save(withdrawalsRecord));
    }

    public Result<PageResult<LotteryFlowFinanceVO>> getLotteryFlows(LotteryFlowFinanceDTO lotteryFlowFinanceDTO) throws InvocationTargetException, IllegalAccessException {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        //PageRequest pageRequest = this.lotteryFlowRepository.pageRequest(lotteryFlowFinanceDTO.getPageNumber(), lotteryFlowFinanceDTO.getPageSize(),sort);
        String sql = this.buildLotteryFlowQueryString(lotteryFlowFinanceDTO);
        List<Map> maps = this.jpaRepository.createNativeQuery(sql,lotteryFlowFinanceDTO.getStartIndex(),lotteryFlowFinanceDTO.getPageSize());
        long total = this.jpaRepository.nativeCount(sql);
        //解决Apache的BeanUtils对日期的支持不是很好的问题
        ConvertUtils.register(new DateConverter(null),java.util.Date.class);
        List<LotteryFlowFinanceVO> lotteryFlowFinanceVOS = LotteryFlowFinanceVO.toLotteryFlowFinanceVOs(maps);
        PageResult<LotteryFlowFinanceVO> pageResult = new PageResult<>(lotteryFlowFinanceDTO.getPageNumber(),lotteryFlowFinanceDTO.getPageSize(),total,lotteryFlowFinanceVOS);
        return Result.success(pageResult);
    }



    private String buildLotteryFlowQueryString(LotteryFlowFinanceDTO lotteryFlowFinanceDTO){
        StringBuilder sql = new StringBuilder(
                "select o.* from (select lf.id,lf.money,lf.create_time as createTime,u.nick_name as nickName,u.phone,ua.balance,r.code from lottery_flow lf ,user u,user_account ua,reward r" +
                        " where lf.user_id=u.id and lf.reward_id=r.id and lf.user_id=ua.user_id {0}) as o " +
                        "left join lottery_flow_record lfr on o.id = lfr.lottery_flow_id where lfr.id is null");
        StringBuilder condition  = new StringBuilder("");
        if(!StringUtils.isEmpty(lotteryFlowFinanceDTO.getCode())){
            condition.append(" and r.code='").append(lotteryFlowFinanceDTO.getCode()).append("'");
        }
        if(!StringUtils.isEmpty(lotteryFlowFinanceDTO.getPhone())){
            condition.append(" and u.phone='").append(lotteryFlowFinanceDTO.getPhone()).append("'");
        }
        if(lotteryFlowFinanceDTO.getStartTime()!=null){
            condition.append(" and lf.create_time>='").append(DateUtil.formatDate(lotteryFlowFinanceDTO.getStartTime(),DateUtil.FormatType.SECOND)).append("'");
        }
        if(lotteryFlowFinanceDTO.getEndTime()!=null){
            condition.append(" and lf.create_time<'")
                    .append(DateUtil.formatDate(DateUtil.addDays(lotteryFlowFinanceDTO.getEndTime(),1),DateUtil.FormatType.SECOND))
                    .append("'");
        }
        sql.append(" order by o.id desc");
        return MessageFormat.format(sql.toString(),condition);
    }

    public Result transferFromLotteryFlows(Set<Long> ids){
        List<String> errMsgList = new ArrayList<>();
        LotteryFlowRecord lotteryFlowRecord = new LotteryFlowRecord();
        for(Long id:ids){
            LotteryFlowRecord flowRecord = this.lotteryFlowRecordRepository.findByLotteryFlow_Id(id);
            if(flowRecord!=null){
                errMsgList.add("id为"+id+"的记录已转过账");
                continue;
            }
            LotteryFlow flow = this.lotteryFlowRepository.findByLockId(id);
            Result  result = this.commonService.setBalance(flow.getUser().getId(),flow.getMoney().abs().negate(),BalanceFlow.BalanceFlowTypeEnum.XIAOPINHUI.getId(),
                    flow.getId(), flow.getId().toString(),"小品会");
            flowRecord = new LotteryFlowRecord();
            if(result.getCode()==Result.FAILED_CODE){
                errMsgList.add("手机号:"+flow.getUser().getPhone()+"余额不足");
                flowRecord.setStatus(LotteryFlowRecord.LotteryFlowRecordStatusEnum.FAIL.getId());
            }else{
                flowRecord.setStatus(LotteryFlowRecord.LotteryFlowRecordStatusEnum.SUCCESS.getId());
                long uId = Long.valueOf(this.configService.getStringConfig(Constants.XIAOPINHUI));
                Result r = this.commonService.setBalance(uId,flow.getMoney().abs(),BalanceFlow.BalanceFlowTypeEnum.XIAOPINHUI.getId(),
                        flow.getId(),flow.getId().toString(),"小品会");
               /* if(r.getCode()==Result.FAILED_CODE){
                    r = this.commonService.setBalance(uId,flow.getMoney().abs(),BalanceFlow.BalanceFlowTypeEnum.XIAOPINHUI.getId(),
                            flow.getId(),flow.getId().toString(),"小品会");
                    if(r.getCode()==Result.FAILED_CODE){
                        this.logger.errorf("给小品会转账时出错，lotterFlow.id为{0}",flow.getId());
                        errMsgList.add("收款方修改账户余额出错，lotteryFlow.id为"+flow.getId());
                    }
                }*/
                if(r.getCode()==Result.FAILED_CODE){
                    this.logger.errorf("给小品会转账时出错，lotterFlow.id为{0}",flow.getId());
                    errMsgList.add("收款方修改账户余额出错，lotteryFlow.id为"+flow.getId());
                }
            }
            flowRecord.setLotteryFlow(flow);
            flowRecord.setCode(flow.getReward().getCode());
            flowRecord.setMoney(flow.getMoney());
            flowRecord.setNickName(flow.getUser().getNickName());
            flowRecord.setOuterId(flow.getUser().getId());
            flowRecord.setPhone(flow.getUser().getPhone());
            flowRecord.setTransferTime(flow.getCreateTime());
            long userId = Long.valueOf((String) HttpUtil.getHttpSession().getAttribute("curUserId"));
            flowRecord.setUser(this.userRepository.findOne(userId));
            this.lotteryFlowRecordRepository.save(flowRecord);
        }
        if(errMsgList.size()>0){
            return Result.fail(errMsgList.toString());
        }
        return Result.success("成功");
    }

    public Result<PageResult<LotteryFlowRecordVO>> getLotteryFlowRecords(LotteryFLowRecordDTO lotteryFLowRecordDTO) throws InvocationTargetException, IllegalAccessException {
        String phone = StringUtils.isBlank(lotteryFLowRecordDTO.getPhone()) ? null : lotteryFLowRecordDTO.getPhone();
        String code = StringUtils.isBlank(lotteryFLowRecordDTO.getCode()) ? null : lotteryFLowRecordDTO.getCode();
        Date startTime = lotteryFLowRecordDTO.getStartTime();
        Date endTime = lotteryFLowRecordDTO.getEndTime();
        Integer status = lotteryFLowRecordDTO.getStatus();
        Page<LotteryFlowRecordVO> lotteryFlowRecordPage = this.lotteryFlowRecordRepository.findByCondition(phone, code,
                startTime, endTime, status,lotteryFLowRecordDTO.toPageRequest());
        return Result.success(new PageResult<>(lotteryFlowRecordPage));
    }

    public Result transferFromLotteryFlowRecord(long id){

        LotteryFlowRecord lotteryFlowRecord = this.lotteryFlowRecordRepository.findByLockId(id);
        if(lotteryFlowRecord.getStatus()!=LotteryFlowRecord.LotteryFlowRecordStatusEnum.FAIL.getId()){
            return Result.fail("该记录不为转账失败记录");
        }
        LotteryFlow lotteryFlow =lotteryFlowRecord.getLotteryFlow();
        Result  result = this.commonService.setBalance(lotteryFlow.getUser().getId(),lotteryFlow.getMoney().abs().negate(),BalanceFlow.BalanceFlowTypeEnum.XIAOPINHUI.getId(),
                lotteryFlow.getId(), lotteryFlow.getId().toString(),"小品会");
        if(result.getCode()==Result.FAILED_CODE){
            return Result.fail("余额不足，转账失败");
        }
        long uId = Long.valueOf(this.configService.getStringConfig(Constants.XIAOPINHUI));
        Result r = this.commonService.setBalance(uId,lotteryFlowRecord.getMoney().abs(),BalanceFlow.BalanceFlowTypeEnum.XIAOPINHUI.getId(),
                lotteryFlow.getId(),lotteryFlow.getId().toString(),"小品会");
        if(r.getCode()==Result.FAILED_CODE){
            this.logger.errorf("给小品会转账时出错，lotterFlow.id为{0}",lotteryFlow.getId());
        }
        lotteryFlowRecord.setStatus(LotteryFlowRecord.LotteryFlowRecordStatusEnum.SUCCESS.getId());
        this.lotteryFlowRecordRepository.save(lotteryFlowRecord);
        return Result.success("成功");
    }

    public Result<PageResult<VB2Money>> listVB2Money(String phone, PageDTO pageDTO) {
        if (StringUtils.isBlank(phone)) {
            phone = null;
        }
        Page<VB2Money> vb2MoneyPage = this.vb2MoneyRepository.findByTel(phone,pageDTO.toPageRequest());
        return Result.success(new PageResult<>(vb2MoneyPage));
    }

}
