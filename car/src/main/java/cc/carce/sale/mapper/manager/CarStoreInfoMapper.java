package cc.carce.sale.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cc.carce.sale.entity.CarStoreInfoEntity;
import tk.mybatis.mapper.common.Mapper;

public interface CarStoreInfoMapper extends Mapper<CarStoreInfoEntity>{

    void batchInsert(@Param("list") List<CarStoreInfoEntity> list);

    @Delete("DELETE FROM car_store_list")
    void deleteAll();

    @Select("select * from car_store_list where StoreId = #{storeId} limit 1")
    CarStoreInfoEntity selectByStoreId(@Param("storeId") String storeId);
    
}
