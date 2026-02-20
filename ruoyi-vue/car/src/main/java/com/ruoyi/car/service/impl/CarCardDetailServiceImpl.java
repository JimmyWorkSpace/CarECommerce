package com.ruoyi.car.service.impl;

import com.ruoyi.car.domain.CarCardDetailEntity;
import com.ruoyi.car.domain.vo.CarCardDetailVo;
import com.ruoyi.car.mapper.master.CarCardDetailMapper;
import com.ruoyi.car.service.CarCardDetailService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 票券明細 Service 實作（後台核銷）
 *
 * @author ruoyi
 */
@Service
public class CarCardDetailServiceImpl implements CarCardDetailService {

    private static final int REDEEMED_YES = 1;

    @Resource
    private CarCardDetailMapper carCardDetailMapper;

    @Override
    public CarCardDetailVo getDetailById(Long id) {
        return carCardDetailMapper.selectDetailById(id);
    }

    @Override
    public List<CarCardDetailEntity> selectByOrderId(Long orderId) {
        return carCardDetailMapper.selectByOrderId(orderId);
    }

    @Override
    public List<CarCardDetailVo> selectListByTicketCodeLike(String ticketCode) {
        return carCardDetailMapper.selectListByTicketCodeLikeVo(ticketCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int redeemByTicketCode(String ticketCode) {
        if (ticketCode == null || ticketCode.trim().isEmpty()) {
            return 0;
        }
        Long redeemedUserId = SecurityUtils.getUserId();
        return carCardDetailMapper.updateRedeemedByTicketCode(ticketCode.trim(), REDEEMED_YES, new Date(), redeemedUserId);
    }
}
