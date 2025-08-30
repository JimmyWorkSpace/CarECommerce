package cc.carce.sale.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.CarAdvertisementEntity;
import cc.carce.sale.service.CarAdvertisementService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/advertisement")
public class AdvertisementApiController {
	
	@Resource
	private CarAdvertisementService carAdvertisementService;
	
	@GetMapping("list")
	public R<List<CarAdvertisementEntity>> list() {
		return R.ok(carAdvertisementService.getHomeAdvertisements());
	}
}
