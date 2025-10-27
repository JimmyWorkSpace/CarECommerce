<template>
  <div class="wang-editor-container">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editor"
      :defaultConfig="toolbarConfig"
      mode="default"
    />
    <Editor
      :style="{ height: height + 'px', minHeight: minHeight + 'px' }"
      v-model="html"
      :defaultConfig="editorConfig"
      mode="default"
      @onCreated="onCreated"
      @onChange="onChange"
    />
  </div>
</template>

<script>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { i18nAddResources, i18nChangeLanguage } from '@wangeditor/editor'
import { getToken } from "@/utils/auth";

// 添加繁体中文语言资源
i18nAddResources('zh-TW', {
  common: {
    ok: '確定',
    delete: '刪除',
    enter: '輸入'
  },
  blockQuote: {
    title: '引用'
  },
  codeBlock: {
    title: '程式碼區塊'
  },
  color: {
    color: '文字顏色',
    bgColor: '背景顏色',
    default: '預設',
    clear: '清除'
  },
  divider: {
    title: '分割線'
  },
  emotion: {
    title: '表情'
  },
  fontSize: {
    title: '字體大小',
    default: '預設'
  },
  fontFamily: {
    title: '字體',
    default: '預設'
  },
  fullScreen: {
    title: '全螢幕'
  },
  header: {
    title: '標題',
    text: '正文'
  },
  image: {
    netImage: '網路圖片',
    delete: '刪除',
    edit: '編輯',
    viewLink: '查看連結',
    src: '圖片地址',
    desc: '圖片描述',
    link: '連結'
  },
  indent: {
    decrease: '減少縮排',
    increase: '增加縮排'
  },
  justify: {
    left: '左對齊',
    right: '右對齊',
    center: '置中對齊',
    justify: '兩端對齊'
  },
  lineHeight: {
    title: '行高',
    default: '預設'
  },
  link: {
    insert: '插入連結',
    text: '連結文字',
    url: '連結地址',
    unLink: '取消連結',
    edit: '編輯連結',
    view: '查看連結'
  },
  textStyle: {
    bold: '粗體',
    clear: '清除格式',
    code: '程式碼',
    italic: '斜體',
    sub: '下標',
    sup: '上標',
    through: '刪除線',
    underline: '底線'
  },
  undo: {
    undo: '撤銷',
    redo: '重做'
  },
  todo: {
    todo: '待辦事項'
  }
})

// 切換為繁體中文
i18nChangeLanguage('zh-TW')

export default {
  name: 'WangEditor',
  components: {
    Editor,
    Toolbar
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    height: {
      type: Number,
      default: 300
    },
    minHeight: {
      type: Number,
      default: 200
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: '请输入内容...'
    }
  },
  data() {
    return {
      editor: null,
      html: this.value,
      toolbarConfig: {
        toolbarKeys: [
          'headerSelect',
          'bold',
          'italic',
          'underline',
          'through',
          '|',
          'color',
          'bgColor',
          '|',
          'fontSize',
          'fontFamily',
          '|',
          'bulletedList',
          'numberedList',
          '|',
          'insertLink',
          'insertImage',
          'insertTable',
          'codeBlock',
          '|',
          'undo',
          'redo',
          '|',
          'fullScreen'
        ]
      },
      editorConfig: {
        placeholder: this.placeholder,
        readOnly: this.readOnly,
        MENU_CONF: {
          uploadImage: {
            server: process.env.VUE_APP_BASE_API + '/common/upload',
            fieldName: 'file',
            headers: {
              Authorization: 'Bearer ' + getToken()
            },
            allowedFileTypes: ['image/*'],
            maxFileSize: 5 * 1024 * 1024, // 5M
            customInsert: (res, insertFn) => {
              // 自定义插入圖片
              if (res.code === 200) {
                const url = process.env.VUE_APP_BASE_API + res.fileName
                insertFn(url, '', '')
              } else {
                this.$message.error(res.msg || '上传失败')
              }
            }
          }
        }
      }
    }
  },
  watch: {
    value: {
      handler(newVal) {
        if (newVal !== this.html) {
          this.html = newVal
        }
      },
      immediate: true
    }
  },
  beforeDestroy() {
    if (this.editor) {
      this.editor.destroy()
    }
  },
  methods: {
    onCreated(editor) {
      this.editor = editor
      console.log('WangEditor创建成功:', editor)
    },
    onChange(editor) {
      const html = editor.getHtml()
      this.html = html
      this.$emit('input', html)
      this.$emit('change', html)
    }
  }
}
</script>

<style scoped>
.wang-editor-container {
  border: 1px solid #ccc;
  border-radius: 4px;
  overflow: hidden;
}

/* 确保工具栏和编辑器正确显示 */
.wang-editor-container .w-e-toolbar {
  border-bottom: 1px solid #ccc;
  background-color: #fafafa;
}

.wang-editor-container .w-e-text-container {
  border: none;
  min-height: 200px;
}

/* 字体大小选项 */
.wang-editor-container .w-e-text-container .w-e-text-placeholder {
  color: #999;
  font-style: italic;
}

/* 确保编辑器内容区域正确显示 */
.wang-editor-container .w-e-text {
  min-height: 200px;
  padding: 10px;
}
</style>