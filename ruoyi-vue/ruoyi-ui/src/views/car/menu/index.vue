<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="菜單名稱" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="請輸入菜單名稱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否顯示" prop="isShow">
        <el-select v-model="queryParams.isShow" placeholder="請選擇是否顯示" clearable size="small">
          <el-option label="顯示" value="1" />
          <el-option label="隱藏" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['car:menu:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-arrow-up"
          size="mini"
          :disabled="single"
          @click="handleMoveUp"
        >上移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-arrow-down"
          size="mini"
          :disabled="single"
          @click="handleMoveDown"
        >下移</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="!orderChanged"
          @click="handleSaveOrder"
        >保存排序</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['car:menu:edit']"
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
          v-hasPermi="['car:menu:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:menu:export']"
        >導出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="menuList" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="菜單名稱" align="center" prop="title" />
      <el-table-column label="是否顯示" align="center" prop="isShow" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isShow"
            :active-value="1"
            :inactive-value="0"
            @change="handleShowStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="是否可刪除" align="center" prop="canDel" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.canDel === 1 ? 'success' : 'danger'">
            {{ scope.row.canDel === 1 ? '可刪除' : '不可刪除' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="菜單類型" align="center" prop="linkType" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.linkType === 0 ? 'primary' : 'success'">
            {{ scope.row.linkType === 0 ? '鏈接' : '富文本' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="鏈接地址" align="center" prop="linkUrl" show-overflow-tooltip v-if="false" />
      <el-table-column label="內容" align="center" prop="content" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.linkType === 0">{{ scope.row.linkUrl }}</span>
          <span v-else-if="scope.row.linkType === 1" v-html="scope.row.content ? scope.row.content.substring(0, 50) + '...' : ''"></span>
        </template>
      </el-table-column>
      <el-table-column label="創建時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.canDel === 1"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.canDel === 1"
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

    <!-- 添加或修改菜單維護對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="菜單名稱" prop="title">
          <el-input v-model="form.title" placeholder="請輸入菜單名稱" />
        </el-form-item>
        <el-form-item label="顯示順序" prop="showOrder">
          <el-input-number v-model="form.showOrder" :min="0" :max="999" placeholder="請輸入顯示順序" />
        </el-form-item>
        <el-form-item label="是否顯示" prop="isShow">
          <el-radio-group v-model="form.isShow">
            <el-radio :label="1">顯示</el-radio>
            <el-radio :label="0">隱藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否可刪除" prop="canDel">
          <el-radio-group v-model="form.canDel">
            <el-radio :label="1">可刪除</el-radio>
            <el-radio :label="0">不可刪除</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜單類型" prop="linkType">
          <el-radio-group v-model="form.linkType" @change="handleLinkTypeChange">
            <el-radio :label="0">鏈接</el-radio>
            <el-radio :label="1">富文本</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="鏈接地址" prop="linkUrl" v-if="form.linkType === 0">
          <el-input v-model="form.linkUrl" type="textarea" placeholder="請輸入鏈接地址" />
        </el-form-item>
        <el-form-item label="富文本內容" prop="content" v-if="form.linkType === 1">
          <editor v-model="form.content" :min-height="400"/>
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
import { listMenu, getMenu, delMenu, addMenu, updateMenu, updateMenuShowStatus, updateMenuOrder, batchUpdateMenuOrder } from "@/api/car/menu";

export default {
  name: "Menu",
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
      // 菜單維護表格數據
      menuList: [],
      // 排序是否改變
      orderChanged: false,
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        isShow: null
      },
      // 表單參數
      form: {},
      // 表單校驗
      rules: {
        title: [
          { required: true, message: "菜單名稱不能為空", trigger: "blur" }
        ],
        showOrder: [
          { required: true, message: "顯示順序不能為空", trigger: "blur" }
        ],
        isShow: [
          { required: true, message: "是否顯示不能為空", trigger: "change" }
        ],
        canDel: [
          { required: true, message: "是否可刪除不能為空", trigger: "change" }
        ],
        linkType: [
          { required: true, message: "菜單類型不能為空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查詢菜單維護列表 */
    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = response.rows || [];
        this.total = response.total || 0;
        this.orderChanged = false; // 重置排序變更狀態
        this.loading = false;
      }).catch(error => {
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
        title: null,
        showOrder: 0,
        delFlag: null,
        createTime: null,
        isShow: 1,
        canDel: 1,
        linkUrl: null,
        linkType: 0,
        content: null
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
      this.title = "添加菜單維護";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMenu(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改菜單維護";
      });
    },
    /** 提交按鈕 */
    submitForm() {
      // 动态设置验证规则
      this.setDynamicRules();
      
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 设置动态验证规则 */
    setDynamicRules() {
      // 清除之前的动态规则
      this.$refs["form"].clearValidate(['linkUrl', 'content']);
      
      if (this.form.linkType === 0) {
        // 链接类型，验证链接地址
        this.rules.linkUrl = [
          { required: true, message: "鏈接地址不能為空", trigger: "blur" }
        ];
        this.rules.content = [];
      } else if (this.form.linkType === 1) {
        // 富文本类型，验证富文本内容
        this.rules.content = [
          { required: true, message: "富文本內容不能為空", trigger: "blur" }
        ];
        this.rules.linkUrl = [];
      }
    },
    /** 处理菜单类型变化 */
    handleLinkTypeChange(value) {
      // 清除相关字段的验证状态
      this.$refs["form"].clearValidate(['linkUrl', 'content']);
      
      // 不再清空字段内容，保留用户输入的数据
      // 这样用户可以在链接和富文本之间切换而不丢失数据
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否確認刪除菜單維護編號為"' + ids + '"的數據項？').then(function() {
        return delMenu(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 導出按鈕操作 */
    handleExport() {
      this.download('car/menu/export', {
        ...this.queryParams
      }, `menu_${new Date().getTime()}.xlsx`)
    },
    /** 顯示狀態修改 */
    handleShowStatusChange(row) {
      let text = row.isShow === 1 ? "啟用" : "停用";
      this.$modal.confirm('確認要"' + text + '""' + row.title + '"菜單嗎？').then(function() {
        return updateMenuShowStatus(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isShow = row.isShow === 0 ? 1 : 0;
      });
    },
    /** 上移操作 */
    handleMoveUp() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("請選擇一條記錄進行上移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.menuList.findIndex(item => item.id === selectedId);
      
      if (currentIndex <= 0) {
        this.$modal.msgWarning("已經是第一條記錄，無法上移");
        return;
      }
      
      // 交換當前記錄與上一條記錄的排序
      const currentItem = this.menuList[currentIndex];
      const prevItem = this.menuList[currentIndex - 1];
      
      // 交換排序值
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = prevItem.showOrder;
      prevItem.showOrder = tempOrder;
      
      // 重新排序陣列
      this.menuList.sort((a, b) => a.showOrder - b.showOrder);
      
      // 標記排序已改變
      this.orderChanged = true;
    },
    /** 下移操作 */
    handleMoveDown() {
      if (this.ids.length !== 1) {
        this.$modal.msgWarning("請選擇一條記錄進行下移操作");
        return;
      }
      
      const selectedId = this.ids[0];
      const currentIndex = this.menuList.findIndex(item => item.id === selectedId);
      
      if (currentIndex >= this.menuList.length - 1) {
        this.$modal.msgWarning("已經是最後一條記錄，無法下移");
        return;
      }
      
      // 交換當前記錄與下一條記錄的排序
      const currentItem = this.menuList[currentIndex];
      const nextItem = this.menuList[currentIndex + 1];
      
      // 交換排序值
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = nextItem.showOrder;
      nextItem.showOrder = tempOrder;
      
      // 重新排序陣列
      this.menuList.sort((a, b) => a.showOrder - b.showOrder);
      
      // 標記排序已改變
      this.orderChanged = true;
    },
    /** 保存排序 */
    handleSaveOrder() {
      if (!this.orderChanged) {
        this.$modal.msgWarning("沒有排序變更需要保存");
        return;
      }
      
      // 批量更新排序
      batchUpdateMenuOrder(this.menuList).then(() => {
        this.$modal.msgSuccess("排序保存成功");
        this.orderChanged = false;
      }).catch(() => {
        this.$modal.msgError("排序保存失敗");
      });
    }
  }
};
</script>
