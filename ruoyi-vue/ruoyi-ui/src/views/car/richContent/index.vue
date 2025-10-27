<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="標題" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入標題"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="内容類型" prop="contentType">
        <el-select v-model="queryParams.contentType" placeholder="请选择内容類型" clearable size="small">
          <el-option
            v-for="dict in contentTypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['car:richContent:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['car:richContent:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-arrow-up"
          size="mini"
          :disabled="single"
          @click="handleMoveUp"
          v-hasPermi="['car:richContent:edit']"
        >上移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-arrow-down"
          size="mini"
          :disabled="single"
          @click="handleMoveDown"
          v-hasPermi="['car:richContent:edit']"
        >下移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="handleSaveOrder"
          v-hasPermi="['car:richContent:edit']"
        >保存排序</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['car:richContent:remove']"
        >刪除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table 
      v-loading="loading" 
      :data="richContentList" 
      @selection-change="handleSelectionChange"
      row-key="id"
      :row-class-name="tableRowClassName">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主键ID" align="center" prop="id" /> -->
      <el-table-column label="標題" align="center" prop="title" />
      <!-- <el-table-column label="内容類型" align="center" prop="contentType">
        <template slot-scope="scope">
          <dict-tag :options="contentTypeOptions" :value="scope.row.contentType"/>
        </template>
      </el-table-column> -->
      <el-table-column label="建立時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:richContent:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:richContent:remove']"
          >刪除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改富文本内容对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="95%" :top="'15vh'" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="標題" prop="title">
              <el-input v-model="form.title" placeholder="请输入標題" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="内容" prop="content">
          <editor v-model="form.content" :min-height="500"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRichContent, getRichContent, delRichContent, addRichContent, updateRichContent, updateRichContentOrder } from "@/api/car/richContent";

export default {
  name: "RichContent",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 富文本内容表格数据
      richContentList: [],
      // 弹出层標題
      title: "",
      // 是否显示弹出层
      open: false,
      // 内容類型字典
      contentTypeOptions: [
        { label: "关于", value: 1 },
        { label: "頻道", value: 2 }
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        contentType: 2
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "標題不能为空", trigger: "blur" }
        ]
      },
      // 排序相关
      orderChanged: false,
      originalOrder: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询富文本内容列表 */
    getList() {
      this.loading = true;
      listRichContent(this.queryParams).then(response => {
        this.richContentList = response.rows;
        this.total = response.total;
        this.loading = false;
        // 保存原始排序
        this.originalOrder = this.richContentList.map(item => ({ id: item.id, showOrder: item.showOrder }));
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加富文本内容";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getRichContent(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改富文本内容";
      });
    },
    /** 刪除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认刪除富文本内容编號为"' + ids + '"的数据项？').then(function() {
        return delRichContent(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        content: null,
        contentType: 2,
        showOrder: 0
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRichContent(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRichContent(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 上移操作 */
    handleMoveUp() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("请选择一条记录进行上移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.richContentList.findIndex(item => item.id === selectedId);
      
      if (currentIndex <= 0) {
        this.$modal.msgWarning("已经是第一条记录，无法上移");
        return;
      }
      
      // 交换当前记录与上一条记录的排序
      const currentItem = this.richContentList[currentIndex];
      const prevItem = this.richContentList[currentIndex - 1];
      
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = prevItem.showOrder;
      prevItem.showOrder = tempOrder;
      
      // 重新排序数组
      this.richContentList.sort((a, b) => a.showOrder - b.showOrder);
      
      this.orderChanged = true;
    },
    /** 下移操作 */
    handleMoveDown() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("请选择一条记录进行下移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.richContentList.findIndex(item => item.id === selectedId);
      
      if (currentIndex >= this.richContentList.length - 1) {
        this.$modal.msgWarning("已经是最后一条记录，无法下移");
        return;
      }
      
      // 交换当前记录与下一条记录的排序
      const currentItem = this.richContentList[currentIndex];
      const nextItem = this.richContentList[currentIndex + 1];
      
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = nextItem.showOrder;
      nextItem.showOrder = tempOrder;
      
      // 重新排序数组
      this.richContentList.sort((a, b) => a.showOrder - b.showOrder);
      
      this.orderChanged = true;
    },
    /** 保存排序 */
    handleSaveOrder() {
      // 批量更新排序
      updateRichContentOrder(this.richContentList).then(response => {
        this.$modal.msgSuccess("排序保存成功");
        this.orderChanged = false;
        this.getList();
      });
    },
    /** 表格行样式 */
    tableRowClassName({row, rowIndex}) {
      // 如果排序发生变化，给行添加高亮样式
      if (this.orderChanged) {
        const original = this.originalOrder.find(o => o.id === row.id);
        if (original && original.showOrder !== row.showOrder) {
          return 'order-changed-row';
        }
      }
      return '';
    }
  }
};
</script>

<style scoped>
.order-changed-row {
  background-color: #f0f9ff !important;
}
</style>
