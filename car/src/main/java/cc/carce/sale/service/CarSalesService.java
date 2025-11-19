package cc.carce.sale.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

import cc.carce.sale.dto.CarListDto;
import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.entity.CarRecommandEntity;
import cc.carce.sale.entity.CarDealerEntity;
import cc.carce.sale.form.CarSalesSearchForm;
import cc.carce.sale.mapper.carcecloud.CarMapper;
import cc.carce.sale.mapper.carcecloud.CarSalesMapper;
import cc.carce.sale.mapper.carcecloud.CarDealerMapper;
import cc.carce.sale.mapper.manager.CarRecommandMapper;
import cc.carce.sale.util.ImageUrlUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarSalesService {

	@Resource
	private CarSalesMapper carSalesMapper;
	
	@Resource
	private CarMapper carMapper;
	
	@Resource
	private CarRecommandMapper carRecommandMapper;
	
	@Resource
	private CarDealerMapper carDealerMapper;
	
	@Resource
	private ImageUrlUtil imageUrlUtil;

	/**
	 * 根据id获取uid,  如果uid为空则生成一个
	 * @param id
	 * @return
	 */
	public String getUidById(Long id) {
		CarSalesEntity cs = carSalesMapper.getById(id);
		if (cs != null) {
			if (StrUtil.isBlank(cs.getUid())) {
				cs.setUid(generateShortCode());
				carSalesMapper.updateUidById(cs.getUid(), cs.getId());
			}
			return cs.getUid();
		}
		return null;
	}

	/**
	 * 生成短码
	 * @return
	 */
	private String generateShortCode() {
		String shortId = "";
		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		do {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 4; i++) {
				int index = (int) (Math.random() * letters.length());
				sb.append(letters.charAt(index));
			}
			shortId = sb.toString();
		}
		while (getByUid(shortId) != null);
		return shortId;
	}

	public CarSalesEntity getByUid(String uid) {
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria()
			.andEqualTo("uid", uid)
			.andEqualTo("status", "上架");
		return carSalesMapper.selectOneByExample(example);
	}

	public CarSalesEntity getById(Long carSaleId) {
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria().andEqualTo("id", carSaleId);
		return carSalesMapper.selectOneByExample(example);
	}

	public PageInfo<CarSalesEntity> getRecommendCarsSalesByPage(CarSalesSearchForm form) {
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria()
			.andGreaterThan("recommendedValue", 0L)
			.andEqualTo("status", "上架");
		example.orderBy("recommendedValue").desc();
		PageInfo<CarSalesEntity> pi = PageHelper
				.startPage(form.getPageNum(), form.getPageSize())
				.doSelectPageInfo(() -> {
			carSalesMapper.selectByExample(example);
		});
		return pi;
	}
	
	/**
	 * 分页查询车辆列表
	 * @param form 搜索条件
	 * @return 分页结果
	 */
	public PageInfo<CarListDto> getCarListByPage(CarSalesSearchForm form) {
		PageInfo<CarListDto> pageInfo = PageHelper.startPage(form.getPageNum(), form.getPageSize())
		.doSelectPageInfo(() -> {
			carMapper.selectCarListWithCover(form);
		});
		
		// 处理coverImage字段，添加前缀并转换为缩略图
		if (pageInfo.getList() != null) {
			for (CarListDto car : pageInfo.getList()) {
				if (StrUtil.isNotBlank(car.getCoverImage())) {
					car.setCoverImage(imageUrlUtil.getThumbnailUrlWithPrefix(car.getCoverImage()));
				}
			}
		}
		
		return pageInfo;
	}
	
	/**
	 * 获取推荐车辆列表（用于首页精选好车）
	 * @param limit 限制数量
	 * @return 推荐车辆列表
	 */
	public List<CarSalesEntity> getRecommendedCars(int limit) {
		// 查询推荐类型为1的推荐记录
		Example recommandExample = new Example(CarRecommandEntity.class);
		recommandExample.createCriteria()
			.andEqualTo("recommandType", 1)
			.andEqualTo("delFlag", 0);
			recommandExample.orderBy("showOrder").asc();
			
			List<CarRecommandEntity> recommandList = carRecommandMapper.selectByExample(recommandExample);
			
			if (recommandList == null || recommandList.isEmpty()) {
				return new ArrayList<>();
			}
			
			// 提取推荐ID列表
			List<Long> recommandIds = new ArrayList<>();
			for (CarRecommandEntity recommand : recommandList) {
				recommandIds.add(recommand.getRecommandId());
			}
			
			// 查询对应的车辆销售信息
			Example salesExample = new Example(CarSalesEntity.class);
			salesExample.createCriteria()
			.andEqualTo("isAdminCheck", 1)
			.andEqualTo("isPublish", 1)
			.andIn("id", recommandIds);
			//			.andEqualTo("status", "上架")
			//			.andEqualTo("isVisible", 1);
		
		List<CarSalesEntity> salesList = carSalesMapper.selectByExample(salesExample);
		
		// 按照推荐顺序排序
		List<CarSalesEntity> orderedSalesList = new ArrayList<>();
		for (CarRecommandEntity recommand : recommandList) {
			for (CarSalesEntity sales : salesList) {
				if (sales.getId().equals(recommand.getRecommandId())) {
					orderedSalesList.add(sales);
					break;
				}
			}
		}
		
		// 限制返回数量
		if (orderedSalesList.size() > limit) {
			return orderedSalesList.subList(0, limit);
		}
		
		return orderedSalesList;
	}
	
	/**
	 * 获取推荐店家列表（用于首页精选店家）
	 * @param limit 限制数量
	 * @return 推荐店家列表
	 */
	public List<CarDealerEntity> getRecommendedDealers(int limit) {
		// 查询推荐类型为0的推荐记录（店家推荐）
		Example recommandExample = new Example(CarRecommandEntity.class);
		recommandExample.createCriteria()
			.andEqualTo("recommandType", 0)
			.andEqualTo("delFlag", 0);
		recommandExample.orderBy("showOrder").asc();
		
		List<CarRecommandEntity> recommandList = carRecommandMapper.selectByExample(recommandExample);
		
		if (recommandList == null || recommandList.isEmpty()) {
			return new ArrayList<>();
		}
		
		// 提取推荐ID列表
		List<Long> recommandIds = new ArrayList<>();
		for (CarRecommandEntity recommand : recommandList) {
			recommandIds.add(recommand.getRecommandId());
		}
		
		// 查询对应的店家信息
		Example dealerExample = new Example(CarDealerEntity.class);
		dealerExample.createCriteria()
			.andIn("id", recommandIds);
		
		List<CarDealerEntity> dealerList = carDealerMapper.selectByExample(dealerExample);
		
		// 按照推荐顺序排序
		List<CarDealerEntity> orderedDealerList = new ArrayList<>();
		for (CarRecommandEntity recommand : recommandList) {
			for (CarDealerEntity dealer : dealerList) {
				if (dealer.getId().equals(recommand.getRecommandId())) {
					orderedDealerList.add(dealer);
					break;
				}
			}
		}
		
		// 限制返回数量
		if (orderedDealerList.size() > limit) {
			return orderedDealerList.subList(0, limit);
		}
		
		return orderedDealerList;
	}

    public List<CarListDto> getAllCars() {
        CarSalesSearchForm form = new CarSalesSearchForm();
        form.setPageNum(1);
        form.setPageSize(1000);
        List<CarListDto> list = getCarListByPage(form).getList();
        
        // 批量查询经销商信息，避免N+1查询问题
        if (list != null && !list.isEmpty()) {
            // 收集所有需要查询的garageId
            List<Long> garageIds = list.stream()
                .map(CarListDto::getGarageId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            
            if (!garageIds.isEmpty()) {
                // 批量查询经销商信息
                Example dealerExample = new Example(CarDealerEntity.class);
                dealerExample.createCriteria().andIn("idGarage", garageIds);
                List<CarDealerEntity> dealers = carDealerMapper.selectByExample(dealerExample);
                
                // 创建ID到经销商名称的映射
                Map<Long, String> dealerNameMap = dealers.stream()
                    .collect(Collectors.toMap(
                        CarDealerEntity::getIdGarage,
                        CarDealerEntity::getDealerName,
                        (existing, replacement) -> existing
                    ));
                
                // 设置经销商名称
                for (CarListDto car : list) {
                    if (car.getGarageId() != null) {
                        String dealerName = dealerNameMap.get(car.getGarageId());
                        if (dealerName != null) {
                            car.setDealerName(dealerName);
                        }
                    }
                }
            }
        }
        
		return list;
    }
    
    /**
     * 根据店家ID获取车辆列表
     * @param idGarage 店家ID
     * @return 车辆列表
     */
    public List<CarListDto> getCarsByGarageId(Long idGarage) {
        CarSalesSearchForm form = new CarSalesSearchForm();
        form.setIdGarage(idGarage);
        form.setPageNum(1);
        form.setPageSize(100); // 获取足够多的车辆
        PageInfo<CarListDto> pageInfo = getCarListByPage(form);
        return pageInfo.getList() != null ? pageInfo.getList() : new ArrayList<>();
    }
}
