package cc.carce.sale.mapper.carcecloud;

import cc.carce.sale.entity.CarDealerEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface CarDealerMapper extends Mapper<CarDealerEntity> {

	@Select("select * from car_dealers cd where cd.id_garage = #{idGarage}")
	List<CarDealerEntity> selectByIdGarage(@Param("idGarage") Long idGarage);
}
