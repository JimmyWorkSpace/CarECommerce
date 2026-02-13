<template>
  <div class="app-container">
    <el-form style="display: none;" :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="輪播圖位置" prop="bannerType">
        <el-select v-model="queryParams.bannerType" placeholder="請選擇輪播圖位置" clearable>
          <el-option label="首頁輪播" :value="1" />
          <el-option label="商品頁輪播" :value="2" />
        </el-select>
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table 
      v-loading="loading" 
      :data="bannerList" 
      row-key="id">
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
      <el-table-column label="輪播圖位置" align="center" prop="bannerType" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.bannerType === 1 || scope.row.bannerType === '1'">首頁輪播</span>
          <span v-else-if="scope.row.bannerType === 2 || scope.row.bannerType === '2'">商品頁輪播</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="是否跳轉" align="center" prop="isLink" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no_num" :value="scope.row.isLink"/>
        </template>
      </el-table-column>
      <el-table-column label="跳轉地址" align="center" prop="linkUrl" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.isLink === 1 || scope.row.isLink === '1'">{{ scope.row.linkUrl }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="建立時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-up"
            @click="handleMoveUp(scope.row)"
            :disabled="!canMoveUp(scope.row)"
          >上移</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-down"
            @click="handleMoveDown(scope.row)"
            :disabled="!canMoveDown(scope.row)"
          >下移</el-button>
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
        <el-form-item label="是否跳轉" prop="isLink">
          <el-radio-group v-model="form.isLink" @change="handleIsLinkChange">
            <el-radio
              v-for="dict in dict.type.sys_yes_no_num"
              :key="parseInt(dict.value)"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.isLink === 1" label="跳轉地址" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="請輸入跳轉地址" />
        </el-form-item>
        <el-form-item label="輪播圖位置" prop="bannerType">
          <el-select v-model="form.bannerType" placeholder="請選擇輪播圖位置" clearable style="width: 100%">
            <el-option label="首頁輪播" :value="1" />
            <el-option label="商品頁輪播" :value="2" />
          </el-select>
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
        bannerType: null,
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
      }
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
        this.bannerList = response.rows || [];
        // 確保所有記錄的showOrder不為null
        this.bannerList.forEach(banner => {
          if (banner.showOrder == null) {
            banner.showOrder = 0;
          }
        });
        this.total = response.total;
        this.loading = false;
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
        delFlag: 0,
        bannerType: null
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
        // 确保 isLink 和 delFlag 是数字类型
        if (this.form.isLink !== null && this.form.isLink !== undefined) {
          this.form.isLink = parseInt(this.form.isLink);
        } else {
          this.form.isLink = 0;
        }
        // delFlag 默认为 0
        if (this.form.delFlag !== null && this.form.delFlag !== undefined) {
          this.form.delFlag = parseInt(this.form.delFlag);
        } else {
          this.form.delFlag = 0;
        }
        // bannerType 確保為數字
        if (this.form.bannerType !== null && this.form.bannerType !== undefined) {
          this.form.bannerType = parseInt(this.form.bannerType);
        } else {
          this.form.bannerType = null;
        }
        // 不再在修改时清空 linkUrl，保留数据库中的值
        // 如果 isLink 为 0，linkUrl 可能已经有值，但提交时会根据 isLink 清空
        this.open = true;
        this.title = "修改輪播圖";
      });
    },
    /** 提交按鈕 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 确保 delFlag 默认为 0
          if (this.form.delFlag === null || this.form.delFlag === undefined) {
            this.form.delFlag = 0;
          }
          // 如果 isLink 为 0，清空 linkUrl
          if (this.form.isLink === 0 || this.form.isLink === '0') {
            this.form.linkUrl = '';
          }
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
    /** 判斷是否可以上移 */
    canMoveUp(banner) {
      const sortedList = [...this.bannerList].sort((a, b) => {
        const orderA = a.showOrder || 0;
        const orderB = b.showOrder || 0;
        return orderA - orderB;
      });
      return sortedList[0].id !== banner.id;
    },
    /** 判斷是否可以下移 */
    canMoveDown(banner) {
      const sortedList = [...this.bannerList].sort((a, b) => {
        const orderA = a.showOrder || 0;
        const orderB = b.showOrder || 0;
        return orderA - orderB;
      });
      return sortedList[sortedList.length - 1].id !== banner.id;
    },
    /** 上移操作 */
    handleMoveUp(row) {
      const sortedList = [...this.bannerList].sort((a, b) => {
        const orderA = a.showOrder || 0;
        const orderB = b.showOrder || 0;
        return orderA - orderB;
      });
      const currentIndex = sortedList.findIndex(banner => banner.id === row.id);
      
      if (currentIndex <= 0) {
        this.$modal.msgWarning("已經是第一條記錄，無法上移");
        return;
      }
      
      // 找到原始列表中的对象（通过ID）
      const currentItem = this.bannerList.find(banner => banner.id === row.id);
      const prevItem = this.bannerList.find(banner => banner.id === sortedList[currentIndex - 1].id);
      
      if (!currentItem || !prevItem) {
        this.$modal.msgError("找不到對應的記錄");
        return;
      }
      
      // 確保showOrder不為null
      const currentOrder = currentItem.showOrder != null ? currentItem.showOrder : 0;
      const prevOrder = prevItem.showOrder != null ? prevItem.showOrder : 0;
      
      // 交換排序值
      currentItem.showOrder = prevOrder;
      prevItem.showOrder = currentOrder;
      
      // 立即保存排序
      this.saveBannerOrder([currentItem, prevItem]);
    },
    /** 下移操作 */
    handleMoveDown(row) {
      const sortedList = [...this.bannerList].sort((a, b) => {
        const orderA = a.showOrder || 0;
        const orderB = b.showOrder || 0;
        return orderA - orderB;
      });
      const currentIndex = sortedList.findIndex(banner => banner.id === row.id);
      
      if (currentIndex >= sortedList.length - 1) {
        this.$modal.msgWarning("已經是最後一條記錄，無法下移");
        return;
      }
      
      // 找到原始列表中的对象（通过ID）
      const currentItem = this.bannerList.find(banner => banner.id === row.id);
      const nextItem = this.bannerList.find(banner => banner.id === sortedList[currentIndex + 1].id);
      
      if (!currentItem || !nextItem) {
        this.$modal.msgError("找不到對應的記錄");
        return;
      }
      
      // 確保showOrder不為null
      const currentOrder = currentItem.showOrder != null ? currentItem.showOrder : 0;
      const nextOrder = nextItem.showOrder != null ? nextItem.showOrder : 0;
      
      // 交換排序值
      currentItem.showOrder = nextOrder;
      nextItem.showOrder = currentOrder;
      
      // 立即保存排序
      this.saveBannerOrder([currentItem, nextItem]);
    },
    /** 保存輪播圖排序 */
    saveBannerOrder(banners) {
      // 確保所有記錄的showOrder不為null
      const bannersToSave = banners.map(banner => ({
        ...banner,
        showOrder: banner.showOrder != null ? banner.showOrder : 0
      }));
      // 批量更新排序
      updateBannerOrder(bannersToSave).then(() => {
        this.$modal.msgSuccess("排序保存成功");
        // 重新加載列表以刷新顯示
        this.getList();
      }).catch(() => {
        this.$modal.msgError("排序保存失敗");
        // 重新加載列表以恢復原狀
        this.getList();
      });
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
    },
    /** 是否跳转改变时的处理 */
    handleIsLinkChange(value) {
      // 不再清空跳转地址，保留用户输入的内容
      // 这样用户可以在"是"和"否"之间切换而不丢失已输入的地址
    }
  }
};
</script>

<style scoped>
</style> 