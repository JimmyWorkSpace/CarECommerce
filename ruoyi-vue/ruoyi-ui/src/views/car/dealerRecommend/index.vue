<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="經銷商名稱" prop="dealerName">
        <el-input
          v-model="queryParams.dealerName"
          placeholder="請輸入經銷商名稱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="聯繫人" prop="contactPerson">
        <el-input
          v-model="queryParams.contactPerson"
          placeholder="請輸入聯繫人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" v-show="false">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:dealerRecommend:export']"
        >導出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dealerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="ID" align="center" prop="id" width="80" /> -->
      <el-table-column label="經銷商名稱" align="center" prop="dealerName" :show-overflow-tooltip="true" />
      <el-table-column label="註冊名稱" align="center" prop="registeredName" :show-overflow-tooltip="true" />
      <el-table-column label="聯繫人" align="center" prop="contactPerson" />
      <!-- <el-table-column label="公司电话" align="center" prop="companyPhone" />
      <el-table-column label="公司手机" align="center" prop="companyMobile" /> -->
      <el-table-column label="推薦狀態" align="center" width="120">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.recommendedValue"
            :active-value="1"
            :inactive-value="0"
            @change="handleRecommendChange(scope.row)"
            v-hasPermi="['car:dealerRecommend:edit']"
          />
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="cDt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.cDt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['car:dealerRecommend:query']"
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

    <!-- 查看经销商详情对话框 -->
    <el-dialog title="经销商详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="经销商ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="店家ID">{{ viewData.idGarage }}</el-descriptions-item>
        <el-descriptions-item label="经销商名称">{{ viewData.dealerName }}</el-descriptions-item>
        <el-descriptions-item label="注册名称">{{ viewData.registeredName }}</el-descriptions-item>
        <el-descriptions-item label="税号">{{ viewData.taxId || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="Line ID">{{ viewData.lineId || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ viewData.owner || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ viewData.contactPerson || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="注册地址" :span="2">{{ viewData.registeredAddress || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="公开地址" :span="2">{{ viewData.publicAddress || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="公司电话">{{ viewData.companyPhone || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="公司手机">{{ viewData.companyMobile || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="网站">{{ viewData.website || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="营业时间">{{ viewData.businessHours || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="推荐状态">
          <el-tag :type="viewData.recommendedValue === 1 ? 'success' : 'info'">
            {{ viewData.recommendedValue === 1 ? '已推荐' : '未推荐' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewData.cDt, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ viewData.description || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listDealerRecommend, setDealerRecommended } from "@/api/car/dealerRecommend";

export default {
  name: "DealerRecommend",
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
      // 经销商表格数据
      dealerList: [],
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
        dealerName: null,
        contactPerson: null,
        companyPhone: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询经销商列表 */
    getList() {
      this.loading = true;
      listDealerRecommend(this.queryParams).then(response => {
        this.dealerList = response.rows;
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
      setDealerRecommended(data).then(response => {
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
      this.download('car/dealerRecommend/export', {
        ...this.queryParams
      }, `精选卖家_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
