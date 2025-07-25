<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            :inline="true"
            label-width="100px"
        >
            <el-form-item
                label="选择年份"
                prop="companyIds"
            >
                <el-select
                    v-model="queryParams.year"
                    placeholder="请选择"
                >
                    <el-option
                        v-for="(item, $idx) in years"
                        :key="$idx"
                        :label="item.year + '年'"
                        :value="item.year"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item
                label="选择企业"
                prop="companyIds"
            >
                <el-select
                    v-model="companySelectedType"
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in companySearchOptions.fixedGroup"
                        :key="item.value"
                        :label="item.name"
                        :value="item.value"
                    >
                    </el-option>
                </el-select>
                <el-select
                    v-model="queryParams.companyIds"
                    clearable
                    collapse-tags
                    multiple
                    style="margin-left: 20px"
                    v-show="companySelectedType=='other'"
                    placeholder="请选择"
                >
                    <el-option
                        v-for="item in companySearchOptions.companyList"
                        :key="item.id"
                        :label="item.shortName"
                        :value="item.id"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button
                    type="primary"
                    icon="el-icon-search"
                    size="mini"
                    @click="handleQuery"
                >搜索</el-button>
                <el-button
                    icon="el-icon-refresh"
                    size="mini"
                    @click="resetQuery"
                >重置</el-button>
            </el-form-item>
        </el-form>
        <div id="sandianChart"></div>
    </div>
</template>

<script>
import * as compApi from "@/api/company/companyApi";
import * as echarts from "echarts";

export default {
    name: "CompanyReportSandianData",
    components: {},
    data() {
        return {
            // 查询参数
            queryParams: {
                companyIds: [],
                year: 2020,
            },
            years: [],
            companySelectedType: "slyt",
            companySearchOptions: {},
            sandianData: [],
            chart: {},
        };
    },
    async created() {
        let _this = this;
        let years = await compApi.getValidYears(false);
        _this.years = years.data;
        _this.queryParams.year = years.data[0]["year"];
        let succ = await compApi.getCompanySearchOptions();
        succ.data.fixedGroup.push({
            name: "手动选择",
            value: "other",
        });
        _this.companySearchOptions = succ.data;
        _this.queryParams.companyIds =
            _this.companySearchOptions.fixedGroup.filter(
                (f) => f.value == _this.companySelectedType
            )[0]["companyIds"];
        setTimeout(() => {
            let chartDom = document.getElementById("sandianChart");
            _this.chart = echarts.init(chartDom);
            _this.handleQuery();
        });
    },
    watch: {
        companySelectedType() {
            let _this = this;
            if (_this.companySelectedType != "other") {
                _this.queryParams.companyIds =
                    _this.companySearchOptions.fixedGroup.filter(
                        (f) => f.value == _this.companySelectedType
                    )[0]["companyIds"];
                console.log(
                    "query params company ids change",
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
                    )[0]["companyIds"];
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
        initChart() {
            let option = {
                xAxis: {},
                yAxis: {},
                series: [
                    {
                        symbolSize: 15,
                        data: this.sandianData,
                        type: "scatter",
                        label: {
                            show: true,
                            formatter: "{@[2]}",
                            position: "top",
                        },
                    },
                ],
            };
            this.chart.setOption(option, true);
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
    },
};
</script>
<style lang="scss" scoped>
#sandianChart {
    width: 100%;
    height: 75vh;
}
</style>
