package com.h9.admin.model.dto.basis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.h9.admin.validation.ParamException;
import com.h9.common.db.entity.config.GlobalProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: George
 * @date: 2017/11/5 14:56
 */
public class GlobalPropertyAddDTO {

    @ApiModelProperty(value = "名称",required = true)
    @NotBlank(message = "名称不能为空")
    @Size(max = 512,message = "名称过长")
    private String name;

    @ApiModelProperty(value = "参数类型， 0：文本 1：对象 2:数组",required = true)
    @NotNull(message = "参数类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "参数标识",required = true)
    @NotBlank(message = "参数标识不能为空")
    @Size(max= 50,message = "参数标识不能大于50")
    private String code;

    @ApiModelProperty(value = "参数值",required = true)
    @NotEmpty(message = "参数值不能为空")
    @Valid
    private List<Map<String,Object>> val;

    @ApiModelProperty(value = "说明",required = true)
    @NotBlank(message = "说明不能为空")
    @Size(max = 512,message = "说明过长")
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
            v = val.get(0).get("val").toString();
        }else if(type==1){
            Map map = new HashMap();
            for(int i=0;i<val.size();i++){
                map.put(val.get(i).get("key"),val.get(i).get("code"));
            }
            v = JSON.toJSONString(map);
        }else{
            List stringList = new ArrayList<>();
            for(Map m:val){
                Object o = m.get("val");
                stringList.add(o.toString());
            }
            v = JSONObject.toJSONString(stringList);
        }
        return v;
    }

    public void setVal(List<Map<String,Object>> val) {
        this.val = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GlobalProperty toGlobalProperty() throws ParamException{
        for (Map<String, Object> v : val) {
            for (Map.Entry entry : v.entrySet()) {
                if (entry.getValue() == null || StringUtils.isBlank(entry.getValue().toString())) {
                    throw new ParamException("值不能为空");
                }
            }
        }
        GlobalProperty globalProperty = new GlobalProperty();
        BeanUtils.copyProperties(this,globalProperty);
        return globalProperty;
    }

    public static void main(String[] args){
    Map map = new HashMap();
    map.put("1","2");
    System.out.println(map);
    }
}
