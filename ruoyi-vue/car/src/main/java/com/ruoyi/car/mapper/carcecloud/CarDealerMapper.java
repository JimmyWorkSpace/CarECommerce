package com.ruoyi.car.mapper.carcecloud;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ruoyi.car.domain.CarDealerEntity;

import tk.mybatis.mapper.common.Mapper;

public interface CarDealerMapper extends Mapper<CarDealerEntity> {

	@Select("select * from car_dealers cd where cd.id_garage = #{idGarage}")
	List<CarDealerEntity> selectByIdGarage(@Param("idGarage") Long idGarage);

	/**
	 * 查询經銷商列表（用于精选卖家，包含过滤条件）
	 * 过滤条件：
	 * 1. car_dealers表的id_garage相同的只取第一条（id最小的）
	 * 2. id_garage需要存在于car_sales表
	 * 3. car_sales表的is_admin_check为1
	 * 
	 * @param dealerName 經銷商名稱（可選，用於模糊查詢）
	 * @param contactPerson 聯繫人（可選，用於模糊查詢）
	 * @param companyPhone 公司電話（可選，用於模糊查詢）
	 * @return 經銷商集合
	 */
	@Select("<script>" +
			"SELECT cd.* FROM car_dealers cd " +
			"INNER JOIN (" +
			"    SELECT id_garage, MIN(id) as min_id " +
			"    FROM car_dealers " +
			"    GROUP BY id_garage" +
			") grouped ON cd.id_garage = grouped.id_garage AND cd.id = grouped.min_id " +
			"INNER JOIN car_sales cs ON cd.id_garage = cs.id_garage " +
			"WHERE cs.is_admin_check = 1 " +
			"<if test='dealerName != null and dealerName != \"\"'>" +
			"    AND cd.dealer_name LIKE CONCAT('%', #{dealerName}, '%') " +
			"</if>" +
			"<if test='contactPerson != null and contactPerson != \"\"'>" +
			"    AND cd.contact_person LIKE CONCAT('%', #{contactPerson}, '%') " +
			"</if>" +
			"<if test='companyPhone != null and companyPhone != \"\"'>" +
			"    AND cd.company_phone LIKE CONCAT('%', #{companyPhone}, '%') " +
			"</if>" +
			"ORDER BY cd.c_dt DESC" +
			"</script>")
	List<CarDealerEntity> selectCarDealerListForRecommend(
			@Param("dealerName") String dealerName,
			@Param("contactPerson") String contactPerson,
			@Param("companyPhone") String companyPhone);
}
