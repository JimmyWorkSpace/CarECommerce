package cc.carce.sale.controller;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.dto.CarAppointmentDto;
import cc.carce.sale.form.CarAppointmentForm;
import cc.carce.sale.service.CarAppointmentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 预约看车控制器
 */
@Controller
@RequestMapping("/appointment")
@Slf4j
@Api(tags = "预约看车", description = "预约看车相关接口")
public class CarAppointmentController extends BaseController {

    @Resource
    private CarAppointmentService carAppointmentService;

    /**
     * 预约看车页面
     */
    @GetMapping("/create/{carSaleId}")
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
            
            // 添加菜单数据
            addMenuData(model);
            
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
    @GetMapping("/my-appointments")
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
            
            // 添加菜单数据
            addMenuData(model);
            
            log.info("用户访问我的预约页面，用户ID: {}", userInfo.getId());
            
            return "/layout/main";
        } catch (Exception e) {
            log.error("显示我的预约页面异常", e);
            model.addAttribute("error", "页面加载失败：" + e.getMessage());
            return "/layout/main";
        }
    }

    /**
     * 创建预约API
     */
    @PostMapping("/api/create")
    @ResponseBody
    public R<Long> createAppointment(@Valid @RequestBody CarAppointmentForm form, HttpServletRequest request) {
        try {
            // 从session中获取用户ID，这里需要根据实际的用户认证方式调整
            Long userId = getCurrentUserId();
            if (userId == null) {
                return R.fail("请先登录", null);
            }

            Long appointmentId = carAppointmentService.createAppointment(form, userId);
            return R.ok("预约成功", appointmentId);
        } catch (Exception e) {
            log.error("创建预约失败", e);
            return R.fail("预约失败：" + e.getMessage(), null);
        }
    }

    /**
     * 获取我的预约列表API
     */
    @GetMapping("/api/my-appointments")
    @ResponseBody
    public R<List<CarAppointmentDto>> getMyAppointments(HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return R.fail("请先登录", null);
            }

            List<CarAppointmentDto> appointments = carAppointmentService.getAppointmentsByUserId(userId);
            return R.ok("查询成功", appointments);
        } catch (Exception e) {
            log.error("查询预约列表失败", e);
            return R.fail("查询失败：" + e.getMessage(), null);
        }
    }

    /**
     * 取消预约API
     */
    @PostMapping("/api/cancel/{id}")
    @ResponseBody
    public R<Boolean> cancelAppointment(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return R.fail("请先登录", null);
            }

            // 验证预约是否属于当前用户
            CarAppointmentDto appointment = carAppointmentService.getAppointmentById(id);
            if (appointment == null || !appointment.getUserId().equals(userId)) {
                return R.fail("预约不存在或无权限操作", false);
            }

            boolean success = carAppointmentService.cancelAppointment(id);
            if (success) {
                return R.ok("取消成功", true);
            } else {
                return R.fail("取消失败", false);
            }
        } catch (Exception e) {
            log.error("取消预约失败", e);
            return R.fail("取消失败：" + e.getMessage(), false);
        }
    }

    /**
     * 确认看车API
     */
    @PostMapping("/api/confirm/{id}")
    @ResponseBody
    public R<Boolean> confirmViewing(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return R.fail("请先登录", null);
            }

            // 验证预约是否属于当前用户
            CarAppointmentDto appointment = carAppointmentService.getAppointmentById(id);
            if (appointment == null || !appointment.getUserId().equals(userId)) {
                return R.fail("预约不存在或无权限操作", false);
            }

            boolean success = carAppointmentService.confirmViewing(id);
            if (success) {
                return R.ok("确认成功", true);
            } else {
                return R.fail("确认失败", false);
            }
        } catch (Exception e) {
            log.error("确认看车失败", e);
            return R.fail("确认失败：" + e.getMessage(), false);
        }
    }

}
