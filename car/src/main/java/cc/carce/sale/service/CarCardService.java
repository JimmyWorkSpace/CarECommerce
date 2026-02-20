package cc.carce.sale.service;

import cc.carce.sale.entity.CarCardEntity;
import cc.carce.sale.mapper.manager.CarCardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 票券方案服務（讀取 car_card，供下單與金流使用）
 */
@Slf4j
@Service
public class CarCardService {

    /** 啟用狀態 */
    public static final int STATUS_ENABLED = 1;

    @Resource
    private CarCardMapper carCardMapper;

    public CarCardEntity getById(Long id) {
        return carCardMapper.selectByPrimaryKey(id);
    }

    /**
     * 查詢啟用中的票券列表（供前台票券列表、下單使用）
     */
    public List<CarCardEntity> listEnabled() {
        Example example = new Example(CarCardEntity.class);
        example.createCriteria().andEqualTo("status", STATUS_ENABLED);
        example.orderBy("id").asc();
        return carCardMapper.selectByExample(example);
    }
}
