package cc.carce.sale.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.dto.CarBaseInfoDto;
import cc.carce.sale.dto.CarDealerInfoDto;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.service.CarOrderInfoService;
import cc.carce.sale.service.CarOrderDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import cc.carce.sale.dto.CarEquipmentDto;
import cc.carce.sale.dto.CarGuaranteeDto;
import cc.carce.sale.dto.CarReportDto;
import cc.carce.sale.entity.CarPaymentOrderEntity;
import cc.carce.sale.entity.CarRichContentEntity;
import cc.carce.sale.entity.CarQuestionAnswerEntity;
import cc.carce.sale.entity.CarOrderInfoEntity;
import cc.carce.sale.entity.CarOrderDetailEntity;
import cc.carce.sale.entity.CarShoppingCartEntity;
import cc.carce.sale.entity.CarUserEntity;
import cc.carce.sale.entity.dto.CarConfigContent;
import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.entity.CarDealerEntity;
import cc.carce.sale.form.PaymentRequestForm;
import cc.carce.sale.form.CarReportForm;
import cc.carce.sale.entity.CarAdvertisementEntity;
import cc.carce.sale.service.CarBannerService;
import cc.carce.sale.service.CarDealerService;
import cc.carce.sale.service.CarReportService;
import cc.carce.sale.service.CarRichContentService;
import cc.carce.sale.service.CarQuestionAnswerService;
import cc.carce.sale.service.CarSalesService;
import cc.carce.sale.service.CarService;
import cc.carce.sale.service.ECPayService;
import cc.carce.sale.service.CarAdvertisementService;
import cc.carce.sale.service.CarMenuService;
import cc.carce.sale.service.CarConfigService;
import cc.carce.sale.service.CarAppointmentService;
import cc.carce.sale.service.CarOrderInfoService;
import cc.carce.sale.service.CarOrderDetailService;
import cc.carce.sale.service.CarShoppingCartService;
import cc.carce.sale.service.CarUserService;
import cc.carce.sale.service.SmsService;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import cc.carce.sale.common.JwtUtils;

/**
 * 汽车详情页面控制器
 */
@Api(tags = "汽车视图控制器", description = "处理汽车相关页面展示")
@Slf4j
@Controller
@CrossOrigin()
@RequestMapping("/")
public class CarViewController extends BaseController {
	
    private static final String CurrencyUnit = "$";
	@Value("${carce.webUrl}")
	private String webUrl;

	@Resource
	private CarService carService;

	@Resource
	private CarSalesService carSalesService;

	@Resource
	private CarDealerService carDealerService;
	
	@Resource
	private CarReportService carReportService;
	
	@Resource
	private CarRichContentService carRichContentService;
	
	@Resource
	private CarQuestionAnswerService carQuestionAnswerService;
	
	@Resource
	private CarBannerService carBannerService;
	
	@Resource
	private CarAdvertisementService carAdvertisementService;
	
	@Resource
    private ECPayService ecPayService;
    
    @Resource
    private CarMenuService carMenuService;
    
	@Resource
    private CarConfigService carConfigService;
    
    @Resource
    private CarAppointmentService carAppointmentService;
    
    @Resource
    private CarOrderInfoService carOrderInfoService;
    
    @Resource
    private CarOrderDetailService carOrderDetailService;
    
    @Resource
    private CarShoppingCartService carShoppingCartService;
    
    @Resource
    private CarUserService carUserService;
    
    @Resource
    private SmsService smsService;
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    
    /**
     * 首页
     */
    @ApiOperation(value = "首页展示", notes = "显示首页内容，包括Banner、精选好车、店家和广告")
    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            // 设置页面标题和描述
            // 设置网站图标
            model.addAttribute("favicon", webUrl + "/favicon.ico");
            
            // 设置首页主图
            
            // 设置精选好车数据 - 从数据库查询推荐车辆
            List<Map<String, String>> cars = new ArrayList<>();
            try {
                List<CarSalesEntity> recommendedCars = carSalesService.getRecommendedCars(9);
                for (CarSalesEntity carSales : recommendedCars) {
                    Map<String, String> car = new HashMap<>();
                    // 构建车型信息
                    String carModel = carSales.getSaleTitle() != null ? carSales.getSaleTitle() : "精選車輛";
                    car.put("model", carModel);
                    
                    // 格式化价格
                    String price = "面議";
                    if (carSales.getSalePrice() != null && carSales.getSalePrice() > 0) {
                        price = String.format("%,d", carSales.getSalePrice());
                    }
                    car.put("price", price);
                    
                    // 设置图片 - 使用默认图片或根据ID生成
                    String image = "/img/car/car" + (8 + cars.size() + 1) + ".jpg";
                    car.put("image", image);
                    
                    // 添加车辆ID用于链接
                    car.put("id", carSales.getId().toString());
                    car.put("uid", carSales.getUid() != null ? carSales.getUid() : "");
                    
                    cars.add(car);
                }
            } catch (Exception e) {
                log.error("获取推荐车辆失败，使用默认数据", e);
            }
            
            model.addAttribute("cars", cars);
			model.addAttribute("allCars", carSalesService.getAllCars());
            
            // 设置精选店家数据 - 从数据库查询推荐店家
            List<Map<String, String>> dealers = new ArrayList<>();
            try {
                List<CarDealerEntity> recommendedDealers = carSalesService.getRecommendedDealers(6);
                String[] carImages = {
                    "/img/car/car4.jpg", "/img/car/car6.jpg", "/img/car/car8.jpg", 
                    "/img/car/car9.jpg", "/img/car/car10.jpg", "/img/car/car11.jpg"
                };
                
                for (int i = 0; i < recommendedDealers.size(); i++) {
                    CarDealerEntity dealerEntity = recommendedDealers.get(i);
                    Map<String, String> dealer = new HashMap<>();
                    
                    // 设置店家名称
                    String dealerName = dealerEntity.getDealerName() != null ? 
                        dealerEntity.getDealerName() : "精選店家";
                    dealer.put("name", dealerName);
                    
                    // 设置图片 - 使用默认图片或根据索引生成
                    String image = carImages[i % carImages.length];
                    dealer.put("image", image);
                    
                    // 添加店家ID用于链接
                    dealer.put("id", dealerEntity.getId().toString());
                    
                    // 添加其他有用信息
                    if (dealerEntity.getPublicAddress() != null) {
                        dealer.put("address", dealerEntity.getPublicAddress());
                    }
                    if (dealerEntity.getCompanyPhone() != null) {
                        dealer.put("phone", dealerEntity.getCompanyPhone());
                    }
                    
                    dealers.add(dealer);
                }
            } catch (Exception e) {
                log.error("获取推荐店家失败，使用默认数据", e);
            }
            
            model.addAttribute("dealers", dealers);
            
            // 设置模板内容
            model.addAttribute("content", "/home/index.ftl");
            
        } catch (Exception e) {
            model.addAttribute("error", "頁面載入失敗：" + e.getMessage());
        }
        
        
        
        return "/layout/main";
    }
    
    /**
     * 我要买车页面
     */
    @GetMapping("/buy-cars")
    public String buyCarsPage(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            model.addAttribute("content", "/buy-cars/index.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
        }
        
        
        
        return "/layout/main";
    }
    
    /**
     * 商城页面
     */
    @GetMapping("/mall")
    public String mallPage(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            // 设置页面基本信息
            model.addAttribute("title", "商城 - 汽車配件專營店");
            model.addAttribute("CurrencyUnit", CurrencyUnit);
            model.addAttribute("content", "/mall/index.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
        }
        
        
        
        return "/layout/main";
    }
    
     /**
      * 关于我们页面
      */
     @GetMapping("/about")
     public String aboutPage(Model model, HttpServletRequest req) {
         try {
             // 检查用户登录状态
             
             model.addAttribute("title", "關於我們 - 二手車銷售平台");
             model.addAttribute("description", "了解我們的服務理念和團隊");
             model.addAttribute("image", "/img/swipper/slide1.jpg");
             model.addAttribute("url", req.getRequestURL().toString());
             model.addAttribute("content", "/about/index.ftl");
         } catch (Exception e) {
             model.addAttribute("error", "頁面載入失敗：" + e.getMessage());
         }
         
         
         
         return "/layout/main";
     }
     
     /**
      * 关于页面内容iframe
      */
     @GetMapping("/about/content")
     public String aboutContentIframe(Model model) {
         try {
             // 从数据库获取关于页面的富文本内容
             CarRichContentEntity aboutContent = carRichContentService.getFirstAboutContent();
             if (aboutContent == null) {
                 model.addAttribute("error", "关于页面内容不存在");
                 return "error/index";
             }
             
             model.addAttribute("aboutContent", aboutContent);
             model.addAttribute("title", aboutContent.getTitle());
             
             return "about/content-iframe";
         } catch (Exception e) {
             log.error("获取关于页面内容iframe失败", e);
             model.addAttribute("error", "获取数据失败");
             return "error/index";
         }
     }
    
    /**
     * 返回URL测试页面
     */
    @GetMapping("/test-return-url")
    public String testReturnUrl(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            model.addAttribute("title", "返回URL測試 - 二手車銷售平台");
            model.addAttribute("description", "測試登入後返回URL功能");
            model.addAttribute("image", "/img/swipper/slide1.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
            model.addAttribute("content", "/test/return-url-test.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "頁面載入失敗：" + e.getMessage());
            model.addAttribute("content", "/error/index.ftl");
        }
        
        
        
        return "/layout/main";
    }
    
    /**
     * 静态详情页演示
     */
    @GetMapping("/static-demo")
    public String staticCarDetail(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            model.addAttribute("title", "2020年 Toyota Camry 2.5L 豪華版 - 二手車銷售平台");
            model.addAttribute("description", "2020年 Toyota Camry 2.5L 豪華版，車況極佳，里程僅45,000公里");
            model.addAttribute("image", "/img/car/car4.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
            model.addAttribute("content", "/car/detail-static.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "頁面載入失敗：" + e.getMessage());
            model.addAttribute("content", "/error/index.ftl");
        }
        
        
        
        return "/layout/main";
    }
    
    /**
     * 跳转到汽车详情页面
     */
     @GetMapping("/detail/{saleId}")
    public String carDetail(Model model, @PathVariable("saleId") Long saleId, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            // 获取车辆基本信息
            CarBaseInfoDto carInfo = carService.getBaseInfoBySaleId(saleId);
            model.addAttribute("carInfo", carInfo);
            model.addAttribute("carInfoJson", JSONUtil.toJsonPrettyStr(carInfo));
            
            // 获取车辆图片
            List<String> images = carService.getImagesBySaleId(saleId);
            model.addAttribute("images", images);
            model.addAttribute("imagesJson", JSONUtil.toJsonPrettyStr(images));
            
            // 获取车辆配置信息
            List<CarEquipmentDto> equipments = carService.getEquipmentBySaleId(saleId);
            model.addAttribute("equipments", equipments);
            model.addAttribute("equipmentsJson", JSONUtil.toJsonPrettyStr(equipments));
            
            // 获取车辆保证信息
            List<CarGuaranteeDto> guarantees = carService.getGuaranteeBySaleId(saleId);
            model.addAttribute("guarantees", guarantees);
            model.addAttribute("guaranteesJson", JSONUtil.toJsonPrettyStr(guarantees));
            
            // 获取店家信息
            CarDealerInfoDto dealerInfo = new CarDealerInfoDto();
            if (carInfo.getGarageId() != null && !carInfo.getGarageId().isEmpty()) {
                try {
                    Long garageId = Long.parseLong(carInfo.getGarageId());
                    dealerInfo = carDealerService.getInfoById(garageId);
                } catch (NumberFormatException e) {
                    log.warn("GarageId格式错误: {}", carInfo.getGarageId());
                }
            }
            model.addAttribute("dealerInfo", dealerInfo);
            model.addAttribute("dealerInfoJson", JSONUtil.toJsonPrettyStr(dealerInfo));
            
            model.addAttribute("saleId", saleId);
            
            // 设置页面标题和描述
            String title = dealerInfo.getDealerName() + " / " + carInfo.getManufactureYear() + "年 " + carInfo.getBrand() + "," + carInfo.getCustomModel();
            String ogTitle = dealerInfo.getDealerName() + "," + carInfo.getBrand() + "," + carInfo.getCustomModel();
            model.addAttribute("title", title);
            model.addAttribute("ogTitle", ogTitle);
            model.addAttribute("ogDescription", ogTitle + "," + carInfo.getSaleTitle());
            // model.addAttribute("description", carInfo.getSaleDescription() != null ? 
            //     carInfo.getSaleDescription().replaceAll("<[^>]*>", "").substring(0, Math.min(150, carInfo.getSaleDescription().length())) : 
            //     "專業的二手車銷售平台，提供優質二手車資訊");
            model.addAttribute("ogImage", images != null && !images.isEmpty() ? images.get(0) : "/img/car/car4.jpg");
            model.addAttribute("ogUrl", req.getRequestURL().toString());
            
            model.addAttribute("content", "/car/detail.ftl");
            
        } catch (Exception e) {
            model.addAttribute("loading", false);
            model.addAttribute("error", "獲取數據失敗：" + e.getMessage());
            model.addAttribute("content", "/error/index.ftl");
        }
        
        
        
        return "/layout/main";
    }
    
    /**
     * 检车报告PDF文件访问
     */
    @GetMapping("/api/pdf/inspection-report")
    @ResponseBody
    public ResponseEntity<byte[]> getInspectionReport() {
//        try {
//            System.out.println("开始读取PDF文件...");
//            ClassPathResource resource = new ClassPathResource("static/检车报告.pdf");
//            
//            if (!resource.exists()) {
//                System.out.println("PDF文件不存在: " + resource.getURI());
//                return ResponseEntity.notFound().build();
//            }
//            
//            System.out.println("PDF文件存在，开始读取...");
//            InputStream inputStream = resource.getInputStream();
//            byte[] pdfBytes = inputStream.readAllBytes();
//            inputStream.close();
//            
//            System.out.println("PDF文件读取成功，大小: " + pdfBytes.length + " bytes");
//            
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.add("Content-Disposition", "inline; filename=\"检车报告.pdf\"");
//            headers.setContentLength(pdfBytes.length);
//            
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(pdfBytes);
//        } catch (IOException e) {
//            System.out.println("读取PDF文件失败: " + e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.notFound().build();
//        }
    	return null;
    }
    
    /**
     * 跳转到购物车页面
     */
    @ApiOperation(value = "购物车页面", notes = "显示用户购物车内容")
    @GetMapping("/cart")
    public String cartPage(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            // 设置页面标题和描述
            model.addAttribute("title", "購物車 - 二手車銷售平台");
            model.addAttribute("description", "查看和管理您的購物車商品");
            
            // 设置网站图标
            model.addAttribute("favicon", webUrl + "/favicon.ico");
            
            // 设置购物车页面内容
            model.addAttribute("content", "/cart/index.ftl");
            
            // 获取当前请求的完整URL
            String requestUrl = req.getRequestURL().toString();
            model.addAttribute("url", requestUrl);
            model.addAttribute("CurrencyUnit", CurrencyUnit);
            // 添加一些必要的属性，避免模板中的变量未定义错误
            model.addAttribute("id", ""); // 添加空的id属性
            model.addAttribute("image", "/img/swipper/slide1.jpg"); // 添加默认图片
            
        } catch (Exception e) {
            model.addAttribute("error", "頁面載入失敗：" + e.getMessage());
            model.addAttribute("content", "/error/index.ftl");
        }
        
        
        
        return "/layout/main";
    }
    
    
    /**
     * 显示广告内容页面
     */
    @GetMapping("/ad-content/{id}")
    public String showAdContent(@PathVariable("id") Long id, Model model) {
        try {
            CarAdvertisementEntity advertisement = carAdvertisementService.getById(id);
            if (advertisement != null) {
                model.addAttribute("advertisement", advertisement);
                model.addAttribute("title", advertisement.getTitle());
                model.addAttribute("content", advertisement.getContent());
            } else {
                model.addAttribute("error", "廣告不存在");
            }
        } catch (Exception e) {
            model.addAttribute("error", "獲取廣告內容失敗：" + e.getMessage());
        }
        return "/ad-content";
    }
    
    /**
	 * 显示支付页面（GET请求，用于直接访问）
	 */
	@GetMapping("/payment/index")
	public String showPaymentPageGet(@RequestParam(required = false) String itemName,
			@RequestParam(required = false) Integer amount, @RequestParam(required = false) String description,
			@RequestParam(required = false) String cartData, @RequestParam(required = false) Long orderId, 
			Model model, HttpSession session) {

		// 检查用户登录状态
		UserInfo userInfo = getSessionUser();
		if (userInfo == null) {
			log.warn("未登录用户尝试访问支付页面");
			return "redirect:/login?returnUrl=/payment/index";
		}

		model.addAttribute("orderInfoJson", "{}");
		// 如果提供了orderId，则从订单获取信息
		if (orderId != null) {
			try {
				CarOrderInfoEntity order = carOrderInfoService.getOrderById(orderId);
				if (order != null && order.getUserId().equals(userInfo.getId())) {
					// 验证订单状态，只有未支付的订单才能重新支付
					if (CarOrderInfoEntity.OrderStatus.UNPAID.getCode().equals(order.getOrderStatus())) {
						// 从订单获取信息
						itemName = "订单商品";
						amount = order.getTotalPrice();
						description = "重新支付订单";
						
						// 获取订单详情作为购物车数据
						List<CarOrderDetailEntity> orderDetails = carOrderDetailService.getOrderDetailsByOrderId(orderId);
						if (orderDetails != null && !orderDetails.isEmpty()) {
							List<Map<String, Object>> cartItems = new ArrayList<>();
							for (CarOrderDetailEntity detail : orderDetails) {
								Map<String, Object> item = new HashMap<>();
								item.put("id", detail.getId());
								item.put("productId", detail.getProductId());
								item.put("productName", detail.getProductName());
								item.put("productAmount", detail.getProductAmount());
								item.put("productPrice", detail.getProductPrice());
								item.put("subtotal", detail.getTotalPrice());
								cartItems.add(item);
							}
							Map<String, Object> orderCartData = new HashMap<>();
							orderCartData.put("items", cartItems);
							orderCartData.put("totalAmount", order.getTotalPrice());
							orderCartData.put("totalQuantity", orderDetails.size());
							try {
								ObjectMapper objectMapper = new ObjectMapper();
								model.addAttribute("cartData", objectMapper.writeValueAsString(orderCartData));
							} catch (Exception e) {
								log.error("序列化订单数据失败", e);
								model.addAttribute("cartData", "");
							}
						}
						
						// 设置订单相关信息
						model.addAttribute("orderId", orderId);
						model.addAttribute("orderNo", order.getOrderNo());
						model.addAttribute("orderInfoJson", JSONUtil.toJsonStr(order));
						
						log.info("用户重新支付订单，用户ID: {}, 订单ID: {}, 订单号: {}, 金额: {}", 
								userInfo.getId(), orderId, order.getOrderNo(), amount);
					} else {
						log.warn("订单状态不允许重新支付，用户ID: {}, 订单ID: {}, 状态: {}", 
								userInfo.getId(), orderId, order.getOrderStatus());
						model.addAttribute("errorMessage", "订单状态不允许重新支付");
					}
				} else {
					log.warn("订单不存在或无权限访问，用户ID: {}, 订单ID: {}", userInfo.getId(), orderId);
					model.addAttribute("errorMessage", "订单不存在或无权限访问");
				}
			} catch (Exception e) {
				log.error("获取订单信息失败，用户ID: {}, 订单ID: {}", userInfo.getId(), orderId, e);
				model.addAttribute("errorMessage", "获取订单信息失败");
			}
		}

		// 设置页面数据
		model.addAttribute("itemName", itemName != null ? itemName : "汽车商品");
		model.addAttribute("amount", amount != null ? amount.toString() : "0");
		model.addAttribute("description", description != null ? description : "");
		model.addAttribute("cartData", cartData);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isDevOrTest", isDevOrTestEnvironment());
		model.addAttribute("CurrencyUnit", CurrencyUnit);
		// 设置模板内容
		model.addAttribute("content", "/payment/index.ftl");

		log.info("用户通过GET请求访问支付页面，用户ID: {}, 商品: {}, 金额: {}, 订单ID: {}", 
				userInfo.getId(), itemName, amount, orderId);

		return "/layout/main";
	}

	/**
	 * 处理支付页面请求（POST请求，用于购物车结算）
	 */
	@PostMapping("/payment/index")
	public String showPaymentPagePost(@Valid PaymentRequestForm paymentRequest,
			BindingResult bindingResult, Model model, HttpSession session) {

		// 检查用户登录状态
		UserInfo userInfo = getSessionUser();
		if (userInfo == null) {
			log.warn("未登录用户尝试访问支付页面");
			return "redirect:/login?returnUrl=/payment/index";
		}

		// 参数验证
		if (bindingResult.hasErrors()) {
			log.warn("支付请求参数验证失败: {}", bindingResult.getAllErrors());
			// 重定向到购物车页面，显示错误信息
			return "redirect:/cart?error=支付参数错误";
		}

		// 获取当前环境配置
		String activeProfile = System.getProperty("spring.profiles.active");
		if (activeProfile == null) {
			activeProfile = "dev"; // 默认使用dev环境
		}

		// 如果是dev或test环境，金额固定为1元
		Integer finalAmount = paymentRequest.getAmount();
		// if ("dev".equals(activeProfile) || "test".equals(activeProfile)) {
		// 	finalAmount = 1;
		// 	log.info("开发/测试环境，支付金额固定为1元，原始金额: {}", paymentRequest.getAmount());
		// }
		model.addAttribute("orderInfoJson", "{}");
		// 设置页面数据
		model.addAttribute("itemName", paymentRequest.getItemName());
		model.addAttribute("amount", finalAmount.toString());
		model.addAttribute("description", paymentRequest.getDescription());
		model.addAttribute("cartData", paymentRequest.getCartData());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isDevOrTest", "dev".equals(activeProfile) || "test".equals(activeProfile));
		model.addAttribute("CurrencyUnit", CurrencyUnit);
		// 设置模板内容
		model.addAttribute("content", "/payment/index.ftl");

		log.info("用户通过POST请求访问支付页面，用户ID: {}, 商品: {}, 金额: {}, 环境: {}", userInfo.getId(), paymentRequest.getItemName(),
				finalAmount, activeProfile);


		return "/layout/main";
	}
	
	/**
     * 显示支付结果页面
     */
    @GetMapping("/result")
    public String showPaymentResult(@RequestParam(required = false) String merchantTradeNo,
                                   Model model,
                                   HttpSession session) {
        
        // 检查用户登录状态
        UserInfo userInfo = getSessionUser();
        if (userInfo == null) {
            log.warn("未登录用户尝试访问支付结果页面");
            return "redirect:/login?returnUrl=/payment/result";
        }
        
        try {
            if (merchantTradeNo != null && !merchantTradeNo.trim().isEmpty()) {
                // 查询支付订单状态
            	R<CarPaymentOrderEntity> result = ecPayService.queryPaymentStatus(merchantTradeNo);
                
                if (result.isSuccess() && result.getData() != null) {
                    CarPaymentOrderEntity paymentOrder = result.getData();
                    
                    // 检查订单所属用户
                    if (!paymentOrder.getUserId().equals(userInfo.getId())) {
                        log.warn("用户尝试查看不属于自己的支付订单，用户ID: {}, 订单用户ID: {}", 
                                userInfo.getId(), paymentOrder.getUserId());
                        return "redirect:/payment/result";
                    }
                    
                    model.addAttribute("paymentOrder", paymentOrder);
                    model.addAttribute("paymentStatus", paymentOrder.getPaymentStatus());
                    
                    log.info("显示支付结果页面，商户订单号: {}, 支付状态: {}", 
                            merchantTradeNo, paymentOrder.getPaymentStatus());
                } else {
                    log.warn("未找到支付订单，商户订单号: {}", merchantTradeNo);
                    model.addAttribute("paymentStatus", -1); // 未知状态
                }
            } else {
                // 没有订单号，显示默认状态
                model.addAttribute("paymentStatus", -1);
            }
            
        } catch (Exception e) {
            log.error("查询支付订单状态异常", e);
            model.addAttribute("paymentStatus", -1);
        }
        
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("user", userInfo); // 添加user属性用于模板
        model.addAttribute("content", "/payment/result.ftl");
        
        
        return "/layout/main";
    }

	/**
	 * 检查是否为开发或测试环境
	 */
	private boolean isDevOrTestEnvironment() {
		String activeProfile = System.getProperty("spring.profiles.active");
		if (activeProfile == null) {
			activeProfile = "dev"; // 默认使用dev环境
		}
		return "dev".equals(activeProfile) || "test".equals(activeProfile);
	}
	
	/**
	 * 我的檢舉列表頁面
	 */
	@GetMapping("/my-reports")
	public String myReports(Model model) {
		UserInfo sessionUser = getSessionUser();
		if (sessionUser == null) {
			return "redirect:/login";
		}
		
		// 獲取用戶的檢舉列表
		List<CarReportDto> reports = carReportService.getReportsByReporterId(sessionUser.getId());
		model.addAttribute("reports", reports);
		model.addAttribute("content", "/report/my-reports.ftl");
		model.addAttribute("title", "我的檢舉");
		
		
		return "layout/main";
	}
	
	/**
	 * 創建檢舉API
	 */
	@PostMapping("/api/report/create")
	@ResponseBody
	public R<String> createReport(@RequestBody CarReportForm form) {
		try {
			UserInfo sessionUser = getSessionUser();
			if (sessionUser == null) {
				return R.failMsg("請先登入");
			}
			
			form.setReporterId(sessionUser.getId());
			
			boolean success = carReportService.createReport(form);
			if (success) {
				return R.okMsg("檢舉提交成功");
			} else {
				return R.failMsg("檢舉提交失敗");
			}
		} catch (Exception e) {
			log.error("創建檢舉失敗", e);
			return R.failMsg("系統錯誤，請稍後再試");
		}
	}
	
	/**
	 * 檢舉詳情API
	 */
	@GetMapping("/api/report/detail/{id}")
	@ResponseBody
	public R<CarReportDto> getReportDetail(@PathVariable Long id) {
		try {
			UserInfo sessionUser = getSessionUser();
			if (sessionUser == null) {
				return R.failMsg("請先登入");
			}
			
			CarReportDto report = carReportService.getReportById(id);
			if (report == null) {
				return R.failMsg("檢舉不存在");
			}
			
			return R.ok(report);
		} catch (Exception e) {
			log.error("查詢檢舉詳情失敗", e);
			return R.failMsg("系統錯誤，請稍後再試");
		}
	}
	
	/**
	 * 撤銷檢舉API
	 */
	@PostMapping("/api/report/cancel/{id}")
	@ResponseBody
	public R<String> cancelReport(@PathVariable Long id) {
		try {
			UserInfo sessionUser = getSessionUser();
			if (sessionUser == null) {
				return R.failMsg("請先登入");
			}
			
			// 检查檢舉是否存在且属于当前用户
			CarReportDto report = carReportService.getReportById(id);
			if (report == null) {
				return R.failMsg("檢舉不存在");
			}
			
			if (!sessionUser.getId().equals(report.getReporterId())) {
				return R.failMsg("無權限撤銷此檢舉");
			}
			
			// 只有已提交状态的檢舉才能撤銷
			if (!"submitted".equals(report.getStatus())) {
				return R.failMsg("只有已提交的檢舉才能撤銷");
			}
			
			// 执行撤銷操作
			boolean success = carReportService.cancelReport(id);
			if (success) {
				return R.okMsg("檢舉已成功撤銷");
			} else {
				return R.failMsg("撤銷失敗");
			}
		} catch (Exception e) {
			log.error("撤銷檢舉失敗", e);
			return R.failMsg("系統錯誤，請稍後再試");
		}
	}

	/**
	 * 頻道頁面
	 */
	@GetMapping("/channel")
	public String channel(Model model) {
		try {
			// 从数据库获取所有频道内容
			List<CarRichContentEntity> channelContentList = carRichContentService.getChannelContent();
			model.addAttribute("channelContentList", channelContentList);
			
			// 获取每个频道的问答数据
			Map<String, List<CarQuestionAnswerEntity>> channelQAMap = new HashMap<>();
			if (channelContentList != null) {
				for (CarRichContentEntity channel : channelContentList) {
					List<CarQuestionAnswerEntity> qaList = carQuestionAnswerService.getQuestionAnswersByChannelIdOrderByShowOrder(channel.getId());
					if (qaList != null && !qaList.isEmpty()) {
						channelQAMap.put(channel.getId().toString(), qaList);
					}
				}
			}
			model.addAttribute("channelQAMap", channelQAMap);
			
			model.addAttribute("title", "頻道");
			model.addAttribute("content", "/channel/index.ftl");
		} catch (Exception e) {
			log.error("获取频道页面失败", e);
			model.addAttribute("error", "获取数据失败");
		}
		
		
		return "layout/main";
	}
	
	/**
	 * 频道内容iframe页面
	 */
	@GetMapping("/channel/content/{channelId}")
	public String channelContentIframe(@PathVariable Long channelId, Model model) {
		try {
			// 获取指定频道的内容
			CarRichContentEntity channelContent = carRichContentService.getRichContentById(channelId);
			if (channelContent == null) {
				model.addAttribute("error", "频道内容不存在");
				return "error/index";
			}
			
			model.addAttribute("channelContent", channelContent);
			model.addAttribute("title", channelContent.getTitle());
			
			return "channel/content-iframe";
		} catch (Exception e) {
			log.error("获取频道内容iframe失败", e);
			model.addAttribute("error", "获取数据失败");
			return "error/index";
		}
	}
	
	
	
	
	/**
	 * 预约看车页面
	 */
	@GetMapping("/appointment/create/{carSaleId}")
	public String createAppointmentPage(@PathVariable Long carSaleId, Model model) {
		try {
			// 检查用户登录状态
			UserInfo userInfo = getSessionUser();
			if (userInfo == null) {
				log.warn("未登录用户尝试访问预约页面");
				return "redirect:/login?returnUrl=/appointment/create/" + carSaleId;
			}
			
			model.addAttribute("carSaleId", carSaleId);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("user", userInfo); // 添加user属性用于模板
			model.addAttribute("content", "/appointment/create.ftl");
			
			return "/layout/main";
		} catch (Exception e) {
			log.error("显示预约页面异常", e);
			model.addAttribute("error", "页面加载失败：" + e.getMessage());
			return "/layout/main";
		}
	}

	/**
	 * 我的预约列表页面
	 */
	@GetMapping("/appointment/my-appointments")
	public String myAppointmentsPage(Model model) {
		try {
			// 检查用户登录状态
			UserInfo userInfo = getSessionUser();
			if (userInfo == null) {
				log.warn("未登录用户尝试访问我的预约页面");
				return "redirect:/login?returnUrl=/appointment/my-appointments";
			}
			
			// 设置用户信息
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("user", userInfo); // 添加user属性用于模板
			
			// 设置模板内容
			model.addAttribute("content", "/appointment/my-appointments.ftl");
			
			log.info("用户访问我的预约页面，用户ID: {}", userInfo.getId());
			
			return "/layout/main";
		} catch (Exception e) {
			log.error("显示我的预约页面异常", e);
			model.addAttribute("error", "页面加载失败：" + e.getMessage());
			return "/layout/main";
		}
	}
	
	/**
	 * 我的订单页面
	 */
	@GetMapping("/my-order/index")
	public String showMyOrderPage(Model model, HttpServletRequest request) {
		try {
			// 检查用户登录状态
			UserInfo userInfo = getSessionUser();
			if (userInfo == null) {
				log.warn("未登录用户尝试访问我的订单页面");
				return "redirect:/login?returnUrl=/my-order/index";
			}

			// 获取用户的所有订单
			List<CarOrderInfoEntity> orders = carOrderInfoService.getOrdersByUserId(userInfo.getId());
			model.addAttribute("orders", orders);
			model.addAttribute("ordersJson", JSONUtil.toJsonPrettyStr(orders));
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("user", userInfo); // 添加user属性用于模板
			model.addAttribute("CurrencyUnit", CurrencyUnit);
			// 设置模板内容
			model.addAttribute("content", "/my-order/index.ftl");

			log.info("用户访问我的订单页面，用户ID: {}, 订单数量: {}", userInfo.getId(), orders.size());

			return "/layout/main";
		} catch (Exception e) {
			log.error("显示我的订单页面异常", e);
			model.addAttribute("error", "页面加载失败：" + e.getMessage());
			return "/layout/main";
		}
	}
	
	/**
	 * 显示登录页面
	 */
	@GetMapping("/login")
	public String loginPage(Model model, HttpServletRequest req) {
		// 获取返回URL参数
		String returnUrl = req.getParameter("returnUrl");
		if (returnUrl != null && !returnUrl.trim().isEmpty()) {
			model.addAttribute("returnUrl", returnUrl);
		}
		
		// 获取错误信息参数
		String error = req.getParameter("error");
		if (error != null && !error.trim().isEmpty()) {
			model.addAttribute("error", error);
		}
		
		// 设置页面标题和描述
		model.addAttribute("title", "登入/註冊 - 二手車銷售平台");
		model.addAttribute("description", "用戶登入和註冊頁面");
		model.addAttribute("url", req.getRequestURL().toString());
		model.addAttribute("image", "/img/swipper/slide1.jpg");
		
		// 设置模板内容
		model.addAttribute("content", "/login/index.ftl");
		
		return "/layout/main";
	}
	
	/**
	 * 拦截器测试页面
	 */
	@GetMapping("/test-interceptor")
	public String testInterceptorPage(Model model) {
		model.addAttribute("title", "拦截器测试页面");
		model.addAttribute("content", "/test-interceptor.ftl");
		return "/layout/main";
	}
} 