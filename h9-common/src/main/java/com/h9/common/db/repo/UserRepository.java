package com.h9.common.db.repo;


import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.user.User;
import com.h9.common.modle.vo.admin.basis.SystemUserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * @ClassName: UserRepository
 * @Description: User 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface UserRepository extends BaseRepository<User> {

    @Query("select u from User u where u.phone = ?1 and u.status<>3")
    User findByPhone(String phone);

    @Query("select u from User u where u.openId = ?1 and u.status<>3")
    User findByOpenId(String openId);


    @Query(value = "select o from User o where o.phone=?1 and o.password=?2 and o.isAdmin=?3")
   //@Lock(LockModeType.PESSIMISTIC_WRITE)
    User findByPhoneAndPasswordAndIsAdmin(String phone, String password, Integer isAdmin);

    @Query(value = "select o from User o where o.phone=?1 and o.isAdmin=?2")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    User findByPhoneAndIsAdmin(String phone, Integer isAdmin);


    @Query("select new com.h9.common.modle.vo.admin.basis.SystemUserVO(o) from User o where o.isAdmin = 1 order by o.status asc ,o.id desc ")
    Page<SystemUserVO> findAllByPage(Pageable page);

    @Override
    User findOne(Long id);

    //////////////////////////////////数据迁移方法//////////////////////
    User findByH9UserId(Long userId);


}
