<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="車型" prop="saleTitle">
        <el-input
          v-model="queryParams.saleTitle"
          placeholder="請輸入車型"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="狀態" prop="status">
        <el-select v-model="queryParams.status" placeholder="請選擇狀態" clearable size="small">
          <el-option label="在售" value="在售" />
          <el-option label="已售" value="已售" />
          <el-option label="下架" value="下架" />
        </el-select>
      </el-form-item> -->
      <!-- <el-form-item label="行照照片" prop="hasRegImage">
        <el-select v-model="queryParams.hasRegImage" placeholder="請選擇" clearable size="small">
          <el-option label="已上傳" :value="1" />
          <el-option label="未上傳" :value="0" />
        </el-select>
      </el-form-item> -->
      <el-form-item label="驗證結果" prop="isAdminCheck">
        <el-select v-model="queryParams.isAdminCheck" placeholder="請選擇" clearable size="small">
          <el-option label="待審核" :value="0" />
          <el-option label="驗證通過" :value="1" />
          <el-option label="驗證未通過" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="刊登曝光" prop="isPublish">
        <el-select v-model="queryParams.isPublish" placeholder="請選擇" clearable size="small">
          <el-option label="未選擇" :value="0" />
          <el-option label="代上架宣傳" :value="1" />
          <el-option label="下架不要宣傳" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="salesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="車型" align="center" prop="saleTitleShow" :show-overflow-tooltip="true" />
      <el-table-column label="狀態" align="center" prop="status" width="110">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="行照" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.regImage ? 'success' : 'danger'" size="small">
            {{ scope.row.regImage ? '已上傳' : '未上傳' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="驗證結果" align="center" width="120">
        <template slot-scope="scope">
          <el-tag :type="getAdminCheckType(scope.row.isAdminCheck)">
            {{ getAdminCheckText(scope.row.isAdminCheck) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="多元刊登曝光" align="center" width="140">
        <template slot-scope="scope">
          <el-tag :type="getPublishType(scope.row.isPublish)">
            {{ getPublishText(scope.row.isPublish) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="推薦狀態" align="center" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recommendedValue"
            :active-value="1"
            :inactive-value="0"
            :disabled="!isRecommendEnabled(scope.row)"
            @change="handleRecommendChange(scope.row)"
            v-hasPermi="['car:salesRecommend:edit']"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['car:salesRecommend:query']"
          >查看</el-button>
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

    <!-- 查看車輛詳情對話框 -->
    <el-dialog title="車輛詳情" :visible.sync="viewOpen" width="80%" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="車型">{{ viewData.saleTitleShow + ' ' + viewData.saleTitle }}</el-descriptions-item>
        <el-descriptions-item label="價格">
          <span v-if="viewData.salePrice">${{ viewData.salePrice.toLocaleString() }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="狀態">
          <el-tag :type="getStatusType(viewData.status)">{{ viewData.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推薦狀態">
          <el-tag :type="viewData.recommendedValue === 1 ? 'success' : 'info'">
            {{ viewData.recommendedValue === 1 ? '已推薦' : '未推薦' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="行照照片">
          <div v-if="viewData.regImage" style="cursor: pointer;" @click="previewImage(viewData.regImage)">
            <img :src="viewData.regImage" style="width: 100px; height: 100px; object-fit: cover; border: 1px solid #ddd; border-radius: 4px;" />
          </div>
          <span v-else style="color: #909399;">未上傳</span>
        </el-descriptions-item>
        <el-descriptions-item></el-descriptions-item>
        <el-descriptions-item label="站方驗證結果" :span="2">
          <el-select v-model="viewData.isAdminCheck" placeholder="請選擇" size="small" style="width: 200px;" :disabled="!viewData.regImage" @change="handleAdminCheckChange">
            <el-option label="待審核" :value="0" />
            <el-option label="驗證通過" :value="1" />
            <el-option label="驗證未通過" :value="2" />
          </el-select>
        </el-descriptions-item>
        <el-descriptions-item label="店家刊登曝光">
          <span>{{ getPublishText(viewData.isPublish) }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 圖片預覽對話框 -->
    <el-dialog :visible.sync="imagePreviewVisible" width="80%" append-to-body>
      <div style="text-align: center;">
        <img :src="previewImageUrl" style="max-width: 100%; max-height: 80vh;" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSalesRecommend, setSalesRecommended, updateAdminCheck } from "@/api/car/salesRecommend";

export default {
  name: "SalesRecommend",
  data() {
    return {
      // 遮罩層
      loading: true,
      // 選中數組
      ids: [],
      // 非單個禁用
      single: true,
      // 非多個禁用
      multiple: true,
      // 顯示搜索條件
      showSearch: true,
      // 總條數
      total: 0,
      // 車輛銷售表格數據
      salesList: [],
      // 彈出層標題
      title: "",
      // 是否顯示查看彈出層
      viewOpen: false,
      // 查看數據
      viewData: {},
      // 圖片預覽
      imagePreviewVisible: false,
      previewImageUrl: '',
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        saleTitle: null,
        salesperson: null,
        status: null,
        isVisible: 1,
        // hasRegImage: null,
        isAdminCheck: null,
        isPublish: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查詢車輛銷售列表 */
    getList() {
      this.loading = true;
      listSalesRecommend(this.queryParams).then(response => {
        this.salesList = response.rows;
        // 處理推薦狀態：當 isAdminCheck !== 1 或 isPublish !== 1 時，將推薦狀態設為關閉
        this.salesList.forEach(item => {
          if (!this.isRecommendEnabled(item)) {
            item.recommendedValue = 0;
          }
        });
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按鈕操作 */
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
    /** 推薦狀態改變 */
    handleRecommendChange(row) {
      const data = {
        id: row.id,
        recommendedValue: row.recommendedValue
      };
      setSalesRecommended(data).then(response => {
        this.$modal.msgSuccess(row.recommendedValue === 1 ? "設置推薦成功" : "取消推薦成功");
        this.getList();
      }).catch(() => {
        // 如果失敗，恢復原狀態
        row.recommendedValue = row.recommendedValue === 1 ? 0 : 1;
      });
    },
    /** 查看按鈕操作 */
    handleView(row) {
      this.viewData = { ...row };
      this.viewOpen = true;
    },
    /** 站方驗證結果改變 */
    handleAdminCheckChange(value) {
      const oldValue = this.viewData.isAdminCheck;
      const data = {
        id: this.viewData.id,
        isAdminCheck: value
      };
      updateAdminCheck(data).then(response => {
        this.$modal.msgSuccess("更新成功");
        // 更新列表中的数据
        const index = this.salesList.findIndex(item => item.id === this.viewData.id);
        if (index !== -1) {
          this.salesList[index].isAdminCheck = value;
        }
      }).catch(() => {
        // 如果失敗，恢復原狀態
        this.viewData.isAdminCheck = oldValue;
        this.$modal.msgError("更新失敗");
      });
    },
    /** 預覽圖片 */
    previewImage(imageUrl) {
      this.previewImageUrl = imageUrl;
      this.imagePreviewVisible = true;
    },
    /** 導出按鈕操作 */
    handleExport() {
      this.download('car/salesRecommend/export', {
        ...this.queryParams
      }, `精選好車_${new Date().getTime()}.xlsx`)
    },
    /** 獲取狀態類型 */
    getStatusType(status) {
      const statusMap = {
        '在售': 'success',
        '已售': 'info',
        '下架': 'danger'
      };
      return statusMap[status] || 'info';
    },
    /** 獲取驗證結果文本 */
    getAdminCheckText(isAdminCheck) {
      if (isAdminCheck === null || isAdminCheck === undefined) {
        return '待審核';
      }
      const textMap = {
        0: '待審核',
        1: '驗證通過',
        2: '驗證未通過'
      };
      return textMap[isAdminCheck] || '待審核';
    },
    /** 獲取驗證結果標籤類型 */
    getAdminCheckType(isAdminCheck) {
      if (isAdminCheck === null || isAdminCheck === undefined) {
        return 'info';
      }
      const typeMap = {
        0: 'info',
        1: 'success',
        2: 'danger'
      };
      return typeMap[isAdminCheck] || 'info';
    },
    /** 獲取多元刊登曝光文本 */
    getPublishText(isPublish) {
      if (isPublish === null || isPublish === undefined) {
        return '未選擇';
      }
      const textMap = {
        0: '未選擇',
        1: '代上架宣傳',
        2: '下架不要宣傳'
      };
      return textMap[isPublish] || '未選擇';
    },
    /** 獲取多元刊登曝光標籤類型 */
    getPublishType(isPublish) {
      if (isPublish === null || isPublish === undefined) {
        return 'info';
      }
      const typeMap = {
        0: 'info',
        1: 'success',
        2: 'warning'
      };
      return typeMap[isPublish] || 'info';
    },
    /** 判斷推薦狀態是否可用 */
    isRecommendEnabled(row) {
      // 當 isAdminCheck === 1 且 isPublish === 1 時可用
      return row.isAdminCheck === 1 && row.isPublish === 1;
    }
  }
};
</script>
