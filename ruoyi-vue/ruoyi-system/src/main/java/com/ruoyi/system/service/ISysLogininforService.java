package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysLogininfor;

/**
 * 系统访问日志情况信息 服务层
 * 
 * @author ruoyi
 */
public interface ISysLogininforService
{
    /**
     * 新增系统登入日志
     * 
     * @param logininfor 访问日志对象
     */
    public void insertLogininfor(SysLogininfor logininfor);

    /**
     * 查询系统登入日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登入记录集合
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * 批量刪除系统登入日志
     * 
     * @param infoIds 需要刪除的登入日志ID
     * @return 结果
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登入日志
     */
    public void cleanLogininfor();
}
