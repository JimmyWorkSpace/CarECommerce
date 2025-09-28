package cc.carce.sale.controller;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.CarConfigEntity;
import cc.carce.sale.entity.dto.CarConfigContent;
import cc.carce.sale.service.CarConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网站配置控制器
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Api(tags = "网站配置API", description = "网站配置相关接口")
@RestController
@RequestMapping("/api/config")
@Slf4j
@CrossOrigin
public class CarConfigController {

    @Resource
    private CarConfigService carConfigService;

    /**
     * 获取所有配置项
     */
    @ApiOperation("获取所有配置项")
    @GetMapping("/list")
    public R<List<CarConfigEntity>> getAllConfigs() {
        try {
            List<CarConfigEntity> configs = carConfigService.getAllConfigs();
            return R.ok(configs);
        } catch (Exception e) {
            log.error("获取配置项失败", e);
            return R.failMsg("获取配置项失败");
        }
    }

    /**
     * 根据代码获取配置值
     */
    @ApiOperation("根据代码获取配置值")
    @GetMapping("/value/{code}")
    public R<String> getConfigValue(@PathVariable String code) {
        try {
            String value = carConfigService.getConfigValue(code);
            return R.ok(value);
        } catch (Exception e) {
            log.error("获取配置值失败，code: {}", code, e);
            return R.failMsg("获取配置值失败");
        }
    }

    /**
     * 获取网站配置内容
     */
    @ApiOperation("获取网站配置内容")
    @GetMapping("/content")
    public R<CarConfigContent> getConfigContent() {
        try {
            CarConfigContent content = carConfigService.getConfigContent();
            return R.ok(content);
        } catch (Exception e) {
            log.error("获取配置内容失败", e);
            return R.failMsg("获取配置内容失败");
        }
    }
}
