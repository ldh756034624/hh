package com.h9.admin.model.dto.stick;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import sun.util.resources.ga.LocaleNames_ga;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by 李圆 on 2018/1/16
 */
@Data
public class UpdateStickDTO {

    private Long stickId;

    @NotBlank(message = "请填写标题")
    @Length(max = 64,message = "标题填写过长")
    private String title;
    @NotBlank(message = "请填写内容")
    @Length(max = 1000,message = "内容过长")
    private String content;
    @NotNull(message = "请选择分类")
    private Long typeId;
}
