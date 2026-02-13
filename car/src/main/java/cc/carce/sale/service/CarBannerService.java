package cc.carce.sale.service;

import cc.carce.sale.entity.CarBannerEntity;
import cc.carce.sale.mapper.manager.CarBannerMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class CarBannerService {

    @Resource
    private CarBannerMapper carBannerMapper;

    /** 轮播图类型：首页轮播 */
    public static final int BANNER_TYPE_HOME = 1;
    /** 轮播图类型：商品页/商城页轮播 */
    public static final int BANNER_TYPE_MALL = 2;

    /**
     * 获取首页轮播图列表（bannerType=1）
     * 只返回未删除且按排序字段排序的banner
     * @return banner列表
     */
    public List<CarBannerEntity> getHomeBanners() {
        return getBannersByType(BANNER_TYPE_HOME);
    }

    /**
     * 获取商城页轮播图列表（bannerType=2）
     * @return banner列表
     */
    public List<CarBannerEntity> getMallBanners() {
        return getBannersByType(BANNER_TYPE_MALL);
    }

    /**
     * 按轮播图类型获取banner列表
     * @param bannerType 1 首页轮播 2 商品页轮播
     * @return banner列表
     */
    public List<CarBannerEntity> getBannersByType(Integer bannerType) {
        Example example = new Example(CarBannerEntity.class);
        example.createCriteria()
                .andEqualTo("delFlag", false)
                .andEqualTo("bannerType", bannerType);
        example.orderBy("showOrder").asc();
        return carBannerMapper.selectByExample(example);
    }

    /**
     * 根据ID获取banner
     * @param id banner ID
     * @return banner实体
     */
    public CarBannerEntity getById(Long id) {
        return carBannerMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存banner
     * @param carBanner banner实体
     * @return 是否保存成功
     */
    public boolean save(CarBannerEntity carBanner) {
        return carBannerMapper.insert(carBanner) > 0;
    }

    /**
     * 更新banner
     * @param carBanner banner实体
     * @return 是否更新成功
     */
    public boolean update(CarBannerEntity carBanner) {
        return carBannerMapper.updateByPrimaryKeySelective(carBanner) > 0;
    }

    /**
     * 删除banner（逻辑删除）
     * @param id banner ID
     * @return 是否删除成功
     */
    public boolean deleteById(Long id) {
        CarBannerEntity carBanner = new CarBannerEntity();
        carBanner.setId(id);
        carBanner.setDelFlag(true);
        return carBannerMapper.updateByPrimaryKeySelective(carBanner) > 0;
    }
} 