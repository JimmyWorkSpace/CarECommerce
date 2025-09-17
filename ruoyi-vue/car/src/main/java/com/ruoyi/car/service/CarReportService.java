package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarReportEntity;
import com.ruoyi.car.dto.CarReportDto;

/**
 * 車輛檢舉Service接口
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
public interface CarReportService 
{
    /**
     * 查询車輛檢舉
     * 
     * @param id 車輛檢舉主键
     * @return 車輛檢舉
     */
    public CarReportEntity selectCarReportById(Long id);

    /**
     * 查询車輛檢舉列表
     * 
     * @param carReport 車輛檢舉
     * @return 車輛檢舉集合
     */
    public List<CarReportDto> selectCarReportList(CarReportEntity carReport);

    /**
     * 新增車輛檢舉
     * 
     * @param carReport 車輛檢舉
     * @return 结果
     */
    public int insertCarReport(CarReportEntity carReport);

    /**
     * 修改車輛檢舉
     * 
     * @param carReport 車輛檢舉
     * @return 结果
     */
    public int updateCarReport(CarReportEntity carReport);

    /**
     * 批量删除車輛檢舉
     * 
     * @param ids 需要删除的車輛檢舉主键集合
     * @return 结果
     */
    public int deleteCarReportByIds(Long[] ids);

    /**
     * 删除車輛檢舉信息
     * 
     * @param id 車輛檢舉主键
     * @return 结果
     */
    public int deleteCarReportById(Long id);
}
