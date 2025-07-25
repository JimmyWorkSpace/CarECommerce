package cc.carce.sale.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.carce.sale.dto.CarBaseInfoDto;
import cc.carce.sale.entity.CarBannerEntity;
import cc.carce.sale.service.CarBannerService;
import cc.carce.sale.service.CarDealerService;
import cc.carce.sale.service.CarSalesService;
import cc.carce.sale.service.CarService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;

/**
 * 汽车详情页面控制器
 */
@Controller
@CrossOrigin()
@RequestMapping("/")
public class CarViewController {
	
	@Value("${carce.webUrl}")
	private String webUrl;

	@Resource
	private CarService carService;

	@Resource
	private CarSalesService carSalesService;

	@Resource
	private CarDealerService carDealerService;
	
	@Resource
	private CarBannerService carBannerService;
    
    /**
     * 首页
     */
    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest req) {
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            // 设置页面标题和描述
            model.addAttribute("title", "二手车销售平台 - 精选好车，值得信赖");
            model.addAttribute("description", "专业的二手车销售平台，提供优质二手车信息，让您轻松找到心仪的座驾");
            
            // 设置网站图标
            model.addAttribute("favicon", webUrl + "/favicon.ico");
            
            // 设置首页主图
            model.addAttribute("image", "/img/swipper/slide1.jpg");
            
            // 获取banner数据
            List<CarBannerEntity> banners = carBannerService.getHomeBanners();
            List<Map<String, String>> heroSlides = new ArrayList<>();
            
            if (banners != null && !banners.isEmpty()) {
                for (CarBannerEntity banner : banners) {
                    Map<String, String> slide = new HashMap<>();
                    slide.put("image", banner.getImageUrl());
                    slide.put("title", "精选二手车");
                    slide.put("subtitle", "品质保证，价格实惠");
                    slide.put("link", banner.getIsLink() ? banner.getLinkUrl() : "#");
                    slide.put("isLink", banner.getIsLink().toString());
                    heroSlides.add(slide);
                }
            } else {
                // 如果没有banner数据，使用默认数据
                Map<String, String> slide1 = new HashMap<>();
                slide1.put("image", "/img/swipper/slide1.jpg");
                slide1.put("title", "精选二手车");
                slide1.put("subtitle", "品质保证，价格实惠");
                slide1.put("link", "/cars");
                slide1.put("isLink", "true");
                heroSlides.add(slide1);
                
                Map<String, String> slide2 = new HashMap<>();
                slide2.put("image", "/img/swipper/slide2.jpg");
                slide2.put("title", "专业检测");
                slide2.put("subtitle", "每辆车都经过严格检测");
                slide2.put("link", "/inspection");
                slide2.put("isLink", "true");
                heroSlides.add(slide2);
            }
            
            model.addAttribute("heroSlides", heroSlides);
            
            // 设置精选好车数据
            List<Map<String, String>> cars = new ArrayList<>();
            
            String[] carModels = {
                "Toyota Camry 2020", "Honda Civic 2021", "Nissan Altima 2019", 
                "Mazda 3 2020", "Subaru Impreza 2021", "Mitsubishi Lancer 2019",
                "Toyota Corolla 2020", "Honda Accord 2021", "Nissan Sentra 2019"
            };
            
            String[] carPrices = {
                "850,000", "780,000", "720,000", "680,000", "750,000", "650,000",
                "820,000", "890,000", "700,000"
            };
            
            for (int i = 8; i <= 16; i++) {
                Map<String, String> car = new HashMap<>();
                car.put("model", carModels[i - 8]);
                car.put("price", carPrices[i - 8]);
                car.put("image", "/img/car/car" + i + ".jpg");
                cars.add(car);
            }
            
            model.addAttribute("cars", cars);
            
            // 设置精选店家数据
            List<Map<String, String>> dealers = new ArrayList<>();
            
            String[] dealerNames = {
                "台北汽车", "新北车行", "桃园汽车", "台中车行", "台南汽车", "高雄车行"
            };
            
            // 随机挑选的汽车图片
            String[] carImages = {
                "/img/car/car4.jpg", "/img/car/car6.jpg", "/img/car/car8.jpg", 
                "/img/car/car9.jpg", "/img/car/car10.jpg", "/img/car/car11.jpg"
            };
            
            for (int i = 0; i < dealerNames.length; i++) {
                Map<String, String> dealer = new HashMap<>();
                dealer.put("name", dealerNames[i]);
                dealer.put("image", carImages[i]);
                dealers.add(dealer);
            }
            
            model.addAttribute("dealers", dealers);
            
            // 获取当前请求的完整URL
            String requestUrl = req.getRequestURL().toString();
            model.addAttribute("url", requestUrl);
            
            // 设置模板内容
            model.addAttribute("content", "/home/index.ftl");
            
        } catch (Exception e) {
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
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
            
            // 获取搜索参数
            String brand = req.getParameter("brand");
            String carModel = req.getParameter("model");
            String year = req.getParameter("year");
            String priceMin = req.getParameter("priceMin");
            String priceMax = req.getParameter("priceMax");
            String fuelType = req.getParameter("fuelType");
            String transmission = req.getParameter("transmission");
            String mileage = req.getParameter("mileage");
            String bodyType = req.getParameter("bodyType");
            String page = req.getParameter("page");
            
            int currentPage = page != null ? Integer.parseInt(page) : 1;
            int pageSize = 24;
            
            // 设置搜索条件
            model.addAttribute("brand", brand);
            model.addAttribute("model", carModel);
            model.addAttribute("year", year);
            model.addAttribute("priceMin", priceMin);
            model.addAttribute("priceMax", priceMax);
            model.addAttribute("fuelType", fuelType);
            model.addAttribute("transmission", transmission);
            model.addAttribute("mileage", mileage);
            model.addAttribute("bodyType", bodyType);
            model.addAttribute("currentPage", currentPage);
            
            // 生成车辆数据（模拟数据）
            List<Map<String, Object>> cars = new ArrayList<>();
            String[] brands = {"Toyota", "Honda", "Nissan", "BMW", "Audi", "Mercedes", "Lexus", "Mazda"};
            String[] models = {"Camry", "Civic", "Altima", "3 Series", "A4", "C-Class", "ES", "3"};
            String[] fuelTypes = {"Gasoline", "Diesel", "Electric", "Hybrid"};
            String[] transmissions = {"Automatic", "Manual", "CVT"};
            String[] bodyTypes = {"Sedan", "SUV", "Hatchback", "Coupe"};
            
            for (int i = 1; i <= 48; i++) {
                Map<String, Object> car = new HashMap<>();
                int brandIndex = (i - 1) % brands.length;
                int modelIndex = (i - 1) % models.length;
                
                car.put("id", i);
                car.put("brand", brands[brandIndex]);
                car.put("model", models[modelIndex]);
                car.put("year", 2018 + (i % 7));
                car.put("price", 15000 + (i * 1000));
                car.put("mileage", 10000 + (i * 5000));
                car.put("fuelType", fuelTypes[i % fuelTypes.length]);
                car.put("transmission", transmissions[i % transmissions.length]);
                car.put("bodyType", bodyTypes[i % bodyTypes.length]);
                car.put("image", "/img/car/car" + ((i % 12) + 4) + ".jpg");
                car.put("title", brands[brandIndex] + " " + models[modelIndex] + " " + (2018 + (i % 7)));
                
                cars.add(car);
            }
            
            // 分页处理
            int totalCars = cars.size();
            int totalPages = (int) Math.ceil((double) totalCars / pageSize);
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalCars);
            
            List<Map<String, Object>> currentPageCars = cars.subList(startIndex, endIndex);
            
            model.addAttribute("cars", currentPageCars);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCars", totalCars);
            
            // 设置品牌和型号选项
            model.addAttribute("brands", brands);
            model.addAttribute("fuelTypes", fuelTypes);
            model.addAttribute("transmissions", transmissions);
            model.addAttribute("bodyTypes", bodyTypes);
            
            model.addAttribute("title", "我要买车 - 二手车销售平台");
            model.addAttribute("description", "浏览精选二手车，找到您的理想座驾");
            model.addAttribute("image", "/img/swipper/slide1.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
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
            
            // 生成商城商品数据（模拟数据）
            List<Map<String, Object>> products = new ArrayList<>();
            String[] productNames = {
                "汽车座套", "车载充电器", "行车记录仪", "汽车香水", "方向盘套", 
                "汽车脚垫", "车载冰箱", "汽车音响", "导航仪", "汽车贴膜",
                "轮胎", "机油", "刹车片", "雨刷器", "电瓶", "火花塞"
            };
            
            String[] productImages = {
                "/img/car/car4.jpg", "/img/car/car5.jpg", "/img/car/car6.jpg", "/img/car/car7.jpg", "/img/car/car8.jpg", "/img/car/car9.jpg",
                "/img/car/car10.jpg", "/img/car/car11.jpg", "/img/car/car12.jpg", "/img/car/car13.jpg", "/img/car/car14.jpg", "/img/car/car15.jpg",
                "/img/car/car16.jpg", "/img/car/car4.jpg", "/img/car/car5.jpg", "/img/car/car6.jpg"
            };
            
            int[] prices = {
                299, 89, 599, 129, 159, 399, 899, 1299, 799, 599, 899, 299, 199, 89, 599, 89
            };
            
            // 商品分类
            String[] categories = {
                "exterior", "electrical", "electrical", "exterior", "exterior", 
                "exterior", "electrical", "electrical", "electrical", "exterior",
                "brake", "engine", "brake", "exterior", "electrical", "engine"
            };
            
            for (int i = 0; i < productNames.length; i++) {
                Map<String, Object> product = new HashMap<>();
                product.put("id", "product_" + (i + 1));
                product.put("name", productNames[i]);
                product.put("price", prices[i]);
                product.put("image", productImages[i]);
                product.put("category", categories[i]);
                product.put("description", "高品质汽车配件，提升您的驾驶体验");
                products.add(product);
            }
            
            model.addAttribute("products", products);
            model.addAttribute("title", "商城 - 汽车配件专营店");
            model.addAttribute("description", "专业汽车配件商城，提供优质汽车用品");
            model.addAttribute("image", "/img/swipper/slide1.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
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
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
            
            model.addAttribute("title", "关于我们 - 二手车销售平台");
            model.addAttribute("description", "了解我们的服务理念和团队");
            model.addAttribute("image", "/img/swipper/slide1.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
            model.addAttribute("content", "/about/index.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
        }
        return "/layout/main";
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
            
            model.addAttribute("title", "返回URL测试 - 二手车销售平台");
            model.addAttribute("description", "测试登录后返回URL功能");
            model.addAttribute("image", "/img/swipper/slide1.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
            model.addAttribute("content", "/test/return-url-test.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
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
            
            model.addAttribute("title", "2020年 Toyota Camry 2.5L 豪华版 - 二手车销售平台");
            model.addAttribute("description", "2020年 Toyota Camry 2.5L 豪华版，车况极佳，里程仅45,000公里");
            model.addAttribute("image", "/img/car/car4.jpg");
            model.addAttribute("url", req.getRequestURL().toString());
            model.addAttribute("content", "/car/detail-static.ftl");
        } catch (Exception e) {
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
            model.addAttribute("content", "/error/index.ftl");
        }
        return "/layout/main";
    }
    
    /**
     * 跳转到汽车详情页面
     */
    @GetMapping("/{uid}")
    public String carDetail(Model model, @PathVariable("uid") String uid, HttpServletRequest req) {
        // 排除静态资源路径
        if (uid.contains(".") || uid.startsWith("ad-") || uid.startsWith("img/") || uid.startsWith("css/") || uid.startsWith("js/")) {
            return "redirect:/";
        }
        try {
            // 检查用户登录状态
            Object user = req.getSession().getAttribute("user");
            model.addAttribute("user", user);
        	model.addAttribute("image", "");
        	model.addAttribute("imagesJson", JSONUtil.toJsonPrettyStr(new ArrayList<>()));
        	model.addAttribute("videosJson", JSONUtil.toJsonPrettyStr(new ArrayList<>()));

			Future<Boolean> videoF = ThreadUtil.execAsync(() -> {
				List<String> videos = carService.getVideoByUid(uid);
				model.addAttribute("videosJson", JSONUtil.toJsonPrettyStr(videos));

				return true;
			});

//            获取图片
			List<String> images = carService.getImagesByUid(uid);
			if(CollUtil.isNotEmpty(images)) {
				model.addAttribute("image", images.get(0));
			}else {
				model.addAttribute("image", "");
			}

			model.addAttribute("imagesJson", JSONUtil.toJsonPrettyStr(images));
        		


        	
            // 获取车辆基本信息
            CarBaseInfoDto carInfo = carService.getBaseInfoByUidId(uid);
            model.addAttribute("carInfo", carInfo);
            model.addAttribute("carInfoJson", JSONUtil.toJsonPrettyStr(carInfo));
            
            model.addAttribute("carId", uid);
            
            model.addAttribute("content", "/car/detail-static.ftl");
            
//            设置描述
            model.addAttribute("title", CollUtil.join(CollUtil.newArrayList(carInfo.getBrand(), carInfo.getCustomModel(), carInfo.getManufactureYear()), " "));
            model.addAttribute("description", carInfo.getSaleTitle());
            
            // 设置网站图标
            model.addAttribute("favicon", webUrl + "/favicon.ico");
            
            videoF.get(5, TimeUnit.SECONDS);
            // 获取当前请求的完整URL
            String requestUrl = req.getRequestURL().toString();
            
            model.addAttribute("url", requestUrl);
            
            model.addAttribute("loading", false);
        } catch (Exception e) {
            model.addAttribute("loading", false);
            model.addAttribute("error", "获取数据失败：" + e.getMessage());
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
     * 获取banner数据的API接口
     */
    @GetMapping("/api/banners")
    @ResponseBody
    public List<CarBannerEntity> getBanners() {
        return carBannerService.getHomeBanners();
    }
} 