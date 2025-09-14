package cc.carce.sale.mapper.carcecloud;

import cc.carce.sale.dto.CarBaseInfoDto;
import cc.carce.sale.dto.CarListDto;
import cc.carce.sale.entity.CarEntity;
import cc.carce.sale.entity.dto.CarEquipment;
import cc.carce.sale.entity.dto.CarGuarantee;
import cc.carce.sale.form.CarSalesSearchForm;
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
	
	// 车辆列表查询
	List<CarListDto> selectCarListWithCover(CarSalesSearchForm form);
	
	// 查询变速系统选项
	List<String> selectDistinctTransmissions();
	
	// 查询驱动方式选项
	List<String> selectDistinctDrivetrains();
	
	// 查询燃料系统选项
	List<String> selectDistinctFuelSystems();
}
