package cc.carce.sale.mapper.manager;

import cc.carce.sale.dto.CarReportDto;
import cc.carce.sale.entity.CarReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 車輛檢舉Mapper
 */
@Mapper
public interface CarReportMapper {
    
    /**
     * 插入檢舉記錄
     */
    int insert(CarReportEntity entity);
    
    /**
     * 根據ID查詢檢舉記錄
     */
    CarReportEntity selectById(@Param("id") Long id);
    
    /**
     * 更新檢舉記錄
     */
    int updateById(CarReportEntity entity);
    
    /**
     * 根據檢舉人ID查詢檢舉列表
     */
    List<CarReportDto> selectReportsByReporterId(@Param("reporterId") Long reporterId);
    
    /**
     * 根據ID查詢檢舉詳情
     */
    CarReportDto selectReportById(@Param("id") Long id);
}
