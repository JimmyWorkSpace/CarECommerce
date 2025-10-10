package cc.carce.sale.service;

import cc.carce.sale.entity.CarAdvertisementEntity;
import cc.carce.sale.mapper.manager.CarAdvertisementMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class CarAdvertisementService {

    @Resource
    private CarAdvertisementMapper carAdvertisementMapper;

    /**
     * 获取首页广告列表
     * 只返回未删除且按排序字段排序的前两个广告
     * @return 广告列表
     */
    public List<CarAdvertisementEntity> getHomeAdvertisements() {
        Example example = new Example(CarAdvertisementEntity.class);
        example.createCriteria()
                .andEqualTo("delFlag", 0);  // 0表示未删除
        example.orderBy("showOrder").asc();    // 按排序字段升序排列
        List<CarAdvertisementEntity> result = carAdvertisementMapper.selectByExample(example);
        return result;
    }

    /**
     * 根据ID获取广告
     * @param id 广告 ID
     * @return 广告实体
     */
    public CarAdvertisementEntity getById(Long id) {
        return carAdvertisementMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存广告
     * @param carAdvertisement 广告实体
     * @return 是否保存成功
     */
    public boolean save(CarAdvertisementEntity carAdvertisement) {
        return carAdvertisementMapper.insert(carAdvertisement) > 0;
    }

    /**
     * 更新广告
     * @param carAdvertisement 广告实体
     * @return 是否更新成功
     */
    public boolean update(CarAdvertisementEntity carAdvertisement) {
        return carAdvertisementMapper.updateByPrimaryKeySelective(carAdvertisement) > 0;
    }

    /**
     * 删除广告（逻辑删除）
     * @param id 广告 ID
     * @return 是否删除成功
     */
    public boolean deleteById(Long id) {
        CarAdvertisementEntity carAdvertisement = new CarAdvertisementEntity();
        carAdvertisement.setId(id);
        carAdvertisement.setDelFlag(1);  // 1表示已删除
        return carAdvertisementMapper.updateByPrimaryKeySelective(carAdvertisement) > 0;
    }
} 