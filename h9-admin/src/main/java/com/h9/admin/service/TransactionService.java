package com.h9.admin.service;

import com.h9.admin.model.dto.community.GoodsTypeAddDTO;
import com.h9.admin.model.dto.community.GoodsTypeEditDTO;
import com.h9.admin.model.dto.transaction.CardCouponsListAddDTO;
import com.h9.admin.model.dto.transaction.TransferDTO;
import com.h9.admin.model.vo.TransferVO;
import com.h9.common.common.ConfigService;
import com.h9.common.db.entity.Transactions;
import com.h9.common.db.entity.account.CardCoupons;
import com.h9.common.db.entity.order.Goods;
import com.h9.common.db.entity.order.GoodsType;
import com.h9.common.db.repo.CardCouponsRepository;
import com.h9.common.db.repo.GoodsReposiroty;
import com.h9.common.db.repo.TransactionsRepository;
import com.h9.common.modle.dto.transaction.CardCouponsDTO;
import com.h9.common.modle.vo.admin.transaction.CardCouponsVO;
import com.h9.common.modle.vo.admin.transaction.GoodsTypeVO;
import com.h9.common.base.PageResult;
import com.h9.common.base.Result;
import com.h9.common.db.repo.GoodsTypeReposiroty;
import com.h9.common.db.entity.order.GoodsType;
import com.h9.common.modle.dto.PageDTO;
import com.h9.common.utils.DateUtil;
import com.h9.common.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.h9.common.db.entity.account.BalanceFlow.BalanceFlowTypeEnum.RED_ENVELOPE;
import static com.h9.common.db.entity.account.BalanceFlow.BalanceFlowTypeEnum.USER_TRANSFER;


/**
 * @author: George
 * @date: 2017/11/29 16:42
 */
@Service
@Transactional
public class TransactionService {

    @Autowired
    private GoodsTypeReposiroty goodsTypeReposiroty;
    @Autowired
    private CardCouponsRepository cardCouponsRepository;
    @Autowired
    private GoodsReposiroty goodsReposiroty;
    @Autowired
    private ConfigService configService;
    @Resource
    private TransactionsRepository transactionsRepository;

    public Result<GoodsType> addGoodsType(GoodsTypeAddDTO goodsTypeAddDTO) {
        if (this.goodsTypeReposiroty.findByCode(goodsTypeAddDTO.getCode()) != null) {
            return Result.fail("标识已存在");
        }
        return Result.success(this.goodsTypeReposiroty.save(goodsTypeAddDTO.toGoodsType()));
    }

    public Result<GoodsType> updateGoodsType(GoodsTypeEditDTO goodsTypeEditDTO) {
        GoodsType goodsType = this.goodsTypeReposiroty.findOne(goodsTypeEditDTO.getId());
        if (goodsType == null) {
            return Result.fail("商品类型不存在");
        }
        if (this.goodsTypeReposiroty.findByCodeAndIdNot(goodsTypeEditDTO.getCode(), goodsTypeEditDTO.getId()) != null) {
            return Result.fail("标识已存在");
        }
        BeanUtils.copyProperties(goodsTypeEditDTO, goodsType);
        return Result.success(this.goodsTypeReposiroty.save(goodsType));
    }

    public Result<PageResult<GoodsTypeVO>> listGoodsType(PageDTO pageDTO) {
        Page<GoodsTypeVO> goodsTypeVOPage = this.goodsTypeReposiroty.findAllByPage(pageDTO.toPageRequest());
        return Result.success(new PageResult<>(goodsTypeVOPage));
    }

    public Result<List<GoodsType>> listEnableGoodsType() {
        List<GoodsType> goodsTypeList = this.goodsTypeReposiroty.findByStatus(GoodsType.StatusEnum.ENABLED.getId());
        return Result.success(goodsTypeList);
    }

    public Result addCardCouponsList(CardCouponsListAddDTO cardCouponsListAddDTO) {
        List<String> noList = Arrays.asList(cardCouponsListAddDTO.getNos().split("\n"));

        List<String> validNoList = new ArrayList<>();
        for (int i = 0; i < noList.size(); i++) {
            if (StringUtils.isNotBlank(noList.get(i))) {
                String validNo = noList.get(i).replaceAll("\\s*", "");
                if (validNoList.contains(validNo)) {
                    return Result.fail("重复输入：" + noList.get(i));
                }
                validNoList.add(validNo);
            }
        }

        if (validNoList.size() < 1) {
            return Result.fail("请输入有效的卡券号");
        }

        Goods goods = this.goodsReposiroty.findByLockId(cardCouponsListAddDTO.getGoodsId());
        if (goods == null) {
            return Result.fail("商品不存在");
        }

        for (int i = 0; i < validNoList.size(); i++) {
            if (this.cardCouponsRepository.findByNo(validNoList.get(i)) != null) {
                return Result.fail("券号:" + validNoList.get(i) + ",已存在");
            }
        }

        Date date = new Date();
        String dayString = DateUtil.formatDate(date, DateUtil.FormatType.NON_SEPARATOR_DAY);
        String lastBatchNo = this.cardCouponsRepository.findLastBatchNo(cardCouponsListAddDTO.getGoodsId(), dayString);

        if (lastBatchNo == null || lastBatchNo.length() <= DateUtil.FormatType.NON_SEPARATOR_DAY.getFormat().length()) {
            lastBatchNo = dayString + 1;
        } else {
            int currentId = Integer.valueOf(lastBatchNo.substring(DateUtil.FormatType.NON_SEPARATOR_DAY.getFormat()
                    .length(), lastBatchNo.length())) + 1;
            lastBatchNo = dayString + currentId;
        }
        List<CardCoupons> cardCouponsList = new ArrayList<>();
        final String batchNo = lastBatchNo;
        validNoList.forEach(item -> {
            CardCoupons cardCoupons = new CardCoupons();
            cardCoupons.setNo(item);
            cardCoupons.setStatus(CardCoupons.StatusEnum.ENABLED.getId());
            cardCoupons.setUserId(HttpUtil.getCurrentUserId());
            cardCoupons.setCreateTime(date);
            cardCoupons.setGoodsId(cardCouponsListAddDTO.getGoodsId());
            cardCoupons.setBatchNo(batchNo);
            cardCoupons.setMoney(goods.getPrice());
            cardCouponsList.add(cardCoupons);
        });
        this.cardCouponsRepository.save(cardCouponsList);
        goods.setStock(goods.getStock() + validNoList.size());
        this.goodsReposiroty.save(goods);
        return Result.success();
    }

    public Result<List<String>> listCardCouponsBatchNo(long goodsId) {
        List<String> batchNoList = this.cardCouponsRepository.findAllBatchNoByGoodsId(goodsId);
        return Result.success(batchNoList);
    }

    public Result<PageResult<CardCouponsVO>> listCardCoupons(CardCouponsDTO cardCouponsDTO) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<CardCoupons> cardCouponsPage = this.cardCouponsRepository.findAll(this.cardCouponsRepository
                .buildSpecification(cardCouponsDTO), cardCouponsDTO.toPageRequest(sort));
        return Result.success(new PageResult<>(CardCouponsVO.toCardCouponsVO(cardCouponsPage)));
    }

    public Result changeCardCouponsStatus(long cardCouponsId) {
        CardCoupons cardCoupons = this.cardCouponsRepository.findOne(cardCouponsId);
        if (cardCoupons == null) {
            return Result.fail("卡券不存在");
        }
        if (cardCoupons.getStatus() == CardCoupons.StatusEnum.USED.getId()) {
            return Result.fail("卡券已使用,不允许修改卡券状态");
        }
        Goods goods = this.goodsReposiroty.findByLockId(cardCoupons.getGoodsId());
        if (cardCoupons.getStatus() == CardCoupons.StatusEnum.ENABLED.getId()) {
            cardCoupons.setStatus(CardCoupons.StatusEnum.DISABLED.getId());
            goods.setStock(goods.getStock() - 1);
        } else if (cardCoupons.getStatus() == CardCoupons.StatusEnum.DISABLED.getId()) {
            cardCoupons.setStatus(CardCoupons.StatusEnum.ENABLED.getId());
            goods.setStock(goods.getStock() + 1);
        }
        CardCoupons coupons = this.cardCouponsRepository.save(cardCoupons);
        this.goodsReposiroty.save(goods);
        return Result.success(coupons);
    }

    public Result transferList(TransferDTO transferDTO) {

        Specification<Transactions> specification = getTransferListSpecification(transferDTO);
        Sort sort = new Sort( Sort.Direction.DESC,"id");
        PageRequest pageRequest = transactionsRepository.pageRequest(transferDTO.getPageNumber(), transferDTO.getPageSize(),sort);
        Page<Transactions> transactionsPage = transactionsRepository.findAll(specification, pageRequest);
        PageResult<TransferVO> pageResult = new PageResult<>(transactionsPage).result2Result(el -> new TransferVO(el));
        return Result.success(pageResult);
    }

    private Specification<Transactions> getTransferListSpecification(TransferDTO transferDTO) {

        return (root, query, builder) -> {
            String originPhone = transferDTO.getOriginPhone();

            List<Predicate> predicateList = new ArrayList<>();

            if (originPhone != null) {
                predicateList.add(builder.equal(root.get("phone"), originPhone));
            }

            String targetPhone = transferDTO.getTargetPhone();
            if (StringUtils.isNotBlank(targetPhone)) {
                predicateList.add(builder.equal(root.get("targetPhone"), targetPhone));

            }

            Integer type = transferDTO.getType();
            Long balanceType = null;

            if (type == 1) {
                balanceType = USER_TRANSFER.getId();
                predicateList.add(builder.equal(root.get("balanceFlowType"), balanceType));
            } else if(type ==2){
                balanceType = RED_ENVELOPE.getId();
                predicateList.add(builder.equal(root.get("balanceFlowType"), balanceType));
            }else{
                predicateList.add(builder.
                        between(root.get("balanceFlowType"),
                        USER_TRANSFER.getId(),
                        RED_ENVELOPE.getId()));
            }

            Date startTime = transferDTO.getStartTime();
            Date endTime = transferDTO.getEndTime();
            if (startTime != null && endTime != null) {
                predicateList.add(builder.between(root.get("createTime"), startTime, endTime));
            }


            return builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };

    }
}
