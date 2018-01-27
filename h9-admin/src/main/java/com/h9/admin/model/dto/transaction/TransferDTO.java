package com.h9.admin.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by itservice on 2018/1/27.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private Integer type = 1;
    private String originPhone;
    private String targetPhone;
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        if(startTime != null){
            this.startTime = new Date(startTime);
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        if(endTime != null){
            this.endTime = new Date(endTime);
        }
    }

    private Integer page = 1;
    private Integer limit = 10;

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }
}
