package cc.carce.sale.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor;
import cc.carce.sale.dto.CarListDto;
import cc.carce.sale.dto.RecommendCarsDto;
import cc.carce.sale.entity.CarEntity;
import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.form.CarSalesSearchForm;
import cc.carce.sale.service.CarSalesService;
import cc.carce.sale.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "汽车销售API", description = "汽车销售相关接口")
@RestController
@RequestMapping("api/car-sales")
@Slf4j
@CrossOrigin
public class CarSalesApiController {

	@Resource
	private CarSalesService carSalesService;
	
	@ApiOperation(value = "获取推荐车辆", notes = "获取系统推荐的车辆列表")
	@GetMapping("/recommend")
	public R<List<RecommendCarsDto>> recommendCars() {
		try {
			// 这里可以实现推荐算法
			CarSalesSearchForm form = new CarSalesSearchForm();
			form.setPageNum(1);
			form.setPageSize(6);
			PageInfo<CarSalesEntity> pageInfo = carSalesService.getRecommendCarsSalesByPage(form);
			return R.ok(pageInfo.getList().stream().map(cs -> {
				return RecommendCarsDto.builder()
						
						.build();
			}).collect(Collectors.toList()));
		} catch (Exception e) {
			log.error("获取推荐车辆失败", e);
			return R.fail("获取推荐车辆失败", null);
		}
	}
	
//	@ApiOperation(value = "分页查询车辆", notes = "根据条件分页查询车辆列表")
//	@PostMapping("/list")
//	public R getCarsSalesList(CarSalesSearchForm form) {
//		try {
//			PageInfo<CarSalesEntity> pageInfo = carSalesService.getRecommendCarsSalesByPage(form);
//			return R.ok("查询成功", pageInfo);
//		} catch (Exception e) {
//			log.error("分页查询车辆失败", e);
//			return R.fail("查询失败");
//		}
//	}
	
	@ApiOperation(value = "分页查询车辆列表", notes = "根据条件分页查询车辆列表")
    @PostMapping("/list")
    public R<PageInfo<CarListDto>> getCarList(@RequestBody CarSalesSearchForm form) {
        try {
            // 设置默认值
            if (form.getPageNum() == null || form.getPageNum() <= 0) {
                form.setPageNum(1);
            }

            form.setPageSize(9);
            
            PageInfo<CarListDto> pageInfo = carSalesService.getCarListByPage(form);
            return R.ok("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("分页查询车辆列表失败", e);
            return R.fail("查询失败", null);
        }
    }
}
