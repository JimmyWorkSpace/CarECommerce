<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="預約人姓名" prop="appointmentName">
        <el-input
          v-model="queryParams.appointmentName"
          placeholder="請輸入預約人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="預約人電話" prop="appointmentPhone">
        <el-input
          v-model="queryParams.appointmentPhone"
          placeholder="請輸入預約人電話"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="預約狀態" prop="appointmentStatus">
        <el-select v-model="queryParams.appointmentStatus" placeholder="請選擇預約狀態" clearable size="small">
          <el-option
            v-for="dict in dict.type.car_appointment_status"
            :key="dict.value"
            :label="dict.label"
            :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['car:appointment:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['car:appointment:edit']"
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
          v-hasPermi="['car:appointment:remove']"
        >刪除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:appointment:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appointmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="車輛標題" align="center" prop="saleTitle" width="200" show-overflow-tooltip />
      <el-table-column label="預約人姓名" align="center" prop="appointmentName" />
      <el-table-column label="預約人電話" align="center" prop="appointmentPhone" />
      <el-table-column label="預約時間" align="center" prop="appointmentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.appointmentTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="預約狀態" align="center" prop="appointmentStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.car_appointment_status" :value="scope.row.appointmentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="提交時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:appointment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:appointment:remove']"
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

    <!-- 添加或修改預約看車對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        
        <el-form-item label="預約人姓名" prop="appointmentName">
          <el-input v-model="form.appointmentName" placeholder="請輸入預約人姓名" />
        </el-form-item>
        <el-form-item label="預約人電話" prop="appointmentPhone">
          <el-input v-model="form.appointmentPhone" placeholder="請輸入預約人電話" />
        </el-form-item>
        <el-form-item label="預約時間" prop="appointmentTime">
          <el-date-picker clearable size="small"
            v-model="form.appointmentTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm"
            placeholder="請選擇預約時間">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="預約備註" prop="appointmentNote">
          <el-input v-model="form.appointmentNote" type="textarea" placeholder="請輸入預約備註" />
        </el-form-item>
        <el-form-item label="預約狀態" prop="appointmentStatus">
          <el-select v-model="form.appointmentStatus" placeholder="請選擇預約狀態">
            <el-option
              v-for="dict in dict.type.car_appointment_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
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
import { listAppointment, getAppointment, delAppointment, addAppointment, updateAppointment } from "@/api/car/appointment";

export default {
  name: "Appointment",
  dicts: ['car_appointment_status'],
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
      // 預約看車表格數據
      appointmentList: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appointmentName: null,
        appointmentPhone: null,
        appointmentStatus: null,
      },
      // 表單參數
      form: {},
      // 表單校驗
      rules: {
        appointmentName: [
          { required: true, message: "預約人姓名不能為空", trigger: "blur" }
        ],
        appointmentPhone: [
          { required: true, message: "預約人電話不能為空", trigger: "blur" }
        ],
        appointmentTime: [
          { required: true, message: "預約時間不能為空", trigger: "blur" }
        ],
        appointmentStatus: [
          { required: true, message: "預約狀態不能為空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查詢預約看車列表 */
    getList() {
      this.loading = true;
      listAppointment(this.queryParams).then(response => {
        this.appointmentList = response.rows;
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
        carSaleId: null,
        userId: null,
        appointmentName: null,
        appointmentPhone: null,
        appointmentTime: null,
        appointmentNote: null,
        appointmentStatus: 1,
        delFlag: false,
        createTime: null,
        updateTime: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多選框選中數據
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按鈕操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加預約";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAppointment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改預約";
      });
    },
    /** 提交按鈕 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAppointment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppointment(this.form).then(response => {
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
      this.$modal.confirm('是否確認刪除預約看車編號為"' + ids + '"的數據項？').then(function() {
        return delAppointment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 導出按鈕操作 */
    handleExport() {
      this.download('car/appointment/export', {
        ...this.queryParams
      }, `appointment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
