//package cc.carce.sale.controller;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import cc.carce.sale.common.R;
//import cc.carce.sale.dto.CarBaseInfoDto;
//import cc.carce.sale.dto.CarDealerInfoDto;
//import cc.carce.sale.dto.CarEquipmentDto;
//import cc.carce.sale.dto.CarGuaranteeDto;
//import cc.carce.sale.service.CarDealerService;
//import cc.carce.sale.service.CarSalesService;
//import cc.carce.sale.service.CarService;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping(value = "api")
//@CrossOrigin(origins = "*")
//@Slf4j
//public class CommonApiController {
//	
//	@Value("${carce.webUrl}")
//	private String webUrl;
//
//	@Resource
//	private CarService carService;
//
//	@Resource
//	private CarSalesService carSalesService;
//
//	@Resource
//	private CarDealerService carDealerService;
//
//	/**
//	 * 车辆分享链接生成(弃用)
//	 * @param id
//	 * @return
//	 */
//	@GetMapping("/sharePath/{id}")
//	public String shareRedirect(@PathVariable("id") Long id) {
//		String uid = carSalesService.getUidById(id);
//		if (uid != null) {
//			return webUrl + "/" + uid;
//		}
//		return ""; // 跳转到错误页
//	}
//
//	/**
//	 * 车辆基本信息
//	 * 
//	 * @param uid
//	 * @return
//	 */
//	@GetMapping("/baseInfo/{uid}")
//	public R<CarBaseInfoDto> baseInfo(@PathVariable("uid") String uid) {
//		CarBaseInfoDto car = carService.getBaseInfoByUidId(uid);
//		return new R<>(car);
//	}
//
//	/**
//	 * 车辆配备
//	 * 
//	 * @param uid
//	 * @return
//	 */
//	@GetMapping("/equipment/{uid}")
//	public R<List<CarEquipmentDto>> equipment(@PathVariable("uid") String uid) {
//		List<CarEquipmentDto> result = carService.getEquipmentByUidId(uid);
//		return new R<>(result);
//	}
//
//	/**
//	 * 卖家保证
//	 * 
//	 * @param uid
//	 * @return
//	 */
//	@GetMapping("/guarantee/{uid}")
//	public R<List<CarGuaranteeDto>> guarantee(@PathVariable("uid") String uid) {
//		List<CarGuaranteeDto> result = carService.getGuaranteeByUid(uid);
//		return new R<>(result);
//	}
//
//	/**
//	 * 车辆卖家信息
//	 * @param uid
//	 * @return
//	 */
//	@GetMapping("/dealer/{uid}")
//	public R<CarDealerInfoDto> dealer(@PathVariable("uid") String uid) {
//		CarDealerInfoDto result = carDealerService.getInfoByUid(uid);
//		return new R<>(result);
//	}
//
//	/**
//	 * 车辆图片
//	 * @param uid
//	 * @return
//	 */
//	@GetMapping("/image/{uid}")
//	public R<List<String>> images(@PathVariable("uid") String uid){
//		return new R<>(carService.getImagesByUid(uid));
//	}
//
//	/**
//	 * 车辆视频
//	 * @param uid
//	 * @return
//	 */
//	@GetMapping("/video/{uid}")
//	public R<List<String>> video(@PathVariable("uid") String uid){
//		return new R<>(carService.getVideoByUid(uid));
//	}
//}
