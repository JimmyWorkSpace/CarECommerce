package cc.carce.sale.mapper.carcecloud;

import cc.carce.sale.dto.CarBaseInfoDto;
import cc.carce.sale.entity.CarEntity;
import cc.carce.sale.entity.dto.CarEquipment;
import cc.carce.sale.entity.dto.CarGuarantee;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface CarMapper extends Mapper<CarEntity> {

	CarBaseInfoDto selectCarBaseInfoBySaleId(@Param("saleId") Long saleId);

	List<CarEquipment> selectCarEquipmentBySaleId(@Param("saleId") Long saleId);

	List<CarGuarantee> selectCarGuaranteeBySaleId(@Param("saleId") Long saleId);

	CarBaseInfoDto selectCarBaseInfoByUid(@Param("uid") String uid);

	List<CarEquipment> selectCarEquipmentByUid(@Param("uid") String uid);

	List<CarGuarantee> selectCarGuaranteeByUid(String uid);
}
