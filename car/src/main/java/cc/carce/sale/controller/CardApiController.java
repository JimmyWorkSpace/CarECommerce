package cc.carce.sale.controller;

import cc.carce.sale.common.R;
import cc.carce.sale.entity.CarCardEntity;
import cc.carce.sale.service.CarCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 票券 API（供前台票券列表、下單使用）
 */
@RestController
@RequestMapping("/api/card")
public class CardApiController extends BaseController {

    @Resource
    private CarCardService carCardService;

    /**
     * 票券列表（僅啟用中的票券，供展示與後續加入購物車使用）
     */
    @GetMapping("/list")
    public R<List<CarCardEntity>> list() {
        List<CarCardEntity> list = carCardService.listEnabled();
        return R.ok(list);
    }
}
