<template>
  <div class="app-container">
    <el-form style="display: none;" :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="跳转地址" prop="linkUrl">
        <el-input
          v-model="queryParams.linkUrl"
          placeholder="请输入跳转地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否跳转" prop="isLink">
        <el-select v-model="queryParams.isLink" placeholder="请选择是否跳转" clearable>
          <el-option
            v-for="dict in dict.type.sys_yes_no_num"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['car:advertisement:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-arrow-up"
          size="mini"
          :disabled="single"
          @click="handleMoveUp"
          v-hasPermi="['car:advertisement:edit']"
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
          v-hasPermi="['car:advertisement:edit']"
        >下移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="handleSaveOrder"
          v-hasPermi="['car:advertisement:edit']"
        >保存排序</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table 
      v-loading="loading" 
      :data="advertisementList" 
      @selection-change="handleSelectionChange"
      row-key="id"
      :row-class-name="tableRowClassName">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图片" align="center" prop="imageUrl" width="200">
        <template slot-scope="scope">
          <el-image 
            style="width: 100px; height: 60px"
            :src="scope.row.imageUrl"
            fit="cover"
            :preview-src-list="[scope.row.imageUrl]">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="是否跳转" align="center" prop="isLink" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no_num" :value="scope.row.isLink"/>
        </template>
      </el-table-column>
      <el-table-column label="跳转地址" align="center" prop="linkUrl" :show-overflow-tooltip="true" />
      <el-table-column label="排序" align="center" prop="showOrder" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
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
            v-hasPermi="['car:advertisement:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:advertisement:remove']"
          >删除</el-button>
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

    <!-- 添加或修改广告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="图片地址" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="请输入图片地址" />
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="false"
            style="margin-top: 10px;">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png/gif文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="是否跳转" prop="isLink">
          <el-radio-group v-model="form.isLink" @change="handleIsLinkChange">
            <el-radio
              v-for="dict in dict.type.sys_yes_no_num"
              :key="parseInt(dict.value)"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="跳转地址" prop="linkUrl" v-if="form.isLink === 1">
          <el-input v-model="form.linkUrl" placeholder="请输入跳转地址" />
        </el-form-item>
        <el-form-item label="显示内容" prop="content" v-if="form.isLink === 0">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="是否删除" prop="delFlag">
          <el-radio-group v-model="form.delFlag">
            <el-radio
              v-for="dict in dict.type.sys_yes_no_num"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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
import { listAdvertisement, getAdvertisement, delAdvertisement, addAdvertisement, updateAdvertisement, updateAdvertisementOrder } from "@/api/car/advertisement";
import Editor from "@/components/Editor";

export default {
  name: "Advertisement",
  dicts: ['sys_yes_no_num'],
  components: {
    Editor
  },
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
      // 广告表格数据
      advertisementList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        linkUrl: null,
        isLink: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        imageUrl: [
          { required: true, message: "图片地址不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ]
      },
      // 上传相关
      uploadUrl: process.env.VUE_APP_BASE_API + "/car/advertisement/upload",
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
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
    /** 查询广告列表 */
    getList() {
      this.loading = true;
      listAdvertisement(this.queryParams).then(response => {
        this.advertisementList = response.rows;
        this.total = response.total;
        this.loading = false;
        // 保存原始排序
        this.originalOrder = this.advertisementList.map(item => ({ id: item.id, showOrder: item.showOrder }));
      });
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
        imageUrl: null,
        linkUrl: null,
        isLink: 0,
        content: null,
        title: null,
        delFlag: 0
      };
      this.resetForm("form");
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
      this.title = "添加广告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAdvertisement(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改广告";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAdvertisement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAdvertisement(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除广告编号为"' + ids + '"的数据项？').then(function() {
        return delAdvertisement(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('car/advertisement/export', {
        ...this.queryParams
      }, `advertisement_${new Date().getTime()}.xlsx`)
    },
    /** 上移操作 */
    handleMoveUp() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("请选择一条记录进行上移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.advertisementList.findIndex(item => item.id === selectedId);
      
      if (currentIndex <= 0) {
        this.$modal.msgWarning("已经是第一条记录，无法上移");
        return;
      }
      
      // 交换当前记录与上一条记录的排序
      const currentItem = this.advertisementList[currentIndex];
      const prevItem = this.advertisementList[currentIndex - 1];
      
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = prevItem.showOrder;
      prevItem.showOrder = tempOrder;
      
      // 重新排序数组
      this.advertisementList.sort((a, b) => a.showOrder - b.showOrder);
      
      this.orderChanged = true;
    },
    /** 下移操作 */
    handleMoveDown() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("请选择一条记录进行下移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.advertisementList.findIndex(item => item.id === selectedId);
      
      if (currentIndex >= this.advertisementList.length - 1) {
        this.$modal.msgWarning("已经是最后一条记录，无法下移");
        return;
      }
      
      // 交换当前记录与下一条记录的排序
      const currentItem = this.advertisementList[currentIndex];
      const nextItem = this.advertisementList[currentIndex + 1];
      
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = nextItem.showOrder;
      nextItem.showOrder = tempOrder;
      
      // 重新排序数组
      this.advertisementList.sort((a, b) => a.showOrder - b.showOrder);
      
      this.orderChanged = true;
    },
    /** 保存排序 */
    handleSaveOrder() {
      // 批量更新排序
      updateAdvertisementOrder(this.advertisementList).then(response => {
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
    },
    /** 上传成功回调 */
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.form.imageUrl = response.data;
        this.$modal.msgSuccess("上传成功");
      } else {
        this.$modal.msgError(response.msg || "上传失败");
      }
    },
    /** 上传失败回调 */
    handleUploadError(err, file, fileList) {
      this.$modal.msgError("上传失败");
    },
    /** 上传前校验 */
    beforeUpload(file) {
      const isImage = file.type.indexOf('image') !== -1;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
        return false;
      }
      return true;
    },
    /** 是否跳转变化处理 */
    handleIsLinkChange(value) {
      if (value === 1) {
        // 选择跳转时，清空内容
        this.form.content = null;
      } else {
        // 选择不跳转时，清空跳转地址
        this.form.linkUrl = null;
      }
    }
  }
};
</script>

<style scoped>
.order-changed-row {
  background-color: #f0f9ff !important;
}
</style> 