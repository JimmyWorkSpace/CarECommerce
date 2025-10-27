<template>
  <div class="app-container">
    <el-row class="searchArea">
      <el-col :span="10">
        <el-form label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="选择改制企业">
                <el-select v-model="companySelectType" placeholder="请选择">
                  <el-option v-for="(c, $idx) in companySearchOptions.fixedGroup" :key="$idx" :label="c.name" :value="c.value">
                  </el-option>
                  <!-- <el-option :label="'手动选择'" :value="'other'">
                  </el-option> -->
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="选择项目">
                <el-select v-model="queryParams.head" placeholder="请选择">
                  <el-option v-for="item in heads" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-form-item :label="'手动选择'">
              <el-popover width="800" trigger="click">
                <div>
                  <div style="text-align: right;">
                    <el-link type="primary" @click="clearShow">清除</el-link>
                  </div>
                  <el-checkbox-group v-model="companysToShow">
                    <el-checkbox style="width: 16%" v-for="item in companysToShowChoose" :key="item.id" :label="item.id" :value="item.id">{{item.shortName}}</el-checkbox>
                  </el-checkbox-group>
                </div>
                <el-button slot="reference">点击选择</el-button>
              </el-popover>
              <!-- <el-select v-model="queryParams.companyIds" clearable collapse-tags multiple placeholder="请选择" style="width: 98%">
                <el-option v-for="item in companySearchOptions.companyList" :key="item.id" :label="item.shortName" :value="item.id">
                </el-option>
              </el-select> -->
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="每次显示">
                <el-input-number v-model="queryParams.max" :min="1" :max="10"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="刷新時間（秒）">
                <el-input-number v-model="queryParams.refresh" :min="3" :max="1000"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="固定Y轴">
                <el-radio-group v-model="queryParams.fixY">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否，自动调整Y轴</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- <el-row>
            <el-form-item label="选择年份">
              <el-slider :marks="yearMarks" style="width: 80%; margin-left: 10px" v-model="queryParams.yearRange" :step="1" range :max="validYears[0]['year']" :min="validYears[validYears.length-1]['year']">
              </el-slider>
            </el-form-item>
          </el-row> -->
        </el-form>
        <!-- <div style="text-align: right; padding: 0 20px">
          <el-button type="primary" @click="getList">查询</el-button>
        </div> -->
      </el-col>
      <el-col :span="14" style="padding: 0 0 0 20px">
        <div id="chart"></div>
      </el-col>
    </el-row>
    <el-row style="height: 55vh">
      <h4>{{ year }}年改制企业{{ pageTitle }}月报</h4>
      <el-table v-if="pageType != 'jyjc'" :data="companyReportDataList" class="table" height="43vh" ref="table" @cell-mouse-enter="cellMouseIn=true" @cell-mouse-leave="cellMouseIn=false">
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column label="公司名稱" align="left" width="100" prop="companyName">
          <template slot-scope="scope">
            <el-link type="primary" @click="showDlg(scope.row)">{{scope.row.companyName}}</el-link>
          </template>
        </el-table-column>
        <el-table-column v-for="h in heads" :label="h.dictLabel" align="center" :prop="h.dictValue" :key="h.dictValue" :formatter="persentFormatter" />
      </el-table>
      <el-table v-if="pageType == 'jyjc'" :data="companyReportDataList" class="table" height="43vh" ref="table" @cell-mouse-enter="cellMouseIn=true" @cell-mouse-leave="cellMouseIn=false">
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column label="公司名稱" align="left" width="100" prop="companyShortName">
          <template slot-scope="scope">
            <el-link type="primary" @click="showDlg(scope.row)">{{scope.row.companyShortName}}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="年份" align="center" prop="year" width="120" />
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
      </el-table>
    </el-row>
    <!-- 添加或修改公司信息对话框 -->
    <el-dialog :title="companyReportDataDlgList[0]['companyName'] + companyReportDataDlgList[companyReportDataDlgList.length - 1]['year'] + '年-' + companyReportDataDlgList[0]['year'] + '年' + pageTitle + '月报'" :visible.sync="dlgOpen" width="80%" append-to-body>
      <el-table v-if="pageType != 'jyjc'" :data="companyReportDataDlgList" class="table" height="400" ref="table" @cell-mouse-enter="cellMouseIn=true" @cell-mouse-leave="cellMouseIn=false">
        <!-- <el-table-column type="index" width="50"> </el-table-column> -->
        <el-table-column label="年份" align="left" prop="year" />
        <el-table-column v-for="h in heads" :label="h.dictLabel" align="center" :prop="h.dictValue" :key="h.dictValue" :formatter="persentFormatter" />
      </el-table>
      <el-table v-if="pageType == 'jyjc'" :data="companyReportDataDlgList" class="table" height="400" ref="table" @cell-mouse-enter="cellMouseIn=true" @cell-mouse-leave="cellMouseIn=false">
        <!-- <el-table-column type="index" width="50"> </el-table-column> -->
        <el-table-column label="年份" align="center" prop="year" width="120" />
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
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import * as compApi from '@/api/company/companyApi';
import * as echarts from 'echarts';

export default {
  name: 'CompanyReportData',
  components: {},
  data() {
    return {
      companysToShowChoose: [],
      companysToShow: [],
      dlgOpen: false,
      chartInterval: null,
      chartTimeout: null,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      cellMouseIn: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      pageType: '',
      // 总条数
      total: 0,
      companySelectType: null,
      companySearchOptions: {},
      // 公司月报详细表格数据
      companyReportDataList: [],
      companyReportDataDlgList: [{}],
      validYears: [{}],
      yearMarks: {},
      year: '',
      scrollInterval: null,
      // 弹出层標題
      title: '',
      pageTitle: '',
      heads: [],
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        companyIds: [],
        head: '',
        yearRange: [],
        max: 5,
        refresh: 10,
        fixY: 0,
      },
      chart: {},
      chartMaxCount: 5,
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  async created() {
    let _this = this;

    let succ = await compApi.getValidYears(false);
    _this.validYears = succ.data;
    _this.queryParams.yearRange = [
      succ.data[0]['year'],
      succ.data[succ.data.length - 1]['year'],
    ];
    succ.data.forEach((d) => {
      _this.yearMarks[d.year] = d.year + '';
    });
    setTimeout(() => {
      let chartDom = document.getElementById('chart');
      _this.chart = echarts.init(chartDom);
    });
    _this.pageType = _this.$route.name.split('/').pop();
    switch (_this.pageType) {
      case 'jyjc':
        _this.queryParams.head = 'dnhj';
        _this.pageTitle = '经营监测';
        _this.getDicts('jyybheads').then((resp) => {
          // resp.data.unshift({
          //     dictValue: "zhfz",
          //     dictLabel: "综合分值",
          // });
          _this.heads = resp.data;
        });
        break;
      case 'pjjc':
        _this.queryParams.head = 'zhfz';
        _this.pageTitle = '评价监测';
        _this.getDicts('zhheads').then((resp) => {
          resp.data.unshift({
            dictValue: 'zhfz',
            dictLabel: '综合分值',
          });
          _this.heads = resp.data;
        });
        break;
      case 'jjxy':
        _this.pageTitle = '经济效益监测';
        _this.queryParams.head = 'jjxy';
        _this.getDicts('jjxyheads').then((resp) => {
          resp.data.unshift({
            dictValue: 'jjxy',
            dictLabel: '综合分值',
          });
          _this.heads = resp.data;
        });
        break;
      case 'jyfz':
        _this.pageTitle = '经营发展监测';
        _this.queryParams.head = 'jyfz';
        _this.getDicts('jyfzheads').then((resp) => {
          resp.data.unshift({
            dictValue: 'jyfz',
            dictLabel: '综合分值',
          });
          _this.heads = resp.data;
        });
        break;
    }
    await this.getCompanySearchOptions();
    this.getList();
  },
  watch: {
    companySelectType() {
      let _this = this;
      if (_this.companySelectType != 'other') {
        _this.queryParams.companyIds =
          _this.companySearchOptions.fixedGroup.filter(
            (f) => f.value == _this.companySelectType
          )[0]['companyIds'];
        _this.companysToShowChoose =
          _this.companySearchOptions.companyList.filter((c) => {
            return _this.queryParams.companyIds.indexOf(c.id) > -1;
          });
        _this.companysToShow = _this.queryParams.companyIds;
        // console.log(
        //   'query params company ids change',
        //   _this.queryParams.companyIds
        // );
      }
    },
    companysToShow() {
        this.getList();
    },
    queryParams: {
      handler() {
        this.getList();
      },
      deep: true,
    },
  },
  methods: {
    async showDlg(row) {
      let result = null;
      switch (this.pageType) {
        case 'jyjc':
          result = await compApi.getReportDataByCompany(row.companyId);
          break;
        default:
          result = await compApi.getDetailDataByCompany(row.companyId);
          break;
      }
      this.companyReportDataDlgList = result.data;
      this.dlgOpen = true;
    },
    clearShow() {
      this.companysToShow = [];
    },
    persentFormatter(row, column) {
      let d = row[column.property];
      if (
        column.property == 'zhfz' ||
        column.property == 'jyfz' ||
        column.property == 'jjxy'
      ) {
        try {
          return d.toFixed(2);
        } catch (e) {
          return d;
        }
      } else {
        try {
          return (d * 100).toFixed(2) + '%';
        } catch (e) {
          return d;
        }
      }
    },
    async initChart() {
      let _this = this;
      let succ = null;
      switch (_this.pageType) {
        case 'jyjc':
          succ = await compApi.getDataLineChart(_this.queryParams);
          break;
        default:
          succ = await compApi.getDetailLineChart(_this.queryParams);
          break;
      }
      let option = {
        title: {
          text: '',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: [],
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: succ.data.xAxis,
        },
        yAxis: {
          type: 'value',
        },
        series: [],
      };
      let max = 0,
        min = 0;
      let avgData = [];
      succ.data.series.forEach((d) => {
        d.data = d.data.slice(0, succ.data.xAxis.length);
        d.smooth = false;
        for (let i = 0; i < d.data.length; i++) {
          let dt = parseInt(d.data[i] || 0);
          // let dt = d.data[i] = (d.data[i] || 0) / 10000;
          if (!avgData[i]) {
            avgData[i] = dt;
          } else {
            avgData[i] += dt;
          }
          if (dt > max) {
            max = dt;
          }
          if (dt < min) {
            min = dt;
          }
        }
      });
      for (let i in avgData) {
        avgData[i] = parseInt(avgData[i] / succ.data.series.length);
      }
      let avg = {
        data: avgData,
        name: '平均',
        // stack: 'Total',
        type: 'line',
        // smooth: false,
        itemStyle: {
          color: '#00ff00',
          borderWidth: 5,
        },
      };
      if (_this.queryParams.fixY) {
        option.yAxis.max = parseInt(max * 1.2);
        option.yAxis.min = min;
        // console.log('max:', max)
        // console.log('min:', min)
      }
      let i = 0;
      function refresh() {
        let filtered = succ.data.series.filter(s => _this.companysToShow.indexOf(s.compId) > -1)
        option.series = filtered.slice(i, i + _this.queryParams.max);
        option.series.push(avg);
        console.log('chart refresh');
        console.log(option.series);
        _this.fillChart(option);
        i += _this.queryParams.max;
        if (i > filtered.length - 1) {
          i = 0;
        }
        return refresh;
      }

      try {
        clearInterval(_this.chartInterval);
      } catch (e) {}
      _this.chartInterval = setInterval(
        refresh(),
        _this.queryParams.refresh * 1000
      );
    },
    fillChart(option) {
      let _this = this;
      let title = _this.heads.filter(
        (h) => h.dictValue == _this.queryParams.head
      )[0]['dictLabel'];
      option.legend.data = option.series.map((o) => o.name);
      option.title.text = title;
      _this.chart.setOption(option, true);
    },
    async getCompanySearchOptions() {
      let _this = this;
      let succ = await compApi.getCompanySearchOptions();
      _this.companySearchOptions = succ.data;
      _this.queryParams.companyIds =
        _this.companySearchOptions.fixedGroup.filter(
          (f) => f.value == 'slyt'
        )[0]['companyIds'];
      _this.companySelectType = 'slyt';
    },
    /** 查询公司月报详细列表 */
    async getList() {
      this.loading = true;
      let response = null;
      switch (this.pageType) {
        case 'jyjc':
          response = await compApi.getCompanyReportData(this.queryParams);
          break;
        case 'pjjc':
        case 'jjxy':
        case 'jyfz':
          response = await compApi.getLastestYearData(this.queryParams);
          break;
      }
      // debugger;
      this.companyReportDataList = response.data;
      // this.total = response.total;
      try {
        this.year = this.companyReportDataList[0]['year'];
      } catch (e) {}
      this.loading = false;
      this.initChart();
      this.startScroll();
    },
    startScroll() {
      setTimeout(() => {
        setTimeout(() => {
          const table = this.$refs['table'];
          // 拿到表格中承载数据的div元素
          const divData = table.bodyWrapper;
          this.scrollInterval && clearInterval(this.scrollInterval);
          let lastScrollTop = 0;
          divData.scrollTop = 0;
          // 拿到元素后，对元素进行定时增加距离顶部距离，实现滚动效果(此配置为每100毫秒移动1像素)
          this.scrollInterval = setInterval(() => {
            if (this.cellMouseIn == true) {
              return;
            }
            // 元素自增距离顶部1像素
            divData.scrollTop += 1;
            if (lastScrollTop == divData.scrollTop) {
              divData.scrollTop = 0;
              lastScrollTop = 0;
              return;
            } else {
              lastScrollTop = divData.scrollTop;
            }
            // 判断元素是否滚动到底部(可视高度+距离顶部=整个高度)
            if (
              divData.clientHeight + divData.scrollTop ==
              divData.scrollHeight
            ) {
              // 重置table距离顶部距离
              divData.scrollTop = 0;
            }
          }, 40); // 滚动速度
        });
      });
    },
  },
};
</script>
<style lang="scss" scoped>
#chart {
  width: 100%;
  height: 20vw;
}
.searchArea {
  height: 20vw;
  .fieldArea {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin: 20px 0;
  }
  h5 {
    margin: 10px 0;
  }
  .row {
    padding: 5px;
  }
}
.table {
  // height: 40vh;
}
</style>
