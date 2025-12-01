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
            v-if="!scope.row.parentId || scope.row.parentId === 0"
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
                :disable-branch-nodes="true"
                :disabled="isFirstLevelCategory"
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
import { listProductCategory, getProductCategory, delProductCategory, addProductCategory, updateProductCategory, treeselect, treeselectFirstLevel } from "@/api/car/productCategory";
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
  computed: {
    /** 判断当前编辑的是否为一级类目 */
    isFirstLevelCategory() {
      // 如果是修改操作，且当前分类的parentId为0或null，则是一级类目
      if (this.form.id != undefined && (!this.form.parentId || this.form.parentId === 0)) {
        return true;
      }
      return false;
    }
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
      // 支持两种数据结构：TreeSelect（有label）和Entity（有categoryName）
      const label = node.label || node.categoryName || '';
      return {
        id: node.id,
        label: label,
        children: node.children
      };
    },
    /** 查詢商品目錄分類下拉樹結構 */
    getTreeselect() {
      // 使用新接口，只返回一级分类（不包含二级分类）
      treeselectFirstLevel().then(response => {
        this.categoryOptions = [];
        const category = { id: 0, label: '主類目', children: [] };
        // 后端已经返回只包含一级分类的列表
        const firstLevelCategories = response.data || [];
        // 将一级分类转换为treeselect组件需要的格式
        category.children = firstLevelCategories.map(item => ({
          id: item.id,
          label: item.label || item.categoryName || '',
          children: [] // 不显示二级分类
        }));
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
      // 如果是从工具栏点击新增按钮（row为null），默认上级为主目录
      // 如果是从列表行点击新增按钮，设置父级为当前行
      if (row != null && row.id) {
        // 检查层级限制：如果当前行是二级目录，不能再添加子分类
        if (row.parentId && row.parentId !== 0) {
          this.$modal.msgWarning("最多只能维护两层分类，无法继续添加子分类");
          return;
        }
        this.form.parentId = row.id;
      } else {
        // 工具栏新增按钮，默认上级为主目录
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
          // 层级限制验证：如果选择的父分类不是主目录，检查父分类是否已经是二级分类
          if (this.form.parentId && this.form.parentId !== 0) {
            // 从下拉选项树中查找父分类信息
            const parentCategory = this.findCategoryInOptions(this.form.parentId);
            if (parentCategory && parentCategory.parentId && parentCategory.parentId !== 0) {
              this.$modal.msgWarning("最多只能维护两层分类，无法继续添加子分类");
              return;
            }
            // 如果父分类在二级分类列表中（有children但children不为空），说明它已经有子分类，不能再添加
            // 这个检查由后端完成，前端只做基本验证
          }
          
          if (this.form.id != undefined) {
            // 修改时也要检查层级限制
            if (this.form.parentId && this.form.parentId !== 0) {
              const parentCategory = this.findCategoryInOptions(this.form.parentId);
              if (parentCategory && parentCategory.parentId && parentCategory.parentId !== 0) {
                this.$modal.msgWarning("最多只能维护两层分类，无法继续添加子分类");
                return;
              }
            }
            updateProductCategory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              // 后端验证失败时的错误处理
              if (error && error.msg) {
                this.$modal.msgError(error.msg);
              }
            });
          } else {
            addProductCategory(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              // 后端验证失败时的错误处理
              if (error && error.msg) {
                this.$modal.msgError(error.msg);
              }
            });
          }
        }
      });
    },
    /** 在下拉选项树中根据ID查找分类 */
    findCategoryInOptions(id) {
      const findInTree = (list) => {
        for (let item of list) {
          if (item.id === id) {
            return item;
          }
          if (item.children && item.children.length > 0) {
            const found = findInTree(item.children);
            if (found) return found;
          }
        }
        return null;
      };
      // 从categoryOptions中查找（包含主类目）
      if (this.categoryOptions && this.categoryOptions.length > 0) {
        return findInTree(this.categoryOptions);
      }
      return null;
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

