package com.h9.admin.service;

import com.h9.admin.model.dto.basis.*;
import com.h9.admin.model.vo.FundsInfo;
import com.h9.common.common.CommonService;
import com.h9.common.db.bean.RedisBean;
import com.h9.common.db.bean.RedisKey;
import com.h9.common.db.entity.account.BankType;
import com.h9.common.db.entity.account.RechargeRecord;
import com.h9.common.db.entity.config.GlobalProperty;
import com.h9.common.db.entity.config.Image;
import com.h9.common.db.entity.config.Version;
import com.h9.common.db.entity.config.WhiteUserList;
import com.h9.common.db.entity.order.Orders;
import com.h9.common.db.entity.user.User;
import com.h9.common.db.entity.user.UserAccount;
import com.h9.common.db.entity.withdrawals.WithdrawalsRecord;
import com.h9.common.modle.vo.Config;
import com.h9.common.modle.vo.admin.basis.*;
import com.h9.admin.model.vo.StatisticsItemVO;
import com.h9.common.common.ConfigService;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.db.repo.*;
import com.h9.common.modle.dto.PageDTO;
import com.h9.common.utils.MD5Util;
import com.h9.common.utils.MoneyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

import static com.h9.common.db.entity.order.Orders.PayMethodEnum.*;
import static com.h9.common.db.entity.withdrawals.WithdrawalsRecord.statusEnum.FINISH;

/**
 * @author: George
 * @date: 2017/11/5 14:34
 */
@Service
@Transactional
public class BasisService {

    @Resource
    private GlobalPropertyRepository globalPropertyRepository;
    @Resource
    private BankTypeRepository bankTypeRepository;
    @Resource
    private LotteryFlowRepository lotteryFlowRepository;
    @Resource
    private WithdrawalsRecordRepository withdrawalsRecordRepository;
    @Resource
    private UserAccountRepository userAccountRepository;
    @Resource
    private VCoinsFlowRepository vCoinsFlowRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private ConfigService configService;
    @Resource
    private ImageRepository imageRepository;
    @Resource
    private VersionRepository versionRepository;
    @Resource
    private WhiteUserListRepository whiteUserListRepository;
    @Resource
    private VB2MoneyRepository vb2MoneyRepository;
    @Resource
    private RedisBean redisBean;
    @Resource
    private OrdersRepository ordersRepository;
    @Resource
    private OrderItemReposiroty orderItemReposiroty;
    @Resource
    private RechargeRecordRepository rechargeRecordRepository;

    public static final String IMAGE_FOLDER = "imageFolder";


    public Result<GlobalPropertyVO> addGlobalProperty(GlobalProperty globalProperty) {
        if (this.globalPropertyRepository.findByCode(globalProperty.getCode()) != null) {

            return Result.fail("标识已存在");
        }
        GlobalProperty saveGLo = globalPropertyRepository.saveAndFlush(globalProperty);
        gloToRedis(saveGLo);
        return Result.success(GlobalPropertyVO.toGlobalPropertyVO(saveGLo));
    }

    public void gloToRedis(GlobalProperty globalProperty) {
        String code = globalProperty.getCode();
        redisBean.setStringValue(RedisKey.getConfigValue(code), globalProperty.getVal());
        redisBean.setStringValue(RedisKey.getConfigType(code), globalProperty.getType() + "");
    }

    public Result<GlobalPropertyVO> updateGlobalProperty(GlobalPropertyEditDTO globalPropertyEditDTO) {
        GlobalProperty globalProperty = this.globalPropertyRepository.findOne(globalPropertyEditDTO.getId());
        if (globalProperty == null) {
            return Result.fail("参数不存在");
        }
        if (this.globalPropertyRepository.findByIdNotAndCode(globalPropertyEditDTO.getId(), globalPropertyEditDTO.getCode()) != null) {
            return Result.fail("标识已存在");
        }
        BeanUtils.copyProperties(globalPropertyEditDTO, globalProperty);
        this.configService.expireConfig(globalPropertyEditDTO.getCode());
        GlobalProperty saveGlo = this.globalPropertyRepository.save(globalProperty);
        gloToRedis(saveGlo);
        return Result.success(GlobalPropertyVO.toGlobalPropertyVO(saveGlo));
    }

    public Result<PageResult<GlobalPropertyVO>> getGlobalProperties(String key, PageDTO pageDTO) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        //PageRequest pageRequest = this.globalPropertyRepository.pageRequest(pageDTO.getPageNumber(),pageDTO.getPageSize());
        Page<GlobalProperty> globalProperties = this.globalPropertyRepository.findAll(this.globalPropertyRepository
                .buildActivitySpecification(key), pageDTO.toPageRequest(sort));
        Page<GlobalPropertyVO> globalPropertyVOPage = GlobalPropertyVO.toGlobalPropertyVO(globalProperties);
        PageResult<GlobalPropertyVO> pageResult = new PageResult<>(globalPropertyVOPage);
        return Result.success(pageResult);
    }

    public Result deleteGlobalProperty(long globalPropertyId) {
        this.globalPropertyRepository.delete(globalPropertyId);
        return Result.success();
    }

    public Result<BankType> addBankType(BankTypeAddDTO bankTypeAddDTO) {
        if (this.bankTypeRepository.findByBankName(bankTypeAddDTO.getBankName()) != null) {
            return Result.fail("银行已存在");
        }
        return Result.success(this.bankTypeRepository.save(bankTypeAddDTO.toBankType()));
    }

    public Result<BankType> updateBankType(BankTypeEditDTO bankTypeEditDTO) {
        if (this.bankTypeRepository.findByIdNotAndBankName(bankTypeEditDTO.getId(), bankTypeEditDTO.getBankName()) != null) {
            return Result.fail("银行已存在");
        }
        BankType bankType = this.bankTypeRepository.findOne(bankTypeEditDTO.getId());
        if (bankType == null) {
            return Result.fail("银行不存在");
        }
        BeanUtils.copyProperties(bankTypeEditDTO, bankType);
        return Result.success(this.bankTypeRepository.save(bankType));
    }

    public Result<BankType> updateBankTypeStatus(long id) {
        BankType bankType = this.bankTypeRepository.findOne(id);
        if (bankType == null) {
            return Result.fail("银行不存在");
        }
        if (bankType.getStatus() == BankType.StatusEnum.DISABLED.getId()) {
            bankType.setStatus(BankType.StatusEnum.ENABLED.getId());
        } else {
            bankType.setStatus(BankType.StatusEnum.DISABLED.getId());
        }
        return Result.success(this.bankTypeRepository.save(bankType));
    }

    public Result<PageResult<BankType>> getBankTypes(PageDTO pageDTO) {
        PageRequest pageRequest = this.bankTypeRepository.pageRequest(pageDTO.getPageNumber(), pageDTO.getPageSize());
        Page<BankType> bankTypes = this.bankTypeRepository.findAllByPage(pageRequest);
        PageResult<BankType> pageResult = new PageResult<>(bankTypes);
        return Result.success(pageResult);
    }

    public Result statistics() {
        BigDecimal lotteryCount = lotteryFlowRepository.getLotteryCount();
        BigDecimal withdrawalsCount = withdrawalsRecordRepository.getWithdrawalsCount(FINISH.getCode());
        BigDecimal userVCoins = userAccountRepository.getUserVCoins();
        BigDecimal totalVCoins = vCoinsFlowRepository.getGrantVCoins();
        BigDecimal totalExchangeVB = this.vb2MoneyRepository.sumVB();
        BigDecimal totalExchangeMoney = this.vb2MoneyRepository.sumMoney();
        lotteryCount = lotteryCount == null ? BigDecimal.ZERO : lotteryCount;
        withdrawalsCount = withdrawalsCount == null ? BigDecimal.ZERO : withdrawalsCount;
        userVCoins = userVCoins == null ? BigDecimal.valueOf(0) : userVCoins;
        totalVCoins = totalVCoins == null ? BigDecimal.valueOf(0) : totalVCoins;
        totalExchangeVB = totalExchangeVB == null ? BigDecimal.ZERO : totalExchangeVB;
        totalExchangeMoney = totalExchangeMoney == null ? BigDecimal.ZERO : totalExchangeMoney;
        String vbExchange = totalExchangeVB.setScale(2, BigDecimal.ROUND_HALF_UP) + " → " +
                totalExchangeMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        List<StatisticsItemVO> list = new ArrayList<>();
        list.add(new StatisticsItemVO("奖金", lotteryCount.setScale(2, BigDecimal.ROUND_HALF_UP), "总奖金（元）"));
        list.add(new StatisticsItemVO("提现金额", withdrawalsCount.setScale(2, BigDecimal.ROUND_HALF_UP), "总提现奖金（元）"));
        list.add(new StatisticsItemVO("V币", totalVCoins.setScale(2, BigDecimal.ROUND_HALF_UP), "总V币"));
        list.add(new StatisticsItemVO("剩余V币", userVCoins.setScale(2, BigDecimal.ROUND_HALF_UP), "剩余V币总量"));
        list.add(new StatisticsItemVO("V币兑换酒元", vbExchange, "V币兑换数量"));
        return Result.success(list);
    }

    public Result addUser(SystemUserAddDTO systemUserAddDTO) {
        User user = this.userRepository.findByPhone(systemUserAddDTO.getPhone());
        if (user != null) {
            if (user.getIsAdmin() == User.IsAdminEnum.ADMIN.getId()) {
                return Result.fail("用户已存在");
            } else {
                user.setStatus(User.IsAdminEnum.ADMIN.getId());
                this.userRepository.save(systemUserAddDTO.toUser(user));
            }
        } else {
            User u = this.userRepository.save(systemUserAddDTO.toUser(user));
            UserAccount userAccount = new UserAccount();
            userAccount.setBalance(BigDecimal.ZERO);
            userAccount.setUserId(u.getId());
            userAccount.setvCoins(BigDecimal.ZERO);
            this.userAccountRepository.save(userAccount);
        }
        return Result.success("成功");
    }

    public Result updateUser(SystemUserEditDTO systemUserEditDTO) {
        User user = this.userRepository.findOne(systemUserEditDTO.getId());
        if (user == null) {
            return Result.fail("用户不存在");
        }
        if (user.getIsAdmin() != User.IsAdminEnum.ADMIN.getId()) {
            return Result.fail("该用户不是后台用户");
        }
        user.setPassword(MD5Util.getMD5(systemUserEditDTO.getPassword()));
        user.setNickName(systemUserEditDTO.getNickName());
        user.setStatus(systemUserEditDTO.getStatus());
        this.userRepository.save(user);
        return Result.success("成功");
    }

    public Result updateUserStatus(long id) {
        User user = this.userRepository.findOne(id);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        if (user.getIsAdmin() != User.IsAdminEnum.ADMIN.getId()) {
            return Result.fail("该用户不是后台用户");
        }
        if (user.getStatus() == User.StatusEnum.DISABLED.getId()) {
            user.setStatus(User.StatusEnum.ENABLED.getId());
        } else {
            user.setStatus(User.StatusEnum.DISABLED.getId());
        }
        this.userRepository.save(user);
        return Result.success("成功");
    }

    public Result<PageResult<SystemUserVO>> getUsers(PageDTO pageDTO) {
        PageRequest pageRequest = this.bankTypeRepository.pageRequest(pageDTO.getPageNumber(), pageDTO.getPageSize());
        Page<SystemUserVO> systemUserVOS = this.userRepository.findAllByPage(pageRequest);
        PageResult<SystemUserVO> pageResult = new PageResult<>(systemUserVOS);
        return Result.success(pageResult);
    }

    public Result addImage(ImageAddDTO imageAddDTO) {
        return Result.success(this.imageRepository.save(imageAddDTO.toImage()));
    }

    public Result updateImage(ImageEditDTO imageEditDTO) {
        Image image = this.imageRepository.findOne(imageEditDTO.getId());
        if (image == null) {
            return Result.fail("图片不存在");
        }
        image.setTitle(imageEditDTO.getTitle());
        return Result.success(this.imageRepository.save(image));
    }

    public Result<PageResult<ImageVO>> getImages(String key, PageDTO pageDTO) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<Image> imagePage = this.imageRepository.findAll(this.imageRepository.buildImageSpecification(key),
                pageDTO.toPageRequest(sort));
        PageResult<ImageVO> pageResult = new PageResult<>(ImageVO.toImageVO(imagePage));
        return Result.success(pageResult);
    }

    public Result<List<String>> getImageFolders() {
        return Result.success(this.configService.getStringListConfig(IMAGE_FOLDER));
    }

    public Result addVersion(VersionAddDTO versionAddDTO) {
        Result validationResult = this.versionValid(versionAddDTO);
        if (validationResult.getCode() != Result.SUCCESS_CODE) {
            return validationResult;
        }
        return Result.success(this.versionRepository.save(versionAddDTO.toVersion()));
    }

    public Result updateVersion(VersionEditDTO versionEditDTO) {
        Version version = this.versionRepository.findOne(versionEditDTO.getId());
        if (version == null) {
            return Result.fail("版本不存在");
        }
        Result validationResult = this.versionValid(versionEditDTO);
        if (validationResult.getCode() != Result.SUCCESS_CODE) {
            return validationResult;
        }
        BeanUtils.copyProperties(versionEditDTO, version);
        return Result.success(this.versionRepository.save(version));
    }

    public Result deleteVersion(long id) {
        this.versionRepository.delete(id);
        return Result.success("成功");
    }

    public Result<PageResult<VersionVO>> listVersion(PageDTO pageDTO) {
        Page<VersionVO> versionVOPage = this.versionRepository.findAllByPage(pageDTO.toPageRequest());
        return Result.success(new PageResult<>(versionVOPage));
    }

    public Result<String> getNickNameByPhone(String phone) {
        User user = this.userRepository.findByPhone(phone);
        if (user == null) {
            return Result.fail("号码不存在");
        }
        return Result.success(user.getNickName());
    }

    public Result addWhiteList(WhiteListAddDTO whiteListAddDTO) {
        User user = this.userRepository.findByPhone(whiteListAddDTO.getPhone());
        if (user == null) {
            return Result.fail("号码不存在");
        }
        WhiteUserList whiteUserList = whiteListAddDTO.toWhiteUserList();
        whiteUserList.setUserId(user.getId());
        whiteUserList.setStatus(WhiteUserList.StatusEnum.ENABLED.getId());
        return Result.success(this.whiteUserListRepository.save(whiteUserList));
    }

    public Result updateWhiteList(WhiteListEditDTO whiteListEditDTO) {
        WhiteUserList whiteUserList = this.whiteUserListRepository.findOne(whiteListEditDTO.getId());
        if (whiteUserList == null) {
            return Result.fail("白名单不存在");
        }
        User user = this.userRepository.findByPhone(whiteListEditDTO.getPhone());
        if (user == null) {
            return Result.fail("号码不存在");
        }
        if (whiteUserList.getEndTime().before(new Date())) {
            return Result.fail("有效期内才能编辑");
        }
        BeanUtils.copyProperties(whiteListEditDTO, whiteUserList);
        whiteUserList.setUserId(user.getId());
        return Result.success(this.whiteUserListRepository.save(whiteUserList));
    }

    public Result cancelWhiteList(long id) {
        WhiteUserList whiteUserList = this.whiteUserListRepository.findOne(id);
        if (whiteUserList == null) {
            return Result.fail("白名单不存在");
        }
        whiteUserList.setStatus(WhiteUserList.StatusEnum.DISABLED.getId());
        return Result.success(this.whiteUserListRepository.save(whiteUserList));
    }

    public Result<PageResult<WhiteListVO>> listWhiteListVO(PageDTO pageDTO) {
        Page<WhiteListVO> whiteListVOPage = this.whiteUserListRepository.findAllByPage(pageDTO.toPageRequest());
        return Result.success(new PageResult<>(whiteListVOPage));
    }

    public Result versionValid(VersionAddDTO versionEditDTO) {
        if (versionEditDTO.getClientType() == Version.ClientTypeEnum.ANDROID.getCode()) {
            if (StringUtils.isBlank(versionEditDTO.getPackageUrl())) {
                return Result.fail("包url不能为空");
            }
            if (StringUtils.isBlank(versionEditDTO.getPackageName())) {
                return Result.fail("包名不能为空");
            }
        }
        return Result.success();
    }

    public Result<FundsInfo> fundsInfo(Long startTime, Long endTime) {

        BigDecimal balanceSum = null;
        BigDecimal withdrawalsCount = null;
        BigDecimal payOrderMoneySum = null;
        BigDecimal wxPayMoneySum = null;
        BigDecimal PayMoneyBalance = null;
        BigDecimal rechargeMoneySum = null;

        Date startTimeDate = null;
        Date endTimeDate = null;

        if (startTime != null && endTime != null) {
            startTimeDate = new Date(startTime);
            endTimeDate = new Date(endTime + 1000 * 60 * 60 * 24);
            withdrawalsCount = withdrawalsRecordRepository.getWithdrawalsCountAndDate(FINISH.getCode(), startTimeDate, endTimeDate);
            payOrderMoneySum = ordersRepository.findPayMoneySumAndDate(startTimeDate, endTimeDate);
            wxPayMoneySum = ordersRepository.findWXPayMoneySumAndDate(WX_PAY.getCode(), startTimeDate, endTimeDate);
            PayMoneyBalance = ordersRepository.findWXPayMoneySumAndDate(BALANCE_PAY.getCode(), startTimeDate, endTimeDate);
            rechargeMoneySum = rechargeRecordRepository.findRecharMoneySumAndDate(startTimeDate, endTimeDate);
        } else {
            withdrawalsCount = withdrawalsRecordRepository.getWithdrawalsCount(FINISH.getCode());
            payOrderMoneySum = ordersRepository.findPayMoneySum();
            wxPayMoneySum = ordersRepository.findWXPayMoneySum(WX_PAY.getCode());
            PayMoneyBalance = ordersRepository.findWXPayMoneySum(Orders.PayMethodEnum.BALANCE_PAY.getCode());
            rechargeMoneySum = rechargeRecordRepository.findRecharMoneySum();
        }

        balanceSum = userAccountRepository.findBalanceSum();


        FundsInfo fundsInfo = new FundsInfo(MoneyUtils.formatMoney(withdrawalsCount)
                , MoneyUtils.formatMoney(rechargeMoneySum), MoneyUtils.formatMoney(payOrderMoneySum)
                , MoneyUtils.formatMoney(wxPayMoneySum), MoneyUtils.formatMoney(PayMoneyBalance)
                , MoneyUtils.formatMoney(balanceSum));

        return Result.success(fundsInfo);
    }

    @Resource
    private CommonService commonService;


    public Result globalConfig(Integer type, String code) {
        if (type < 1 || type > 3) {
            return Result.fail("type 范围在 1-3");
        }
        Result result = new Result(0,"");
        switch (type) {
            case 1:
                String value = configService.getStringConfig(code);
                result.setData(value);
               break;
            case 2:
                Map<String, String> valueMap = configService.getMapConfig(code);
                List list = new ArrayList();
                valueMap.forEach((k,v) -> {
                    Map newMap = new HashMap();
                    newMap.put("key", k);
                    newMap.put("value",v);
                    list.add(newMap);
                });
                result.setData(list);
                break;
            default:
                List<Config> configValue = configService.getMapListConfig(code);
                result.setData(configValue);
                break;
        }
        return result;
    }

}
