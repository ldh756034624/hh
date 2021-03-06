package com.h9.admin.controller;

import com.h9.admin.interceptor.Secured;
import com.h9.admin.model.dto.article.ArticleDTO;
import com.h9.admin.model.dto.article.ArticleTypeDTO;
import com.h9.admin.service.ArticleService;
import com.h9.admin.validation.Edit;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.db.entity.config.Article;
import com.h9.common.db.entity.config.ArticleType;
import com.h9.common.modle.dto.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;

/**
 * 文章
 * Created by Gonyb on 2017/11/9.
 */
@RestController
@Api()
@RequestMapping(value = "/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;
    
    @Secured(accessCode = "article:category:list")
    @GetMapping(value = "/category/list")
    @ApiOperation("获取文章类别list")
    public Result<PageResult<ArticleType>> categoryList(PageDTO pageDTO){
        return articleService.categoryList(pageDTO);
    }

    @Secured(accessCode = "article:category:get")
    @GetMapping(value = "/category/{id}")
    @ApiOperation("获取文章类别")
    public Result<ArticleType> getCategory(@PathVariable Long id){
        return articleService.getCategory(id);
    }

    @Secured(accessCode = "article:category:delete")
    @DeleteMapping(value = "/category/{id}")
    @ApiOperation("删除文章类别")
    public Result deleteCategory(@PathVariable Long id){
        return articleService.deleteCategory(id);
    }

    @Secured(accessCode = "article:category:add")
    @PostMapping(value = "/category")
    @ApiOperation("新增文章类别")
    public Result<ArticleType> addCategory(@Validated @RequestBody ArticleTypeDTO articleTypeDTO){
        return articleService.addCategory(articleTypeDTO);
    }

    @Secured(accessCode = "article:category:update")
    @PutMapping(value = "/category")
    @ApiOperation("编辑文章类别")
    public Result<ArticleType> editCategory(@Validated({Edit.class, Default.class}) @RequestBody ArticleTypeDTO articleTypeDTO){
        return articleService.editCategory(articleTypeDTO);
    }

    @Secured(accessCode = "article:list")
    @GetMapping(value = "/list")
    @ApiOperation("获取文章列表")
    public Result<PageResult<Article>> articleList(PageDTO pageDTO){
        return articleService.articleList(pageDTO);
    }

    @Secured(accessCode = "article:get")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取文章")
    public Result<Article> getArticle(@PathVariable Long id){
        return articleService.getArticle(id);
    }

    @Secured(accessCode = "article:delete")
    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除文章")
    public Result deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

    @Secured(accessCode = "article:add")
    @PostMapping(value = "/")
    @ApiOperation("新增文章")
    public Result<Article> addArticle(@Validated @RequestBody ArticleDTO articleDTO){
        return articleService.addArticle(articleDTO);
    }

    @Secured(accessCode = "article:update")
    @PutMapping(value = "/")
    @ApiOperation("修改文章")
    public Result<Article> editArticle(@Validated({Edit.class, Default.class}) @RequestBody ArticleDTO articleDTO){
        return articleService.editArticle(articleDTO);
    }
}
