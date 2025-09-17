<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="销售标题" prop="saleTitle">
        <el-input
          v-model="queryParams.saleTitle"
          placeholder="请输入销售标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="销售员" prop="salesperson">
        <el-input
          v-model="queryParams.salesperson"
          placeholder="请输入销售员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="在售" value="在售" />
          <el-option label="已售" value="已售" />
          <el-option label="下架" value="下架" />
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:salesRecommend:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="salesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="销售标题" align="center" prop="saleTitle" :show-overflow-tooltip="true" />
      <el-table-column label="车辆ID" align="center" prop="carId" width="80" />
      <el-table-column label="销售员" align="center" prop="salesperson" />
      <el-table-column label="销售价格" align="center" prop="salePrice" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.salePrice">¥{{ scope.row.salePrice.toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="里程数" align="center" prop="mileage" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.mileage">{{ scope.row.mileage.toLocaleString() }}km</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="推荐状态" align="center" width="120">
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
      <el-table-column label="创建时间" align="center" prop="createDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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

    <!-- 查看车辆详情对话框 -->
    <el-dialog title="车辆详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="销售ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="车辆ID">{{ viewData.carId }}</el-descriptions-item>
        <el-descriptions-item label="销售标题">{{ viewData.saleTitle }}</el-descriptions-item>
        <el-descriptions-item label="销售员">{{ viewData.salesperson }}</el-descriptions-item>
        <el-descriptions-item label="销售价格">
          <span v-if="viewData.salePrice">¥{{ viewData.salePrice.toLocaleString() }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="里程数">
          <span v-if="viewData.mileage">{{ viewData.mileage.toLocaleString() }}km</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.status)">{{ viewData.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推荐状态">
          <el-tag :type="viewData.recommendedValue === 1 ? 'success' : 'info'">
            {{ viewData.recommendedValue === 1 ? '已推荐' : '未推荐' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="销售描述" :span="2">
          {{ viewData.saleDescription || '暂无描述' }}
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
      // 车辆销售表格数据
      salesList: [],
      // 弹出层标题
      title: "",
      // 是否显示查看弹出层
      viewOpen: false,
      // 查看数据
      viewData: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        saleTitle: null,
        salesperson: null,
        status: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车辆销售列表 */
    getList() {
      this.loading = true;
      listSalesRecommend(this.queryParams).then(response => {
        this.salesList = response.rows;
        this.total = response.total;
        this.loading = false;
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
    /** 推荐状态改变 */
    handleRecommendChange(row) {
      const data = {
        id: row.id,
        recommendedValue: row.recommendedValue
      };
      setSalesRecommended(data).then(response => {
        this.$modal.msgSuccess(row.recommendedValue === 1 ? "设置推荐成功" : "取消推荐成功");
        this.getList();
      }).catch(() => {
        // 如果失败，恢复原状态
        row.recommendedValue = row.recommendedValue === 1 ? 0 : 1;
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewData = row;
      this.viewOpen = true;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('car/salesRecommend/export', {
        ...this.queryParams
      }, `精选好车_${new Date().getTime()}.xlsx`)
    },
    /** 获取状态类型 */
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
