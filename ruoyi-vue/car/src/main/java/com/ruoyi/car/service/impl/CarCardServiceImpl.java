package com.ruoyi.car.service.impl;

import com.ruoyi.car.domain.CarCardEntity;
import com.ruoyi.car.mapper.master.CarCardMapper;
import com.ruoyi.car.service.CarCardService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import tk.mybatis.mapper.entity.Example;

/**
 * 票券方案 Service 實作
 *
 * @author ruoyi
 */
@Service
public class CarCardServiceImpl implements CarCardService {

    @Resource
    private CarCardMapper carCardMapper;

    @Override
    public List<CarCardEntity> selectCardList(CarCardEntity query) {
        Example example = new Example(CarCardEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if (query != null) {
            if (StringUtils.isNotEmpty(query.getCardName())) {
                criteria.andLike("cardName", "%" + query.getCardName().trim() + "%");
            }
            if (query.getStatus() != null) {
                criteria.andEqualTo("status", query.getStatus());
            }
        }

        example.orderBy("createTime").desc();
        return carCardMapper.selectByExample(example);
    }

    @Override
    public CarCardEntity selectCardById(Long id) {
        return carCardMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCard(CarCardEntity carCard) {
        if (carCard.getStatus() == null) {
            carCard.setStatus(1);
        }
        if (carCard.getValidityType() == null) {
            carCard.setValidityType(1);
        }
        if (carCard.getCreateTime() == null) {
            carCard.setCreateTime(new Date());
        }
        return carCardMapper.insertSelective(carCard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCard(CarCardEntity carCard) {
        if (carCard.getUpdateTime() == null) {
            carCard.setUpdateTime(new Date());
        }
        return carCardMapper.updateByPrimaryKeySelective(carCard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCardById(Long id) {
        return carCardMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCardByIds(Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += carCardMapper.deleteByPrimaryKey(id);
        }
        return count;
    }
}
