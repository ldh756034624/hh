package com.h9.admin.model.dto.customModule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Created by Ln on 2018/4/23.
 */
@Data
@ApiModel
public class AddCustomModuleDTO {
    @NotNull(message = "type 不能为空")
    @ApiModelProperty("模板类型")
    private Long type;

    @ApiModelProperty("名字")
    @Length(max = 20, message = "模块名20字以内")
    private String name;

    @ApiModelProperty("组件信息")
    @Size(max = 2, min = 2, message = "请传入组件信息")
    @Valid
    private List<Info> infos;

    @ApiModel("组件信息,第一个无素为瓶身定制信息,第二个为瓶箱定制信息")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        @ApiModelProperty("主图")
        private List<String> mainImages;

        @ApiModelProperty("定制化图片组件数量")
        private Integer customImagesCount;

        @ApiModelProperty("定制化文本组件数量")
        private Integer textCount;
        @ApiModelProperty("1 为瓶身，2为其他")
        @Max(value = 2, message = "itemType 不能大余2")
        @Min(value = 1, message = "itemType 不能小余1")
        @NotNull(message = "请传入itemType参数")
        private Integer itemType;
    }

    @ApiModelProperty("商品信息,请传入 {goodsId:number} 此格式，eg: {3:100}")
    private Map<Long, Long> goodsInfo;

    @ApiModelProperty("模块Id")
    private Long customModuleId;
}
