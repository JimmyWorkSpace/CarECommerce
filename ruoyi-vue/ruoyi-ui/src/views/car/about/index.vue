<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <!-- <el-form-item label="標題" prop="title">
        <el-input v-model="form.title" placeholder="請輸入標題" />
      </el-form-item> -->
      <el-form-item label="內容" prop="content">
        <editor v-model="form.content" :min-height="300"/>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm" :loading="loading">保 存</el-button>
    </div>
  </div>
</template>

<script>
import { getAbout, updateAbout } from "@/api/car/about";

export default {
  name: "About",
  data() {
    return {
      // 表單參數
      form: {
        id: null,
        title: null,
        content: null,
        contentType: 1
      },
      // 表單校驗
      rules: {
        title: [
          { required: true, message: "標題不能為空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "內容不能為空", trigger: "blur" }
        ]
      },
      // 加載狀態
      loading: false
    };
  },
  created() {
    this.getAboutData();
  },
  methods: {
    /** 獲取關於內容 */
    getAboutData() {
      getAbout().then(response => {
        this.form = response.data || this.form;
      });
    },
    /** 提交表單 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.loading = true;
          updateAbout(this.form).then(response => {
            this.$modal.msgSuccess("保存成功");
            this.loading = false;
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    }
  }
};
</script>
