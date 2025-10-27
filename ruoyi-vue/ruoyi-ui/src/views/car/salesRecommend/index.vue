<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="銷售標題" prop="saleTitle">
        <el-input
          v-model="queryParams.saleTitle"
          placeholder="請輸入銷售標題"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="銷售員" prop="salesperson">
        <el-input
          v-model="queryParams.salesperson"
          placeholder="請輸入銷售員"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="狀態" prop="status">
        <el-select v-model="queryParams.status" placeholder="請選擇狀態" clearable size="small">
          <el-option label="在售" value="在售" />
          <el-option label="已售" value="已售" />
          <el-option label="下架" value="下架" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否可見" prop="isVisible">
        <el-select v-model="queryParams.isVisible" placeholder="請選擇是否可見" clearable size="small">
          <el-option label="可見" :value="1" />
          <el-option label="不可見" :value="0" />
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:salesRecommend:export']"
        >導出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="salesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="銷售標題" align="center" prop="saleTitleShow" :show-overflow-tooltip="true" />
      <el-table-column label="銷售價格" align="center" prop="salePrice" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.salePrice">¥{{ scope.row.salePrice.toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="里程數" align="center" prop="mileage" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.mileage">{{ scope.row.mileage.toLocaleString() }}km</span>
        </template>
      </el-table-column>
      <el-table-column label="狀態" align="center" prop="status" width="110">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="推薦狀態" align="center" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recommendedValue"
            :active-value="1"
            :inactive-value="0"
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
    <el-dialog title="車輛詳情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="銷售ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="車輛ID">{{ viewData.carId }}</el-descriptions-item>
        <el-descriptions-item label="銷售標題">{{ viewData.saleTitle }}</el-descriptions-item>
        <el-descriptions-item label="銷售員">{{ viewData.salesperson }}</el-descriptions-item>
        <el-descriptions-item label="銷售價格">
          <span v-if="viewData.salePrice">¥{{ viewData.salePrice.toLocaleString() }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="里程數">
          <span v-if="viewData.mileage">{{ viewData.mileage.toLocaleString() }}km</span>
        </el-descriptions-item>
        <el-descriptions-item label="狀態">
          <el-tag :type="getStatusType(viewData.status)">{{ viewData.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推薦狀態">
          <el-tag :type="viewData.recommendedValue === 1 ? 'success' : 'info'">
            {{ viewData.recommendedValue === 1 ? '已推薦' : '未推薦' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="銷售描述" :span="2">
          {{ viewData.saleDescription || '暫無描述' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listSalesRecommend, setSalesRecommended } from "@/api/car/salesRecommend";

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
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        saleTitle: null,
        salesperson: null,
        status: null,
        isVisible: 1
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
      this.viewData = row;
      this.viewOpen = true;
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
    }
  }
};
</script>
