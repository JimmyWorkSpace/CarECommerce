<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公司" prop="companyId">
        <el-select
          v-model="queryParams.companyId"
          placeholder="请选择公司"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        >
          <el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.shortName">{{c.shortName}}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年份" prop="year">
        <el-select
          v-model="queryParams.year"
          placeholder="请选择年份"
          size="small"
        >
          <el-option v-for="y in years" :key="y.year" :value="y.year">{{y.year}}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" style="display: none">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['company:companyReportDetail:add']"
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
          v-hasPermi="['company:companyReportDetail:edit']"
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
          v-hasPermi="['company:companyReportDetail:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['company:companyReportDetail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="companyReportDetailList" @selection-change="handleSelectionChange">
      <el-table-column label="排名" align="center" width="80" prop="paiming" />
      <el-table-column label="公司" align="left" width="100" prop="companyName" />
      <el-table-column label="年份" align="center" prop="year" />
      <el-table-column label="综合分值" align="center" prop="zhfz" />
      <el-table-column label="净资产收益率" align="center" prop="jzcsyl" />
      <el-table-column label="销售净利润率" align="center" prop="xsjlrl" />
      <el-table-column label="资产净利润率" align="center" prop="zcjlrl" />
      <el-table-column label="净资产报酬率" align="center" prop="jzcbcl" />
      <el-table-column label="资产保值增值率" align="center" prop="zcbzzzl" />
      <el-table-column label="总资产增长率" align="center" prop="zzczzl" />
      <el-table-column label="净资产增长率" align="center" prop="jzczzl" />
      <el-table-column label="总资产周转率" align="center" prop="zzczhzl" />
      <el-table-column label="全员经济生产率" align="center" prop="qyjjscl" />
      <el-table-column label="销售收入增长率" align="center" prop="xssrzzl" />
      <el-table-column label="营业利润增长率" align="center" prop="yelrzzl" />
      <el-table-column label="外部收入增长率" align="center" prop="wbsrzzl" />

      <!-- <el-table-column label="外部收入占比" align="center" prop="wbsrzb" />
      <el-table-column label="全员劳动生产率" align="center" prop="qyldscl" />
      <el-table-column label="纳税额增长率" align="center" prop="nsezzl" />
      <el-table-column label="外部收入增长率" align="center" prop="showIndex" /> -->
      <!-- <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['company:companyReportDetail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['company:companyReportDetail:remove']"
          >刪除</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改改制企业综合评价明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="公司ID" prop="companyId">
          <el-input v-model="form.companyId" placeholder="请输入公司ID" />
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-input v-model="form.year" placeholder="请输入年份" />
        </el-form-item>
        <el-form-item label="综合分值" prop="zhfz">
          <el-input v-model="form.zhfz" placeholder="请输入综合分值" />
        </el-form-item>
        <el-form-item label="净资产收益率" prop="jzcsyl">
          <el-input v-model="form.jzcsyl" placeholder="请输入净资产收益率" />
        </el-form-item>
        <el-form-item label="销售净利润率" prop="xsjlrl">
          <el-input v-model="form.xsjlrl" placeholder="请输入销售净利润率" />
        </el-form-item>
        <el-form-item label="资产净利润率" prop="zcjlrl">
          <el-input v-model="form.zcjlrl" placeholder="请输入资产净利润率" />
        </el-form-item>
        <el-form-item label="净资产报酬率" prop="jzcbcl">
          <el-input v-model="form.jzcbcl" placeholder="请输入净资产报酬率" />
        </el-form-item>
        <el-form-item label="外部收入占比" prop="wbsrzb">
          <el-input v-model="form.wbsrzb" placeholder="请输入外部收入占比" />
        </el-form-item>
        <el-form-item label="资产保值增值率" prop="zcbzzzl">
          <el-input v-model="form.zcbzzzl" placeholder="请输入资产保值增值率" />
        </el-form-item>
        <el-form-item label="总资产增长率" prop="zzczzl">
          <el-input v-model="form.zzczzl" placeholder="请输入总资产增长率" />
        </el-form-item>
        <el-form-item label="净资产增长率" prop="jzczzl">
          <el-input v-model="form.jzczzl" placeholder="请输入净资产增长率" />
        </el-form-item>
        <el-form-item label="总资产周转率" prop="zzczhzl">
          <el-input v-model="form.zzczhzl" placeholder="请输入总资产周转率" />
        </el-form-item>
        <el-form-item label="全员劳动生产率" prop="qyldscl">
          <el-input v-model="form.qyldscl" placeholder="请输入全员劳动生产率" />
        </el-form-item>
        <el-form-item label="全员经济生产率" prop="qyjjscl">
          <el-input v-model="form.qyjjscl" placeholder="请输入全员经济生产率" />
        </el-form-item>
        <el-form-item label="销售收入增长率" prop="xssrzzl">
          <el-input v-model="form.xssrzzl" placeholder="请输入销售收入增长率" />
        </el-form-item>
        <el-form-item label="营业利润增长率" prop="yelrzzl">
          <el-input v-model="form.yelrzzl" placeholder="请输入营业利润增长率" />
        </el-form-item>
        <el-form-item label="纳税额增长率" prop="nsezzl">
          <el-input v-model="form.nsezzl" placeholder="请输入纳税额增长率" />
        </el-form-item>
        <el-form-item label="外部收入增长率" prop="wbsrzzl">
          <el-input v-model="form.wbsrzzl" placeholder="请输入外部收入增长率" />
        </el-form-item>
        <el-form-item label="外部收入增长率" prop="showIndex">
          <el-input v-model="form.showIndex" placeholder="请输入外部收入增长率" />
        </el-form-item>
        <el-form-item label="外部收入增长率" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入外部收入增长率" />
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
import { listCompanyReportDetail, getCompanyReportDetail, delCompanyReportDetail, addCompanyReportDetail, updateCompanyReportDetail, exportCompanyReportDetail } from "@/api/company/companyReportDetail";
import * as compApi from '@/api/company/companyApi';

export default {
  name: "CompanyReportDetail",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      years: [],
      companys: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 改制企业综合评价明细表格数据
      companyReportDetailList: [],
      // 弹出层標題
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        companyId: null,
        year: null,
        zhfz: null,
        jzcsyl: null,
        xsjlrl: null,
        zcjlrl: null,
        jzcbcl: null,
        wbsrzb: null,
        zcbzzzl: null,
        zzczzl: null,
        jzczzl: null,
        zzczhzl: null,
        qyldscl: null,
        qyjjscl: null,
        xssrzzl: null,
        yelrzzl: null,
        nsezzl: null,
        wbsrzzl: null,
        showIndex: null,
        orderBy: 'paiming'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  async created() {
    let years = await compApi.getValidYears(false);
    this.years = years.data;
    this.queryParams.year = this.years[0]['year'];

    let succ = await compApi.getCompanySearchOptions();
      this.companys = succ.data.companyList;
    this.getList();
  },
  methods: {
    /** 查询改制企业综合评价明细列表 */
    getList() {
      this.loading = true;
      listCompanyReportDetail(this.queryParams).then(response => {
        this.companyReportDetailList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        companyId: null,
        year: null,
        zhfz: null,
        jzcsyl: null,
        xsjlrl: null,
        zcjlrl: null,
        jzcbcl: null,
        wbsrzb: null,
        zcbzzzl: null,
        zzczzl: null,
        jzczzl: null,
        zzczhzl: null,
        qyldscl: null,
        qyjjscl: null,
        xssrzzl: null,
        yelrzzl: null,
        nsezzl: null,
        wbsrzzl: null,
        createTime: null,
        createBy: null,
        showIndex: null,
        delFlag: null,
        updateTime: null,
        updateBy: null
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
      this.title = "添加改制企业综合评价明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCompanyReportDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改改制企业综合评价明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCompanyReportDetail(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCompanyReportDetail(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 刪除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认刪除改制企业综合评价明细编號为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCompanyReportDetail(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("刪除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有改制企业综合评价明细数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportCompanyReportDetail(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
