package cc.carce.sale.controller;

import cc.carce.sale.entity.CarQuestionAnswerEntity;
import cc.carce.sale.service.CarQuestionAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * QA模块测试控制器
 */
@Slf4j
@Controller
@RequestMapping("/test")
public class TestQAController {

    @Autowired
    private CarQuestionAnswerService carQuestionAnswerService;

    /**
     * 测试QA模块
     */
    @GetMapping("/qa")
    public String testQA(Model model) {
        try {
            log.info("开始测试QA模块");
            
            // 测试获取所有QA数据
            List<CarQuestionAnswerEntity> allQA = carQuestionAnswerService.getAllQuestionAnswers();
            log.info("获取到所有QA数据数量: {}", allQA != null ? allQA.size() : 0);
            model.addAttribute("allQA", allQA);
            
            // 测试按频道ID获取QA数据
            List<CarQuestionAnswerEntity> channel10QA = carQuestionAnswerService.getQuestionAnswersByChannelIdOrderByShowOrder(10L);
            log.info("频道10的QA数量: {}", channel10QA != null ? channel10QA.size() : 0);
            model.addAttribute("channel10QA", channel10QA);
            
            List<CarQuestionAnswerEntity> channel11QA = carQuestionAnswerService.getQuestionAnswersByChannelIdOrderByShowOrder(11L);
            log.info("频道11的QA数量: {}", channel11QA != null ? channel11QA.size() : 0);
            model.addAttribute("channel11QA", channel11QA);
            
            return "test/qa-test";
        } catch (Exception e) {
            log.error("测试QA模块失败", e);
            model.addAttribute("error", "测试失败: " + e.getMessage());
            return "error/index";
        }
    }
}
