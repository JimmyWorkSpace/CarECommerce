package cc.carce.sale.controller;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.CarRichContentEntity;
import cc.carce.sale.service.CarRichContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 富文本内容控制器
 */
@Slf4j
@Controller
@RequestMapping("/rich-content")
@Api(tags = "富文本内容管理")
public class CarRichContentController {
    
    @Autowired
    private CarRichContentService carRichContentService;
    
    /**
     * 富文本内容管理页面
     */
    @GetMapping("/manage")
    public String managePage(Model model) {
        try {
            List<CarRichContentEntity> allContent = carRichContentService.getAllRichContent();
            model.addAttribute("contentList", allContent);
            return "rich-content/manage";
        } catch (Exception e) {
            log.error("获取富文本内容管理页面失败", e);
            model.addAttribute("error", "获取数据失败");
            return "error/index";
        }
    }
    
    /**
     * 关于页面
     */
    @GetMapping("/about")
    public String aboutPage(Model model) {
        try {
            CarRichContentEntity aboutContent = carRichContentService.getFirstAboutContent();
            model.addAttribute("aboutContent", aboutContent);
            // 设置HTML内容属性
            if (aboutContent != null && aboutContent.getContent() != null) {
                model.addAttribute("htmlContent", aboutContent.getContent());
            }
            return "about/index";
        } catch (Exception e) {
            log.error("获取关于页面失败", e);
            model.addAttribute("error", "获取数据失败");
            return "error/index";
        }
    }
    
    /**
     * 频道页面
     */
    @GetMapping("/channel")
    public String channelPage(Model model) {
        try {
            CarRichContentEntity channelContent = carRichContentService.getFirstChannelContent();
            model.addAttribute("channelContent", channelContent);
            // 设置HTML内容属性
            if (channelContent != null && channelContent.getContent() != null) {
                model.addAttribute("htmlContent", channelContent.getContent());
            }
            return "channel/index";
        } catch (Exception e) {
            log.error("获取频道页面失败", e);
            model.addAttribute("error", "获取数据失败");
            return "error/index";
        }
    }
    
    /**
     * 创建富文本内容
     */
    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("创建富文本内容")
    public R<String> createRichContent(@RequestBody CarRichContentEntity entity) {
        try {
            boolean success = carRichContentService.createRichContent(entity);
            if (success) {
                return R.okMsg("创建成功");
            } else {
                return R.failMsg("创建失败");
            }
        } catch (Exception e) {
            log.error("创建富文本内容失败", e);
            return R.failMsg("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新富文本内容
     */
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新富文本内容")
    public R<String> updateRichContent(@RequestBody CarRichContentEntity entity) {
        try {
            boolean success = carRichContentService.updateRichContent(entity);
            if (success) {
                return R.okMsg("更新成功");
            } else {
                return R.failMsg("更新失败");
            }
        } catch (Exception e) {
            log.error("更新富文本内容失败", e);
            return R.failMsg("更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除富文本内容
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除富文本内容")
    public R<String> deleteRichContent(@PathVariable Long id) {
        try {
            boolean success = carRichContentService.deleteRichContent(id);
            if (success) {
                return R.okMsg("删除成功");
            } else {
                return R.failMsg("删除失败");
            }
        } catch (Exception e) {
            log.error("删除富文本内容失败", e);
            return R.failMsg("删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取富文本内容
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    @ApiOperation("根据ID获取富文本内容")
    public R<CarRichContentEntity> getRichContentById(@PathVariable Long id) {
        try {
            CarRichContentEntity content = carRichContentService.getRichContentById(id);
            if (content != null) {
                return R.ok(content);
            } else {
                return R.failMsg("内容不存在");
            }
        } catch (Exception e) {
            log.error("获取富文本内容失败", e);
            return R.failMsg("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据类型获取富文本内容列表
     */
    @GetMapping("/list/{contentType}")
    @ResponseBody
    @ApiOperation("根据类型获取富文本内容列表")
    public R<List<CarRichContentEntity>> getRichContentByType(@PathVariable Integer contentType) {
        try {
            List<CarRichContentEntity> contentList = carRichContentService.getRichContentByType(contentType);
            return R.ok(contentList);
        } catch (Exception e) {
            log.error("根据类型获取富文本内容列表失败", e);
            return R.failMsg("获取失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有富文本内容列表
     */
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("获取所有富文本内容列表")
    public R<List<CarRichContentEntity>> getAllRichContent() {
        try {
            List<CarRichContentEntity> contentList = carRichContentService.getAllRichContent();
            return R.ok(contentList);
        } catch (Exception e) {
            log.error("获取所有富文本内容列表失败", e);
            return R.failMsg("获取失败：" + e.getMessage());
        }
    }
}
