package com.ruoyi.car.task;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.car.service.CarSalePhotoService;
import com.ruoyi.car.service.ImageService;
import com.ruoyi.framework.service.FtpService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SyncImageDataTask {

  @Resource
  private FtpService ftpService;

  @Resource
  private ImageService imageService;

  @Resource
  private CarSalePhotoService carSalePhotoService;

  /**
   * 不用
   */
  @Deprecated
  public void syncImageData() {

    String basePath = "/img";
    try {
        String dir = "/img/car_sale";
        List<String> carSaleIds = ftpService.listFiles(dir);
        if(CollUtil.isNotEmpty(carSaleIds)){
          for(String carSaleId : carSaleIds) {
            try{
              Long carSaleIdLong = Long.parseLong(carSaleId);
              List<String> images = new ArrayList<>();
              List<String> ftpImages = ftpService.listFiles(dir + "/" + carSaleId + "/img");
              for(String img : ftpImages) {
                images.add(dir + "/" + carSaleId + "/img/" + img);
              }

              List<String> ftpVideos = ftpService.listFiles(dir + "/" + carSaleId + "/video");
              for(String video : ftpVideos) {
                if(!video.endsWith(".txt")) {
                  images.add(0, dir + "/" + carSaleId + "/video" + video);
                }else {
                  images.add(0, "https://www.youtube.com/watch?v=" + video.substring(0, video.lastIndexOf(".")));
                }
              }

              carSalePhotoService.deleteAndBatchInsert(carSaleIdLong, images);
            }catch(Exception ex){
              log.warn("图片信息同步，获取到非正常的id：", carSaleId);
            }
          }
        }
    } catch (Exception e) {
      log.error("从ftp获取图片信息失败", e);
    }
    System.out.println("开始同步图片数据");
  }
}
