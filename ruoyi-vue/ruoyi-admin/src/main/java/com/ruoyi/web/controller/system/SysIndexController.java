package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.StringUtils;

/**
 * 首頁
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController
{
    /** 系統基礎配置 */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    /**
     * 存取首頁，提示語
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("歡迎使用{}後台管理框架，當前版本：v{}，請通過前端地址存取。", ruoyiConfig.getName(), ruoyiConfig.getVersion());
    }
}
