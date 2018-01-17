package com.h9.api.service.handler;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * PayHandlerFactory:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/12
 * Time: 11:15
 */
@Component
public class PayHandlerFactory {
    
    @Resource
    private RechargePayHandler rechargePayHandler;
    @Resource
    private HotelPayHandler hotelPayHandler;
    
    public AbPayHandler getPayHandler(int type){
        AbPayHandler abPayHandler;
        switch (type){
            case 0:{
                abPayHandler = rechargePayHandler;
                break;
            }
            case 1:{
                abPayHandler = hotelPayHandler;
                break;
            }

            default:{
                abPayHandler = rechargePayHandler;
            }
        }
        return abPayHandler;
    }


}

