package com.ruoyi.car.service;

import com.ruoyi.car.domain.CarRichContentEntity;

/**
 * 關於Service接口
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
public interface ICarAboutService 
{
    /**
     * 獲取關於內容
     * 
     * @return 關於內容
     */
    public CarRichContentEntity getAbout();

    /**
     * 修改關於內容
     * 
     * @param carRichContent 關於內容
     * @return 結果
     */
    public int updateAbout(CarRichContentEntity carRichContent);
}
