package com.h9.common.db.repo;


import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.Product;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: ProductRepository
 * @Description: Product 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface ProductRepository extends BaseRepository<Product> {


}