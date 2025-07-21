package cc.carce.sale.service;

import cc.carce.sale.entity.CarSalePhotoEntity;
import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.mapper.carcecloud.CarSalePhotoMapper;
import cc.carce.sale.config.DsConstants;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
public class CarSalePhotoService {

  @Resource
  private CarSalePhotoMapper carSalePhotoMapper;

  @Resource
  private CarSalesService  carSalesService;

  @Deprecated
  @Transactional(rollbackFor = Exception.class , transactionManager = DsConstants.tranCarce)
  public void deleteAndBatchInsert(Long carSaleId, List<String> images) {
    CarSalesEntity cs = carSalesService.getById(carSaleId);
    carSalePhotoMapper.deleteByCarSalesId(carSaleId);

    int idx = 0;
    for(String img : images){
      CarSalePhotoEntity entity = new CarSalePhotoEntity();
      entity.setCarSalesId(carSaleId);
      entity.setPhotoUrl(img);
      entity.setIdGarage(cs.getIdGarage());
      entity.setPhotoOrder(idx++);
      entity.setIsCover(0);

      carSalePhotoMapper.insert(entity);
    }
  }

  public List<CarSalePhotoEntity> getByCarSalesId(Long id) {
    Example example = new Example(CarSalePhotoEntity.class);
    example.createCriteria().andEqualTo("carSalesId", id);
    example.orderBy("photoOrder");
    return carSalePhotoMapper.selectByExample(example);
  }
}
