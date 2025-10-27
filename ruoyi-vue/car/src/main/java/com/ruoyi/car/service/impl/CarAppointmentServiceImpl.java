package com.ruoyi.car.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.car.domain.CarAppointmentEntity;
import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarAppointmentDto;
import com.ruoyi.car.mapper.master.CarAppointmentMapper;
import com.ruoyi.car.service.CarAppointmentService;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.framework.service.BaseServiceImpl;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

/**
 * 预约看车Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
@Service
@Slf4j
public class CarAppointmentServiceImpl extends BaseServiceImpl  implements CarAppointmentService
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
        Example example = new Example(CarAppointmentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 只查询未刪除的数据
        criteria.andEqualTo("delFlag", Boolean.FALSE);
        
        // 根据预约人姓名模糊查询
        if (carAppointment.getAppointmentName() != null && !carAppointment.getAppointmentName().trim().isEmpty()) {
            criteria.andLike("appointmentName", "%" + carAppointment.getAppointmentName() + "%");
        }
        
        // 根据预约人電話模糊查询
        if (carAppointment.getAppointmentPhone() != null && !carAppointment.getAppointmentPhone().trim().isEmpty()) {
            criteria.andLike("appointmentPhone", "%" + carAppointment.getAppointmentPhone() + "%");
        }
        
        // 根据预约狀態查询
        if (carAppointment.getAppointmentStatus() != null) {
            criteria.andEqualTo("appointmentStatus", carAppointment.getAppointmentStatus());
        }
        
        // 根据用户ID查询
        if (carAppointment.getUserId() != null) {
            criteria.andEqualTo("userId", carAppointment.getUserId());
        }
        
        // 根据车辆销售ID查询
        if (carAppointment.getCarSaleId() != null) {
            criteria.andEqualTo("carSaleId", carAppointment.getCarSaleId());
        }
        
        // 按建立時間倒序排列
        example.orderBy("createTime").desc();
        
        return carAppointmentMapper.selectByExample(example);
    }

    /**
     * 查询预约看车列表（带车辆销售標題）
     * 
     * @param carAppointment 预约看车
     * @return 预约看车
     */
    @Override
    public List<CarAppointmentDto> selectCarAppointmentListWithSaleTitle(CarAppointmentEntity carAppointment)
    {
        // 先查询预约列表
        List<CarAppointmentDto> appointmentList = carAppointmentMapper.selectCarAppointmentListWithSaleTitle(carAppointment, carDbName);
        
//        // 获取所有车辆销售ID
//        List<Long> carSaleIds = appointmentList.stream()
//                .map(CarAppointmentDto::getCarSaleId)
//                .distinct()
//                .collect(Collectors.toList());
//        
//        // 批量查询车辆销售信息
//        List<CarSalesEntity> carSalesList = carSalesService.selectCarSalesByIds(carSaleIds);
//        Map<Long, String> saleTitleMap = carSalesList.stream()
//                .collect(Collectors.toMap(
//                    CarSalesEntity::getId,
//                    carSales -> carSales.getSaleTitle() != null ? carSales.getSaleTitle() : "未知车辆",
//                    (existing, replacement) -> existing // 处理重复key的情况
//                ));
//        
//        // 为没有查询到的ID设置默认值
//        carSaleIds.forEach(id -> {
//            if (!saleTitleMap.containsKey(id)) {
//                saleTitleMap.put(id, "查询失败");
//            }
//        });
//        
//        // 设置车辆销售標題
//        appointmentList.forEach(appointment -> {
//            appointment.setSaleTitle(saleTitleMap.get(appointment.getCarSaleId()));
//        });
        
        return appointmentList;
    }

    /**
     * 根据用户ID查询预约列表
     * 
     * @param userId 用户ID
     * @return 预约列表
     */
    public List<CarAppointmentEntity> selectAppointmentsByUserId(Long userId)
    {
        Example example = new Example(CarAppointmentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("delFlag", Boolean.FALSE);
        example.orderBy("createTime").desc();
        return carAppointmentMapper.selectByExample(example);
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
     * 批量刪除预约看车
     * 
     * @param ids 需要刪除的预约看车主键
     * @return 结果
     */
    @Override
    public int deleteCarAppointmentByIds(Long[] ids)
    {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        
        Example example = new Example(CarAppointmentEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", java.util.Arrays.asList(ids));
        
        return carAppointmentMapper.deleteByExample(example);
    }

    /**
     * 刪除预约看车信息
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
