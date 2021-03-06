package com.h9;

import com.alibaba.fastjson.JSONObject;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.common.ServiceException;
import com.h9.common.db.entity.order.Goods;
import com.h9.common.db.repo.GoodsReposiroty;
import com.h9.store.modle.dto.ConvertGoodsDTO;
import com.h9.store.modle.vo.GoodsListVO;
import com.h9.store.service.GoodService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by itservice on 2017/11/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOrder {

    private static final String host = "http://localhost:6305/h9/api";
    private RestTemplate restTemplate = new RestTemplate();

    @Resource
    private GoodService goodService;
    /**
     * description: 测试兑换商品
     */
    public  void converGoodsTest() throws ServiceException {

        Long userId = 9676L;
        Long addressId = 452l;

        Result result = goodService.goodsList("", 0, 10);
        PageResult<GoodsListVO> data = (PageResult)result.getData();
        List<GoodsListVO> goodsList = data.getData();
        if (CollectionUtils.isEmpty(goodsList)) {
            System.out.println("goodsList is empty");
        }
        GoodsListVO goods = goodsList.get(0);

        ConvertGoodsDTO dto = new ConvertGoodsDTO();
        dto.setCount(1);
        dto.setAddressId(addressId);
        dto.setGoodsId(goods.getId());
        Result convertResult = goodService.convertGoods(dto, userId);
        System.out.println(JSONObject.toJSON(convertResult));
    }

    @Resource
    private GoodsReposiroty goodsReposiroty;
    @Test
    public void test22(){
        Goods one = goodsReposiroty.findOne(1L);
        System.out.println();
    }
}
