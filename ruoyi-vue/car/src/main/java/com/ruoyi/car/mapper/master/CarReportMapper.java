package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarReportEntity;
import com.ruoyi.car.dto.CarReportDto;

import tk.mybatis.mapper.common.Mapper;

/**
 * 車輛檢舉Mapper
 */
public interface CarReportMapper extends Mapper<CarReportEntity> {
    
    /**
     * 根據檢舉人ID查詢檢舉列表
     */
    List<CarReportDto> selectReportsByReporterId(@Param("reporterId") Long reporterId);
    
    /**
     * 根據ID查詢檢舉詳情
     */
    CarReportDto selectReportById(@Param("id") Long id);
    
    /**
     * 查詢檢舉列表（分頁）
     */
    List<CarReportDto> selectCarReportList(CarReportEntity carReport);
}
