<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品標題" prop="productTitle">
        <el-input
          v-model="queryParams.productTitle"
          placeholder="請輸入商品標題"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分類" prop="categoryId">
        <treeselect
          v-model="queryParams.categoryId"
          :options="categoryOptions"
          :normalizer="normalizer"
          :show-count="true"
          placeholder="請選擇分類"
          clearable
          style="width: 200px"
          class="treeselect-small"
        />
      </el-form-item>
      <el-form-item label="上架狀態" prop="onSale">
        <el-select v-model="queryParams.onSale" placeholder="請選擇上架狀態" clearable size="small">
          <el-option label="已上架" :value="1" />
          <el-option label="未上架" :value="0" />
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
          v-hasPermi="['car:product:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['car:product:edit']"
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
          v-hasPermi="['car:product:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:product:export']"
        >導出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商品標題" align="center" prop="productTitle" :show-overflow-tooltip="true" />
      <el-table-column label="分類" align="center" prop="categoryName" width="120" />
      <el-table-column label="供價" align="center" prop="supplyPrice" width="100" />
      <el-table-column label="售價" align="center" prop="salePrice" width="100" />
      <el-table-column label="數量" align="center" prop="amount" width="80" />
      <el-table-column label="上架狀態" align="center" prop="onSale" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.onSale === 1 ? 'success' : 'info'">
            {{ scope.row.onSale === 1 ? '已上架' : '未上架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['car:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:product:remove']"
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

    <!-- 添加或修改商品對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品標題" prop="productTitle">
              <el-input v-model="form.productTitle" placeholder="請輸入商品標題" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分類" prop="categoryId">
              <treeselect
                v-model="form.categoryId"
                :options="categoryOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="請選擇分類"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="簡要介紹" prop="productDespShort">
              <el-input v-model="form.productDespShort" type="textarea" :rows="2" placeholder="請輸入簡要介紹" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="供價" prop="supplyPrice">
              <el-input-number v-model="form.supplyPrice" :precision="2" :min="0" :step="0.01" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="售價" prop="salePrice">
              <el-input-number v-model="form.salePrice" :precision="2" :min="0" :step="0.01" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="數量" prop="amount">
              <el-input-number v-model="form.amount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上架狀態" prop="onSale">
              <el-radio-group v-model="form.onSale">
                <el-radio :label="1">已上架</el-radio>
                <el-radio :label="0">未上架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="標籤" prop="productTags">
              <el-input v-model="form.productTags" placeholder="請輸入標籤，多個標籤用逗號分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="詳細介紹" prop="productDesp">
              <editor v-model="form.productDesp" :min-height="300"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品圖片">
              <el-upload
                ref="upload"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :file-list="imageList"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :on-success="handleUploadSuccess"
                :before-upload="handleBeforeUpload"
                :auto-upload="true"
                :data="{ productId: form.id || 0 }"
                multiple
              >
                <i class="el-icon-plus"></i>
              </el-upload>
              <el-dialog :visible.sync="dialogVisible" title="圖片預覽" width="800px">
                <img :src="dialogImageUrl" style="width: 100%" />
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品屬性">
              <el-input
                v-model="attrText"
                type="textarea"
                :rows="6"
                placeholder="請輸入商品屬性，格式：屬性名:屬性值，每行一個屬性，例如：&#10;顏色:紅色&#10;尺寸:大號&#10;材質:純棉"
              />
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
import { listProduct, getProduct, delProduct, addProduct, updateProduct, uploadProductImages, deleteProductImage, saveProductAttrs } from "@/api/car/product";
import { treeselect } from "@/api/car/productCategory";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { getToken } from "@/utils/auth";

export default {
  name: "Product",
  components: { Treeselect },
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
      // 商品表格數據
      productList: [],
      // 分類樹選項
      categoryOptions: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 查詢參數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productTitle: null,
        categoryId: null,
        onSale: null
      },
      // 表單參數
      form: {},
      // 表單校驗
      rules: {
        productTitle: [
          { required: true, message: "商品標題不能為空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "數量不能為空", trigger: "blur" }
        ]
      },
      // 圖片上傳相關
      uploadUrl: process.env.VUE_APP_BASE_API + "/car/product/uploadImages",
      uploadHeaders: {
        Authorization: "Bearer " + getToken()
      },
      imageList: [],
      dialogVisible: false,
      dialogImageUrl: "",
      // 屬性文本
      attrText: ""
    };
  },
  created() {
    this.getList();
    this.getCategoryTree();
  },
  methods: {
    /** 查詢商品列表 */
    getList() {
      this.loading = true;
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查詢分類樹 */
    getCategoryTree() {
      treeselect().then(response => {
        this.categoryOptions = [];
        const category = { id: 0, label: '主類目', children: [] };
        // 后端返回的是TreeSelect结构，已经包含label和children字段
        const allCategories = response.data || [];
        // 递归转换TreeSelect结构为treeselect组件需要的格式
        const convertTree = (items) => {
          if (!items || items.length === 0) return [];
          return items.map(item => ({
            id: item.id,
            label: item.label || item.categoryName || '',
            children: item.children ? convertTree(item.children) : []
          }));
        };
        category.children = convertTree(allCategories);
        this.categoryOptions.push(category);
      });
    },
    /** 轉換分類數據結構 */
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
    // 取消按鈕
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表單重置
    reset() {
      this.form = {
        id: null,
        productTitle: null,
        productDespShort: null,
        productDesp: null,
        productTags: null,
        supplyPrice: null,
        salePrice: null,
        amount: 0,
        categoryId: null,
        onSale: 0,
        delFlag: 0
      };
      this.imageList = [];
      this.attrText = "";
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按鈕操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加商品";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getProduct(id).then(response => {
        this.form = response.data;
        // 處理圖片列表
        if (this.form.images && this.form.images.length > 0) {
          this.imageList = this.form.images.map(img => ({
            id: img.id,
            name: img.imageUrl,
            url: img.fullImageUrl || img.imageUrl
          }));
        } else {
          this.imageList = [];
        }
        // 處理屬性文本
        if (this.form.attrs && this.form.attrs.length > 0) {
          this.attrText = this.form.attrs.map(attr => `${attr.attrName}:${attr.attrValue}`).join('\n');
        }
        this.open = true;
        this.title = "修改商品";
      });
    },
    /** 提交按鈕 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProduct(this.form).then(response => {
              // 保存商品屬性
              if (this.form.id && this.attrText !== undefined) {
                saveProductAttrs(this.form.id, this.attrText).catch(() => {
                  // 屬性保存失敗不影響主流程
                });
              }
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProduct(this.form).then(response => {
              if (response.data && response.data.id) {
                this.form.id = response.data.id;
                // 保存商品屬性
                if (this.attrText !== undefined) {
                  saveProductAttrs(this.form.id, this.attrText).catch(() => {
                    // 屬性保存失敗不影響主流程
                  });
                }
              }
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
      this.$modal.confirm('是否確認刪除商品編號為"' + ids + '"的數據項？').then(function() {
        return delProduct(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 導出按鈕操作 */
    handleExport() {
      this.download('car/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`);
    },
    /** 圖片上傳前檢查 */
    handleBeforeUpload(file) {
      if (!this.form.id) {
        this.$modal.msgWarning("請先保存商品信息");
        return false;
      }
      const isImage = file.type.indexOf('image') !== -1;
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isImage) {
        this.$modal.msgError('只能上傳圖片文件！');
        return false;
      }
      if (!isLt10M) {
        this.$modal.msgError('圖片大小不能超過10MB！');
        return false;
      }
      // 更新上傳參數中的productId
      this.$refs.upload.data = { productId: this.form.id };
      return true;
    },
    /** 圖片上傳成功 */
    handleUploadSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.$modal.msgSuccess("圖片上傳成功");
        // 重新獲取商品信息以更新圖片列表
        if (this.form.id) {
          getProduct(this.form.id).then(res => {
            if (res.data && res.data.images) {
              this.imageList = res.data.images.map(img => ({
                id: img.id,
                name: img.imageUrl,
                url: img.fullImageUrl || img.imageUrl
              }));
            }
          });
        }
      } else {
        this.$modal.msgError(response.msg || "圖片上傳失敗");
      }
    },
    /** 刪除圖片 */
    handleRemove(file, fileList) {
      if (file.id) {
        deleteProductImage(file.id).then(() => {
          this.$modal.msgSuccess("圖片刪除成功");
          this.imageList = fileList;
        });
      } else {
        this.imageList = fileList;
      }
    },
    /** 圖片預覽 */
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    }
  }
};
</script>

<style scoped>
.treeselect-small ::v-deep .vue-treeselect__control {
  height: 32px;
  line-height: 32px;
}

.treeselect-small ::v-deep .vue-treeselect__placeholder,
.treeselect-small ::v-deep .vue-treeselect__single-value {
  line-height: 32px;
}
</style>

