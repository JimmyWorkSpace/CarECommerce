<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="分類名稱" prop="categoryName">
        <el-input
          v-model="queryParams.categoryName"
          placeholder="請輸入分類名稱"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['car:productCategory:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展開/摺疊</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="categoryList"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="categoryName" label="分類名稱" :show-overflow-tooltip="true" width="260"></el-table-column>
      <el-table-column prop="parentName" label="上級分類" :show-overflow-tooltip="true" width="200"></el-table-column>
      <el-table-column prop="showOrder" label="排序" width="100"></el-table-column>
      <el-table-column label="建立時間" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button 
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:productCategory:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['car:productCategory:add']"
          >新增</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:productCategory:remove']"
          >刪除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改商品目錄分類對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上級分類">
              <treeselect
                v-model="form.parentId"
                :options="categoryOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="選擇上級分類"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分類名稱" prop="categoryName">
              <el-input v-model="form.categoryName" placeholder="請輸入分類名稱" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="顯示排序" prop="showOrder">
              <el-input-number v-model="form.showOrder" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">確 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProductCategory, getProductCategory, delProductCategory, addProductCategory, updateProductCategory, treeselect } from "@/api/car/productCategory";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "ProductCategory",
  components: { Treeselect },
  data() {
    return {
      // 遮罩層
      loading: true,
      // 顯示搜索條件
      showSearch: true,
      // 商品目錄分類表格樹數據
      categoryList: [],
      // 商品目錄分類樹選項
      categoryOptions: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 是否展開，默認全部摺疊
      isExpandAll: false,
      // 重新渲染表格狀態
      refreshTable: true,
      // 查詢參數
      queryParams: {
        categoryName: undefined
      },
      // 表單參數
      form: {},
      // 表單校驗
      rules: {
        categoryName: [
          { required: true, message: "分類名稱不能為空", trigger: "blur" }
        ],
        showOrder: [
          { required: true, message: "顯示排序不能為空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查詢商品目錄分類列表 */
    getList() {
      this.loading = true;
      listProductCategory(this.queryParams).then(response => {
        this.categoryList = this.handleTree(response.rows, "id", "parentId");
        this.loading = false;
      });
    },
    /** 轉換商品目錄分類數據結構 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.categoryName,
        children: node.children
      };
    },
    /** 查詢商品目錄分類下拉樹結構 */
    getTreeselect() {
      treeselect().then(response => {
        this.categoryOptions = [];
        const category = { id: 0, categoryName: '主類目', children: [] };
        category.children = this.handleTree(response.data, "id", "parentId");
        this.categoryOptions.push(category);
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
        id: undefined,
        parentId: 0,
        categoryName: undefined,
        showOrder: 0,
        delFlag: 0
      };
      this.resetForm("form");
    },
    /** 搜索按鈕操作 */
    handleQuery() {
      this.getList();
    },
    /** 重設按鈕操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按鈕操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.id) {
        this.form.parentId = row.id;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加商品目錄分類";
    },
    /** 展開/摺疊操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getProductCategory(row.id).then(response => {
        this.form = response.data;
        if (this.form.parentId == null) {
          this.form.parentId = 0;
        }
        this.open = true;
        this.title = "修改商品目錄分類";
      });
    },
    /** 提交按鈕 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateProductCategory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProductCategory(this.form).then(response => {
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
      this.$modal.confirm('是否確認刪除名稱為"' + row.categoryName + '"的數據項？').then(function() {
        return delProductCategory(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    }
  }
};
</script>

