<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="菜單" prop="menuId">
        <el-select v-model="queryParams.menuId" placeholder="请选择菜單" clearable size="small">
          <el-option
            v-for="menu in menuList"
            :key="menu.id"
            :label="menu.title"
            :value="menu.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="問題" prop="question">
        <el-input
          v-model="queryParams.question"
          placeholder="请输入問題"
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
          v-hasPermi="['car:questionAnswer:add']"
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
          v-hasPermi="['car:questionAnswer:edit']"
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
          v-hasPermi="['car:questionAnswer:remove']"
        >刪除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['car:questionAnswer:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="questionAnswerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="菜單" align="center" prop="menuTitle" />
      <el-table-column label="問題" align="center" prop="question" show-overflow-tooltip />
      <!-- <el-table-column label="回答" align="center" prop="answer" show-overflow-tooltip /> -->
      <el-table-column label="排序" align="center" prop="showOrder" />
      <el-table-column label="建立時間" align="center" prop="createTime" width="180">
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
            v-hasPermi="['car:questionAnswer:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['car:questionAnswer:remove']"
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

    <!-- 添加或修改问答模块对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" :top="'10vh'" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜單" prop="menuId">
              <el-select v-model="form.menuId" placeholder="请选择菜單" style="width: 100%">
                <el-option
                  v-for="menu in menuList"
                  :key="menu.id"
                  :label="menu.title"
                  :value="menu.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="showOrder">
              <el-input-number v-model="form.showOrder" :min="0" placeholder="请输入排序" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="問題" prop="question">
          <el-input v-model="form.question" type="textarea" :rows="3" placeholder="请输入問題" />
        </el-form-item>
        <el-form-item label="回答" prop="answer">
          <editor v-model="form.answer" :min-height="300"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listQuestionAnswer, getQuestionAnswer, delQuestionAnswer, addQuestionAnswer, updateQuestionAnswer } from "@/api/car/questionAnswer";
import { listMenu } from "@/api/car/menu";

export default {
  name: "QuestionAnswer",
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
      // 问答模块表格数据
      questionAnswerList: [],
      // 菜單列表
      menuList: [],
      // 弹出层標題
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        menuId: null,
        question: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        menuId: [
          { required: true, message: "菜單不能为空", trigger: "change" }
        ],
        question: [
          { required: true, message: "問題不能为空", trigger: "blur" }
        ],
        answer: [
          { required: true, message: "回答不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getMenuList();
    this.getList();
  },
  methods: {
    /** 查询菜單列表（包括子菜单，扁平化显示） */
    getMenuList() {
      listMenu({}).then(response => {
        const treeList = response.data || [];
        // 将树状结构扁平化，提取所有菜单（包括子菜单）
        this.menuList = this.flattenMenuList(treeList);
      });
    },
    /** 将树状菜单列表扁平化 */
    flattenMenuList(menuList) {
      const result = [];
      const flatten = (menus) => {
        menus.forEach(menu => {
          // 添加当前菜单
          result.push({
            id: menu.id,
            title: menu.title
          });
          // 如果有子菜单，递归处理
          if (menu.children && menu.children.length > 0) {
            flatten(menu.children);
          }
        });
      };
      flatten(menuList);
      return result;
    },
    /** 查询问答模块列表 */
    getList() {
      this.loading = true;
      listQuestionAnswer(this.queryParams).then(response => {
        this.questionAnswerList = response.rows;
        this.total = response.total;
        this.loading = false;
        
        // 为每个问答项添加菜單標題
        this.questionAnswerList.forEach(item => {
          const menu = this.menuList.find(m => m.id === item.menuId);
          item.menuTitle = menu ? menu.title : '未知菜單';
        });
      });
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
      this.title = "添加问答";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getQuestionAnswer(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改问答";
      });
    },
    /** 刪除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认刪除问答编號为"' + ids + '"的数据项？').then(function() {
        return delQuestionAnswer(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("刪除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('car/questionAnswer/export', {
        ...this.queryParams
      }, `questionAnswer_${new Date().getTime()}.xlsx`)
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
        menuId: null,
        question: null,
        answer: null,
        showOrder: 0
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateQuestionAnswer(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addQuestionAnswer(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>