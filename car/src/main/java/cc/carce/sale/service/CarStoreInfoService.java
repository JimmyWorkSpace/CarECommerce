package cc.carce.sale.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cc.carce.sale.entity.CarStoreInfoEntity;
import cc.carce.sale.mapper.manager.CarStoreInfoMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class CarStoreInfoService {
    
    @Resource
    private CarStoreInfoMapper carStoreInfoMapper;

    public List<CarStoreInfoEntity> getStoreList(String string) {
        Example example = new Example(CarStoreInfoEntity.class);
        example.createCriteria().andLike("storeAddr", string + "%");
        return carStoreInfoMapper.selectByExample(example);
    }
}
