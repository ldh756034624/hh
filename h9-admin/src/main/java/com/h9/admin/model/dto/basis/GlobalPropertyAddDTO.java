package com.h9.admin.model.dto.basis;

import com.alibaba.fastjson.JSON;
import com.h9.common.db.entity.GlobalProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: George
 * @date: 2017/11/5 14:56
 */
public class GlobalPropertyAddDTO {

    @ApiModelProperty(value = "名称",required = true)
    @NotEmpty(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "参数类型， 0：文本 1：对象 2:数组",required = true)
    @NotNull(message = "参数类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "参数标识",required = true)
    @NotEmpty(message = "参数标识不能为空")
    private String code;

    @ApiModelProperty(value = "参数值",required = true)
    @NotNull(message = "参数值不能为空")
    private List<Map<String,String>> val;

    @ApiModelProperty(value = "说明",required = true)
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVal() {
        String v="";
        if(type==0){
            v = val.get(0).get("val");
        }else if(type==1){
            v = JSON.toJSONString(val);
        }else{
            List<String> stringList = new ArrayList<>();
            for(Map m:val){
                stringList.add(m.get("val").toString());
            }
            v = JSON.toJSONString(stringList);
        }
        return v;
    }

    public void setVal(List val) {
        this.val = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GlobalProperty toGlobalProperty(){

        GlobalProperty globalProperty = new GlobalProperty();
        BeanUtils.copyProperties(this,globalProperty);
        return globalProperty;
    }
}
