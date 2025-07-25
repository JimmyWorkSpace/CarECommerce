package com.ruoyi.car.service.impl;

import com.ruoyi.car.domain.CarSalePhotoEntity;
import com.ruoyi.car.domain.CarSalesEntity;
import com.ruoyi.car.mapper.carcecloud.CarSalePhotoMapper;
import com.ruoyi.car.service.CarSalePhotoService;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.common.constant.DsConstants;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CarSalePhotoServiceImpl implements CarSalePhotoService {

  @Resource
  private CarSalePhotoMapper carSalePhotoMapper;

  @Resource
  private CarSalesService  carSalesService;

  @Deprecated
  @Override
  @Transactional(rollbackFor = Exception.class , transactionManager = DsConstants.tranCarceCloud)
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
}
