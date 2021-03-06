package com.h9.admin.model.dto.basis;

import com.h9.common.db.entity.config.Version;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;


/**
 * @author: George
 * @date: 2017/11/28 16:21
 */
public class VersionAddDTO {
    @ApiModelProperty(value = "版本",required = true)
    @NotBlank(message = "版本不能为空")
    private String version;

    @ApiModelProperty(value = "客户端,1:ios,2:android",required = true)
    @NotNull(message = "客户端不能为空")
    private Integer clientType;

    @ApiModelProperty(value = "升级类型,1:不提示升级,2:建议升级,3:强制升级",required = true)
    @NotNull(message = "升级类型不能为空")
    private Integer upgradeType;

    @ApiModelProperty(value = "内容",required = true)
    @NotBlank(message = "内容不能为空")
    private String description;

    @ApiModelProperty(value = "包url",required = true)
    private String packageUrl;

    @ApiModelProperty(value = "包名",required = true)
    private String packageName;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Version toVersion() {
        Version version = new Version();
        BeanUtils.copyProperties(this,version);
        return version;
    }
}
