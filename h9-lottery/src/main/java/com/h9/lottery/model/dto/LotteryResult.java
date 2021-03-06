package com.h9.lottery.model.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * LotteryResult:刘敏华 shadow.liu@hey900.com
 * Date: 2017/11/3
 * Time: 10:26
 */
public class LotteryResult {

    private String qrCode;
    private String code;
    private boolean lottery;
    private int userCount = 0;
    private boolean roomUser;
    private String money ="0.00";
    private List<LotteryUser> lotteryUsers;
    private String endTime;
    private long differentDate;
    private String nowTime;
    private BigDecimal refreshTime = new BigDecimal(10.0);
    private String tel = "";

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<LotteryUser> getLotteryUsers() {
        return lotteryUsers;
    }

    public void setLotteryUsers(List<LotteryUser> lotteryUsers) {
        this.lotteryUsers = lotteryUsers;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public boolean isLottery() {
        return lottery;
    }

    public void setLottery(boolean lottery) {
        this.lottery = lottery;
    }

    public boolean isRoomUser() {
        return roomUser;
    }

    public void setRoomUser(boolean roomUser) {
        this.roomUser = roomUser;
    }

    public long getDifferentDate() {
        return differentDate;
    }

    public void setDifferentDate(long differentDate) {
        this.differentDate = differentDate;
    }

    public BigDecimal getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(BigDecimal refreshTime) {
        this.refreshTime = refreshTime;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}

