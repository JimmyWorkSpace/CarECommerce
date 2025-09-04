package cc.carce.sale.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.service.CarProductsService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/products")
@Slf4j
public class CarProductsApiController extends BaseController {

    @Resource
    private CarProductsService carProductsService;

    /**
     * 获取所有已发布的商品列表
     */
    @GetMapping("/list")
    public R<List<Map<String, Object>>> getProductsList() {
        try {
            List<Map<String, Object>> products = carProductsService.getPublicProducts();
            return R.ok("获取商品列表成功", products);
        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            return R.fail("获取商品列表失败：" + e.getMessage(), null);
        }
    }

    /**
     * 根据分类获取商品列表
     */
    @GetMapping("/category")
    public R<List<Map<String, Object>>> getProductsByCategory(@RequestParam String category) {
        try {
            List<Map<String, Object>> products = carProductsService.getProductsByCategory(category);
            return R.ok("获取分类商品成功", products);
        } catch (Exception e) {
            log.error("获取分类商品失败", e);
            return R.fail("获取分类商品失败：" + e.getMessage(), null);
        }
    }
}
