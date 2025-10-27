<template>
  <div class="test-container">
    <h2>WangEditor 測試頁面</h2>
    <div class="editor-wrapper">
      <wang-editor 
        v-model="content" 
        :height="400" 
        :min-height="300"
        placeholder="請輸入測試內容..."
      />
    </div>
    <div class="content-display">
      <h3>編輯器內容：</h3>
      <div v-html="content"></div>
    </div>
  </div>
</template>

<script>
import WangEditor from '@/components/WangEditor'

export default {
  name: 'TestWangEditor',
  components: {
    WangEditor
  },
  data() {
    return {
      content: '<p>這是測試內容</p>'
    }
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px;
}

.editor-wrapper {
  margin-bottom: 20px;
}

.content-display {
  border: 1px solid #ddd;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 4px;
}
</style>
