package com.ruoyi.car.service;

import java.util.List;
import com.ruoyi.car.domain.CarAppointmentEntity;
import com.ruoyi.car.dto.CarAppointmentDto;

/**
 * 预约看车Service接口
 * 
 * @author ruoyi
 * @date 2025-01-16
 */
public interface CarAppointmentService 
{
    /**
     * 查询预约看车
     * 
     * @param id 预约看车主键
     * @return 预约看车
     */
    public CarAppointmentEntity selectCarAppointmentById(Long id);

    /**
     * 查询预约看车列表
     * 
     * @param carAppointment 预约看车
     * @return 预约看车集合
     */
    public List<CarAppointmentEntity> selectCarAppointmentList(CarAppointmentEntity carAppointment);

    /**
     * 查询预约看车列表（带车辆销售標題）
     * 
     * @param carAppointment 预约看车
     * @return 预约看车集合
     */
    public List<CarAppointmentDto> selectCarAppointmentListWithSaleTitle(CarAppointmentEntity carAppointment);

    /**
     * 根据用户ID查询预约列表
     * 
     * @param userId 用户ID
     * @return 预约列表
     */
    public List<CarAppointmentEntity> selectAppointmentsByUserId(Long userId);

    /**
     * 新增预约看车
     * 
     * @param carAppointment 预约看车
     * @return 结果
     */
    public int insertCarAppointment(CarAppointmentEntity carAppointment);

    /**
     * 修改预约看车
     * 
     * @param carAppointment 预约看车
     * @return 结果
     */
    public int updateCarAppointment(CarAppointmentEntity carAppointment);

    /**
     * 批量刪除预约看车
     * 
     * @param ids 需要刪除的预约看车主键集合
     * @return 结果
     */
    public int deleteCarAppointmentByIds(Long[] ids);

    /**
     * 刪除预约看车信息
     * 
     * @param id 预约看车主键
     * @return 结果
     */
    public int deleteCarAppointmentById(Long id);
}
