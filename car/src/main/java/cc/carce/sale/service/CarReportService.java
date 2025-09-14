package cc.carce.sale.service;

import cc.carce.sale.dto.CarBaseInfoDto;
import cc.carce.sale.dto.CarReportDto;
import cc.carce.sale.entity.CarReportEntity;
import cc.carce.sale.form.CarReportForm;
import cc.carce.sale.mapper.manager.CarReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 車輛檢舉服務
 */
@Slf4j
@Service
public class CarReportService {
    
    @Autowired
    private CarReportMapper carReportMapper;
    
    @Autowired
    private CarService carService;
    
    /**
     * 創建檢舉
     */
    public boolean createReport(CarReportForm form) {
        try {
            CarReportEntity entity = new CarReportEntity();
            BeanUtils.copyProperties(form, entity);
            entity.setStatus("submitted");
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            
            int result = carReportMapper.insert(entity);
            log.info("創建檢舉成功，ID: {}, 檢舉人: {}, 車輛ID: {}", 
                    entity.getId(), entity.getReporterId(), entity.getSaleId());
            return result > 0;
        } catch (Exception e) {
            log.error("創建檢舉失敗", e);
            return false;
        }
    }
    
    /**
     * 根據檢舉人ID查詢檢舉列表
     */
    public List<CarReportDto> getReportsByReporterId(Long reporterId) {
        try {
            List<CarReportDto> reports = carReportMapper.selectReportsByReporterId(reporterId);
            
            // 为每个檢舉记录查询车辆信息
            if (reports != null && !reports.isEmpty()) {
                for (CarReportDto report : reports) {
                    try {
                        CarBaseInfoDto carInfo = carService.getBaseInfoBySaleId(report.getSaleId());
                        if (carInfo != null) {
                            report.setCarTitle(carInfo.getSaleTitle());
                            report.setCarBrand(carInfo.getBrand());
                            report.setCarModel(carInfo.getCustomModel());
                            report.setCarYear(carInfo.getManufactureYear());
                        }
                    } catch (Exception e) {
                        log.warn("查询车辆信息失败，saleId: {}", report.getSaleId(), e);
                        // 设置默认值
                        report.setCarTitle("车辆信息获取失败");
                        report.setCarBrand("");
                        report.setCarModel("");
                        report.setCarYear(null);
                    }
                }
            }
            
            return reports;
        } catch (Exception e) {
            log.error("查詢檢舉列表失敗，檢舉人ID: {}", reporterId, e);
            return null;
        }
    }
    
    /**
     * 根據ID查詢檢舉詳情
     */
    public CarReportDto getReportById(Long id) {
        try {
            CarReportDto report = carReportMapper.selectReportById(id);
            
            // 查询车辆信息
            if (report != null) {
                try {
                    CarBaseInfoDto carInfo = carService.getBaseInfoBySaleId(report.getSaleId());
                    if (carInfo != null) {
                        report.setCarTitle(carInfo.getSaleTitle());
                        report.setCarBrand(carInfo.getBrand());
                        report.setCarModel(carInfo.getCustomModel());
                        report.setCarYear(carInfo.getManufactureYear());
                    }
                } catch (Exception e) {
                    log.warn("查询车辆信息失败，saleId: {}", report.getSaleId(), e);
                    // 设置默认值
                    report.setCarTitle("车辆信息获取失败");
                    report.setCarBrand("");
                    report.setCarModel("");
                    report.setCarYear(null);
                }
            }
            
            return report;
        } catch (Exception e) {
            log.error("查詢檢舉詳情失敗，ID: {}", id, e);
            return null;
        }
    }
    
    /**
     * 更新檢舉狀態
     */
    public boolean updateReportStatus(Long id, String status, String processNote, Long processorId) {
        try {
            CarReportEntity entity = carReportMapper.selectById(id);
            if (entity == null) {
                log.warn("檢舉不存在，ID: {}", id);
                return false;
            }
            
            entity.setStatus(status);
            entity.setProcessNote(processNote);
            entity.setProcessorId(processorId);
            entity.setProcessedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
            
            int result = carReportMapper.updateById(entity);
            log.info("更新檢舉狀態成功，ID: {}, 狀態: {}", id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新檢舉狀態失敗，ID: {}", id, e);
            return false;
        }
    }
    
    /**
     * 撤銷檢舉
     */
    public boolean cancelReport(Long id) {
        try {
            CarReportEntity entity = carReportMapper.selectById(id);
            if (entity == null) {
                log.warn("檢舉不存在，ID: {}", id);
                return false;
            }
            
            // 更新狀態為已撤銷
            entity.setStatus("cancelled");
            entity.setUpdatedAt(LocalDateTime.now());
            
            int result = carReportMapper.updateById(entity);
            log.info("撤銷檢舉成功，ID: {}", id);
            return result > 0;
        } catch (Exception e) {
            log.error("撤銷檢舉失敗，ID: {}", id, e);
            return false;
        }
    }
}
