package cc.carce.sale.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.common.R;
import cc.carce.sale.config.AuthInterceptor.UserInfo;
import cc.carce.sale.dto.MyTicketItemDto;
import cc.carce.sale.service.CarCardDetailService;
import lombok.extern.slf4j.Slf4j;

/**
 * 我的票券 API 控制器（RESTful，非頁面跳轉）
 */
@Slf4j
@RestController
@RequestMapping("/my-tickets")
public class MyTicketController extends BaseController {

    @Resource
    private CarCardDetailService carCardDetailService;

    /**
     * 我的票券列表（登入後取得）
     */
    @GetMapping("/list")
    @ResponseBody
    public R<List<MyTicketItemDto>> list() {
        try {
            UserInfo userInfo = getSessionUser();
            if (userInfo == null) {
                return R.fail("請先登錄", null);
            }
            List<MyTicketItemDto> list = carCardDetailService.listMyTicketsByUserId(userInfo.getId());
            return R.ok("獲取成功", list != null ? list : java.util.Collections.emptyList());
        } catch (Exception e) {
            log.error("獲取我的票券列表異常", e);
            return R.fail("獲取票券列表異常: " + e.getMessage(), null);
        }
    }
}
