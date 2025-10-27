package com.ruoyi.generator.mapper.master;

import java.util.List;
import com.ruoyi.generator.domain.GenTableColumn;

/**
 * 业务字段 数据层
 * 
 * @author ruoyi
 */
public interface GenTableColumnMapper
{
    /**
     * 根据表名稱查询列信息
     * 
     * @param tableName 表名稱
     * @return 列信息
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);

    /**
     * 查询业务字段列表
     * 
     * @param tableId 业务字段编號
     * @return 业务字段集合
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

    /**
     * 新增业务字段
     * 
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 修改业务字段
     * 
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * 刪除业务字段
     * 
     * @param genTableColumns 列数据
     * @return 结果
     */
    public int deleteGenTableColumns(List<GenTableColumn> genTableColumns);

    /**
     * 批量刪除业务字段
     * 
     * @param ids 需要刪除的数据ID
     * @return 结果
     */
    public int deleteGenTableColumnByIds(Long[] ids);
}
