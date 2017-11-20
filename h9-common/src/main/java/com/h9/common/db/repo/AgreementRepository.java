package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.HtmlContent;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

/**
 * Created by liyuan on 2017/11/1.
 */
public interface AgreementRepository extends BaseRepository<HtmlContent> {

    /**
     * 返回单页内容
     * @param code
     * @return
     */
    HtmlContent findByCode(String code);
}
