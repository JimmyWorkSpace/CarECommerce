<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="訂單號" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入訂單號"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      
      <el-form-item label="訂單狀態" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择訂單狀態" clearable size="small">
          <el-option
            v-for="dict in dict.type.order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="訂單類型" prop="orderType">
        <el-select v-model="queryParams.orderType" placeholder="请选择訂單類型" clearable size="small">
          <el-option
            v-for="dict in dict.type.order_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="收件人" prop="receiverName">
        <el-input
          v-model="queryParams.receiverName"
          placeholder="请输入收件人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件人電話" prop="receiverMobile">
        <el-input
          v-model="queryParams.receiverMobile"
          placeholder="请输入收件人電話"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重設</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['car:orderInfo:edit']"
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
          v-hasPermi="['car:orderInfo:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:orderInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="訂單號" align="center" prop="orderNo" />
      <el-table-column label="總價格" align="center" prop="totalPrice" />
      <el-table-column label="訂單狀態" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="收件人" align="center" prop="receiverName" />
      <el-table-column label="收件人電話" align="center" prop="receiverMobile" />
      <el-table-column label="物流單號" align="center" prop="logicNumber" />
      <el-table-column label="訂單類型" align="center" prop="orderType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_type" :value="scope.row.orderType"/>
        </template>
      </el-table-column>
      <el-table-column label="建立時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['car:orderInfo:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:orderInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:orderInfo:remove']"
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

    <!-- 添加或修改訂單信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="訂單號" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入訂單號" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户ID" prop="userId">
              <el-input v-model="form.userId" placeholder="请输入用户ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="總價格" prop="totalPrice">
              <el-input v-model="form.totalPrice" placeholder="请输入總價格" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="訂單狀態" prop="orderStatus">
              <el-select v-model="form.orderStatus" placeholder="请选择訂單狀態">
                <el-option
                  v-for="dict in dict.type.order_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收件人" prop="receiverName">
              <el-input v-model="form.receiverName" placeholder="请输入收件人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收件人電話" prop="receiverMobile">
              <el-input v-model="form.receiverMobile" placeholder="请输入收件人電話" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收件人地址" prop="receiverAddress">
          <el-input v-model="form.receiverAddress" type="textarea" placeholder="请输入收件人地址" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="物流單號" prop="logicNumber">
              <el-input v-model="form.logicNumber" placeholder="请输入物流單號" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="訂單類型" prop="orderType">
              <el-select v-model="form.orderType" placeholder="请选择訂單類型">
                <el-option
                  v-for="dict in dict.type.order_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.orderType === 2">
          <el-col :span="12">
            <el-form-item label="超商店舖編號" prop="cvsStoreID">
              <el-input v-model="form.cvsStoreID" placeholder="请输入超商店舖編號" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超商店舖名稱" prop="cvsStoreName">
              <el-input v-model="form.cvsStoreName" placeholder="请输入超商店舖名稱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.orderType === 2">
          <el-col :span="12">
            <el-form-item label="超商店舖地址" prop="cvsAddress">
              <el-input v-model="form.cvsAddress" placeholder="请输入超商店舖地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="超商店舖電話" prop="cvsTelephone">
              <el-input v-model="form.cvsTelephone" placeholder="请输入超商店舖電話" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.orderType === 2">
          <el-col :span="12">
            <el-form-item label="是否離島店鋪" prop="cvsOutSide">
              <el-radio-group v-model="form.cvsOutSide">
                <el-radio :label="0">本島</el-radio>
                <el-radio :label="1">離島</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 訂單详情对话框 -->
    <el-dialog title="訂單详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>訂單基本信息</span>
            </div>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="訂單號">{{ orderDetail.orderNo }}</el-descriptions-item>
              <el-descriptions-item label="用户ID">{{ orderDetail.userId }}</el-descriptions-item>
              <el-descriptions-item label="總價格">¥{{ orderDetail.totalPrice }}</el-descriptions-item>
              <el-descriptions-item label="訂單狀態">
                <dict-tag :options="dict.type.order_status" :value="orderDetail.orderStatus"/>
              </el-descriptions-item>
              <el-descriptions-item label="訂單類型">
                <dict-tag :options="dict.type.order_type" :value="orderDetail.orderType"/>
              </el-descriptions-item>
              <el-descriptions-item label="建立時間">{{ parseTime(orderDetail.createTime) }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>收货信息</span>
            </div>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="收件人">{{ orderDetail.receiverName }}</el-descriptions-item>
              <el-descriptions-item label="收件人電話">{{ orderDetail.receiverMobile }}</el-descriptions-item>
              <el-descriptions-item label="收件人地址" :span="2">{{ orderDetail.receiverAddress }}</el-descriptions-item>
              <el-descriptions-item label="物流單號">{{ orderDetail.logicNumber || '暂无' }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;" v-if="orderDetail.orderType === 2">
        <el-col :span="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>超商取貨信息</span>
            </div>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="超商店舖編號">{{ orderDetail.cvsStoreID }}</el-descriptions-item>
              <el-descriptions-item label="超商店舖名稱">{{ orderDetail.cvsStoreName }}</el-descriptions-item>
              <el-descriptions-item label="超商店舖地址" :span="2">{{ orderDetail.cvsAddress }}</el-descriptions-item>
              <el-descriptions-item label="超商店舖電話">{{ orderDetail.cvsTelephone }}</el-descriptions-item>
              <el-descriptions-item label="是否離島店鋪">
                <el-tag :type="orderDetail.cvsOutSide === 1 ? 'warning' : 'success'">
                  {{ orderDetail.cvsOutSide === 1 ? '離島' : '本島' }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>訂單商品详情</span>
            </div>
            <el-table :data="orderDetailList" border>
              <el-table-column label="产品ID" align="center" prop="productId" />
              <el-table-column label="产品名稱" align="center" prop="productName" />
              <el-table-column label="产品数量" align="center" prop="productAmount" />
              <el-table-column label="单价" align="center" prop="productPrice">
                <template slot-scope="scope">
                  ¥{{ scope.row.productPrice }}
                </template>
              </el-table-column>
              <el-table-column label="总价" align="center" prop="totalPrice">
                <template slot-scope="scope">
                  ¥{{ scope.row.totalPrice }}
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrderInfo, getOrderInfo, delOrderInfo, addOrderInfo, updateOrderInfo } from "@/api/car/orderInfo";
import { listOrderDetail } from "@/api/car/orderDetail";

export default {
  name: "OrderInfo",
  dicts: ['order_status', 'order_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 訂單信息表格数据
      orderInfoList: [],
      // 弹出层標題
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 訂單详情数据
      orderDetail: {},
      // 訂單详情列表
      orderDetailList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        orderStatus: null,
        orderType: null,
        receiverName: null,
        receiverMobile: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "訂單號不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        orderStatus: [
          { required: true, message: "訂單狀態不能为空", trigger: "change" }
        ],
        orderType: [
          { required: true, message: "訂單類型不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询訂單信息列表 */
    getList() {
      this.loading = true;
      listOrderInfo(this.queryParams).then(response => {
        this.orderInfoList = response.rows;
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
        orderNo: null,
        userId: null,
        totalPrice: null,
        delFlag: null,
        createTime: null,
        showOrder: null,
        orderStatus: null,
        receiverAddress: null,
        receiverName: null,
        receiverMobile: null,
        logicNumber: null,
        orderType: 1,
        cvsStoreID: null,
        cvsStoreName: null,
        cvsAddress: null,
        cvsTelephone: null,
        cvsOutSide: null
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
      this.title = "添加訂單信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getOrderInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改訂單信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrderInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrderInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
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
      this.$modal.confirm('是否确认刪除訂單信息编號为"' + ids + '"的数据项？').then(function() {
        return delOrderInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('car/orderInfo/export', {
        ...this.queryParams
      }, `orderInfo_${new Date().getTime()}.xlsx`)
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.orderDetail = row;
      this.detailOpen = true;
      // 查询訂單详情列表
      listOrderDetail({ orderId: row.id }).then(response => {
        this.orderDetailList = response.rows;
      });
    }
  }
};
</script>
