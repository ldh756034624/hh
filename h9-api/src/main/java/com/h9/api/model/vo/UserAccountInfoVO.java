package com.h9.api.model.vo;

import com.h9.common.db.entity.user.User;
import com.h9.common.db.entity.user.UserAccount;
import com.h9.common.utils.MoneyUtils;

/**
 * Created by itservice on 2017/11/2.
 */
public class UserAccountInfoVO {
    private String balance;
    private String vb;
    private String cardNum;
    private String imgUrl;
    private String nickName;
    private Integer withdrawalCount;
    private String tel;
    private boolean canWithdeawal = false;
    private boolean canTransConfig = false;

    public UserAccountInfoVO(User user, UserAccount userAccount, String cardNum, String tel, String max){
        this.balance = MoneyUtils.formatMoney(userAccount.getBalance());
        this.vb = userAccount.getvCoins().toString();
        this.cardNum = cardNum;
        this.imgUrl = user.getAvatar();
        this.nickName = user.getNickName();
        this.withdrawalCount =Integer.valueOf(max);
        this.tel = tel;
    }


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getWithdrawalCount() {
        return withdrawalCount;
    }

    public void setWithdrawalCount(Integer withdrawalCount) {
        this.withdrawalCount = withdrawalCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getVb() {
        return vb;
    }

    public void setVb(String vb) {
        this.vb = vb;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public boolean isCanWithdeawal() {
        return canWithdeawal;
    }

    public void setCanWithdeawal(boolean canWithdeawal) {
        this.canWithdeawal = canWithdeawal;
    }

    public boolean isCanTransConfig() {
        return canTransConfig;
    }

    public void setCanTransConfig(boolean canTransConfig) {
        this.canTransConfig = canTransConfig;
    }
}
