package com.h9.admin.service;

import com.h9.admin.model.dto.finance.WithdrawRecordQueryDTO;
import com.h9.admin.model.vo.WithdrawRecordVO;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.common.CommonService;
import com.h9.common.db.basis.JpaRepository;
import com.h9.common.db.entity.BalanceFlowType;
import com.h9.common.db.entity.BannerType;
import com.h9.common.db.entity.WithdrawalsRecord;
import com.h9.common.db.repo.WithdrawalsRecordRepository;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

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

    public Result<PageResult<WithdrawRecordVO>> getWithdrawRecords(WithdrawRecordQueryDTO withdrawRecordQueryDTO) throws InvocationTargetException, IllegalAccessException {
        /*PageRequest pageRequest = new PageRequest(withdrawRecordQueryDTO.getPageNumber(),withdrawRecordQueryDTO.getPageSize());
        String sql = "select o.* from withdrawals_record o";
        Page<List<Map>> records = this.withdrawalsRecordRepository.findByCondtion(sql,pageRequest);*/
        String sql = this.buildWithdrawRecordQueryString(withdrawRecordQueryDTO);
        List<Map> maps = this.jpaRepository.createNativeQuery(sql,withdrawRecordQueryDTO.getPageNumber()-1,withdrawRecordQueryDTO.getPageSize());
        long total = this.jpaRepository.nativeCount(sql);
        //解决Apache的BeanUtils对日期的支持不是很好的问题
        ConvertUtils.register(new DateConverter(null),java.util.Date.class);
        List<WithdrawRecordVO> withdrawRecordVOS = WithdrawRecordVO.toWithdrawRecordVOs(maps);
        PageResult<WithdrawRecordVO> pageResult = new PageResult<>(withdrawRecordQueryDTO.getPageNumber(),withdrawRecordQueryDTO.getPageSize(),total,withdrawRecordVOS);
        return  Result.success(pageResult);
    }

    private String buildWithdrawRecordQueryString(WithdrawRecordQueryDTO withdrawRecordQueryDTO){
        StringBuilder sql = new StringBuilder(
                "select w.id,w.user_id as userId,w.money,w.create_time as createTime,w.finish_time as finishTime,w.status,u.phone" +
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
        sql.append(" order by w.id desc");
        return sql.toString();
    }

    public Result<WithdrawalsRecord> updateWithdrawRecordStatus(long id){
        this.logger.infov("提现退回，订单id:{0}",id);
        WithdrawalsRecord withdrawalsRecord = this.withdrawalsRecordRepository.findByLockId(id);
        if(withdrawalsRecord.getStatus()==WithdrawalsRecord.statusEnum.FAIL.getCode()){
            return Result.fail("该订单不是失败订单");
        }
        Result result = this.commonService.setBalance(withdrawalsRecord.getUserId(),withdrawalsRecord.getMoney(), BalanceFlowType.BalanceFlowTypeEnum.RETURN.getId(),
                withdrawalsRecord.getId(),withdrawalsRecord.getOrderNo(),"银联退回");
        if(result.getCode()==Result.FAILED_CODE){
            this.logger.errorf("改变用户余额失败,msg:{0}",result.getMsg());
            return Result.fail("改变用户余额失败");
        }
        withdrawalsRecord.setStatus(WithdrawalsRecord.statusEnum.CANCEL.getCode());
        this.withdrawalsRecordRepository.save(withdrawalsRecord);
        return Result.success(this.withdrawalsRecordRepository.save(withdrawalsRecord));
    }

}
