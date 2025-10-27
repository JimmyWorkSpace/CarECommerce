<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="选择年份">
        <el-select v-model="queryParams.yearq" placeholder="请选择">
          <el-option v-for="(item, $idx) in years" :key="$idx" :label="item.year + '年 - ' + (item.middleYear ? '半年' : '全年')" :value="item.year + ',' + (item.middleYear ? '1' : '0')">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择企业">
        <el-select v-model="companySelectedType" placeholder="请选择">
          <el-option v-for="item in companySearchOptions.fixedGroup" :key="item.value" :label="item.name" :value="item.value">
          </el-option>
        </el-select>
        <!-- <el-select v-model="queryParams.companyIds" clearable collapse-tags multiple style="margin-left: 20px" v-show="companySelectedType=='other'" placeholder="请选择">
          <el-option v-for="item in companySearchOptions.companyList" :key="item.id" :label="item.shortName" :value="item.id">
          </el-option>
        </el-select> -->
      </el-form-item>
      <el-form-item :label="'手动选择'">
        <el-popover width="800" trigger="click">
          <div>
            <div style="text-align: right;">
              <el-link type="primary" @click="clearShow">清除</el-link>
            </div>
            <el-checkbox-group v-model="queryParams.companyIds">
              <el-checkbox style="width: 16%" v-for="item in companySearchOptions.companyList" :key="item.id" :label="item.id" :value="item.id">{{item.shortName}}</el-checkbox>
            </el-checkbox-group>
          </div>
          <el-button slot="reference">点击选择</el-button>
        </el-popover>
      </el-form-item>
      <!-- <el-form-item label="选择等级">
        <el-select v-model="selectedLevel" placeholder="请选择">
          <el-option v-for="(u, idx) in levels" :label="level[u]['title']" :value="u" :key="idx">
            <div class="option-div">
              <span>{{ level[u]['title'] }}</span>
              <span style="width: 30px;height: 25px" :style="'backgroundColor:' + level[u]['bgColor']">&nbsp;</span>
            </div>
          </el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="选择项目">
        <el-select v-model="queryParams.head" clearable placeholder="请选择">
          <el-option v-for="(u, idx) in heads" :label="u.dictLabel" :value="u.dictValue" :key="idx">
          </el-option>
        </el-select>
        <el-select v-model="queryParams.type" clearable placeholder="请选择" style="width: 120px">
          <el-option label="大于" value=">"> </el-option>
          <el-option label="大于等于" value=">="> </el-option>
          <el-option label="小于等于" value="<="> </el-option>
          <el-option label="小于" value="<"> </el-option>
          <el-option label="等于" value="="> </el-option>
          <el-option label="不等于" value="!="> </el-option>
        </el-select>
        <el-input-number v-model="queryParams.value" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>
    <div v-if="pageType=='jyjcyj'">
      <div class="title">
        <div></div>
        <div style="font-size: 24px;font-weight: 400">{{current.year}}年{{current.middleYear==1?'半年':''}}改制企业{{pageTitle}}信息</div>
        <div>
          <el-tag v-for="(c, idx) in colors" :key="idx" :color="c.color" :hit="false" style="color: white;margin: 0 2px">
            {{ ((c.type == 0) ? '等于' : (c.type == 1 ? '大于' : '小于')) + c.level }}
          </el-tag>
        </div>

      </div>
      <el-table v-loading="loading" :data="list" height="32vw" :cell-style="cellStyle">
        <el-table-column label="公司名稱" align="center" width="150" prop="shortName" />
        <el-table-column label="职工队伍结构" align="center">
          <el-table-column label="职工总数" align="center" prop="zgzs" />
          <el-table-column label="同比" align="center" prop="zgzsInc" :formatter="incFormatter" />
          <el-table-column label="改制职工月末人数" align="center" prop="gzzg" />
          <el-table-column label="同比" align="center" prop="gzzgInc" :formatter="incFormatter" />
          <el-table-column label="安置油田子女人数" align="center" prop="azzn" />
          <el-table-column label="同比" align="center" prop="azznInc" :formatter="incFormatter" />
          <el-table-column label="安置协解职工人数" align="center" prop="azzg" />
          <el-table-column label="同比" align="center" prop="azzgInc" :formatter="incFormatter" />
          <el-table-column label="自行招聘人数" align="center" prop="zxzp" />
          <el-table-column label="同比" align="center" prop="zxzpInc" :formatter="incFormatter" />
        </el-table-column>
        <el-table-column label="改制职工退休人数" align="center" prop="gztx" />
        <el-table-column label="同比" align="center" prop="gztxInc" :formatter="incFormatter" />
        <el-table-column label="资产结构" align="center">
          <el-table-column label="月末总资产" align="center" prop="ymzzc" />
          <el-table-column label="同比" align="center" prop="ymzzcInc" :formatter="incFormatter" />
          <el-table-column label="月末净资产" align="center" prop="ymjzc" />
          <el-table-column label="同比" align="center" prop="ymjzcInc" :formatter="incFormatter" />
        </el-table-column>
        <el-table-column label="收入结构" align="center">
          <el-table-column label="油田内部市场收入" align="center" prop="nbsr" />
          <el-table-column label="同比" align="center" prop="nbsrInc" :formatter="incFormatter" />
          <el-table-column label="外部市场收入" align="center" prop="wbsr" />
          <el-table-column label="同比" align="center" prop="wbsrInc" :formatter="incFormatter" />
          <el-table-column label="改制企业之间收入" align="center" prop="qyjsr" />
          <el-table-column label="同比" align="center" prop="qyjsrInc" :formatter="incFormatter" />
          <el-table-column label="国际市场收入" align="center" prop="gjsr" />
          <el-table-column label="同比" align="center" prop="gjsrInc" :formatter="incFormatter" />
          <el-table-column label="当年合计" align="center" prop="dnhj" />
          <el-table-column label="同比" align="center" prop="dnhjInc" :formatter="incFormatter" />
        </el-table-column>
        <el-table-column label="利税情况" align="center">
          <el-table-column label="当年利润累计" align="center" prop="dnlr" />
          <el-table-column label="同比" align="center" prop="dnlrInc" :formatter="incFormatter" />
          <el-table-column label="当年已交税金累计" align="center" prop="dnsj" />
          <el-table-column label="同比" align="center" prop="dnsjInc" :formatter="incFormatter" />
        </el-table-column>
      </el-table>
    </div>
    <div v-if="pageType!='jyjcyj'">
      <div class="title">
        <div></div>
        <div style="font-size: 24px;font-weight: 400">{{current.year}}年{{current.middleYear==1?'半年':''}}改制企业{{pageTitle}}信息</div>
        <div>
          <el-tag v-for="(c, idx) in colors" :key="idx" :color="c.color" :hit="false" style="color: white;margin: 0 2px">
            {{ ((c.type == 0) ? '等于' : (c.type == 1 ? '大于' : '小于')) + c.level }}
          </el-tag>
        </div>

      </div>
      <el-table v-loading="loading" :data="list" height="70vh" :cell-style="cellStyle">
        <el-table-column label="公司名稱" align="center" width="150" prop="shortName" />
        <el-table-column v-for="(h, idx) in heads" :key="idx" :label="h.columnTitle" align="center" :prop="h.dictValue" />
      </el-table>
    </div>
  </div>
</template>

<script>
import * as compApi from '@/api/company/companyApi';
import * as echarts from 'echarts';

export default {
  name: 'CompanyReportWithInc',
  components: {},
  data() {
    return {
      // 查询参数
      queryParams: {
        companyIds: [],
        year: 2020,
        middleYear: 0,
        yearq: '',
        head: null,
        type: null,
        value: null,
      },
      incFields: [
        'zgzsInc',
        'gzzgInc',
        'azznInc',
        'azzgInc',
        'zxzpInc',
        'gztxInc',
        'ymzzcInc',
        'ymjzcInc',
        'nbsrInc',
        'wbsrInc',
        'qyjsrInc',
        'gjsrInc',
        'dnhjInc',
        'dnlrInc',
        'dnsjInc',

        'zhfz_inc',
        'jjxy_inc',
        'jyfz_inc',
        'jzcsyl_inc',
        'xsjlrl_inc',
        'zcjlrl_inc',
        'jzcbcl_inc',
        'wbsrzb_inc',
        'zcbzzzl_inc',
        'zzczzl_inc',
        'jzczzl_inc',
        'zzczhzl_inc',
        'qyldscl_inc',
        'qyjjscl_inc',
        'xssrzzl_inc',
        'yelrzzl_inc',
        'nsezzl_inc',
        'wbsrzzl_inc',
      ],
      // levels: ['u2', 'u1', 'd1', 'd2'],
      selectedLevel: null,
      current: {
        year: null,
        middleYear: null,
      },
      pageType: null,
      level: {},
      heads: [],
      years: [],
      colors: [],
      companySelectedType: 'slyt',
      companySearchOptions: {},
      list: [],
      loading: false,
    };
  },
  async created() {
    let _this = this;
    let colors = await compApi.colors();
    _this.colors = colors.data;

    let succ = await compApi.getCompanySearchOptions();
    // succ.data.fixedGroup.push({
    //   name: '手动选择',
    //   value: 'other',
    // });
    _this.companySearchOptions = succ.data;
    _this.queryParams.companyIds = _this.companySearchOptions.fixedGroup.filter(
      (f) => f.value == _this.companySelectedType
    )[0]['companyIds'];

    let years = null;
    let heads = null;
    let d = null;
    _this.pageType = _this.$route.name.split('/').pop();
    switch (_this.pageType) {
      case 'jyjcyj':
        _this.queryParams.head = 'dnhj';
        _this.pageTitle = '经营监测';
        d = await _this.getDicts('jyybheads');
        heads = d.data;
        years = await compApi.getValidYears(true);
        break;
      case 'pjjcyj':
        _this.queryParams.head = 'zhfz';
        _this.pageTitle = '评价监测';
        d = await _this.getDicts('zhheads');
        heads = d.data;
        years = await compApi.getValidYears(false);
        break;
      case 'jjxyyj':
        _this.pageTitle = '经济效益监测';
        _this.queryParams.head = 'jjxy';
        d = await _this.getDicts('jjxyheads');
        heads = d.data;
        years = await compApi.getValidYears(false);
        break;
      case 'jyfzyj':
        _this.pageTitle = '经营发展监测';
        _this.queryParams.head = 'jyfz';
        d = await _this.getDicts('jyfzheads');
        heads = d.data;
        years = await compApi.getValidYears(false);
        break;
    }

    _this.years = years.data;
    _this.queryParams.yearq =
      years.data[0]['year'] + ',' + (years.data[0]['middleYear'] ? '1' : '0');

    for (let i = 0; i < heads.length; i++) {
      _this.heads.push({
        dictLabel: heads[i]['dictLabel'],
        columnTitle: heads[i]['dictLabel'],
        dictValue: heads[i]['dictValue'],
      });
      _this.heads.push({
        dictLabel: heads[i]['dictLabel'] + '同比',
        columnTitle: '同比',
        dictValue: heads[i]['dictValue'] + 'Inc',
      });
    }

    switch (_this.pageType) {
      case 'pjjcyj':
        _this.heads.unshift({
          dictLabel: '综合分值',
          columnTitle: '综合分值',
          dictValue: 'zhfz',
        });
        break;
      case 'jjxyyj':
        _this.heads.unshift({
          dictLabel: '综合分值',
          columnTitle: '综合分值',
          dictValue: 'jjxy',
        });
        break;
      case 'jyfzyj':
        _this.heads.unshift({
          dictLabel: '综合分值',
          columnTitle: '综合分值',
          dictValue: 'jyfz',
        });
        break;
    }

    _this.handleQuery();
  },
  watch: {
    'queryParams.yearq'() {
      this.queryParams.year = this.queryParams.yearq.split(',')[0];
      this.queryParams.middleYear =
        this.queryParams.yearq.split(',')[1] == 1 ? true : false;
    },
    companySelectedType() {
      let _this = this;
      if (_this.companySelectedType != 'other') {
        _this.queryParams.companyIds =
          _this.companySearchOptions.fixedGroup.filter(
            (f) => f.value == _this.companySelectedType
          )[0]['companyIds'];
        console.log(
          'query params company ids change',
          _this.queryParams.companyIds
        );
      }
    },
  },
  methods: {
    clearShow(){
      this.queryParams.companyIds = [];
    },
    incFormatter(row, col) {
      let v = row[col.property];
      if (v) return (v > 0 ? '+' : '') + v.toFixed(2) + '%';
      return v;
    },
    cellStyle(r) {
      return compApi.cellStyle(r, this.colors);
    },
    getCompanySearchOptions() {
      let _this = this;
      compApi.getCompanySearchOptions().then((succ) => {
        _this.companySearchOptions = succ.data;
        _this.queryParams.companyIds =
          _this.companySearchOptions.fixedGroup.filter(
            (f) => f.value == _this.companySelectType
          )[0]['companyIds'];
      });
    },
    /** 搜索按钮操作 */
    async handleQuery() {
      let _this = this;
      let succ = null;
      _this.current.year = _this.queryParams.year;
      _this.current.middleYear = _this.queryParams.middleYear;
      if (_this.pageType == 'jyjcyj') {
        succ = await compApi.getCompanyReportDataWithInc(this.queryParams);
      } else {
        succ = await compApi.getCompanyDetailDataWithInc(this.queryParams);
      }
      // if (_this.selectedLevel) {
      //   let type = _this.level[_this.selectedLevel].type;
      //   let value = _this.level[_this.selectedLevel].value;
      //   succ.data = succ.data.filter((d) => {
      //     return _this.incFields.some((f) => {
      //       return d[f] && (type == 1 ? d[f] > value : d[f] < value);
      //     });
      //   });
      // }
      _this.list = succ.data;
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm');
      this.handleQuery();
    },
  },
};
</script>
<style lang="scss" scoped>
.title {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  padding-bottom: 10px;
}
.option-div {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
#sandianChart {
  width: 100%;
  height: 75vh;
}
</style>
