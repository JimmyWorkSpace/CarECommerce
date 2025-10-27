package com.ruoyi.car.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.car.domain.CarQuestionAnswerEntity;
import com.ruoyi.car.service.ICarQuestionAnswerService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 问答模块Controller
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/car/questionAnswer")
public class CarQuestionAnswerController extends BaseController
{
    @Autowired
    private ICarQuestionAnswerService carQuestionAnswerService;

    /**
     * 查询问答模块列表
     */
    @PreAuthorize("@ss.hasPermi('car:questionAnswer:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarQuestionAnswerEntity carQuestionAnswer)
    {
        startPage();
        List<CarQuestionAnswerEntity> list = carQuestionAnswerService.selectCarQuestionAnswerList(carQuestionAnswer);
        return getDataTable(list);
    }

    /**
     * 导出问答模块列表
     */
    @PreAuthorize("@ss.hasPermi('car:questionAnswer:export')")
    @Log(title = "问答模块", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CarQuestionAnswerEntity carQuestionAnswer)
    {
        List<CarQuestionAnswerEntity> list = carQuestionAnswerService.selectCarQuestionAnswerList(carQuestionAnswer);
        ExcelUtil<CarQuestionAnswerEntity> util = new ExcelUtil<CarQuestionAnswerEntity>(CarQuestionAnswerEntity.class);
        util.exportExcel(response, list, "问答模块数据");
    }

    /**
     * 获取问答模块详细信息
     */
    @PreAuthorize("@ss.hasPermi('car:questionAnswer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(carQuestionAnswerService.selectCarQuestionAnswerById(id));
    }

    /**
     * 新增问答模块
     */
    @PreAuthorize("@ss.hasPermi('car:questionAnswer:add')")
    @Log(title = "问答模块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CarQuestionAnswerEntity carQuestionAnswer)
    {
        return toAjax(carQuestionAnswerService.insertCarQuestionAnswer(carQuestionAnswer));
    }

    /**
     * 修改问答模块
     */
    @PreAuthorize("@ss.hasPermi('car:questionAnswer:edit')")
    @Log(title = "问答模块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CarQuestionAnswerEntity carQuestionAnswer)
    {
        return toAjax(carQuestionAnswerService.updateCarQuestionAnswer(carQuestionAnswer));
    }

    /**
     * 刪除问答模块
     */
    @PreAuthorize("@ss.hasPermi('car:questionAnswer:remove')")
    @Log(title = "问答模块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(carQuestionAnswerService.deleteCarQuestionAnswerByIds(ids));
    }
}