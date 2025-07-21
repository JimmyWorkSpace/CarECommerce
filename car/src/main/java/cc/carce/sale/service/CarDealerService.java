package cc.carce.sale.service;


import cc.carce.sale.dto.CarDealerInfoDto;
import cc.carce.sale.entity.CarDealerEntity;
import cc.carce.sale.entity.CarDealerPhotoEntity;
import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.mapper.carcecloud.CarDealerMapper;
import cc.carce.sale.mapper.carcecloud.CarDealerPhotoMapper;
import cc.carce.sale.mapper.carcecloud.CarSalesMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarDealerService {

	@Resource
	private CarDealerMapper carDealerMapper;

	@Resource
	private CarDealerPhotoMapper carDealerPhotoMapper;
	
	@Resource
	private CarSalesService carSalesService;
	
	@Resource
	private CarSalesMapper carSalesMapper;

	@Resource
	private ImageService imageService;

	public CarDealerInfoDto getInfoById(Long id) {
		CarDealerInfoDto result = new CarDealerInfoDto();
		Example example = new Example(CarDealerEntity.class);
		example.createCriteria().andEqualTo("idGarage", id);
		List<CarDealerEntity> carDealerList = carDealerMapper.selectByExample(example);
		if (CollUtil.isNotEmpty(carDealerList)) {
			CarDealerEntity cd = carDealerList.get(0);
			BeanUtil.copyProperties(cd, result);
			result.setDescription(imageService.replaceImagePrefixInHtml(result.getDescription()));
			example = new Example(CarDealerPhotoEntity.class);
			example.createCriteria().andEqualTo("idGarage", id).andEqualTo("dealerId", cd.getId());
			example.orderBy("photoOrder");
			List<CarDealerPhotoEntity> photoList = carDealerPhotoMapper.selectByExample(example);
			if (CollUtil.isNotEmpty(photoList)) {
				result.setPhotos(photoList.stream().map(p -> imageService.replaceImagePrefix(p.getPhotoUrl()))
						.collect(Collectors.toList()));
			}
		}
		return result;
	}

	/**
	 * 根据uid获取车商信息
	 * @param uid
	 * @return
	 */
	public CarDealerInfoDto getInfoByUid(String uid) {
//		Example example = new Example(CarSalesEntity.class);
//		example.createCriteria().andEqualTo("uid", uid);
//		CarSalesEntity cs = carSalesMapper.selectOneByExample(example);
		CarSalesEntity cs = carSalesMapper.getByUid(uid);
		if(cs == null) {
			return new CarDealerInfoDto();
		}
		
		CarDealerInfoDto result = new CarDealerInfoDto();
//		example = new Example(CarDealerEntity.class);
//		example.createCriteria().andEqualTo("idGarage", cs.getIdGarage());
		List<CarDealerEntity> carDealerList = carDealerMapper.selectByIdGarage(cs.getIdGarage());
		if (CollUtil.isNotEmpty(carDealerList)) {
			CarDealerEntity cd = carDealerList.get(0);
			BeanUtil.copyProperties(cd, result);
			result.setDescription(imageService.replaceImagePrefixInHtml(result.getDescription()));
//			example = new Example(CarDealerPhotoEntity.class);
//			example.createCriteria().andEqualTo("idGarage", cs.getIdGarage()).andEqualTo("dealerId", cd.getId());
//			example.orderBy("photoOrder");
			List<CarDealerPhotoEntity> photoList = carDealerPhotoMapper.selectByIdGarageAndDealerId(cs.getIdGarage(), cd.getId());
			if (CollUtil.isNotEmpty(photoList)) {
				result.setPhotos(photoList.stream().map(p -> imageService.replaceImagePrefix(p.getPhotoUrl()))
						.collect(Collectors.toList()));
			}
		}
		return result;
	}
}
