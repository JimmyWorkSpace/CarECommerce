package com.ruoyi.car.mapper.master;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarAppointmentEntity;
import com.ruoyi.car.dto.CarAppointmentDto;

import tk.mybatis.mapper.common.Mapper;

/**
 * 预约看车Mapper接口
 */
public interface CarAppointmentMapper extends Mapper<CarAppointmentEntity> {

    /**
     * 根据用户ID查询预约列表
     * @param userId 用户ID
     * @return 预约列表
     */
    List<CarAppointmentDto> selectAppointmentsByUserId(@Param("userId") Long userId);

    /**
     * 根据ID查询预约详情
     * @param id 预约ID
     * @return 预约详情
     */
    CarAppointmentDto selectAppointmentById(@Param("id") Long id);

    /**
     * 更新预约状态
     * @param id 预约ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateAppointmentStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 查询预约看车列表（带车辆销售标题）
     * @param carAppointment 预约看车查询条件
     * @return 预约看车列表
     */
    List<CarAppointmentDto> selectCarAppointmentListWithSaleTitle(CarAppointmentEntity carAppointment);
}
