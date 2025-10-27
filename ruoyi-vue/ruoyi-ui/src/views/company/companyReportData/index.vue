<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公司" prop="companyId">
        <el-select v-model="queryParams.companyId" placeholder="请选择公司" clearable size="small" @keyup.enter.native="handleQuery">
          <el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.shortName">{{c.shortName}}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="年份" prop="year">
        <el-select v-model="queryParams.years" placeholder="请选择年份" clearable size="small">
          <el-option v-for="y in years" :key="y.year + '-' + y.middleYear" :value="y.year + '-' + (y.middleYear?1:0)" :label="y.year + '年' + (y.middleYear ? '半年' : '全年')"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['company:companyReportData:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['company:companyReportData:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['company:companyReportData:remove']">刪除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['company:companyReportData:export']">导出</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-upload" size="mini" @click="toImportExcel" v-hasPermi="['company:companyReportData:export']">导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="companyReportDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" prop="id" />
      <el-table-column label="公司名稱" align="left" width="250" prop="companyName" />
      <el-table-column label="年份" align="center" prop="year" width="120" :formatter="yearFormatter" />
      <el-table-column label="职工队伍结构" align="center">
        <el-table-column label="职工总数" align="center" prop="zgzs" />
        <el-table-column label="改制职工月末人数" align="center" prop="gzzg" />
        <el-table-column label="安置油田子女人数" align="center" prop="azzn" />
        <el-table-column label="安置协解职工人数" align="center" prop="azzg" />
        <el-table-column label="自行招聘人数" align="center" prop="zxzp" />
      </el-table-column>
      <el-table-column label="改制职工退休人数" align="center" prop="gztx" />
      <el-table-column label="资产结构" align="center">
        <el-table-column label="月末总资产" align="center" prop="ymzzc" />
        <el-table-column label="月末净资产" align="center" prop="ymjzc" />
      </el-table-column>
      <el-table-column label="收入结构" align="center">
        <el-table-column label="油田内部市场收入" align="center" prop="nbsr" />
        <el-table-column label="外部市场收入" align="center" prop="wbsr" />
        <el-table-column label="改制企业之间收入" align="center" prop="qyjsr" />
        <el-table-column label="国际市场收入" align="center" prop="gjsr" />
        <el-table-column label="当年合计" align="center" prop="dnhj" />
      </el-table-column>
      <el-table-column label="利税情况" align="center">
        <el-table-column label="当年利润累计" align="center" prop="dnlr" />
        <el-table-column label="当年已交税金累计" align="center" prop="dnsj" />
      </el-table-column>
      <el-table-column label="操作" align="center" width="100" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['company:companyReportData:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['company:companyReportData:remove']">刪除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改公司月报详细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="公司" prop="companyId">
              <el-select v-model="form.companyId" placeholder="请选择公司" clearable size="small" @keyup.enter.native="handleQuery">
                <el-option v-for="c in companys" :key="c.id" :value="c.id" :label="c.shortName">{{c.shortName}}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年份" prop="year">
              <el-input-number v-model="form.year" placeholder="请输入年份" :min="1990" :max="2050" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="半年" prop="middleYear">
              <el-radio-group v-model="form.middleYear">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <fieldset>
          <legend>职工队伍结构</legend>
          <el-row>
            <el-col :span="8">
              <el-form-item label="改制职工月末人数" prop="gzzg">
                <el-input-number v-model="form.gzzg" placeholder="请输入改制职工月末人数" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="安置油田子女人数" prop="azzn">
                <el-input-number v-model="form.azzn" placeholder="请输入安置油田子女人数" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="安置协解职工人数" prop="azzg">
                <el-input-number v-model="form.azzg" placeholder="请输入安置协解职工人数" :min="0" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="自行招聘人数" prop="zxzp">
                <el-input-number v-model="form.zxzp" placeholder="请输入自行招聘人数" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="职工总数" prop="zgzs">
                <el-input-number v-model="form.zgzs" placeholder="请输入职工总数" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </fieldset>
        <fieldset>
          <legend>改制职工退休人数</legend>
          <el-row>
            <el-col :span="8">
              <el-form-item label="改制职工退休人数" prop="gztx">
                <el-input-number v-model="form.gztx" placeholder="请输入改制职工退休人数" :min="0" />
              </el-form-item>
            </el-col>
          </el-row>
        </fieldset>
        <fieldset>
          <legend>资产结构</legend>
          <el-row>
            <el-col :span="8">
              <el-form-item label="月末总资产" prop="ymzzc">
                <el-input-number v-model="form.ymzzc" placeholder="请输入月末总资产" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="月末净资产" prop="ymjzc">
                <el-input-number v-model="form.ymjzc" placeholder="请输入月末净资产" :min="0" />
              </el-form-item>
            </el-col>
          </el-row>
        </fieldset>
        <fieldset>
          <legend>收入结构</legend>
          <el-row>
            <el-col :span="8">
              <el-form-item label="油田内部市场收入" prop="nbsr">
                <el-input-number v-model="form.nbsr" placeholder="请输入油田内部市场收入" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="外部市场收入" prop="wbsr">
                <el-input-number v-model="form.wbsr" placeholder="请输入外部市场收入" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="改制企业之间收入" prop="qyjsr">
                <el-input-number v-model="form.qyjsr" placeholder="请输入改制企业之间收入" :min="0" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="国际市场收入" prop="gjsr">
                <el-input-number v-model="form.gjsr" placeholder="请输入国际市场收入" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="当年合计" prop="dnhj">
                <el-input-number v-model="form.dnhj" placeholder="请输入当年合计" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </fieldset>
        <fieldset>
          <legend>利税情况</legend>
          <el-row>
            <el-col :span="8">
              <el-form-item label="当年利润累计" prop="dnlr">
                <el-input-number v-model="form.dnlr" placeholder="请输入当年利润累计" :min="0" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="当年已交税金累计" prop="dnsj">
                <el-input-number v-model="form.dnsj" placeholder="请输入当年已交税金累计" :min="0" />
              </el-form-item>
            </el-col>
          </el-row>
        </fieldset>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="导入数据" :visible.sync="importExcel" width="800px" append-to-body id="importDlg">
      <fieldset>
        <legend>导入数据</legend>
        <p style="padding-left: 45px; color: red; font-weight: bold; ">
          注意：请按照提供的导入模板格式导入，不要自定义格式。<el-link type="primary" @click="exportModal" target="_blank">点击这里</el-link>下载模板。
        </p>
        <el-form ref="form" :model="importParams" :rules="rules" label-width="100px" v-loading="importing" :element-loading-text="loadingText">
          <el-row>
            <el-col :span="8">
              <el-form-item label="年份" prop="year">
                <el-input-number v-model="importParams.year" :min="2000" :max="2050" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="是否半年" prop="middleYear">
                <el-radio-group v-model="importParams.middleYear">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="是否覆盖" prop="override">
                <el-radio-group v-model="importParams.override">
                  <el-radio :label="1">覆盖</el-radio>
                  <el-radio :label="0">略过</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="文件" prop="filePath">
            <fileUpload :multiple="false" v-model="importParams.filePath" :fileType="['xls', 'xlsx']" />
          </el-form-item>
        </el-form>
      </fieldset>
      <div v-html="importResult" style="color: #ff0000"></div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="importExcelConfirm">确 定</el-button>
        <el-button @click="importExcel=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCompanyReportData,
  getCompanyReportData,
  delCompanyReportData,
  addCompanyReportData,
  updateCompanyReportData,
  exportCompanyReportData,
  importCompanyReportData,
  exportCompanyReportDataModal,
} from '@/api/company/companyReportData';
import * as compApi from '@/api/company/companyApi';
import { saveAs } from 'file-saver';

export default {
  name: 'CompanyReportData',
  components: {},
  data() {
    return {
      importing: false,
      loadingText: '正在导入……',
      importExcel: false,
      importResult: '',
      baseUrl: process.env.VUE_APP_BASE_API,
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
      // 公司月报详细表格数据
      companyReportDataList: [],
      // 弹出层標題
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        companyId: null,
        companyName: null,
        year: null,
        middleYear: null,
        years: null,
      },
      importParams: {
        year: new Date().getFullYear(),
        middleYear: 0,
        override: 0,
        filePath: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  async created() {
    let years = await compApi.getValidYears(true);
    this.years = years.data;

    let succ = await compApi.getCompanySearchOptions();
    this.companys = succ.data.companyList;

    this.getList();
  },
  watch: {
    'queryParams.years'() {
      if (this.queryParams.years) {
        this.queryParams.year = this.queryParams.years.split('-')[0];
        this.queryParams.middleYear = this.queryParams.years.split('-')[1];
      } else {
        this.queryParams.year = null;
        this.queryParams.middleYear = null;
      }
    },
    form: {
      handler() {
        let f = this.form;
        f.zgzs = f.gzzg + f.azzn + f.azzg + f.zxzp;
        f.dnhj = f.nbsr + f.wbsr + f.qyjsr + f.gjsr;
        if (f.companyId) {
          f.companyName = this.companys.filter((c) => c.id == f.companyId)[0][
            'name'
          ];
        }
      },
      deep: true,
    },
  },
  methods: {
    async importExcelConfirm() {
      this.importResult = '';
      let result = await importCompanyReportData(this.importParams);
      if (result.code == 200) {
        this.$modal.msgSuccess('导入成功');
        this.handleQuery();
      } else if (result.code == 199) {
        this.$modal.msgError('导入失败');
        this.importResult = result.msg;
      } else {
        this.$modal.msgError('导入失败');
      }
    },
    genImportParams() {
      this.importParams = {
        year: new Date().getFullYear(),
        middleYear: 0,
        override: 0,
        filePath: null,
      };
      this.importResult = '';
    },
    toImportExcel() {
      this.genImportParams();
      this.importExcel = true;
    },
    yearFormatter(row, column) {
      return row.year + '年' + (row.middleYear == 1 ? '半年' : '全年');
    },
    /** 查询公司月报详细列表 */
    getList() {
      this.loading = true;
      listCompanyReportData(this.queryParams).then((response) => {
        this.companyReportDataList = response.rows;
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
        year: new Date().getFullYear(),
        middleYear: 1,
        azzn: null,
        nbsr: null,
        ymjzc: null,
        dnhj: null,
        gztx: null,
        azzg: null,
        zxzp: null,
        dnsj: null,
        gzzg: null,
        qyjsr: null,
        gjsr: null,
        ymzzc: null,
        wbsr: null,
        zgzs: null,
        dnlr: null,
        createTime: null,
        createBy: null,
        showIndex: null,
        delFlag: null,
        updateTime: null,
        updateBy: null,
      };
      this.resetForm('form');
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm');
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = '添加公司月报详细';
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getCompanyReportData(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = '修改公司月报详细';
      });
    },
    /** 提交按钮 */
    submitForm() {
      let _this = this;
      _this.$refs['form'].validate((valid) => {
        if (valid) {
          if (_this.form.id != null) {
            updateCompanyReportData(_this.form).then((response) => {
              _this.$modal.msgSuccess('修改成功');
              _this.open = false;
              _this.getList();
            });
          } else {
            addCompanyReportData(_this.form).then((response) => {
              _this.$modal.msgSuccess('新增成功');
              _this.open = false;
              _this.getList();
            });
          }
        }
      });
    },
    /** 刪除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认刪除公司月报详细编號为"' + ids + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(function () {
          return delCompanyReportData(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess('刪除成功');
        });
    },
    exportModal() {
      exportCompanyReportDataModal().then(succ => {
        saveAs(succ.msg, '导入模板.xlsx');
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有公司月报详细数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(function () {
          return exportCompanyReportData(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
fieldset {
  border: 1px solid #dddddd;
  border-radius: 5px;
  padding-top: 20px;
  margin-bottom: 20px;
}
</style>