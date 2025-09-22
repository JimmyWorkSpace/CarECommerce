package cc.carce.sale.service;

import cc.carce.sale.dto.CarAppointmentDto;
import cc.carce.sale.dto.CarBaseInfoDto;
import cc.carce.sale.entity.CarAppointmentEntity;
import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.form.CarAppointmentForm;
import cc.carce.sale.mapper.manager.CarAppointmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 预约看车服务类
 */
@Service
@Slf4j
public class CarAppointmentService {

    @Resource
    private CarAppointmentMapper carAppointmentMapper;
    
    @Resource
    private CarService carService;
    
    @Resource
    private CarSalesService carSalesService;
    
    @Resource
    private SmsService smsService;
    
    private String dealerMobile = "0975760203";

    /**
     * 创建预约
     * @param form 预约表单
     * @param userId 用户ID
     * @return 预约ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createAppointment(CarAppointmentForm form, Long userId) {
        log.info("创建预约，用户ID: {}, 车辆销售ID: {}", userId, form.getCarSaleId());
        
        CarAppointmentEntity appointment = new CarAppointmentEntity();
        appointment.setCarSaleId(form.getCarSaleId());
        appointment.setUserId(userId);
        appointment.setAppointmentName(form.getAppointmentName());
        appointment.setAppointmentPhone(form.getAppointmentPhone());
        appointment.setAppointmentTime(form.getAppointmentTime());
        appointment.setAppointmentNote(form.getAppointmentNote());
        appointment.setAppointmentStatus(CarAppointmentEntity.AppointmentStatus.APPOINTED.getCode());
        appointment.setDelFlag(false);
        appointment.setCreateTime(new Date());
        appointment.setUpdateTime(new Date());
        
        int result = carAppointmentMapper.insert(appointment);
        if (result > 0) {
            log.info("预约创建成功，预约ID: {}", appointment.getId());
            
            // 发送短信通知
            try {
                sendAppointmentNotifications(appointment, form);
            } catch (Exception e) {
                log.error("发送预约通知短信失败，预约ID: {}, 错误: {}", appointment.getId(), e.getMessage(), e);
                // 短信发送失败不影响预约创建成功
            }
            
            return appointment.getId();
        } else {
            log.error("预约创建失败");
            throw new RuntimeException("预约创建失败");
        }
    }

    /**
     * 根据用户ID查询预约列表
     * @param userId 用户ID
     * @return 预约列表
     */
    public List<CarAppointmentDto> getAppointmentsByUserId(Long userId) {
        log.info("查询用户预约列表，用户ID: {}", userId);
        
        // 先查询预约数据
        List<CarAppointmentDto> appointments = carAppointmentMapper.selectAppointmentsByUserId(userId);
        
        // 为每个预约查询车辆信息
        for (CarAppointmentDto appointment : appointments) {
            try {
                // 根据carSaleId查询车辆销售信息
                CarSalesEntity carSales = carSalesService.getById(appointment.getCarSaleId());
                if (carSales != null) {
                    appointment.setCarTitle(carSales.getSaleTitle());
                    appointment.setCarPrice(carSales.getSalePrice());
                    
                    // 查询车辆基本信息
                    CarBaseInfoDto carBaseInfo = carService.getBaseInfoBySaleId(appointment.getCarSaleId());
                    if (carBaseInfo != null) {
                        appointment.setCarBrand(carBaseInfo.getBrand());
                        appointment.setCarModel(carBaseInfo.getCustomModel());
                    }
                    
                    // 查询车辆封面图片
                    List<String> images = carService.getImagesBySaleId(appointment.getCarSaleId());
                    if (images != null && !images.isEmpty()) {
                        appointment.setCarCoverImage(images.get(0)); // 取第一张图片作为封面
                    }
                }
            } catch (Exception e) {
                log.error("查询车辆信息失败，carSaleId: {}, 错误: {}", appointment.getCarSaleId(), e.getMessage());
                // 设置默认值
                appointment.setCarTitle("车辆信息获取失败");
                appointment.setCarBrand("未知品牌");
                appointment.setCarModel("未知型号");
                appointment.setCarPrice(0);
                appointment.setCarCoverImage("/img/car/car6.jpg");
            }
        }
        
        return appointments;
    }

    /**
     * 根据ID查询预约详情
     * @param id 预约ID
     * @return 预约详情
     */
    public CarAppointmentDto getAppointmentById(Long id) {
        log.info("查询预约详情，预约ID: {}", id);
        
        // 先查询预约数据
        CarAppointmentDto appointment = carAppointmentMapper.selectAppointmentById(id);
        
        if (appointment != null) {
            try {
                // 根据carSaleId查询车辆销售信息
                CarSalesEntity carSales = carSalesService.getById(appointment.getCarSaleId());
                if (carSales != null) {
                    appointment.setCarTitle(carSales.getSaleTitle());
                    appointment.setCarPrice(carSales.getSalePrice());
                    
                    // 查询车辆基本信息
                    CarBaseInfoDto carBaseInfo = carService.getBaseInfoBySaleId(appointment.getCarSaleId());
                    if (carBaseInfo != null) {
                        appointment.setCarBrand(carBaseInfo.getBrand());
                        appointment.setCarModel(carBaseInfo.getCustomModel());
                    }
                    
                    // 查询车辆封面图片
                    List<String> images = carService.getImagesBySaleId(appointment.getCarSaleId());
                    if (images != null && !images.isEmpty()) {
                        appointment.setCarCoverImage(images.get(0)); // 取第一张图片作为封面
                    }
                }
            } catch (Exception e) {
                log.error("查询车辆信息失败，carSaleId: {}, 错误: {}", appointment.getCarSaleId(), e.getMessage());
                // 设置默认值
                appointment.setCarTitle("车辆信息获取失败");
                appointment.setCarBrand("未知品牌");
                appointment.setCarModel("未知型号");
                appointment.setCarPrice(0);
                appointment.setCarCoverImage("/img/car/car6.jpg");
            }
        }
        
        return appointment;
    }

    /**
     * 更新预约状态
     * @param id 预约ID
     * @param status 新状态
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAppointmentStatus(Long id, Integer status) {
        log.info("更新预约状态，预约ID: {}, 新状态: {}", id, status);
        
        int result = carAppointmentMapper.updateAppointmentStatus(id, status);
        if (result > 0) {
            log.info("预约状态更新成功");
            return true;
        } else {
            log.error("预约状态更新失败");
            return false;
        }
    }

    /**
     * 取消预约
     * @param id 预约ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAppointment(Long id) {
        log.info("取消预约，预约ID: {}", id);
        return updateAppointmentStatus(id, CarAppointmentEntity.AppointmentStatus.CANCELLED.getCode());
    }

    /**
     * 确认看车
     * @param id 预约ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmViewing(Long id) {
        log.info("确认看车，预约ID: {}", id);
        return updateAppointmentStatus(id, CarAppointmentEntity.AppointmentStatus.VIEWED.getCode());
    }
    
    /**
     * 发送预约通知短信
     * @param appointment 预约实体
     * @param form 预约表单
     */
    private void sendAppointmentNotifications(CarAppointmentEntity appointment, CarAppointmentForm form) {
        try {
            // 格式化预约时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedTime = dateFormat.format(appointment.getAppointmentTime());
            
            // 获取车辆信息用于短信内容
            String carTitle = "车辆";
            try {
                CarSalesEntity carSales = carSalesService.getById(appointment.getCarSaleId());
                if (carSales != null) {
                    carTitle = carSales.getSaleTitle();
                }
            } catch (Exception e) {
                log.warn("获取车辆信息失败，使用默认标题，carSaleId: {}", appointment.getCarSaleId());
            }
            
            // 1. 发送短信给客户
            String customerMsg = String.format(
                    "親愛的 %s 您好，預約已完成！車輛: %s, 時間: %s, 如有疑問請聯繫客服。",
                    appointment.getAppointmentName(), carTitle, formattedTime
            );
            smsService.sendSms(appointment.getAppointmentPhone(), customerMsg);
            log.info("客户预约确认短信发送成功，手机号: {}", appointment.getAppointmentPhone());
            
            // 2. 发送短信给后台管理员
            String dealerMsg = String.format(
                    "有新的預約: 姓名: %s, 電話: %s, 車輛: %s, 時間: %s",
                    appointment.getAppointmentName(), appointment.getAppointmentPhone(), carTitle, formattedTime
            );
            smsService.sendSms(dealerMobile, dealerMsg);
            log.info("管理员预约通知短信发送成功，手机号: {}", dealerMobile);
            
        } catch (Exception e) {
            log.error("发送预约通知短信异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}
