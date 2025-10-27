package com.ruoyi.car.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.car.domain.CarReportEntity;
import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarReportDto;
import com.ruoyi.car.mapper.master.CarReportMapper;
import com.ruoyi.car.service.CarReportService;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.framework.service.BaseServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 車輛檢舉Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
@Service
@Slf4j
public class CarReportServiceImpl extends BaseServiceImpl implements CarReportService 
{
    @Autowired
    private CarReportMapper carReportMapper;
    
    @Autowired
    private CarSalesService carSalesService;

    /**
     * 查询車輛檢舉
     * 
     * @param id 車輛檢舉主键
     * @return 車輛檢舉
     */
    @Override
    public CarReportEntity selectCarReportById(Long id)
    {
        return carReportMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询車輛檢舉列表
     * 
     * @param carReport 車輛檢舉
     * @return 車輛檢舉
     */
    @Override
    public List<CarReportDto> selectCarReportList(CarReportEntity carReport)
    {
        // 先查询检举列表
        List<CarReportDto> reportList = carReportMapper.selectCarReportList(carReport, carDbName);
        
        return reportList;
    }

    /**
     * 新增車輛檢舉
     * 
     * @param carReport 車輛檢舉
     * @return 结果
     */
    @Override
    public int insertCarReport(CarReportEntity carReport)
    {
        carReport.setCreatedAt(LocalDateTime.now());
        carReport.setUpdatedAt(LocalDateTime.now());
        return carReportMapper.insert(carReport);
    }

    /**
     * 修改車輛檢舉
     * 
     * @param carReport 車輛檢舉
     * @return 结果
     */
    @Override
    public int updateCarReport(CarReportEntity carReport)
    {
        carReport.setUpdatedAt(LocalDateTime.now());
        // 如果处理人ID为空，设置为当前登入用户（这里需要从SecurityContext获取）
        if (carReport.getProcessorId() == null) {
            // 这里需要根据实际的用户获取方式来实现
            // carReport.setProcessorId(getCurrentUserId());
        }
        // 如果处理時間为空，设置为当前時間
        if (carReport.getProcessedAt() == null) {
            carReport.setProcessedAt(LocalDateTime.now());
        }
        return carReportMapper.updateByPrimaryKey(carReport);
    }

    /**
     * 批量刪除車輛檢舉
     * 
     * @param ids 需要刪除的車輛檢舉主键
     * @return 结果
     */
    @Override
    public int deleteCarReportByIds(Long[] ids)
    {
        int count = 0;
        for (Long id : ids) {
            count += carReportMapper.deleteByPrimaryKey(id);
        }
        return count;
    }

    /**
     * 刪除車輛檢舉信息
     * 
     * @param id 車輛檢舉主键
     * @return 结果
     */
    @Override
    public int deleteCarReportById(Long id)
    {
        return carReportMapper.deleteByPrimaryKey(id);
    }
}
