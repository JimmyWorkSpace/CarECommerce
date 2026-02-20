package com.ruoyi.car.controller;

import com.ruoyi.car.domain.CarCardDetailEntity;
import com.ruoyi.car.domain.vo.CarCardDetailVo;
import com.ruoyi.car.service.CarCardDetailService;
import java.util.List;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 票券明細 Controller（後台券碼列表與核銷）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/car/cardDetail")
public class CarCardDetailController extends BaseController {

    @Resource
    private CarCardDetailService carCardDetailService;

    /**
     * 票券明細詳情（含所屬用戶用戶名、手機號）
     */
    @PreAuthorize("@ss.hasPermi('car:cardDetail:list')")
    @GetMapping("/detail/{id}")
    public AjaxResult getDetail(@PathVariable Long id) {
        CarCardDetailVo vo = carCardDetailService.getDetailById(id);
        return vo != null ? AjaxResult.success(vo) : AjaxResult.error("查無該票券明細");
    }

    /**
     * 券碼列表，支援券碼模糊搜尋，分頁
     */
    @PreAuthorize("@ss.hasPermi('car:cardDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ticketCode) {
        startPage();
        List<CarCardDetailVo> list = carCardDetailService.selectListByTicketCodeLike(ticketCode);
        return getDataTable(list);
    }

    /**
     * 依訂單ID查詢票券明細列表
     */
    @PreAuthorize("@ss.hasPermi('car:card:query')")
    @GetMapping("/listByOrder/{orderId}")
    public AjaxResult listByOrderId(@PathVariable Long orderId) {
        List<CarCardDetailEntity> list = carCardDetailService.selectByOrderId(orderId);
        return AjaxResult.success(list);
    }

    /**
     * 依票券唯一碼核銷（記錄核銷時間與當前登入用戶ID）
     */
    @PreAuthorize("@ss.hasPermi('car:cardDetail:redeem')")
    @PostMapping("/redeem")
    public AjaxResult redeem(@RequestBody java.util.Map<String, String> params) {
        String ticketCode = params != null ? params.get("ticketCode") : null;
        int n = carCardDetailService.redeemByTicketCode(ticketCode);
        if (n > 0) {
            return AjaxResult.success("核銷成功");
        }
        return AjaxResult.error("核銷失敗：未找到該票券唯一碼或已核銷");
    }
}
