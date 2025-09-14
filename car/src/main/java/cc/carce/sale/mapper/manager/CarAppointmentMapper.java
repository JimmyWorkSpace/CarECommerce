package cc.carce.sale.mapper.manager;

import cc.carce.sale.dto.CarAppointmentDto;
import cc.carce.sale.entity.CarAppointmentEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

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
}
