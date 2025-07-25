<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
      <el-row>
        <el-form-item label="选择年份" prop="companyIds">
          <el-select v-model="queryParams.year" placeholder="请选择">
            <el-option v-for="(item, $idx) in years" :key="$idx" :label="item.year + '年'" :value="item.year">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择企业" prop="companyIds">
          <el-select v-model="companySelectedType" placeholder="请选择">
            <el-option v-for="item in companySearchOptions.fixedGroup" :key="item.value" :label="item.name" :value="item.value">
            </el-option>
          </el-select>
          <el-select v-model="queryParams.companyIds" clearable collapse-tags multiple style="margin-left: 20px" v-show="companySelectedType=='other'" placeholder="请选择">
            <el-option v-for="item in companySearchOptions.companyList" :key="item.id" :label="item.shortName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="散点大小" prop="symbolSize">
          <el-input-number v-model="chartConfig.symbolSize" :min="2" :max="20" />
        </el-form-item>
        <el-form-item label="散点颜色" prop="symbolSize">
          <div class="color-item">
            <el-checkbox style="margin-right: 10px" v-model="chartConfig.randomColor">随机颜色</el-checkbox>
            <el-color-picker :disabled="chartConfig.randomColor" v-model="chartConfig.color" :predefine="predefineColors">
            </el-color-picker>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
        </el-form-item>
        <el-form-item>
          <em style="color: red">提示：点击散点可删除该项。</em>
        </el-form-item>
      </el-row>
    </el-form>
    <!-- <div>
      <el-checkbox-group v-model="queryParams.companyIds">
        <el-checkbox v-for="item in companySearchOptions.companyList" :key="item.id" :label="item.id">
            <span v-html="item.shortName"></span>
        </el-checkbox>
      </el-checkbox-group>
    </div> -->
    <div id="sandianChart"></div>
  </div>
</template>

<script>
import * as compApi from '@/api/company/companyApi';
import * as echarts from 'echarts';

export default {
  name: 'CompanyReportSandianData',
  components: {},
  data() {
    return {
      // 查询参数
      queryParams: {
        companyIds: [],
        year: 2020,
      },
      predefineColors: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585',
        'rgb(255, 69, 0)',
        'rgb(255, 120, 0)',
        'hsv(51, 100, 98)',
      ],
      years: [],
      companySelectedType: 'slyt',
      companySearchOptions: {},
      sandianData: [],
      chart: {},
      chartConfig: {
        symbolSize: 15,
        color: '#00CED1',
        randomColor: false,
      },
    };
  },
  async created() {
    let _this = this;
    let years = await compApi.getValidYears(false);
    _this.years = years.data;
    _this.queryParams.year = years.data[0]['year'];
    let succ = await compApi.getCompanySearchOptions();
    // let length = 0;
    // succ.data.companyList.forEach(c => {
    //     if(c.shortName.length > length){
    //         length = c.shortName.length;
    //     }
    // });
    // succ.data.companyList.forEach(c => {
    //     c.shortName = c.shortName + Array(length - c.shortName.length + 1).join('&nbsp;&nbsp;&nbsp;');
    // });
    succ.data.fixedGroup.push({
      name: '手动选择',
      value: 'other',
    });
    _this.companySearchOptions = succ.data;
    _this.queryParams.companyIds = _this.companySearchOptions.fixedGroup.filter(
      (f) => f.value == _this.companySelectedType
    )[0]['companyIds'];
    setTimeout(() => {
      let chartDom = document.getElementById('sandianChart');
      _this.chart = echarts.init(chartDom);
      _this.handleQuery();
    });
  },
  watch: {
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
    handleQuery() {
      let _this = this;
      compApi.getSandian(this.queryParams).then((succ) => {
        _this.sandianData = succ.data;
        _this.initChart();
      });
    },
    randomColor() {
      var r = Math.floor(Math.random() * 200); //随机生成256以内r值
      var g = Math.floor(Math.random() * 200); //随机生成256以内g值
      var b = Math.floor(Math.random() * 200); //随机生成256以内b值
      return `rgb(${r},${g},${b})`; //返回rgb(r,g,b)格式颜色
    },
    initChart() {
      let _this = this;
      let option = {
        xAxis: {},
        yAxis: {},
        title: {
          text: `${this.queryParams.year}年改制企业经营发展散点图`,
          left: 'center',
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            symbolSize: _this.chartConfig.symbolSize,
            data: this.sandianData,
            type: 'scatter',
            itemStyle: {
              normal: {
                shadowBlur: 10,
                shadowColor: 'rgba(120, 36, 50, 0.5)',
                shadowOffsetY: 5,
                color: function (e) {
                  if(_this.chartConfig.randomColor){
                      return _this.predefineColors[Math.floor(Math.random() * _this.predefineColors.length - 1)];
                  }else{
                      return _this.chartConfig.color;
                  }
                },
              },
            },
            label: {
              show: true,
              formatter: '{@[2]}',
              position: 'top',
            },
          },
        ],
      };
      this.chart.setOption(option, true);
      this.chart.on('click', (param) => {
        let compId = param.value[3];
        let index = this.queryParams.companyIds.indexOf(compId);
        if (index == -1) {
          return;
        }
        _this.queryParams.companyIds.splice(index, 1);
        _this.companySelectedType = 'other';
        _this.handleQuery();
      });
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
#sandianChart {
  width: 100%;
  height: 78vh;
}
.color-item{
    display: flex;
    align-items: center;
}
</style>
