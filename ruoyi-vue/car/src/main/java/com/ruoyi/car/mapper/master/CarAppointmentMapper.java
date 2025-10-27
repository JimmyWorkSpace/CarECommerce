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
     * 查询预约看车列表（带车辆销售標題）
     * @param carAppointment 预约看车查询条件
     * @return 预约看车列表
     */
    List<CarAppointmentDto> selectCarAppointmentListWithSaleTitle(@Param("entity") CarAppointmentEntity carAppointment, @Param("carDbName") String carDbName);
}
