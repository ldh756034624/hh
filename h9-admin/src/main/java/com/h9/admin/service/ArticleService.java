package com.h9.admin.service;

import com.h9.admin.model.dto.article.ArticleDTO;
import com.h9.admin.model.dto.article.ArticleTypeDTO;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.common.ConfigService;
import com.h9.common.db.entity.config.Article;
import com.h9.common.db.entity.config.ArticleType;
import com.h9.common.db.repo.ArticleRepository;
import com.h9.common.db.repo.ArticleTypeRepository;
import com.h9.common.modle.dto.PageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 文章服务
 * Created by Gonyb on 2017/11/9.
 */
@Service
public class ArticleService {
    @Resource
    private ArticleTypeRepository articleTypeRepository;
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ConfigService configService;

    public Result<PageResult<ArticleType>> categoryList(PageDTO pageDTO) {
        Page<ArticleType> all = articleTypeRepository.findAll(pageDTO.toPageRequest());
        all.forEach(articleType -> articleType.setArticleCount(articleRepository.findCountByArticleType(articleType.getId())));
        return Result.success(new PageResult<>(all));
    }


    public Result<ArticleType> getCategory(Long id) {
        ArticleType one = articleTypeRepository.findOne(id);
        one.setArticleCount(articleRepository.findCountByArticleType(one.getId()));
        return Result.success(one);
    }

    public Result<ArticleType> addCategory(ArticleTypeDTO articleTypeDTO) {
        ArticleType articleType = new ArticleType();
        BeanUtils.copyProperties(articleTypeDTO,articleType,"id");
        articleType.setCreateTime(new Date());
        articleTypeRepository.save(articleType);
        return Result.success(articleType);
    }

    public Result<ArticleType> editCategory(ArticleTypeDTO articleTypeDTO) {
        Long id = articleTypeDTO.getId();
        ArticleType one = articleTypeRepository.findOne(id);
        if(one==null){
            return Result.fail("分类不存在");
        }
        BeanUtils.copyProperties(articleTypeDTO,one);
        one.setUpdateTime(new Date());
        articleTypeRepository.save(one);
        return Result.success(one);
    }

    public Result deleteCategory(Long id) {
        ArticleType one = articleTypeRepository.findOne(id);
        if(one==null){
            return Result.fail("分类不存在");
        }
        one.setEnable(2);
        one.setUpdateTime(new Date());
        articleTypeRepository.save(one);
        return Result.success();
    }

    public Result<PageResult<Article>> articleList(PageDTO pageDTO) {
        Page<Article> all = articleRepository.findAll(pageDTO.toPageRequest());
        Map preLink = configService.getMapConfig("preLink");
        all.forEach(item->this.setArticleUrl(item,preLink));
        return Result.success(new PageResult<>(all));
    }

    public Result<Article> getArticle(Long id) {
        Article one = articleRepository.findOne(id);
        if(one==null){
            return Result.fail("您访问的文章不存在");
        }
        setArticleUrl(one);
        return Result.success(one);
    }

    public Result deleteArticle(Long id) {
        Article one = articleRepository.findOne(id);
        if(one==null){
            return Result.fail("您要删除的文章不存在");
        }
        one.setEnable(2);
        one.setUpdateTime(new Date());
        articleRepository.save(one);
        return Result.success();
    }

    public Result<Article> addArticle(ArticleDTO articleDTO) {
        //检查分类是否存在
        ArticleType one = articleTypeRepository.findOne(articleDTO.getArticleTypeId());
        if(one==null){
            return Result.fail("文章分类不存在,请选择其他的分类");
        }

        Article article = new Article();
        BeanUtils.copyProperties(articleDTO,article);
        article.setId(null);
        article.setArticleType(one);
        article.setCreateTime(new Date());
        if(articleDTO.getStartTime()!=null){
            article.setPublishTime(articleDTO.getStartTime());
        }else{
            article.setPublishTime(new Date());
        }
        articleRepository.save(article);
        return Result.success(article);
    }

    public Result<Article> editArticle(ArticleDTO articleDTO) {
        //检查分类是否存在
        ArticleType one = articleTypeRepository.findOne(articleDTO.getArticleTypeId());
        if(one==null){
            return Result.fail("文章分类不存在,请选择其他的分类");
        }
        //检查是否有这个文章
        Article article = articleRepository.findOne(articleDTO.getId());
        if(article==null){
            return Result.fail("要修改的文章不存在");
        }
        BeanUtils.copyProperties(articleDTO,article);
        article.setArticleType(one);
        article.setUpdateTime(new Date());
        if(articleDTO.getStartTime()!=null){
            article.setPublishTime(articleDTO.getStartTime());
        }else{
            article.setPublishTime(new Date());
        }
        articleRepository.save(article);
        return Result.success(article);
    }

    private void setArticleUrl(Article article){
        String url = article.getUrl();
        if (StringUtils.isBlank(url)) {
            Map preLink = configService.getMapConfig("preLink");
            if (preLink!=null) {
                article.setUrl(preLink.get("article").toString() + article.getId());
            }
        }
    }

    private void setArticleUrl(Article article,Map preLink){
        String url = article.getUrl();
        if (StringUtils.isBlank(url)) {
            if (preLink!=null) {
                article.setJointUrl(preLink.get("article").toString() + article.getId());
            }
        }
    }
}
