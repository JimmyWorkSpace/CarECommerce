<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="票券名稱" prop="cardName">
        <el-input
          v-model="queryParams.cardName"
          placeholder="請輸入票券名稱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="狀態" prop="status">
        <el-select v-model="queryParams.status" placeholder="請選擇狀態" clearable>
          <el-option label="啟用" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['car:card:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:card:export']"
        >導出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardList" row-key="id">
      <el-table-column label="票券名稱" align="center" prop="cardName" min-width="120" :show-overflow-tooltip="true" />
      <el-table-column label="售價" align="center" prop="salePrice" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.salePrice != null ? '¥' + scope.row.salePrice : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用說明" align="left" prop="usageInstruction" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="有效期限" align="center" width="140">
        <template slot-scope="scope">
          <span v-if="scope.row.validityType === 1">
            指定日期：{{ scope.row.validityEndDate ? parseTime(scope.row.validityEndDate, '{y}-{m}-{d}') : '-' }}
          </span>
          <span v-else-if="scope.row.validityType === 2">
            購買後 {{ scope.row.validityDays }} 天內有效
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="狀態" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '啟用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="建立時間" align="center" prop="createTime" width="165">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:card:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:card:remove']"
          >刪除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增或修改票券方案對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="票券名稱" prop="cardName">
          <el-input v-model="form.cardName" placeholder="請輸入票券名稱" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="售價" prop="salePrice">
          <el-input-number v-model="form.salePrice" :min="0" :precision="2" :controls="true" style="width: 100%;" placeholder="請輸入售價" />
        </el-form-item>
        <el-form-item label="使用說明" prop="usageInstruction">
          <el-input v-model="form.usageInstruction" type="textarea" :rows="3" placeholder="請輸入使用說明" />
        </el-form-item>
        <el-form-item label="有效期限類型" prop="validityType">
          <el-radio-group v-model="form.validityType" @change="handleValidityTypeChange">
            <el-radio :label="1">指定日期</el-radio>
            <el-radio :label="2">指定天數</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.validityType === 1" label="有效截止日" prop="validityEndDate">
          <el-date-picker
            v-model="form.validityEndDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="選擇有效截止日"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item v-if="form.validityType === 2" label="有效天數" prop="validityDays">
          <el-input-number v-model="form.validityDays" :min="1" :max="3650" :controls="true" style="width: 100%;" placeholder="購買後N天內有效" />
        </el-form-item>
        <el-form-item label="狀態" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">啟用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="備註" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="請輸入備註" maxlength="500" show-word-limit />
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
import { listCard, getCard, delCard, addCard, updateCard } from '@/api/car/card'

export default {
  name: 'Card',
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      cardList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cardName: null,
        status: null
      },
      form: {},
      rules: {
        cardName: [
          { required: true, message: '票券名稱不能為空', trigger: 'blur' }
        ],
        salePrice: [
          { required: true, message: '售價不能為空', trigger: 'blur' }
        ],
        validityType: [
          { required: true, message: '請選擇有效期限類型', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCard(this.queryParams).then(response => {
        this.cardList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        cardName: null,
        salePrice: 0,
        usageInstruction: null,
        validityType: 1,
        validityEndDate: null,
        validityDays: null,
        status: 1,
        remark: null
      }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增票券方案'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id
      getCard(id).then(response => {
        this.form = response.data
        if (this.form.validityType == null) this.form.validityType = 1
        if (this.form.status == null) this.form.status = 1
        if (this.form.salePrice == null) this.form.salePrice = 0
        this.open = true
        this.title = '修改票券方案'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) return
        if (this.form.validityType === 1) {
          if (!this.form.validityEndDate) {
            this.$modal.msgWarning('請選擇有效截止日')
            return
          }
          this.form.validityDays = null
        } else {
          if (this.form.validityDays == null || this.form.validityDays < 1) {
            this.$modal.msgWarning('請輸入有效天數（至少 1 天）')
            return
          }
          this.form.validityEndDate = null
        }
        if (this.form.id != null) {
          updateCard(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addCard(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      const msg = Array.isArray(ids) ? '是否確認刪除所選的票券方案？' : '是否確認刪除該票券方案？'
      this.$modal.confirm(msg).then(() => {
        return delCard(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('刪除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('car/card/export', {
        ...this.queryParams
      }, `票券方案_${new Date().getTime()}.xlsx`)
    },
    handleValidityTypeChange() {
      if (this.form.validityType === 1) {
        this.form.validityDays = null
      } else {
        this.form.validityEndDate = null
      }
    }
  }
}
</script>
