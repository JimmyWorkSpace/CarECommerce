<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="檢舉原因" prop="reason">
        <el-select v-model="queryParams.reason" placeholder="請選擇檢舉原因" clearable size="small">
          <el-option
            v-for="dict in dict.type.car_report_reason"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="處理狀態" prop="status">
        <el-select v-model="queryParams.status" placeholder="請選擇處理狀態" clearable size="small">
          <el-option
            v-for="dict in dict.type.car_report_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="是否匿名" prop="anonymous">
        <el-select v-model="queryParams.anonymous" placeholder="请选择是否匿名" clearable size="small">
          <el-option label="是" :value="true" />
          <el-option label="否" :value="false" />
        </el-select>
      </el-form-item> -->
      <el-form-item label="檢舉人" prop="reporterName">
        <el-input
          v-model="queryParams.reporterName"
          placeholder="請輸入檢舉人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="創建時間">
        <el-date-picker
          v-model="daterangeCreatedAt"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="開始日期"
          end-placeholder="結束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['car:report:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['car:report:remove']"
        >刪除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:report:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="車輛" align="center" prop="saleTitle" width="200" show-overflow-tooltip />
      <el-table-column label="檢舉人" align="center" prop="reporterName" width="120" />
      <el-table-column label="檢舉人電話" align="center" prop="reporterPhone" width="120" />
      <el-table-column label="檢舉原因" align="center" prop="reason" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.car_report_reason" :value="scope.row.reason"/>
        </template>
      </el-table-column>
      <el-table-column label="詳細說明" align="center" prop="description" width="200" show-overflow-tooltip />
      <el-table-column label="處理狀態" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.car_report_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="處理備註" align="center" prop="processNote" width="150" show-overflow-tooltip />
      <el-table-column label="提交時間" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:report:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:report:remove']"
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

    <!-- 添加或修改車輛檢舉對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <!-- <el-row>
          <el-col :span="12">
            <el-form-item label="車輛銷售ID" prop="saleId">
              <el-input v-model="form.saleId" placeholder="请输入車輛銷售ID" :disabled="form.id != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="檢舉人ID" prop="reporterId">
              <el-input v-model="form.reporterId" placeholder="请输入檢舉人ID" :disabled="form.id != null" />
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="檢舉原因" prop="reason">
              <el-select v-model="form.reason" placeholder="請選擇檢舉原因" :disabled="form.id != null">
                <el-option
                  v-for="dict in dict.type.car_report_reason"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="是否匿名" prop="anonymous">
              <el-radio-group v-model="form.anonymous" :disabled="form.id != null">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-form-item label="詳細說明" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="請輸入詳細說明" :disabled="form.id != null" />
        </el-form-item>
        <el-form-item label="車輛網址" v-if="form.saleId && form.carUrl">
          <el-input v-model="form.carUrl" readonly placeholder="車輛網址">
            <template slot="append">
              <el-button @click="openCarUrl" icon="el-icon-link" size="small">打開</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="處理狀態" prop="status">
              <el-select v-model="form.status" placeholder="請選擇處理狀態">
                <el-option
                  v-for="dict in dict.type.car_report_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="處理人ID" prop="processorId">
              <el-input v-model="form.processorId" placeholder="請輸入處理人ID" :disabled="true" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="處理備註" prop="processNote">
          <el-input v-model="form.processNote" type="textarea" placeholder="請輸入處理備註" />
        </el-form-item>
        <el-form-item label="處理時間" prop="processedAt">
          <el-date-picker clearable size="small"
            v-model="form.processedAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="請選擇處理時間"
            :disabled="true">
          </el-date-picker>
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
import { listReport, getReport, delReport, addReport, updateReport } from "@/api/car/report";
import { getCarbuyPrefix } from "@/api/car/appointment";

export default {
  name: "Report",
  dicts: ['car_report_reason', 'car_report_status'],
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
      // 車輛檢舉表格數據
      reportList: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 創建時間時間範圍
      daterangeCreatedAt: [],
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        reason: null,
        status: null,
        anonymous: null,
        reporterName: null
      },
      // 表單參數
      form: {},
      // 車輛網址前綴
      carbuyPrefix: '',
      // 表單校驗
      rules: {
        saleId: [
          { required: true, message: "車輛銷售ID不能為空", trigger: "blur" }
        ],
        reporterId: [
          { required: true, message: "檢舉人ID不能為空", trigger: "blur" }
        ],
        reason: [
          { required: true, message: "檢舉原因不能為空", trigger: "change" }
        ],
        description: [
          { required: true, message: "詳細說明不能為空", trigger: "blur" }
        ],
        anonymous: [
          { required: true, message: "是否匿名不能為空", trigger: "change" }
        ],
        status: [
          { required: true, message: "處理狀態不能為空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getCarbuyPrefixConfig();
  },
  methods: {
    /** 查詢車輛檢舉列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreatedAt && '' != this.daterangeCreatedAt) {
        this.queryParams.params["beginTime"] = this.daterangeCreatedAt[0];
        this.queryParams.params["endTime"] = this.daterangeCreatedAt[1];
      }
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows;
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
        saleId: null,
        reporterId: null,
        reason: null,
        description: null,
        anonymous: false,
        status: "submitted",
        processNote: null,
        processorId: null,
        processedAt: null,
        carUrl: null
      };
      this.resetForm("form");
    },
    /** 搜索按鈕操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按鈕操作 */
    resetQuery() {
      this.daterangeCreatedAt = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多選框選中數據
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReport(id).then(response => {
        this.form = response.data;
        // 如果后端没有返回carUrl，则根据saleId计算
        if (!this.form.carUrl && this.form.saleId) {
          this.calculateCarUrl();
        }
        // 設置默認值
        this.form.processorId = this.$store.state.user.id;
        this.form.processedAt = new Date();
        this.open = true;
        this.title = "修改車輛檢舉";
      });
    },
    /** 獲取車輛網址前綴配置 */
    getCarbuyPrefixConfig() {
      getCarbuyPrefix().then(response => {
        if (response.data) {
          this.carbuyPrefix = response.data;
        }
      }).catch(() => {
        // 如果获取失败，使用默认值
        this.carbuyPrefix = 'https://carbuy.com.tw';
      });
    },
    /** 計算車輛網址 */
    calculateCarUrl() {
      if (this.form.saleId && this.carbuyPrefix) {
        this.form.carUrl = this.carbuyPrefix + '/detail/' + this.form.saleId;
      }
    },
    /** 打開車輛網址 */
    openCarUrl() {
      if (this.form.carUrl) {
        window.open(this.form.carUrl, '_blank');
      }
    },
    /** 提交按鈕 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReport(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReport(this.form).then(response => {
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
      this.$modal.confirm('是否確認刪除車輛檢舉編號為"' + ids + '"的數據項？').then(function() {
        return delReport(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 導出按鈕操作 */
    handleExport() {
      this.download('car/report/export', {
        ...this.queryParams
      }, `report_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
