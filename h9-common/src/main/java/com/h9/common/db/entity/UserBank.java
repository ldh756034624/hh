package com.h9.common.db.entity;

import com.h9.common.base.BaseEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/28
 * Time: 9:54
 */

@Entity
@Table(name = "user_bank")
public class UserBank extends BaseEntity {

    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint(20) default 0 COMMENT '用户id'")
    private Long userId;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) default '' COMMENT '持卡人名'")
    private String name;

    @Column(name = "no", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '银行卡号'")
    private String no;

    //    @Column(name = "bank_type", nullable = false, columnDefinition = "varchar(32) default '' COMMENT '银行类别'")
    @ManyToOne
    @JoinColumn(name = "bank_type_id", nullable = false, referencedColumnName = "id", columnDefinition = "bigint(20)  COMMENT '银行卡类型'")
    private BankType bankType;


    @Column(name = "provice", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '开户省'")
    private String province;

    @Column(name = "city", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '开户城市'")
    private String city;

    @Column(name = "status", nullable = false, columnDefinition = "tinyint default 1 COMMENT '1:正常 2禁用 3解绑'")
    private Integer status = 1;

    @Column(name = "default_select", nullable = false, columnDefinition = "int default 0 COMMENT '默认选择的银行卡 1 默认 0 为不是默认'")
    private Integer defaultSelect;
    

    public Integer getDefaultSelect() {
        return defaultSelect;
    }

    public void setDefaultSelect(Integer defaultSelect) {
        this.defaultSelect = defaultSelect;
    }

    public BankType getBankType() {
        return bankType;
    }

    public void setBankType(BankType bankType) {
        this.bankType = bankType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
