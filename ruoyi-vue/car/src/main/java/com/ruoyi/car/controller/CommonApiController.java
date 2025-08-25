package com.ruoyi.car.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.car.dto.CarBaseInfoDto;
import com.ruoyi.car.dto.CarDealerInfoDto;
import com.ruoyi.car.dto.CarEquipmentDto;
import com.ruoyi.car.dto.CarGuaranteeDto;
import com.ruoyi.car.service.CarDealerService;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.car.service.CarService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.framework.dto.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(value = "api")
@CrossOrigin(origins = "*")
@Slf4j
public class CommonApiController extends BaseController {
	
	@Value("${carce.webUrl}")
	private String webUrl;

	@Resource
	private CarService carService;

	@Resource
	private CarSalesService carSalesService;

	@Resource
	private CarDealerService carDealerService;

	/**
	 * 車輛分享連結生成(棄用)
	 * @param id
	 * @return
	 */
	@GetMapping("car/sharePath/{id}")
	@Deprecated
	public RedirectView shareRedirect(@PathVariable("id") Long id) {
		String uid = carSalesService.getUidById(id);
		if (uid != null) {
			return new RedirectView(webUrl + "/" + uid);
		}
		        return new RedirectView("/error"); // 跳轉到錯誤頁
	}

	/**
	 * 車輛基本資訊
	 * 
	 * @param uid
	 * @return
	 */
	@GetMapping("car/baseInfo/{uid}")
	public R<CarBaseInfoDto> baseInfo(@PathVariable("uid") String uid) {
		CarBaseInfoDto car = carService.getBaseInfoByUidId(uid);
		return new R<>(car);
	}

	/**
	 * 車輛配備
	 * 
	 * @param uid
	 * @return
	 */
	@GetMapping("car/equipment/{uid}")
	public R<List<CarEquipmentDto>> equipment(@PathVariable("uid") String uid) {
		List<CarEquipmentDto> result = carService.getEquipmentByUidId(uid);
		return new R<>(result);
	}

	/**
	 * 賣家保證
	 * 
	 * @param uid
	 * @return
	 */
	@GetMapping("car/guarantee/{uid}")
	public R<List<CarGuaranteeDto>> guarantee(@PathVariable("uid") String uid) {
		List<CarGuaranteeDto> result = carService.getGuaranteeByUid(uid);
		return new R<>(result);
	}

	/**
	 * 車輛賣家資訊
	 * @param uid
	 * @return
	 */
	@GetMapping("car/dealer/{uid}")
	public R<CarDealerInfoDto> dealer(@PathVariable("uid") String uid) {
		CarDealerInfoDto result = carDealerService.getInfoByUid(uid);
		return new R<>(result);
	}

	/**
	 * 車輛圖片
	 * @param uid
	 * @return
	 */
	@GetMapping("car/image/{uid}")
	public R<List<String>> images(@PathVariable("uid") String uid){
		return new R<>(carService.getImagesByUid(uid));
	}

	/**
	 * 車輛影片
	 * @param uid
	 * @return
	 */
	@GetMapping("car/video/{uid}")
	public R<List<String>> video(@PathVariable("uid") String uid){
		return new R<>(carService.getVideoByUid(uid));
	}
}
