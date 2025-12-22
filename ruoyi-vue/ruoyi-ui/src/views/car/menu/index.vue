<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="選單名稱" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="請輸入選單名稱"
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
          v-hasPermi="['car:menu:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table 
      v-loading="loading" 
      :data="menuList" 
      style="width: 100%"
      row-key="id"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      default-expand-all
    >
      <el-table-column label="選單名稱" align="left" prop="title" />
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
      <el-table-column label="選單類型" align="center" prop="linkType" width="100">
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
      <el-table-column label="建立時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-up"
            @click="handleMoveUp(scope.row)"
            :disabled="!canMoveUp(scope.row)"
          >上移</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-arrow-down"
            @click="handleMoveDown(scope.row)"
            :disabled="!canMoveDown(scope.row)"
          >下移</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-line-round"
            @click="handleQuestionAnswer(scope.row)"
            v-if="scope.row.linkType === 1"
          >問答</el-button>
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
    

    <!-- 添加或修改選單維護對話框 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="父菜單" prop="parentId">
          <el-select v-model="form.parentId" placeholder="請選擇父菜單（不選則為頂級菜單）" clearable style="width: 100%">
            <el-option label="無（頂級菜單）" :value="null" />
            <el-option
              v-for="item in filteredParentMenuOptions"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="選單名稱" prop="title">
          <el-input v-model="form.title" placeholder="請輸入選單名稱" />
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
        <el-form-item label="選單類型" prop="linkType">
          <el-radio-group v-model="form.linkType" @change="handleLinkTypeChange">
            <el-radio :label="0">鏈接</el-radio>
            <el-radio :label="1">富文本</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="鏈接地址" prop="linkUrl" v-if="form.linkType === 0">
          <el-input v-model="form.linkUrl" type="textarea" placeholder="請輸入鏈接地址" />
        </el-form-item>
        <el-form-item label="富文本內容" prop="content" v-if="form.linkType === 1">
          <quill-editor v-model="form.content" :min-height="400"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">確 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 問答管理對話框 -->
    <el-dialog :title="qaTitle" :visible.sync="qaOpen" width="80%" :top="'5vh'" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleQaAdd"
          >新增問答</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="qaSingle"
            @click="handleQaUpdate"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="qaMultiple"
            @click="handleQaDelete"
          >刪除</el-button>
        </el-col>
      </el-row>

      <el-table v-loading="qaLoading" :data="qaList" @selection-change="handleQaSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="問題" align="center" prop="question" show-overflow-tooltip />
        <el-table-column label="排序" align="center" prop="showOrder" width="100" />
        <el-table-column label="建立時間" align="center" prop="createTime" width="180">
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
              @click="handleQaUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleQaDelete(scope.row)"
            >刪除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 問答表單對話框 -->
      <el-dialog :title="qaFormTitle" :visible.sync="qaFormOpen" width="70%" :top="'10vh'" append-to-body>
        <el-form ref="qaForm" :model="qaForm" :rules="qaRules" label-width="80px">
          <el-form-item label="排序" prop="showOrder">
            <el-input-number v-model="qaForm.showOrder" :min="0" placeholder="請輸入排序" style="width: 100%" />
          </el-form-item>
          <el-form-item label="問題" prop="question">
            <el-input v-model="qaForm.question" type="textarea" :rows="3" placeholder="請輸入問題" />
          </el-form-item>
          <el-form-item label="回答" prop="answer">
            <quill-editor v-model="qaForm.answer" :min-height="300"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitQaForm">確 定</el-button>
          <el-button @click="cancelQaForm">取 消</el-button>
        </div>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script>
import { listMenu, getMenu, delMenu, addMenu, updateMenu, updateMenuShowStatus, updateMenuOrder, batchUpdateMenuOrder, listParentMenu } from "@/api/car/menu";
import { listQuestionAnswer, getQuestionAnswer, delQuestionAnswer, addQuestionAnswer, updateQuestionAnswer } from "@/api/car/questionAnswer";
import QuillEditor from "@/components/QuillEditor";

export default {
  name: "Menu",
  components: {
    QuillEditor
  },
  data() {
    return {
      // 遮罩層
      loading: true,
      // 顯示搜索條件
      showSearch: true,
      // 總條數
      total: 0,
      // 選單維護表格數據
      menuList: [],
      // 父菜單選項
      parentMenuOptions: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 問答相關
      qaOpen: false,
      qaTitle: "",
      qaLoading: false,
      qaList: [],
      qaIds: [],
      qaSingle: true,
      qaMultiple: true,
      currentMenuId: null,
      qaFormOpen: false,
      qaFormTitle: "",
      qaForm: {},
      qaRules: {
        question: [
          { required: true, message: "問題不能為空", trigger: "blur" }
        ],
        answer: [
          { required: true, message: "回答不能為空", trigger: "blur" }
        ]
      },
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
          { required: true, message: "選單名稱不能為空", trigger: "blur" }
        ],
        showOrder: [
          { required: true, message: "顯示順序不能為空", trigger: "blur" }
        ],
        isShow: [
          { required: true, message: "是否顯示不能為空", trigger: "change" }
        ],
        linkType: [
          { required: true, message: "選單類型不能為空", trigger: "change" }
        ]
      }
    };
  },
  computed: {
    // 過濾父菜單選項，排除當前編輯的菜單及其子菜單
    filteredParentMenuOptions() {
      if (!this.form.id) {
        // 新增時，返回所有父菜單
        return this.parentMenuOptions;
      }
      // 修改時，排除當前菜單及其子菜單
      const excludeIds = this.getMenuAndChildrenIds(this.form.id, this.menuList);
      return this.parentMenuOptions.filter(item => !excludeIds.includes(item.id));
    }
  },
  created() {
    this.getList();
    this.getParentMenuList();
  },
  methods: {
    // 獲取菜單及其所有子菜單的ID（遞歸處理樹狀結構）
    getMenuAndChildrenIds(menuId, menuList) {
      const ids = [menuId];
      const findChildren = (parentId, list) => {
        list.forEach(menu => {
          if (menu.id === parentId && menu.children && menu.children.length > 0) {
            menu.children.forEach(child => {
              ids.push(child.id);
              if (child.children && child.children.length > 0) {
                findChildren(child.id, [child]);
              }
            });
          }
        });
      };
      findChildren(menuId, menuList);
      return ids;
    },
    /** 查詢選單維護列表 */
    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = response.data || [];
        this.total = this.menuList.length;
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      });
    },
    /** 查詢父菜單列表 */
    getParentMenuList() {
      listParentMenu().then(response => {
        this.parentMenuOptions = response.data || [];
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
        content: null,
        parentId: null
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
    /** 新增按鈕操作 */
    handleAdd() {
      this.reset();
      // 重新加载父菜单列表，确保数据是最新的
      this.getParentMenuList();
      this.open = true;
      this.title = "添加選單維護";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getMenu(id).then(response => {
        this.form = response.data;
        // 重新加载父菜单列表，确保数据是最新的
        this.getParentMenuList();
        this.open = true;
        this.title = "修改選單維護";
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
        // 链接類型，验证链接地址
        this.rules.linkUrl = [
          { required: true, message: "鏈接地址不能為空", trigger: "blur" }
        ];
        this.rules.content = [];
      } else if (this.form.linkType === 1) {
        // 富文本類型，验证富文本内容
        this.rules.content = [
          { required: true, message: "富文本內容不能為空", trigger: "blur" }
        ];
        this.rules.linkUrl = [];
      }
    },
    /** 处理菜单類型变化 */
    handleLinkTypeChange(value) {
      // 清除相关字段的验证狀態
      this.$refs["form"].clearValidate(['linkUrl', 'content']);
      
      // 不再清空字段内容，保留用户输入的数据
      // 这样用户可以在链接和富文本之间切换而不丢失数据
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否確認刪除選單維護編號為"' + id + '"的數據項？').then(() => {
        return delMenu(id);
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
      this.$modal.confirm('確認要"' + text + '""' + row.title + '"選單嗎？').then(function() {
        return updateMenuShowStatus(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isShow = row.isShow === 0 ? 1 : 0;
      });
    },
    /** 獲取所有菜單（扁平化） */
    getAllMenus(menuList) {
      const result = [];
      const flatten = (menus) => {
        menus.forEach(menu => {
          result.push(menu);
          if (menu.children && menu.children.length > 0) {
            flatten(menu.children);
          }
        });
      };
      flatten(menuList);
      return result;
    },
    /** 獲取同級菜單列表 */
    getSiblingMenus(menu) {
      const allMenus = this.getAllMenus(this.menuList);
      const parentId = menu.parentId || null;
      return allMenus.filter(m => {
        const mParentId = m.parentId || null;
        return mParentId === parentId && m.id !== menu.id;
      }).sort((a, b) => a.showOrder - b.showOrder);
    },
    /** 判斷是否可以上移 */
    canMoveUp(menu) {
      const siblings = this.getSiblingMenus(menu);
      if (siblings.length === 0) return false;
      const sortedSiblings = siblings.concat([menu]).sort((a, b) => a.showOrder - b.showOrder);
      return sortedSiblings[0].id !== menu.id;
    },
    /** 判斷是否可以下移 */
    canMoveDown(menu) {
      const siblings = this.getSiblingMenus(menu);
      if (siblings.length === 0) return false;
      const sortedSiblings = siblings.concat([menu]).sort((a, b) => a.showOrder - b.showOrder);
      return sortedSiblings[sortedSiblings.length - 1].id !== menu.id;
    },
    /** 上移操作 */
    handleMoveUp(row) {
      const siblings = this.getSiblingMenus(row);
      if (siblings.length === 0) {
        this.$modal.msgWarning("已經是第一條記錄，無法上移");
        return;
      }
      
      // 獲取排序後的同級菜單（包括當前菜單）
      const sortedSiblings = siblings.concat([row]).sort((a, b) => a.showOrder - b.showOrder);
      const currentIndex = sortedSiblings.findIndex(m => m.id === row.id);
      
      if (currentIndex <= 0) {
        this.$modal.msgWarning("已經是第一條記錄，無法上移");
        return;
      }
      
      // 交換當前記錄與上一條記錄的排序
      const currentItem = sortedSiblings[currentIndex];
      const prevItem = sortedSiblings[currentIndex - 1];
      
      // 交換排序值
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = prevItem.showOrder;
      prevItem.showOrder = tempOrder;
      
      // 立即保存排序
      this.saveMenuOrder([currentItem, prevItem]);
    },
    /** 下移操作 */
    handleMoveDown(row) {
      const siblings = this.getSiblingMenus(row);
      if (siblings.length === 0) {
        this.$modal.msgWarning("已經是最後一條記錄，無法下移");
        return;
      }
      
      // 獲取排序後的同級菜單（包括當前菜單）
      const sortedSiblings = siblings.concat([row]).sort((a, b) => a.showOrder - b.showOrder);
      const currentIndex = sortedSiblings.findIndex(m => m.id === row.id);
      
      if (currentIndex >= sortedSiblings.length - 1) {
        this.$modal.msgWarning("已經是最後一條記錄，無法下移");
        return;
      }
      
      // 交換當前記錄與下一條記錄的排序
      const currentItem = sortedSiblings[currentIndex];
      const nextItem = sortedSiblings[currentIndex + 1];
      
      // 交換排序值
      const tempOrder = currentItem.showOrder;
      currentItem.showOrder = nextItem.showOrder;
      nextItem.showOrder = tempOrder;
      
      // 立即保存排序
      this.saveMenuOrder([currentItem, nextItem]);
    },
    /** 保存菜單排序 */
    saveMenuOrder(menus) {
      // 批量更新排序
      batchUpdateMenuOrder(menus).then(() => {
        this.$modal.msgSuccess("排序保存成功");
        // 重新加載列表以刷新顯示
        this.getList();
      }).catch(() => {
        this.$modal.msgError("排序保存失敗");
        // 重新加載列表以恢復原狀
        this.getList();
      });
    },
    /** 問答按鈕操作 */
    handleQuestionAnswer(row) {
      this.currentMenuId = row.id;
      this.qaTitle = `問答管理 - ${row.title}`;
      this.qaOpen = true;
      this.getQaList();
    },
    /** 查詢問答列表 */
    getQaList() {
      this.qaLoading = true;
      const query = { menuId: this.currentMenuId };
      listQuestionAnswer(query).then(response => {
        this.qaList = response.rows || [];
        this.qaLoading = false;
      }).catch(() => {
        this.qaLoading = false;
      });
    },
    // 問答多選框選中數據
    handleQaSelectionChange(selection) {
      this.qaIds = selection.map(item => item.id);
      this.qaSingle = selection.length !== 1;
      this.qaMultiple = !selection.length;
    },
    /** 新增問答按鈕操作 */
    handleQaAdd() {
      this.resetQaForm();
      this.qaFormOpen = true;
      this.qaFormTitle = "添加問答";
    },
    /** 修改問答按鈕操作 */
    handleQaUpdate(row) {
      this.resetQaForm();
      const id = row ? row.id : this.qaIds[0];
      getQuestionAnswer(id).then(response => {
        this.qaForm = response.data;
        this.qaFormOpen = true;
        this.qaFormTitle = "修改問答";
      });
    },
    /** 刪除問答按鈕操作 */
    handleQaDelete(row) {
      const ids = row ? row.id : this.qaIds;
      this.$modal.confirm('是否確認刪除問答編號為"' + ids + '"的數據項？').then(() => {
        return delQuestionAnswer(ids);
      }).then(() => {
        this.getQaList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    // 問答表單重置
    resetQaForm() {
      this.qaForm = {
        id: null,
        menuId: this.currentMenuId,
        question: null,
        answer: null,
        showOrder: 0
      };
      if (this.$refs.qaForm) {
        this.$refs.qaForm.resetFields();
      }
    },
    // 取消問答表單
    cancelQaForm() {
      this.qaFormOpen = false;
      this.resetQaForm();
    },
    /** 提交問答表單 */
    submitQaForm() {
      this.$refs["qaForm"].validate(valid => {
        if (valid) {
          if (this.qaForm.id != null) {
            updateQuestionAnswer(this.qaForm).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.qaFormOpen = false;
              this.getQaList();
            });
          } else {
            addQuestionAnswer(this.qaForm).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.qaFormOpen = false;
              this.getQaList();
            });
          }
        }
      });
    }
  }
};
</script>
