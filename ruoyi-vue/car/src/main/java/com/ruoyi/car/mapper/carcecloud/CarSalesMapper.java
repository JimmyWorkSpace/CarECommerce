package com.ruoyi.car.mapper.carcecloud;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.dto.CarBaseInfoDto;
import com.ruoyi.car.dto.CarSalesDto;

import tk.mybatis.mapper.common.Mapper;

public interface CarSalesMapper extends Mapper<CarSalesEntity> {

	/**
	 * 根据uid查询车辆销售信息
	 * @param uid 唯一标识
	 * @return 车辆销售信息
	 */
	CarSalesEntity getByUid(@Param("uid") String uid);

	/**
	 * 根据id查询车辆销售信息
	 * @param id 主键ID
	 * @return 车辆销售信息
	 */
	CarSalesEntity getById(@Param("id") Long id);

	/**
	 * 根据id更新uid
	 * @param uid 唯一标识
	 * @param id 主键ID
	 */
	void updateUidById(@Param("uid") String uid, @Param("id") Long id);

	/**
	 * 根据条件查询车辆销售列表
	 * @param carSales 查询条件
	 * @return 车辆销售列表
	 */
	List<CarSalesDto> selectCarSalesList(@Param("entity") CarSalesEntity carSales, @Param("mgrDbName") String mgrDbName);

	/**
	 * 根据ID列表批量查询车辆销售信息
	 * @param ids ID列表
	 * @return 车辆销售列表
	 */
	List<CarSalesEntity> selectCarSalesByIds(@Param("list") List<Long> ids);

	/**
	 * 根据车辆销售ID查询车辆基本信息
	 * @param saleId 车辆销售ID
	 * @return 车辆基本信息
	 */
	CarBaseInfoDto selectCarBaseInfoBySaleId(@Param("saleId") Long saleId);

	/**
	 * 根据uid查询车辆基本信息
	 * @param uid 唯一标识
	 * @return 车辆基本信息
	 */
	CarBaseInfoDto selectCarBaseInfoByUid(@Param("uid") String uid);

}
