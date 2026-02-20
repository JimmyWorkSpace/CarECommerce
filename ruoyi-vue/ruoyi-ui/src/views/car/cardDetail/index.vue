<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="券碼" prop="ticketCode">
        <el-input
          v-model="queryParams.ticketCode"
          placeholder="請輸入券碼模糊搜尋"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" row-key="id">
      <el-table-column label="券碼" align="center" prop="ticketCode" min-width="140" show-overflow-tooltip />
      <el-table-column label="票券名稱" align="center" prop="cardName" min-width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-link type="primary" @click="showDetail(scope.row)">{{ scope.row.cardName || '-' }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="所屬用戶姓名" align="center" prop="buyerUserName" min-width="100" show-overflow-tooltip />
      <el-table-column label="所屬用戶手機號" align="center" prop="buyerPhonenumber" width="120" show-overflow-tooltip />
      <el-table-column label="訂單ID" align="center" prop="orderId" width="100" />
      <el-table-column label="是否核銷" align="center" prop="redeemed" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.redeemed === 1 ? 'success' : 'info'">
            {{ scope.row.redeemed === 1 ? '已核銷' : '未核銷' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="核銷時間" align="center" prop="redeemedTime" width="165">
        <template slot-scope="scope">
          <span>{{ scope.row.redeemedTime ? parseTime(scope.row.redeemedTime, '{y}-{m}-{d} {h}:{i}:{s}') : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="核銷人ID" align="center" prop="redeemedUserId" width="100" />
      <el-table-column label="購買時間" align="center" prop="createTime" width="165">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleRedeem(scope.row)"
            :disabled="scope.row.redeemed === 1"
            v-hasPermi="['car:cardDetail:redeem']"
          >核銷</el-button>
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

    <!-- 票券資訊彈窗：顯示 car_card 方案資訊 -->
    <el-dialog title="票券資訊" :visible.sync="detailOpen" width="560px" append-to-body>
      <div v-if="cardInfo" class="detail-section">
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="票券名稱">{{ cardInfo.cardName }}</el-descriptions-item>
          <el-descriptions-item label="售價">¥ {{ cardInfo.salePrice != null ? cardInfo.salePrice : '-' }}</el-descriptions-item>
          <el-descriptions-item label="使用說明">{{ cardInfo.usageInstruction || '-' }}</el-descriptions-item>
          <el-descriptions-item label="有效期限">{{ formatCardValidity(cardInfo) }}</el-descriptions-item>
          <el-descriptions-item label="狀態">{{ cardInfo.status === 1 ? '啟用' : '停用' }}</el-descriptions-item>
          <el-descriptions-item label="備註">{{ cardInfo.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailOpen = false">關閉</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listCardDetail, redeemCardDetail } from '@/api/car/cardDetail'
import { getCard } from '@/api/car/card'

export default {
  name: 'CardDetail',
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      detailList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ticketCode: null
      },
      detailOpen: false,
      /** 票券方案（car_card） */
      cardInfo: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCardDetail(this.queryParams).then(response => {
        this.detailList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleRedeem(row) {
      if (row.redeemed === 1) return
      this.$modal.confirm('是否確認核銷券碼「' + row.ticketCode + '」？').then(() => {
        return redeemCardDetail(row.ticketCode)
      }).then(() => {
        this.$modal.msgSuccess('核銷成功')
        this.getList()
      }).catch(() => {})
    },
    showDetail(row) {
      this.cardInfo = null
      this.detailOpen = true
      if (row.cardId) {
        getCard(row.cardId).then(res => {
          this.cardInfo = res.data
        }).catch(() => {
          this.$modal.msgError('取得票券方案資訊失敗')
        })
      }
    },
    formatCardValidity(card) {
      if (!card) return '-'
      if (card.validityType === 1) {
        const d = card.validityEndDate
        const str = d ? this.parseTime(d, '{y}-{m}-{d}') : '-'
        return '指定日期至 ' + str
      }
      if (card.validityType === 2 && card.validityDays != null) return '購買後 ' + card.validityDays + ' 天內有效'
      return '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-section { margin-bottom: 16px; }
</style>
