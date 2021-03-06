package com.h9.admin.service;

import com.h9.admin.model.vo.LoginResultVO;
import com.h9.common.base.Result;
import com.h9.common.common.ConfigService;
import com.h9.common.constant.ParamConstant;
import com.h9.common.db.bean.RedisBean;
import com.h9.common.db.bean.RedisKey;
import com.h9.common.db.entity.order.Address;
import com.h9.common.db.entity.user.User;
import com.h9.common.db.entity.user.UserAccount;
import com.h9.common.db.entity.user.UserBank;
import com.h9.common.db.entity.user.UserExtends;
import com.h9.common.db.repo.*;
import com.h9.common.modle.vo.Config;
import com.h9.common.modle.vo.admin.finance.*;
import com.h9.common.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: George
 * @date: 2017/10/31 14:08
 */
@Service
@Transactional
public class UserService {
    private Logger logger = Logger.getLogger(this.getClass());
    public static final String PROFILE_SEX = "profileSex";
    public static final String PROFILE_JOB = "profileJob";
    public static final String PROFILE_EDUCATION = "profileEducation";
    public static final String PROFILE_EMOTION = "profileEmotion";

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserExtendsRepository userExtendsRepository;
    @Resource
    private UserBankRepository userBankRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RedisBean redisBean;
    @Autowired
    private ConfigService configService;
    @Resource
    private UserAccountRepository userAccountRepository;


    public Result<LoginResultVO> login(String name, String password) {
        String actualPassword = MD5Util.getMD5(password);
        this.logger.infov("name:{0},password:{1}", name, actualPassword);
        User user = this.userRepository.findByPhoneAndIsAdmin(name, User.IsAdminEnum.ADMIN.getId());
        if (user == null) {
            return Result.fail("用户不存在");
        }
        if (!actualPassword.equals(user.getPassword())) {
            return Result.fail("密码错误");
        }
        if (user.getStatus() != User.StatusEnum.ENABLED.getId()) {
            return Result.fail("该用户已被禁用");
        }
        //生成token,并保存
        String token = UUID.randomUUID().toString();
        String tokenUserIdKey = RedisKey.getAdminTokenUserIdKey(token);
        redisBean.setStringValue(tokenUserIdKey, user.getId() + "", 30, TimeUnit.MINUTES);
        //HttpUtil.setHttpSessionAttr("curUserId",user.getId());
        user.setLastLoginTime(new Date());
        this.userRepository.save(user);
        return new Result(0, "登录成功", new LoginResultVO(token, name));
    }

    public Result logout(String token) {
        redisBean.expire(RedisKey.getAdminTokenUserIdKey(token), 100, TimeUnit.MICROSECONDS);
        return new Result(0, "成功退出登录");
    }

    public Result<UserVO> getUserInfo(long userId) {
        UserVO userVO = new UserVO();
        User user = this.userRepository.findOne(userId);
        userVO.setUserInfoVO(user == null ? null : new UserInfoVO(user));
        UserExtends userExtends = this.userExtendsRepository.findByUserId(userId);
        UserExtendsInfoVO userExtendsInfoVO = this.getUserExtendsInfoVO(userExtends);
        userVO.setUserExtendsInfoVO(userExtendsInfoVO);
        List<UserBank> userBankList = this.userBankRepository.findByUserId(userId);
        userVO.setUserBankInfoVOList(UserBankInfoVO.toUserBankVO(userBankList));
        List<Address> addressList = this.addressRepository.findByUserId(userId);
        userVO.setUserAddressInfoVOList(UserAddressInfoVO.toUserAddressInfoVO(addressList));
        return Result.success(userVO);
    }

    private UserExtendsInfoVO getUserExtendsInfoVO(UserExtends userExtends) {
        if (userExtends == null) {
            return null;
        }
        UserExtendsInfoVO userExtendsInfoVO = new UserExtendsInfoVO(userExtends);
        List<Config> sex = this.configService.getMapListConfig(PROFILE_SEX);
        List<Config> job = this.configService.getMapListConfig(PROFILE_JOB);
        List<Config> education = this.configService.getMapListConfig(PROFILE_EDUCATION);
        List<Config> emotion = this.configService.getMapListConfig(PROFILE_EMOTION);
        userExtendsInfoVO.setSex(this.configService.getConfigVal(sex, userExtends.getSex().toString()));
        userExtendsInfoVO.setJob(this.configService.getConfigVal(job, userExtendsInfoVO.getJob()));
        userExtendsInfoVO.setEducation(this.configService.getConfigVal(education, userExtendsInfoVO.getEducation()));
        userExtendsInfoVO.setMarriageStatus(this.configService.getConfigVal(emotion, userExtendsInfoVO.getMarriageStatus()));
        return userExtendsInfoVO;
    }

    public User registerByPhone(String phone) {
        User user = initUserInfo(phone);
        user.setClient(1);
        int loginCount = user.getLoginCount();
        user.setLoginCount(++loginCount);
        user.setLastLoginTime(new Date());
        User userFromDb = userRepository.saveAndFlush(user);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userFromDb.getId());
        userAccountRepository.save(userAccount);

        UserExtends userExtends = new UserExtends();
        userExtends.setUserId(userFromDb.getId());
        userExtends.setSex(1);
        userExtendsRepository.save(userExtends);
        return user;
    }

    /**
     * description: 初化一个用户，并返回这个用户对象
     */
    private User initUserInfo(String phone) {
        if (phone == null) return null;
        User user = new User();
        user.setAvatar("");
        user.setLoginCount(0);
        user.setPhone(phone);
        if (StringUtils.isNotBlank(phone)) {
            CharSequence charSequence = phone.subSequence(3, 7);
            user.setNickName(phone.replace(charSequence, "****"));
        }
        user.setLastLoginTime(new Date());
//        GlobalProperty defaultHead = globalPropertyRepository.findByCode(ParamConstant.DEFUALT_HEAD);

        String defaultHead = configService.getStringConfig(ParamConstant.DEFUALT_HEAD);
        if (StringUtils.isBlank(defaultHead)) {
            logger.info("没有在参数配置中找到默认头像的配置");
            defaultHead = "";
        }
        user.setAvatar(defaultHead);
        return user;
    }
}
