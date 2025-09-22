package com.ruoyi.car.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarRichContentMapper;
import com.ruoyi.car.domain.CarRichContentEntity;
import com.ruoyi.car.service.ICarAboutService;
import tk.mybatis.mapper.entity.Example;

/**
 * 關於Service業務層處理
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Service
public class CarAboutServiceImpl implements ICarAboutService 
{
    @Autowired
    private CarRichContentMapper carRichContentMapper;

    /**
     * 獲取關於內容
     * 
     * @return 關於內容
     */
    @Override
    public CarRichContentEntity getAbout()
    {
        Example example = new Example(CarRichContentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("contentType", 1); // 關於類型
        criteria.andCondition("(delFlag = 0 or delFlag is null)");
        example.orderBy("showOrder").asc().orderBy("createTime").desc();
        
        List<CarRichContentEntity> list = carRichContentMapper.selectByExample(example);
        
        if (list != null && !list.isEmpty()) {
            return list.get(0); // 返回第一條記錄
        }
        
        // 如果沒有找到關於內容，創建一個默認的
        CarRichContentEntity about = new CarRichContentEntity();
        about.setTitle("關於我們");
        about.setContent("<p>歡迎來到我們的網站！</p>");
        about.setContentType(1);
        about.setShowOrder(1);
        about.setDelFlag(false);
        about.setCreateTime(LocalDateTime.now());
        
        carRichContentMapper.insertSelective(about);
        return about;
    }

    /**
     * 修改關於內容
     * 
     * @param carRichContent 關於內容
     * @return 結果
     */
    @Override
    public int updateAbout(CarRichContentEntity carRichContent)
    {
        // 確保內容類型為關於
        carRichContent.setContentType(1);
        
        // 如果ID為空，說明是新增
        if (carRichContent.getId() == null) {
            carRichContent.setCreateTime(LocalDateTime.now());
            carRichContent.setDelFlag(false);
            return carRichContentMapper.insertSelective(carRichContent);
        } else {
            // 更新現有記錄
            return carRichContentMapper.updateByPrimaryKeySelective(carRichContent);
        }
    }
}
