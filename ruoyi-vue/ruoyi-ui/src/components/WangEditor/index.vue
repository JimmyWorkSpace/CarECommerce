<template>
  <div class="wang-editor-container">
    <el-upload
      :action="uploadUrl"
      :before-upload="handleBeforeUpload"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      name="file"
      :show-file-list="false"
      :headers="headers"
      :data="uploadData"
      style="display: none"
      ref="upload"
    >
    </el-upload>
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
      uploadUrl: process.env.VUE_APP_BASE_API + '/car/upload/image', // 上传的圖片服务器地址
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      uploadData: {
        dir: '/img/car_product/editor' // 编辑器图片上传目录
      },
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
          // 自定义插入图片菜单，禁用网络图片输入
          insertImage: {
            // 禁用网络图片输入，只使用文件上传
            allowedFileTypes: ['image/*'],
            onInsertedImage: (imageNode) => {
              // 图片插入后的回调
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
      // 自定义图片插入菜单，禁用网络图片输入，直接触发文件选择
      // 使用 DOM 操作绑定图片按钮点击事件
      this.bindImageButton()
    },
    // 触发文件选择
    triggerFileSelect() {
      try {
        if (this.$refs.upload) {
          // 尝试多种方式获取 input 元素
          let input = null
          if (this.$refs.upload.$children && this.$refs.upload.$children[0]) {
            input = this.$refs.upload.$children[0].$refs.input
          } else if (this.$refs.upload.$el) {
            input = this.$refs.upload.$el.querySelector('input[type="file"]')
          }
          if (input) {
            input.click()
          } else {
            this.$message.warning('无法触发文件选择，请检查上传组件')
          }
        }
      } catch (error) {
        console.error('触发文件选择失败:', error)
        this.$message.error('触发文件选择失败')
      }
    },
    // 绑定图片按钮
    bindImageButton() {
      this.$nextTick(() => {
        // 使用 MutationObserver 监听工具栏变化，确保按钮已渲染
        const toolbar = this.$el ? this.$el.querySelector('.w-e-toolbar') : null
        if (toolbar) {
          this.attachImageButtonHandler(toolbar)
        } else {
          // 如果工具栏还未渲染，使用 MutationObserver 等待
          const observer = new MutationObserver((mutations, obs) => {
            const toolbar = this.$el ? this.$el.querySelector('.w-e-toolbar') : null
            if (toolbar) {
              this.attachImageButtonHandler(toolbar)
              obs.disconnect()
            }
          })
          
          if (this.$el) {
            observer.observe(this.$el, {
              childList: true,
              subtree: true
            })
          }
          
          // 3秒后停止观察
          setTimeout(() => {
            observer.disconnect()
          }, 3000)
        }
      })
    },
    // 附加图片按钮事件处理器
    attachImageButtonHandler(toolbar) {
      try {
        // 查找工具栏中的图片按钮
        // wangeditor 的图片按钮可能有不同的选择器
        let imageBtn = null
        
        // 方法1: 通过 data-menu-key 属性查找
        imageBtn = toolbar.querySelector('[data-menu-key="insertImage"]')
        
        // 方法2: 如果方法1失败，遍历所有按钮查找
        if (!imageBtn) {
          const buttons = toolbar.querySelectorAll('button, div[role="button"]')
          for (let btn of buttons) {
            const menuKey = btn.getAttribute('data-menu-key')
            if (menuKey === 'insertImage') {
              imageBtn = btn
              break
            }
          }
        }
        
        if (imageBtn) {
          // 使用事件委托，避免重复绑定
          if (!imageBtn.dataset.customHandler) {
            imageBtn.dataset.customHandler = 'true'
            imageBtn.addEventListener('click', (e) => {
              e.preventDefault()
              e.stopPropagation()
              this.triggerFileSelect()
            }, true) // 使用捕获阶段，确保优先执行
          }
        } else {
          // 如果找不到按钮，延迟重试（最多重试3次）
          if (!this.bindRetryCount) {
            this.bindRetryCount = 0
          }
          if (this.bindRetryCount < 3) {
            this.bindRetryCount++
            setTimeout(() => {
              this.attachImageButtonHandler(toolbar)
            }, 500)
          }
        }
      } catch (error) {
        console.warn('绑定图片按钮失败:', error)
      }
    },
    onChange(editor) {
      const html = editor.getHtml()
      this.html = html
      this.$emit('input', html)
      this.$emit('change', html)
    },
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      // 校检文件大小 (5MB)
      const maxSize = 5 * 1024 * 1024
      if (file.size > maxSize) {
        this.$message.error('上传文件大小不能超过 5 MB!')
        return false
      }
      // 校检文件类型
      const isImage = file.type.startsWith('image/')
      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      return true
    },
    handleUploadSuccess(res, file) {
      // 如果上传成功
      if (res.code == 200) {
        // 获取编辑器实例
        const editor = this.editor
        if (editor) {
          // 获取光标所在位置
          const selection = editor.selection
          let range = null
          try {
            range = selection.getRange()
          } catch (e) {
            // 如果没有选中区域，创建一个默认位置
            range = { start: editor.getText().length, end: editor.getText().length }
          }
          
          // 插入圖片 res.url为服务器返回的完整圖片地址（已包含前缀）
          const imageUrl = res.url || (process.env.VUE_APP_BASE_API + res.fileName)
          
          // 插入图片
          editor.dangerouslyInsertHtml(`<img src="${imageUrl}" alt="" style="max-width: 100%;"/>`)
          
          // 调整光标位置
          if (range) {
            try {
              selection.setRange({
                start: range.start + 1,
                end: range.start + 1
              })
            } catch (e) {
              // 如果设置光标位置失败，忽略错误
            }
          }
        }
      } else {
        this.$message.error(res.msg || '圖片插入失败')
      }
    },
    handleUploadError() {
      this.$message.error('圖片插入失败')
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