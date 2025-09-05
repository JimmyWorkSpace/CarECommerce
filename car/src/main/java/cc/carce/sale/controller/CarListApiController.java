package cc.carce.sale.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import cc.carce.sale.common.R;
import cc.carce.sale.dto.CarListDto;
import cc.carce.sale.form.CarSalesSearchForm;
import cc.carce.sale.service.CarSalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "车辆列表API", description = "车辆列表相关接口")
@RestController
@RequestMapping("/api/cars")
@Slf4j
@CrossOrigin
public class CarListApiController {

    @Resource
    private CarSalesService carSalesService;

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

    @ApiOperation(value = "获取车辆列表", notes = "根据条件获取车辆列表")
    @GetMapping("/list")
    public R<PageInfo<CarListDto>> getCarListGet(CarSalesSearchForm form) {
    	form.setPageSize(9);
        return getCarList(form);
    }
}
