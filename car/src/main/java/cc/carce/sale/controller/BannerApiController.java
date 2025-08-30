package cc.carce.sale.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.CarAdvertisementEntity;
import cc.carce.sale.entity.CarBannerEntity;
import cc.carce.sale.service.CarAdvertisementService;
import cc.carce.sale.service.CarBannerService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/banner")
public class BannerApiController {

	@Resource
	private CarBannerService carBannerService;
	
	@GetMapping("list")
	public R<List<CarBannerEntity>> list() {
		return R.ok(carBannerService.getHomeBanners());
	}
}
