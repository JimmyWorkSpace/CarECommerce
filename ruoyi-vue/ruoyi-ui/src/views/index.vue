<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="年份">
        <el-select v-model="queryParams.year">
          <el-option v-for="(y, idx) in years" :key="idx" :label="y.year + '年'" :value="y.year" />
        </el-select>
      </el-form-item>
      <el-form-item label="企业">
        <el-select v-model="queryParams.type">
          <el-option label="全部" value="all"></el-option>
          <el-option label="胜利油田" value="slyt"></el-option>
          <el-option label="胜利工程" value="slgc"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <swiper ref="mySwiper" :options="swiperOptions">
      <swiper-slide v-for="(w, idx) in swiperData" :key="idx" class="top-slide">
        <!-- <div class="swiper-title">
          {{w.title}}
        </div> -->
        <el-row>
          <el-col v-for="(d, idx2) in w.datas" :span="d.span" :key="idx2">
            <div class="tip" :class="d.inc > 0 ? 'green' : (d.inc < 0 ? 'red' : 'yellow')">
              <div class="tip-left" v-html="d.title"></div>
              <div class="tip-right" :class="{up: d.inc < 0, down: d.inc > 0}">
                <div class="value">{{d.value}}</div>
                <div class="inc">同比：{{(d.inc > 0 ? '+' : '') + d.inc}}%</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </swiper-slide>
    </swiper>
    <el-row class="top-row" :gutter="20">
      <el-col :span="16">
        <el-card>
          <el-row>
            <el-col :span="12">
              <div class="chart-div" id="pieChart1"></div>
            </el-col>
            <el-col :span="12">
              <div class="chart-div" id="pieChart2"></div>
            </el-col>
          </el-row>
          <!-- <el-row>
            <el-col :span="12">
              <div class="chart-div" id="barChart1"></div>
            </el-col>
            <el-col :span="12">
              <div class="chart-div" id="barChart2"></div>
            </el-col>
          </el-row> -->

        </el-card>
        <!-- <el-tabs type="border-card" class="chart-tab">
          <el-tab-pane label="饼图">
          </el-tab-pane>
          <el-tab-pane label="柱图">
            <el-row>
              <el-col :span="12">
              </el-col>
              <el-col :span="12">
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs> -->
      </el-col>
      <el-col :span="8">
        <el-tabs type="border-card">
          <el-tab-pane label="综合排名">
            <el-table :data="lastestYearData" stripe :size="'medium'" :height="`calc(26vw - 35px)`" :ref="'table'">
              <el-table-column align="center" prop="paiming" width="80" label="排名" />
              <el-table-column align="center" prop="companyName" label="企业名称" />
              <el-table-column align="left" label="上年度" prop="prePaiming">
                <template slot-scope="scope">
                  <div class="paiming-td">
                    <span>{{scope.row.prePaiming}}</span>
                    <el-progress :show-text="false" class="pro" v-if="scope.row.paiming>scope.row.prePaiming" :percentage="(scope.row.paiming - scope.row.prePaiming) * 100 / lastestYearData.length" status="exception"></el-progress>
                    <el-progress :show-text="false" class="pro" v-if="scope.row.paiming<scope.row.prePaiming" :percentage="(scope.row.prePaiming - scope.row.paiming) * 100 / lastestYearData.length" status="success"></el-progress>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="经济效益排名">
            <el-table :data="lastestYearDataJjxy" stripe :size="'medium'" :height="`calc(26vw - 35px)`" :ref="'tableJjxy'">
              <el-table-column align="center" prop="paimingJjxy" width="80" label="排名" />
              <el-table-column align="center" prop="companyName" label="企业名称" />
              <el-table-column align="left" label="上年度" prop="prePaimingJjxy">
                <template slot-scope="scope">
                  <div class="paiming-td">
                    <span>{{scope.row.prePaimingJjxy}}</span>
                    <el-progress :show-text="false" class="pro" v-if="scope.row.paimingJjxy>scope.row.prePaimingJjxy" :percentage="(scope.row.paimingJjxy - scope.row.prePaimingJjxy) * 100 / lastestYearData.length" status="exception"></el-progress>
                    <el-progress :show-text="false" class="pro" v-if="scope.row.paimingJjxy<scope.row.prePaimingJjxy" :percentage="(scope.row.prePaimingJjxy - scope.row.paimingJjxy) * 100 / lastestYearData.length" status="success"></el-progress>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="经营发展排名">
            <el-table :data="lastestYearDataJyfz" stripe :size="'medium'" :height="`calc(26vw - 35px)`" :ref="'tableJyfz'">
              <el-table-column align="center" prop="paimingJyfz" width="80" label="排名" />
              <el-table-column align="center" prop="companyName" label="企业名称" />
              <el-table-column align="left" label="上年度" prop="prePaimingJyfz">
                <template slot-scope="scope">
                  <div class="paiming-td">
                    <span>{{scope.row.prePaimingJyfz}}</span>
                    <el-progress :show-text="false" class="pro" v-if="scope.row.paimingJyfz>scope.row.prePaimingJyfz" :percentage="(scope.row.paimingJyfz - scope.row.prePaimingJyfz) * 100 / lastestYearData.length" status="exception"></el-progress>
                    <el-progress :show-text="false" class="pro" v-if="scope.row.paimingJyfz<scope.row.prePaimingJyfz" :percentage="(scope.row.prePaimingJyfz - scope.row.paimingJyfz) * 100 / lastestYearData.length" status="success"></el-progress>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>

        <div class="loginOut">
          <span>滚动</span>
          <el-switch v-model="tableAutoScroll"> </el-switch>
        </div>
      </el-col>
    </el-row>
    <el-row class="top-row" :gutter="20">
      <el-col :span="24">
        <el-card>
          <el-row>
            <el-col :span="8">
              <div class="chart-div" id="barChart1"></div>
            </el-col>
            <el-col :span="8">
              <div class="chart-div" id="barChart2"></div>
            </el-col>
            <el-col :span="8">
              <div class="chart-div" id="barChart3"></div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <!-- <el-row class="bottom-row" :gutter="20">
      <el-table :data="overviewData">
        <el-table-column label="职工队伍结构" align="center">
          <el-table-column label="职工总数" align="center" prop="zgzs" />
          <el-table-column label="改制职工月末人数" align="center" prop="gzzg" />
          <el-table-column label="安置油田子女及协解职工人数" align="center" prop="azzn" :formatter="azznFormatter" />
          <el-table-column label="自行招聘人数" align="center" prop="zxzp" />
        </el-table-column>
        <el-table-column label="收入结构" align="center">
          <el-table-column label="当年合计" align="center" prop="dnhj" />
          <el-table-column label="油田内部市场收入" align="center" prop="nbsr" />
          <el-table-column label="外部市场收入" align="center" prop="wbsr" />
          <el-table-column label="改制企业之间收入" align="center" prop="qyjsr" />
          <el-table-column label="国际市场收入" align="center" prop="gjsr" />
        </el-table-column>
        <el-table-column label="利税情况" align="center">
          <el-table-column label="当年利润累计" align="center" prop="dnlr" />
          <el-table-column label="当年已交税金累计" align="center" prop="dnsj" />
        </el-table-column>
      </el-table>
    </el-row> -->
  </div>
</template>

<script>
import * as compApi from '@/api/company/companyApi';
import { swiper, swiperSlide } from 'vue-awesome-swiper';
import '../../node_modules/swiper/dist/css/swiper.min.css';
// import '../../node_modules/swiper/core/';
// import * as echarts from 'echarts';
// import { customedTheme, chalkTheme, macaronsThemes, infographicThemes} from '../assets/echarts-themes';
// import '../../node_modules/echarts/map/js/china.js'; // 引入中国地图数据

const barChartLabel = {
  color: '#ffffff',
  borderRadius: [4, 4, 4, 4],
  padding: [4, 4, 4, 4],
};

export default {
  name: 'CompanyReportIndexData',
  components: { swiper, swiperSlide },
  data() {
    return {
      // 查询参数
      queryParams: {
        companyIds: [],
        head: 'zhfz',
        year: 2020,
        type: 'slyt',
      },
      tableAutoScroll: true,
      swiperOptions: {
        direction: 'vertical',
        autoHeight: true,
        autoplay: {
          delay: 8000,
        },
        effect: 'cube',
        cubeEffect: {
          slideShadows: false,
          shadow: false,
          shadowOffset: 100,
          shadowScale: 0.6,
        },
        speed: 1000,
        //   autoHeight: true
      },
      colors: [],
      lastestYearData: [],
      lastestYearDataJjxy: [],
      lastestYearDataJyfz: [],
      years: [],
      companySelectedType: 'slyt',
      companySearchOptions: {},
      datas: [],
      swiperData: [],
      //   mapChart: {},
      chart1: {},
      chart2: {},
      overviewData: [],
      chart1Data: {
        values: [],
        labels: [],
      },
      chart2Data: {
        values: [],
        labels: [],
      },
      barChart1: {},
      barChart2: {},
      barChart3: {},
      barChart1Data: {
        values: [],
        labels: [],
      },
      barChart2Data: {
        values: [],
        labels: [],
      },
      barChart3Data: {
        values: [],
        labels: [],
      },
      pieChart1: {},
      pieChart2: {},
      pieChart3: {},
      barChartLabel: {
        show: true,
        position: 'top',
        formatter: (params) => {
          if (params.data.inc < 0) {
            return '{r|同比：' + params.data.inc + '%}';
          } else if (params.data.inc > 0) {
            return '{g|同比：+' + params.data.inc + '%}';
          }
        },
        rich: {
          r: {
            backgroundColor: '#f56c6c',
            ...barChartLabel,
          },
          g: {
            backgroundColor: '#67c23a',
            ...barChartLabel,
          },
        },
      },
    };
  },
  async created() {
    let colors = await compApi.colors();
    this.colors = colors.data;
    debugger;
    await this.initData();
    await this.init();
  },

  watch: {
    'queryParams.year'() {
      this.init();
    },
    'queryParams.type'() {
      this.init();
    },
    // companySelectedType() {
    //     let _this = this;
    //     if (_this.companySelectedType != "other") {
    //         _this.queryParams.companyIds =
    //             _this.companySearchOptions.fixedGroup.filter(
    //                 (f) => f.value == _this.companySelectedType
    //             )[0]["companyIds"];
    //         console.log(
    //             "query params company ids change",
    //             _this.queryParams.companyIds
    //         );
    //     }
    // },
  },
  methods: {
    startScroll(tb) {
      setTimeout(() => {
        setTimeout(() => {
          const table = this.$refs[tb];
          // 拿到表格中承载数据的div元素
          const divData = table.bodyWrapper;
          this['scrollInterval' + tb] &&
            clearInterval(this['scrollInterval' + tb]);
          let lastScrollTop = 0;
          divData.scrollTop = 0;
          // 拿到元素后，对元素进行定时增加距离顶部距离，实现滚动效果(此配置为每100毫秒移动1像素)
          this['scrollInterval' + tb] = setInterval(() => {
            if (this.cellMouseIn == true || this.tableAutoScroll != true) {
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
    async initData() {
      let _this = this;

      let years = await compApi.getValidYears(false);
      _this.years = years.data;
      _this.queryParams.year = years.data[0]['year'];

      let succ = await compApi.getCompanySearchOptions();
      succ.data.fixedGroup.push({
        name: '手动选择',
        value: 'other',
      });
      _this.companySearchOptions = succ.data;
      _this.queryParams.companyIds =
        _this.companySearchOptions.fixedGroup.filter(
          (f) => f.value == _this.companySelectedType
        )[0]['companyIds'];
    },
    async init() {
      let _this = this;
      let echarts = this.echarts;
      let overview = await compApi.overview(_this.queryParams);
      _this.overviewData = [overview.data];
      this.startScroll('table');
      this.startScroll('tableJjxy');
      this.startScroll('tableJyfz');
      let d = overview.data;
      _this.swiperData = [
        {
          title: '职工队伍结构',
          datas: [
            {
              span: 6,
              color: 'blue',
              value: d.zgzs,
              inc: d.zgzsInc,
              title: '职工总数',
            },
            {
              span: 6,
              color: 'green',
              value: d.gzzg,
              inc: d.gzzgInc,
              title: '改制职工<br>月末人数',
            },
            {
              span: 6,
              color: 'red',
              value: d.azzn + d.azzg,
              inc: d.azznInc,
              title: '安置油田子女<br>及协解职工人数',
            },
            {
              span: 6,
              color: 'yellow',
              value: d.zxzp,
              inc: d.zxzpInc,
              title: '自行招聘人数',
            },
          ],
        },
        // <el-table-column label="当年合计" align="center" prop="dnhj" />
        //   <el-table-column label="油田内部市场收入" align="center" prop="nbsr" />
        //   <el-table-column label="外部市场收入" align="center" prop="wbsr" />
        //   <el-table-column label="改制企业之间收入" align="center" prop="qyjsr" />
        //   <el-table-column label="国际市场收入" align="center" prop="gjsr" />
        {
          title: '收入结构',
          datas: [
            {
              span: 5,
              color: 'blue',
              value: d.dnhj,
              inc: d.dnhjInc,
              title: '当年合计',
            },
            {
              span: 5,
              color: 'green',
              value: d.nbsr,
              inc: d.nbsrInc,
              title: '油田内部<br>市场收入',
            },
            {
              span: 5,
              color: 'red',
              value: d.wbsr,
              inc: d.wbsrInc,
              title: '外部市场收入',
            },
            {
              span: 5,
              color: 'yellow',
              value: d.qyjsr,
              inc: d.qyjsrInc,
              title: '改制企业<br>之间收入',
            },
            {
              span: 5,
              color: 'blue',
              value: d.gjsr,
              inc: d.gjsrInc,
              title: '国际市场收入',
            },
          ],
        },
        {
          title: '利税情况',
          datas: [
            {
              span: 6,
              color: 'blue',
              value: d.dnlr,
              inc: d.dnlrInc,
              title: '当年利润累计',
            },
            {
              span: 6,
              color: 'green',
              value: d.dnsj,
              inc: d.dnsjInc,
              title: '当年已交<br>税金累计',
            },
          ],
        },
      ];

      // this.swiperData.forEach(s => {
      //   s.datas.forEach(d => {
      //     let value = d.value;
      //     let color = 'white'
      //   })
      // })

      let barChartWidth = document.getElementById('barChart1').offsetWidth;

      let barWidth1 = (barChartWidth * 0.6) / 8;
      let barOffset1 = barWidth1 / 2 + (barChartWidth * 0.4) / 24;
      let barWidth2 = (barChartWidth * 0.6) / 10;
      let barOffset2 = barWidth2 / 2 + (barChartWidth * 0.4) / 40;
      let barWidth3 = (barChartWidth * 0.6) / 4;
      let barOffset3 = barWidth3 / 2 + (barChartWidth * 0.4) / 16;
      _this.barChart1Data = {
        labels: [
          '职工总数',
          '改制职工人数',
          '安置油田子女及协解职工人数',
          '自行招聘人数',
        ],
        title: '职工队伍结构',
        values: [
          {
            name: '当年',
            type: 'bar',
            label: {
              offset: [barOffset1, 0],
              ..._this.barChartLabel,
            },
            barWidth: barWidth1,
            data: [
              {
                value: d.zgzs,
                inc: d.zgzsInc,
                label: {
                  show: d.zgzs > d.zgzs2,
                },
              },
              {
                value: d.gzzg,
                inc: d.gzzgInc,
                label: {
                  show: d.gzzg > d.gzzg2,
                },
              },
              {
                value: d.azzn + d.azzg,
                inc: d.azznInc,
                label: {
                  show: d.azzn + d.azzg > d.azzn2 + d.azzg2,
                },
              },
              {
                value: d.zxzp,
                inc: d.zxzpInc,
                label: {
                  show: d.zxzp > d.zxzp2,
                },
              },
            ],
          },
          {
            name: '上年',
            type: 'bar',
            label: {
              offset: [barOffset1 * -1, 0],
              ..._this.barChartLabel,
            },
            barWidth: barWidth1,
            data: [
              {
                value: d.zgzs2,
                inc: d.zgzsInc,
                label: {
                  show: d.zgzs < d.zgzs2,
                },
              },
              {
                value: d.gzzg2,
                inc: d.gzzgInc,
                label: {
                  show: d.gzzg < d.gzzg2,
                },
              },
              {
                value: d.azzn2 + d.azzg2,
                inc: d.azznInc,
                label: {
                  show: d.azzn + d.azzg < d.azzn2 + d.azzg2,
                },
              },
              {
                value: d.zxzp2,
                inc: d.zxzpInc,
                label: {
                  show: d.zxzp < d.zxzp2,
                },
              },
            ],
          },
        ],
      };
      _this.barChart2Data = {
        labels: [
          '当年合计',
          '油田内部市场收入',
          '外部市场收入',
          '改制企业之间收入',
          '国际市场收入',
        ],
        title: '收入结构',
        values: [
          {
            name: '当年',
            type: 'bar',
            label: {
              offset: [barOffset2, 0],
              ..._this.barChartLabel,
            },
            barWidth: barWidth2,
            data: [
              {
                value: d.dnhj,
                inc: d.dnhjInc,
                label: {
                  show: d.dnhj > d.dnhj2,
                },
              },
              {
                value: d.nbsr,
                inc: d.nbsrInc,
                label: {
                  show: d.nbsr > d.nbsr2,
                },
              },
              {
                value: d.wbsr,
                inc: d.wbsrInc,
                label: {
                  show: d.wbsr > d.wbsr2,
                },
              },
              {
                value: d.qyjsr,
                inc: d.qyjsrInc,
                label: {
                  show: d.qyjsr > d.qyjsr2,
                },
              },
              {
                value: d.gjsr,
                inc: d.gjsrInc,
                label: {
                  show: d.gjsr > d.gjsr2,
                },
              },
            ],
          },
          {
            name: '上年',
            type: 'bar',
            label: {
              offset: [barOffset2 * -1, 0],
              ..._this.barChartLabel,
            },
            barWidth: barWidth2,
            data: [
              {
                value: d.dnhj2,
                inc: d.dnhjInc,
                label: {
                  show: d.dnhj < d.dnhj2,
                },
              },
              {
                value: d.nbsr2,
                inc: d.nbsrInc,
                label: {
                  show: d.nbsr < d.nbsr2,
                },
              },
              {
                value: d.wbsr2,
                inc: d.wbsrInc,
                label: {
                  show: d.wbsr < d.wbsr2,
                },
              },
              {
                value: d.qyjsr2,
                inc: d.qyjsrInc,
                label: {
                  show: d.qyjsr < d.qyjsr2,
                },
              },
              {
                value: d.gjsr2,
                inc: d.gjsrInc,
                label: {
                  show: d.gjsr < d.gjsr2,
                },
              },
            ],
          },
        ],
      };

      _this.barChart3Data = {
        labels: ['当年已交税金累计', '当年利润累计'],
        title: '利税情况',
        values: [
          {
            name: '当年',
            type: 'bar',
            label: {
              offset: [barOffset3, 0],
              ..._this.barChartLabel,
            },
            barWidth: barWidth3,
            data: [
              {
                value: d.dnlr,
                inc: d.dnlrInc,
                label: {
                  show: d.dnlr > d.dnlr2,
                },
              },
              {
                value: d.dnsj,
                inc: d.dnsjInc,
                label: {
                  show: d.dnsj > d.dnsj2,
                },
              },
            ],
          },
          {
            name: '上年',
            type: 'bar',
            label: {
              offset: [barOffset3 * -1, 0],
              ..._this.barChartLabel,
            },
            barWidth: barWidth3,
            data: [
              {
                value: d.dnlr2,
                inc: d.dnlrInc,
                label: {
                  show: d.dnlr < d.dnlr2,
                },
              },
              {
                value: d.dnsj2,
                inc: d.dnsjInc,
                label: {
                  show: d.dnsj < d.dnsj2,
                },
              },
            ],
          },
        ],
      };

      let data = await compApi.getLastestYearData(_this.queryParams);
      data.data = data.data.sort((a, b) => {
        return a.paiming - b.paiming;
      });
      _this.lastestYearData = JSON.parse(JSON.stringify(data.data));
      data.data = data.data.sort((a, b) => {
        return a.paimingJjxy - b.paimingJjxy;
      });
      _this.lastestYearDataJjxy = JSON.parse(JSON.stringify(data.data));
      data.data = data.data.sort((a, b) => {
        return a.paimingJyfz - b.paimingJyfz;
      });
      _this.lastestYearDataJyfz = JSON.parse(JSON.stringify(data.data));
      data.data.slice(0, 6).forEach((d) => {
        _this.chart1Data.values.push(d.zhfz);
        _this.chart1Data.labels.push(d.companyName);
      });
      _this.chart1Data.title = '综合排名前6';
      data.data
        .reverse()
        .slice(0, 6)
        .forEach((d) => {
          _this.chart2Data.values.push(d.zhfz);
          _this.chart2Data.labels.push(d.companyName);
        });
      _this.chart2Data.title = '综合排名后6';

      setTimeout(() => {
        const theme = 'walden';

        // let mapDom = document.getElementById('mapChart');
        // _this.mapChart = echarts.init(mapDom, theme);
        // _this.handleQuery();

        // let chart1Dom = document.getElementById('chart1');
        // _this.chart1 = echarts.init(chart1Dom, theme);
        // _this.initChart(_this.chart1, _this.chart1Data);

        // let chart2Dom = document.getElementById('chart2');
        // _this.chart2 = echarts.init(chart2Dom, theme);
        // _this.initChart(_this.chart2, _this.chart2Data);

        let barChart1Dom = document.getElementById('barChart1');
        _this.barChart1 = echarts.init(barChart1Dom, theme);
        _this.initBarChart(_this.barChart1, _this.barChart1Data);

        let barChart2Dom = document.getElementById('barChart2');
        _this.barChart2 = echarts.init(barChart2Dom, theme);
        _this.initBarChart(_this.barChart2, _this.barChart2Data);

        let barChart3Dom = document.getElementById('barChart3');
        _this.barChart3 = echarts.init(barChart3Dom, theme);
        _this.initBarChart(_this.barChart3, _this.barChart3Data);

        let pieChart1Dom = document.getElementById('pieChart1');
        _this.pieChart1 = echarts.init(pieChart1Dom, theme);
        _this.initPieChart(_this.pieChart1, _this.barChart1Data, true);

        let pieChart2Dom = document.getElementById('pieChart2');
        _this.pieChart2 = echarts.init(pieChart2Dom, theme);
        _this.initPieChart(_this.pieChart2, _this.barChart2Data, true);

        // let barChart3Dom = document.getElementById("barChart3");
        // _this.barChart3 = echarts.init(barChart3Dom);
        // _this.initBarChart(_this.barChart3, _this.barChart3Data, true);
      });
    },
    azznFormatter(row, column) {
      return row.azzn + row.azzg;
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
    handleQuery() {
      let _this = this;
      // compApi.getSandian(this.queryParams).then((succ) => {
      //     _this.sandianData = succ.data;
      //     _this.initChart();
      // });
      _this.initMapChart();
    },
    initPieChart(chart, data, ignoreFirst) {
      let datas = [];
      for (let i = ignoreFirst ? 1 : 0; i < data.labels.length; i++) {
        datas.push({
          name: data.labels[i],
          value: data.values[0]['data'][i]['value'],
          inc: data.values[0]['data'][i]['inc'],
          tooltip: {
            formatter: '{b}:{c}（占比{d}%）',
          },
        });
      }
      let option = {
        title: {
          text: data.title,
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            // name: 'Access From',
            type: 'pie',
            radius: '50%',
            label: {
              show: true,
              position: 'outside',
            },
            data: datas,
            // emphasis: {
            //     itemStyle: {
            //         shadowBlur: 10,
            //         shadowOffsetX: 0,
            //         shadowColor: "rgba(0, 0, 0, 0.5)",
            //     },
            // },
          },
        ],
      };
      chart.setOption(option, true);
    },
    initBarChart(chart, data) {
      let option = {
        title: {
          text: data.title,
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          show: true,
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, 0.01],
        },
        xAxis: {
          type: 'category',
          axisLabel: {
            interval: 0, //横轴信息全部显示
            // rotate: -45, //-30度角倾斜显示
          },
          data: data.labels,
        },
        series: data.values,
      };
      chart.setOption(option, true);
    },
    initChart(chart, data) {
      let option = {
        title: {
          text: data.title,
        },
        yAxis: {
          type: 'category',
          data: data.labels,
        },
        xAxis: {
          type: 'value',
        },
        series: [
          {
            data: data.values,
            type: 'bar',
          },
        ],
      };
      chart.setOption(option, true);
    },
    initMapChart() {
      let option = {
        tooltip: {
          show: false,
        },
        geo: {
          map: 'china',
          roam: false, // 一定要关闭拖拽
          zoom: 1.23,
          center: [105, 36], // 调整地图位置
          label: {
            normal: {
              show: false, //关闭省份名展示
              fontSize: '10',
              color: 'rgba(0,0,0,0.7)',
            },
            emphasis: {
              show: false,
            },
          },
          itemStyle: {
            normal: {
              areaColor: '#0d0059',
              borderColor: '#389dff',
              borderWidth: 1, //设置外层边框
              shadowBlur: 5,
              shadowOffsetY: 8,
              shadowOffsetX: 0,
              shadowColor: '#01012a',
            },
            emphasis: {
              areaColor: '#184cff',
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowBlur: 5,
              borderWidth: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)',
            },
          },
        },
        series: [
          {
            type: 'map',
            map: 'china',
            roam: false,
            zoom: 1.23,
            center: [105, 36],
            // geoIndex: 1,
            // aspectScale: 0.75, //长宽比
            showLegendSymbol: false, // 存在legend时显示
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                show: false,
                textStyle: {
                  color: '#fff',
                },
              },
            },
            itemStyle: {
              normal: {
                areaColor: '#0d0059',
                borderColor: '#389dff',
                borderWidth: 0.5,
              },
              emphasis: {
                areaColor: '#17008d',
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowBlur: 5,
                borderWidth: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      };
      this.mapChart.setOption(option, true);
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
.app-container {
  .loginOut {
    position: absolute;
    right: 20px;
    top: 11px;
    color: #909399;
    font-weight: normal;
    font-size: 13px;
    height: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    span {
      margin-right: 5px;
    }
  }
  .el-col-5 {
    width: 20%;
  }
  .chart-tab {
    height: 26vw;
  }
  //   background-color: rgba(41,52,65,1);
  .area {
    // background-color: #ffffff;
  }
  .top-row {
    margin: 30px 0;
    height: 60vh;
    #mapChart {
      height: 60vh;
    }
    #chart1,
    #chart2 {
      height: 40vh;
    }
    #barChart1,
    #barChart2,
    #barChart3 {
      height: 26vw;
    }
    #pieChart1,
    #pieChart2,
    #pieChart3 {
      height: 26vw;
    }
    .nav-btn-area {
      height: 14vh;
    }
    margin-bottom: 20px;
  }
  .bottom-row {
    height: 150px;
  }

  .paiming-td {
    display: flex;
    align-items: center;
    span {
      width: 40px;
      text-align: center;
    }
    .pro {
      width: 60%;
    }
  }

  .arrow {
    width: 30px;
    height: 25px;
    background-size: 100%;
  }

  .arrow-up {
    //   background-image: url('../assets/icons/arrow-up-blue.png');
  }

  .arrow-down {
    //   background-image: url('../assets/icons/arrow-down-red.png');
  }

  .top-slide {
    height: 27px;
  }
  .swiper-title {
    margin-bottom: 5px;
  }
  .tip {
    $height: 80px;
    $border-radius: 4px;
    $border-radius2: 3px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    width: 90%;
    height: $height;
    border-radius: $border-radius;
    &.blue {
      $color: #68c7ed;
      border: 1px solid $color;
      .tip-left {
        background-color: $color;
        color: white;
      }
    }
    &.yellow {
      $color: #fbca3e;
      border: 1px solid $color;
      .tip-left {
        background-color: $color;
        color: white;
      }
    }
    &.red {
      $color: #f56c6c;
      border: 1px solid $color;
      .tip-left {
        background-color: $color;
        color: white;
      }
    }
    &.green {
      $color: #67c23a;
      border: 1px solid $color;
      .tip-left {
        background-color: $color;
        color: white;
      }
    }
    .tip-left {
      border-top-left-radius: $border-radius2;
      border-bottom-left-radius: $border-radius2;
      text-align: left;
    }
    div {
      font-size: 16px;
      font-weight: bold;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 78px;
      flex-grow: 1;
    }
    .tip-right {
      background-color: white;
      font-size: 18px;
      padding: 10px 0;
      width: 65px;
      display: flex;
      flex-direction: column;
      border-top-right-radius: $border-radius2;
      border-bottom-right-radius: $border-radius2;
      color: #555555;
      .value {
        font-size: 20px;
      }
      .inc {
        font-size: 16px;
      }

      &.up {
        color: #ff0000;
      }

      &.down {
        color: #67c23a;
      }
    }
  }
}
</style>
