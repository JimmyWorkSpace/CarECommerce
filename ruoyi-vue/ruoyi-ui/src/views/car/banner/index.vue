<template>
  <div class="app-container">
    <el-form style="display: none;" :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="跳轉地址" prop="linkUrl">
        <el-input
          v-model="queryParams.linkUrl"
          placeholder="請輸入跳轉地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否跳轉" prop="isLink">
        <el-select v-model="queryParams.isLink" placeholder="請選擇是否跳轉" clearable>
          <el-option
            v-for="dict in dict.type.sys_yes_no_num"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
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
          v-hasPermi="['car:banner:add']"
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
          v-hasPermi="['car:banner:edit']"
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
          v-hasPermi="['car:banner:edit']"
        >下移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="handleSaveOrder"
          v-hasPermi="['car:banner:edit']"
        >儲存排序</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table 
      v-loading="loading" 
      :data="bannerList" 
      @selection-change="handleSelectionChange"
      row-key="id"
      :row-class-name="tableRowClassName">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="圖片" align="center" prop="imageUrl" width="200">
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
      <el-table-column label="是否跳轉" align="center" prop="isLink" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no_num" :value="scope.row.isLink"/>
        </template>
      </el-table-column>
      <el-table-column label="跳轉地址" align="center" prop="linkUrl" :show-overflow-tooltip="true" />
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
            v-hasPermi="['car:banner:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:banner:remove']"
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

    <!-- 添加或修改輪播圖對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="圖片地址" prop="imageUrl">
          <el-input v-model="form.imageUrl" placeholder="請輸入圖片地址" />
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            :show-file-list="false"
            style="margin-top: 10px;">
            <el-button size="small" type="primary">點擊上傳</el-button>
            <div slot="tip" class="el-upload__tip">只能上傳jpg/png/gif檔案，且不超過2MB</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="跳轉地址" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="請輸入跳轉地址" />
        </el-form-item>
        <el-form-item label="是否跳轉" prop="isLink">
          <el-radio-group v-model="form.isLink">
            <el-radio
              v-for="dict in dict.type.sys_yes_no_num"
              :key="parseInt(dict.value)"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否刪除" prop="delFlag">
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
        <el-button type="primary" @click="submitForm">確 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBanner, getBanner, delBanner, addBanner, updateBanner, updateBannerOrder } from "@/api/car/banner";

export default {
  name: "Banner",
  dicts: ['sys_yes_no_num'],
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
      // 輪播圖表格數據
      bannerList: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        linkUrl: null,
        isLink: null
      },
      // 表單參數
      form: {},
      // 表單校驗
      rules: {
        imageUrl: [
          { required: true, message: "圖片地址不能為空", trigger: "blur" }
        ]
      },
      // 上傳相關
      uploadUrl: process.env.VUE_APP_BASE_API + "/car/banner/upload",
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
      },
      // 排序相關
      orderChanged: false,
      originalOrder: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查詢輪播圖列表 */
    getList() {
      this.loading = true;
      listBanner(this.queryParams).then(response => {
        this.bannerList = response.rows;
        this.total = response.total;
        this.loading = false;
        // 儲存原始排序
        this.originalOrder = this.bannerList.map(item => ({ id: item.id, showOrder: item.showOrder }));
      });
    },
    // 取消按鈕
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表單重置
    reset() {
      this.form = {
        id: null,
        imageUrl: null,
        linkUrl: null,
        isLink: 0,
        delFlag: 0
      };
      this.resetForm("form");
    },
    /** 搜尋按鈕操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按鈕操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多選框選中數據
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按鈕操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加輪播圖";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBanner(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改輪播圖";
      });
    },
    /** 提交按鈕 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBanner(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBanner(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否確認刪除輪播圖編號為"' + ids + '"的數據項？').then(function() {
        return delBanner(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 匯出按鈕操作 */
    handleExport() {
      this.download('car/banner/export', {
        ...this.queryParams
      }, `banner_${new Date().getTime()}.xlsx`)
    },
    /** 上移操作 */
    handleMoveUp() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("請選擇一條記錄進行上移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.bannerList.findIndex(item => item.id === selectedId);
      
      if (currentIndex <= 0) {
        this.$modal.msgWarning("已經是第一條記錄，無法上移");
        return;
      }
      
      // 交換當前記錄與上一條記錄的排序
      const currentItem = this.bannerList[currentIndex];
      const prevItem = this.bannerList[currentIndex - 1];
      
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = prevItem.showOrder;
      prevItem.showOrder = tempOrder;
      
      // 重新排序陣列
      this.bannerList.sort((a, b) => a.showOrder - b.showOrder);
      
      this.orderChanged = true;
      // this.$modal.msgSuccess("上移成功");
    },
    /** 下移操作 */
    handleMoveDown() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("請選擇一條記錄進行下移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.bannerList.findIndex(item => item.id === selectedId);
      
      if (currentIndex >= this.bannerList.length - 1) {
        this.$modal.msgWarning("已經是最後一條記錄，無法下移");
        return;
      }
      
      // 交換當前記錄與下一條記錄的排序
      const currentItem = this.bannerList[currentIndex];
      const nextItem = this.bannerList[currentIndex + 1];
      
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = nextItem.showOrder;
      nextItem.showOrder = tempOrder;
      
      // 重新排序陣列
      this.bannerList.sort((a, b) => a.showOrder - b.showOrder);
      
      this.orderChanged = true;
      // this.$modal.msgSuccess("下移成功");
    },
    /** 儲存排序 */
    handleSaveOrder() {
      // 批次更新排序
      updateBannerOrder(this.bannerList).then(response => {
        this.$modal.msgSuccess("排序儲存成功");
        this.orderChanged = false;
        this.getList();
      });
    },
    /** 表格行樣式 */
    tableRowClassName({row, rowIndex}) {
      // 如果排序發生變化，給行添加高亮樣式
      if (this.orderChanged) {
        const original = this.originalOrder.find(o => o.id === row.id);
        if (original && original.showOrder !== row.showOrder) {
          return 'order-changed-row';
        }
      }
      return '';
    },
    /** 上傳成功回調 */
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.form.imageUrl = response.data;
        this.$modal.msgSuccess("上傳成功");
      } else {
        this.$modal.msgError(response.msg || "上傳失敗");
      }
    },
    /** 上傳失敗回調 */
    handleUploadError(err, file, fileList) {
      this.$modal.msgError("上傳失敗");
    },
    /** 上傳前校驗 */
    beforeUpload(file) {
      const isImage = file.type.indexOf('image') !== -1;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('上傳檔案只能是圖片格式!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('上傳圖片大小不能超過 2MB!');
        return false;
      }
      return true;
    }
  }
};
</script>

<style scoped>
.order-changed-row {
  background-color: #f0f9ff !important;
}
</style> 