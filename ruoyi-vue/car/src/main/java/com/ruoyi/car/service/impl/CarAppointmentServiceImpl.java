package com.ruoyi.car.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.car.mapper.master.CarAppointmentMapper;
import com.ruoyi.car.domain.CarAppointmentEntity;
import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarAppointmentDto;
import com.ruoyi.car.service.CarAppointmentService;
import com.ruoyi.car.service.CarSalesService;
import lombok.extern.slf4j.Slf4j;

/**
 * 预约看车Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
@Service
@Slf4j
public class CarAppointmentServiceImpl implements CarAppointmentService 
{
    @Autowired
    private CarAppointmentMapper carAppointmentMapper;

    @Autowired
    private CarSalesService carSalesService;

    /**
     * 查询预约看车
     * 
     * @param id 预约看车主键
     * @return 预约看车
     */
    @Override
    public CarAppointmentEntity selectCarAppointmentById(Long id)
    {
        return carAppointmentMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询预约看车列表
     * 
     * @param carAppointment 预约看车
     * @return 预约看车
     */
    @Override
    public List<CarAppointmentEntity> selectCarAppointmentList(CarAppointmentEntity carAppointment)
    {
        return carAppointmentMapper.select(carAppointment);
    }

    /**
     * 查询预约看车列表（带车辆销售标题）
     * 
     * @param carAppointment 预约看车
     * @return 预约看车
     */
    @Override
    public List<CarAppointmentDto> selectCarAppointmentListWithSaleTitle(CarAppointmentEntity carAppointment)
    {
        // 先查询预约列表
        List<CarAppointmentDto> appointmentList = carAppointmentMapper.selectCarAppointmentListWithSaleTitle(carAppointment);
        
        // 获取所有车辆销售ID
        List<Long> carSaleIds = appointmentList.stream()
                .map(CarAppointmentDto::getCarSaleId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询车辆销售信息
        Map<Long, String> saleTitleMap = carSaleIds.stream()
                .collect(Collectors.toMap(
                    id -> id,
                    id -> {
                        try {
                            CarSalesEntity carSales = carSalesService.getById(id);
                            return carSales != null ? carSales.getSaleTitle() : "未知车辆";
                        } catch (Exception e) {
                            log.warn("查询车辆销售信息失败，ID: {}", id, e);
                            return "查询失败";
                        }
                    }
                ));
        
        // 设置车辆销售标题
        appointmentList.forEach(appointment -> {
            appointment.setSaleTitle(saleTitleMap.get(appointment.getCarSaleId()));
        });
        
        return appointmentList;
    }

    /**
     * 新增预约看车
     * 
     * @param carAppointment 预约看车
     * @return 结果
     */
    @Override
    public int insertCarAppointment(CarAppointmentEntity carAppointment)
    {
        return carAppointmentMapper.insert(carAppointment);
    }

    /**
     * 修改预约看车
     * 
     * @param carAppointment 预约看车
     * @return 结果
     */
    @Override
    public int updateCarAppointment(CarAppointmentEntity carAppointment)
    {
        return carAppointmentMapper.updateByPrimaryKey(carAppointment);
    }

    /**
     * 批量删除预约看车
     * 
     * @param ids 需要删除的预约看车主键
     * @return 结果
     */
    @Override
    public int deleteCarAppointmentByIds(Long[] ids)
    {
        int count = 0;
        for (Long id : ids) {
            count += carAppointmentMapper.deleteByPrimaryKey(id);
        }
        return count;
    }

    /**
     * 删除预约看车信息
     * 
     * @param id 预约看车主键
     * @return 结果
     */
    @Override
    public int deleteCarAppointmentById(Long id)
    {
        return carAppointmentMapper.deleteByPrimaryKey(id);
    }
}
