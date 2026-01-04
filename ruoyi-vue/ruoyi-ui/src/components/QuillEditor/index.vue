<template>
  <div class="quill-editor-wrapper">
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
    <iframe
      ref="editorFrame"
      :src="iframeSrc"
      frameborder="0"
      class="quill-editor-iframe"
      :style="iframeStyle"
      @load="handleIframeLoad"
    ></iframe>

    <!-- YouTube 输入对话框 -->
    <el-dialog
      title="嵌入 YouTube 視頻"
      :visible.sync="youtubeDialogVisible"
      width="500px"
      append-to-body
    >
      <el-form>
        <el-form-item label="YouTube 網址">
          <el-input
            v-model="youtubeUrl"
            placeholder="請輸入 YouTube 視頻網址，例如：https://www.youtube.com/watch?v=VIDEO_ID"
            @keyup.enter.native="insertYouTubeVideo"
          />
          <div style="margin-top: 10px; color: #909399; font-size: 12px;">
            支持的格式：<br/>
            • https://www.youtube.com/watch?v=VIDEO_ID<br/>
            • https://youtu.be/VIDEO_ID<br/>
            • https://www.youtube.com/embed/VIDEO_ID
          </div>
        </el-form-item>
        <el-form-item label="寬度">
          <el-row :gutter="10">
            <el-col :span="10">
              <el-input
                v-model="youtubeWidth"
                placeholder="例如：560 或 100"
                type="number"
              />
            </el-col>
            <el-col :span="6">
              <el-select v-model="youtubeWidthUnit" style="width: 100%">
                <el-option label="px" value="px"></el-option>
                <el-option label="%" value="%"></el-option>
              </el-select>
            </el-col>
          </el-row>
          <div style="margin-top: 5px; color: #909399; font-size: 12px;">
            默認：560px（16:9 比例）
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="youtubeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="insertYouTubeVideo">確 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";

export default {
  name: "QuillEditor",
  props: {
    // 编辑器的内容
    value: {
      type: String,
      default: ""
    },
    // 编辑器的最小高度
    minHeight: {
      type: Number,
      default: 300
    },
    // 编辑器的高度
    height: {
      type: Number,
      default: null
    },
    // 编辑器是否只读
    readonly: {
      type: Boolean,
      default: false
    },
    // 工具栏配置
    toolbar: {
      type: Array,
      default: () => [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ 'header': 1 }, { 'header': 2 }],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        [{ 'script': 'sub'}, { 'script': 'super' }],
        [{ 'indent': '-1'}, { 'indent': '+1' }],
        [{ 'direction': 'rtl' }],
        [{ 'size': ['small', false, 'large', 'huge'] }],
        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
        [{ 'color': [] }, { 'background': [] }],
        [{ 'font': [] }],
        [{ 'align': [] }],
        ['clean'],
        ['link', 'image', 'video', 'youtube']
      ]
    },
    // 上传文件大小限制(MB)
    fileSize: {
      type: Number,
      default: 5
    },
    // 類型（base64格式、url格式）
    type: {
      type: String,
      default: "url"
    }
  },
  data() {
    return {
      iframeSrc: '',
      editorInstance: null,
      isReady: false,
      isUpdating: false, // 标记是否正在更新内容，避免循环更新
      uploadUrl: process.env.VUE_APP_BASE_API + "/car/upload/image", // 上传的圖片服务器地址
      headers: {
        Authorization: "Bearer " + getToken()
      },
      uploadData: {
        dir: "/img/car_product/editor" // 编辑器图片上传目录
      },
      // YouTube 对话框相关
      youtubeDialogVisible: false,
      youtubeUrl: "",
      youtubeWidth: "560",
      youtubeWidthUnit: "px"
    };
  },
  computed: {
    iframeStyle() {
      const style = {
        width: '100%',
        border: '1px solid #dcdfe6',
        borderRadius: '4px'
      };
      
      if (this.height) {
        style.height = this.height + 'px';
      } else {
        style.minHeight = this.minHeight + 'px';
      }
      
      return style;
    }
  },
  watch: {
    value(newVal) {
      // 如果正在更新中，跳过（避免循环更新）
      if (this.isUpdating) {
        return;
      }
      
      if (this.isReady && this.editorInstance) {
        // 只在内容真正变化时才更新，避免频繁更新导致光标跳动
        this.getContent().then(currentContent => {
          if (currentContent !== newVal) {
            this.isUpdating = true;
            this.setContent(newVal, true); // 第二个参数表示保持光标位置
            // 延迟重置标记，确保更新完成
            setTimeout(() => {
              this.isUpdating = false;
            }, 100);
          }
        });
      }
    },
    readonly(newVal) {
      if (this.isReady && this.editorInstance) {
        this.setReadonly(newVal);
      }
    }
  },
  mounted() {
    // 创建iframe的HTML内容
    this.createIframeContent();
    // 监听来自iframe的消息
    window.addEventListener('message', this.handleMessage);
  },
  beforeDestroy() {
    window.removeEventListener('message', this.handleMessage);
  },
  methods: {
    createIframeContent() {
      const minHeight = this.minHeight;
      const toolbar = JSON.stringify(this.toolbar);
      const readonly = this.readonly;
      
      // 获取本地 Quill 资源路径（从 public/static/quill 目录加载）
      // 在 Vue CLI 中，public 目录下的文件会被复制到输出目录的根目录
      // 开发环境中，public 目录下的文件可以直接通过根路径访问
      // 使用 window.location.origin 确保在 iframe 中也能正确加载
      const baseUrl = process.env.BASE_URL || '/';
      const pathPrefix = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl;
      
      // 构建完整的 URL（使用 window.location.origin 确保在 iframe 中也能正确加载）
      // 在 iframe 中，相对路径可能无法正确解析，所以使用完整路径
      const getFullUrl = (path) => {
        // 如果路径已经是完整 URL，直接返回
        if (path.startsWith('http://') || path.startsWith('https://')) {
          return path;
        }
        // 否则使用当前页面的 origin
        return window.location.origin + (path.startsWith('/') ? path : '/' + path);
      };
      
      const quillCoreCss = getFullUrl(pathPrefix + '/static/quill/quill.core.css');
      const quillSnowCss = getFullUrl(pathPrefix + '/static/quill/quill.snow.css');
      const quillJs = getFullUrl(pathPrefix + '/static/quill/quill.js');
      
      console.log('Quill resource paths:', {
        baseUrl: baseUrl,
        pathPrefix: pathPrefix,
        quillCoreCss: quillCoreCss,
        quillSnowCss: quillSnowCss,
        quillJs: quillJs,
        origin: window.location.origin
      });
      
      const htmlContent = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quill Editor</title>
  <link href="${quillCoreCss}" rel="stylesheet">
  <link href="${quillSnowCss}" rel="stylesheet">
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
      background: #fff;
    }
    #editor-container {
      height: 100%;
      min-height: ${minHeight}px;
    }
    .ql-container {
      font-size: 14px;
      font-family: inherit;
    }
    .ql-editor {
      min-height: ${minHeight}px;
    }
    /* YouTube 按钮文字样式 */
    .ql-youtube {
      position: relative;
    }
    .ql-youtube svg {
      display: none !important;
    }
    .ql-youtube .youtube-text {
      font-size: 12px;
      color: #444;
      font-weight: 500;
      padding: 0 4px;
    }
  </style>
</head>
<body>
  <div id="editor-container"></div>
  <script>
    (function() {
      // 在 iframe 中构建完整的 URL（使用父窗口的 origin）
      function getFullUrl(path) {
        if (path.startsWith('http://') || path.startsWith('https://')) {
          return path;
        }
        // 使用父窗口的 origin 或当前窗口的 origin
        try {
          const origin = window.parent && window.parent.location ? window.parent.location.origin : window.location.origin;
          return origin + (path.startsWith('/') ? path : '/' + path);
        } catch (e) {
          // 如果无法访问 parent，使用当前窗口的 origin
          return window.location.origin + (path.startsWith('/') ? path : '/' + path);
        }
      }
      
      const quillJsUrl = getFullUrl('${quillJs}');
      console.log('Loading Quill from:', quillJsUrl);
      
      // 动态加载 Quill 脚本
      const script = document.createElement('script');
      script.src = quillJsUrl;
      script.onload = function() {
        console.log('Quill script loaded successfully');
        initQuill();
      };
      script.onerror = function() {
        console.error('Failed to load Quill script from:', quillJsUrl);
        // 尝试使用原始路径
        const fallbackScript = document.createElement('script');
        fallbackScript.src = '${quillJs}';
        fallbackScript.onload = function() {
          console.log('Quill loaded with fallback path');
          initQuill();
        };
        fallbackScript.onerror = function() {
          console.error('Failed to load Quill with fallback path: ${quillJs}');
        };
        document.head.appendChild(fallbackScript);
      };
      document.head.appendChild(script);
      
      // 防止重复初始化
      let quillInstance = null;
      let isInitialized = false;
      
      // 等待 Quill 加载完成
      function initQuill() {
        if (isInitialized) {
          console.log('Quill already initialized, skipping...');
          return;
        }
        
        if (typeof Quill === 'undefined') {
          console.error('Quill is not loaded. Script path: ${quillJs}');
          setTimeout(initQuill, 100);
          return;
        }
        
        // 检查容器是否已经存在 Quill 实例
        const container = document.querySelector('#editor-container');
        if (container && container.querySelector('.ql-toolbar')) {
          console.log('Quill toolbar already exists, skipping initialization');
          isInitialized = true;
          quillInstance = Quill.find(container);
          return;
        }
        
        const toolbar = ${toolbar};
        const readonly = ${readonly};
        
        console.log('Quill loaded successfully, initializing...');
        
        // 先注册 YouTube Blot（必须在创建 Quill 实例之前注册）
        const BlockEmbed = Quill.import('blots/block/embed');
        
        class YouTubeBlot extends BlockEmbed {
          static create(value) {
            const node = super.create();
            let videoId, width, height, widthUnit, heightUnit;
            
            if (typeof value === 'string') {
              videoId = YouTubeBlot.extractVideoId(value);
              width = 560;
              height = 315;
              widthUnit = 'px';
              heightUnit = 'px';
            } else {
              videoId = value.videoId;
              width = value.width || 560;
              height = value.height || 315;
              widthUnit = value.widthUnit || 'px';
              heightUnit = value.heightUnit || 'px';
            }
            
            // 确保 width 和 height 是字符串格式（可能包含单位）
            const widthStr = typeof width === 'string' ? width : (width + widthUnit);
            const heightStr = typeof height === 'string' ? height : (height + heightUnit);
            
            node.setAttribute('data-video-id', videoId);
            node.setAttribute('data-width', widthStr);
            node.setAttribute('data-height', heightStr);
            node.setAttribute('data-width-unit', widthUnit);
            node.setAttribute('data-height-unit', heightUnit);
            node.setAttribute('contenteditable', 'false');
            
            const container = document.createElement('div');
            container.className = 'youtube-video-wrapper';
            
            // 构建样式字符串
            let containerStyle = 'position: relative; display: inline-block; max-width: 100%; margin: 10px 0; border: 1px solid #ccc; background: #000; overflow: hidden; box-sizing: border-box;';
            
            // 设置宽度
            if (widthUnit === '%') {
              containerStyle += 'width: ' + widthStr + '; min-width: 320px;';
            } else {
              containerStyle += 'width: ' + widthStr + '; min-width: 320px;';
            }
            
            // 设置高度 - 如果高度为 auto，则使用 aspect-ratio 保持 16:9 比例
            if (heightStr === 'auto' || heightUnit === 'auto') {
              containerStyle += 'height: auto; aspect-ratio: 16 / 9;';
            } else if (heightUnit === '%') {
              containerStyle += 'height: 0; padding-bottom: ' + heightStr + ';';
            } else {
              containerStyle += 'height: ' + heightStr + ';';
            }
            
            container.style.cssText = containerStyle;
            
            const videoContainer = document.createElement('div');
            videoContainer.className = 'youtube-video-container';
            videoContainer.style.cssText = 'position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow: hidden; background: #000;';
            
            const iframe = document.createElement('iframe');
            iframe.src = 'https://www.youtube.com/embed/' + videoId + '?rel=0&amp;showinfo=0';
            iframe.frameBorder = '0';
            iframe.allow = 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share';
            iframe.allowFullscreen = true;
            iframe.style.cssText = 'position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: none; display: block;';
            
            const widthNum = typeof width === 'string' ? parseFloat(width) : width;
            // 如果高度为 auto，iframe 高度也设置为 auto（通过 CSS 控制）
            if (heightStr === 'auto' || heightUnit === 'auto') {
              iframe.width = widthNum || 560;
              iframe.height = 'auto';
            } else {
              const heightNum = typeof height === 'string' ? parseFloat(height) : height;
              iframe.width = widthNum || 560;
              iframe.height = heightNum || 315;
            }
            iframe.loading = 'lazy';
            
            videoContainer.appendChild(iframe);
            container.appendChild(videoContainer);
            node.appendChild(container);
            
            return node;
          }
          
          static value(node) {
            const videoId = node.getAttribute('data-video-id');
            const width = node.getAttribute('data-width') || '560px';
            const height = node.getAttribute('data-height') || '315px';
            const widthUnit = node.getAttribute('data-width-unit') || 'px';
            const heightUnit = node.getAttribute('data-height-unit') || 'px';
            return { videoId, width, height, widthUnit, heightUnit };
          }
          
          static extractVideoId(url) {
            if (!url) return null;
            
            let match = url.match(new RegExp('(?:youtube\\.com/watch\\?v=|youtube\\.com/watch\\?.*&v=)([a-zA-Z0-9_-]{11})'));
            if (match) return match[1];
            
            match = url.match(new RegExp('youtu\\.be/([a-zA-Z0-9_-]{11})'));
            if (match) return match[1];
            
            match = url.match(new RegExp('youtube\\.com/embed/([a-zA-Z0-9_-]{11})'));
            if (match) return match[1];
            
            match = url.match(new RegExp('youtube\\.com/v/([a-zA-Z0-9_-]{11})'));
            if (match) return match[1];
            
            return null;
          }
        }
        
        YouTubeBlot.blotName = 'youtube';
        YouTubeBlot.tagName = 'div';
        YouTubeBlot.className = 'ql-youtube';
        
        Quill.register(YouTubeBlot, true);
        
        // 设置繁体中文语言
        quillInstance = new Quill('#editor-container', {
        theme: 'snow',
        modules: {
          toolbar: toolbar
        },
        readOnly: readonly,
        placeholder: '請輸入內容'
      });
      
      // 设置工具栏按钮的繁体中文标签
      const toolbarEl = document.querySelector('.ql-toolbar');
      if (toolbarEl) {
        const buttons = toolbarEl.querySelectorAll('button, .ql-picker-label');
        buttons.forEach(btn => {
          const title = btn.getAttribute('title');
          if (title) {
            const zhTitleMap = {
              'Bold': '粗體',
              'Italic': '斜體',
              'Underline': '底線',
              'Strike': '刪除線',
              'Blockquote': '引用',
              'Code Block': '代碼塊',
              'Header': '標題',
              'List': '列表',
              'Ordered': '有序列表',
              'Bullet': '無序列表',
              'Script': '上下標',
              'Indent': '縮進',
              'Direction': '文字方向',
              'Size': '字體大小',
              'Color': '顏色',
              'Background': '背景色',
              'Font': '字體',
              'Align': '對齊',
              'Clean': '清除格式',
              'Link': '鏈接',
              'Image': '圖片',
              'Video': '視頻'
            };
            const zhTitle = zhTitleMap[title] || title;
            btn.setAttribute('title', zhTitle);
          }
        });
      }
      
      // 发送内容变化事件
      quillInstance.on('text-change', function() {
        const content = quillInstance.root.innerHTML;
        window.parent.postMessage({
          type: 'content-change',
          content: content
        }, '*');
      });
      
      // 标记为已初始化
      isInitialized = true;
       
       // 自定义图片上传处理器
       const quillToolbar = quillInstance.getModule('toolbar');
       quillToolbar.addHandler('image', function() {
         // 通知父窗口触发图片上传
         window.parent.postMessage({
           type: 'trigger-image-upload'
         }, '*');
       });
       
       // 添加 YouTube 按钮处理器
       quillToolbar.addHandler('youtube', function() {
         // 通知父窗口显示YouTube对话框
         window.parent.postMessage({
           type: 'show-youtube-dialog'
         }, '*');
       });
       
       // 自定义 YouTube 按钮显示为文字
       setTimeout(function() {
         const youtubeButton = document.querySelector('.ql-youtube');
         if (youtubeButton) {
           // 移除 SVG 图标
           const svg = youtubeButton.querySelector('svg');
           if (svg) {
             svg.style.display = 'none';
           }
           // 添加文字
           if (!youtubeButton.querySelector('.youtube-text')) {
             const textSpan = document.createElement('span');
             textSpan.className = 'youtube-text';
             textSpan.textContent = 'YouTube';
             textSpan.style.cssText = 'font-size: 12px; color: #444; font-weight: 500;';
             youtubeButton.appendChild(textSpan);
           }
         }
       }, 100);
      
       // 监听来自父窗口的消息
       window.addEventListener('message', function(event) {
         if (event.data.type === 'set-content') {
           // 保存当前光标位置
           const selection = quillInstance.getSelection(true);
           const currentContent = quillInstance.root.innerHTML;
           
           // 只在内容真正变化时才更新
           if (currentContent !== event.data.content) {
             const delta = quillInstance.clipboard.convert(event.data.content);
             quillInstance.setContents(delta, 'silent');
             
             // 如果设置了保持光标位置，且之前有选择，尝试恢复
             if (event.data.preserveSelection && selection && selection.length === 0) {
               // 尝试将光标恢复到原来的位置，如果位置超出范围则放到末尾
               const length = quillInstance.getLength();
               const newIndex = Math.min(selection.index, length - 1);
               quillInstance.setSelection(newIndex, 'silent');
             } else if (!event.data.preserveSelection) {
               // 如果没有要求保持光标，将光标放到末尾
               const length = quillInstance.getLength();
               quillInstance.setSelection(length - 1, 'silent');
             }
           }
         } else if (event.data.type === 'get-content') {
           window.parent.postMessage({
             type: 'content-response',
             content: quillInstance.root.innerHTML
           }, '*');
         } else if (event.data.type === 'set-readonly') {
           quillInstance.enable(!event.data.readonly);
         } else if (event.data.type === 'insert-image') {
           // 插入图片
           const length = quillInstance.getSelection(true).index;
           quillInstance.insertEmbed(length, 'image', event.data.url);
           quillInstance.setSelection(length + 1);
         } else if (event.data.type === 'insert-youtube') {
           // 插入YouTube视频
           const length = quillInstance.getSelection(true).index;
           quillInstance.insertEmbed(length, 'youtube', {
             videoId: event.data.videoId,
             width: event.data.width,
             height: event.data.height,
             widthUnit: event.data.widthUnit,
             heightUnit: event.data.heightUnit
           });
           quillInstance.setSelection(length + 1);
         } else if (event.data.type === 'init-ready') {
          window.parent.postMessage({
            type: 'editor-ready'
          }, '*');
        }
      });
      
      // 初始化完成后通知父窗口
      setTimeout(function() {
        window.parent.postMessage({
          type: 'editor-ready'
        }, '*');
      }, 100);
      }
    })();
  <\/script>
</body>
</html>`;
      
      // 创建blob URL
      const blob = new Blob([htmlContent], { type: 'text/html' });
      this.iframeSrc = URL.createObjectURL(blob);
    },
    handleIframeLoad() {
      // iframe加载完成后，发送初始化消息
      if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
        this.$refs.editorFrame.contentWindow.postMessage({
          type: 'init-ready'
        }, '*');
        
        // 如果有初始值，设置内容
        if (this.value) {
          this.setContent(this.value);
        }
      }
    },
    handleMessage(event) {
      // 验证消息来源（可选，根据实际需求）
      // if (event.origin !== window.location.origin) return;
      
      const { type, content } = event.data;
      
      if (type === 'editor-ready') {
        this.isReady = true;
        this.editorInstance = this.$refs.editorFrame.contentWindow;
        if (this.value) {
          this.setContent(this.value);
        }
      } else if (type === 'content-change') {
        // 内容变化时，通知父组件
        this.isUpdating = true; // 标记正在更新
        this.$emit('input', content);
        this.$emit('change', content);
        // 延迟重置标记
        setTimeout(() => {
          this.isUpdating = false;
        }, 50);
      } else if (type === 'trigger-image-upload') {
        // 触发图片上传
        if (this.type === 'url' && this.$refs.upload) {
          this.$refs.upload.$children[0].$refs.input.click();
        }
      } else if (type === 'show-youtube-dialog') {
        // 显示YouTube对话框
        this.showYouTubeDialog();
      }
    },
    // 显示 YouTube 输入对话框
    showYouTubeDialog() {
      this.youtubeUrl = '';
      this.youtubeWidth = '560';
      this.youtubeWidthUnit = 'px';
      this.youtubeDialogVisible = true;
    },
    // 从 YouTube URL 中提取视频 ID
    extractYouTubeVideoId(url) {
      if (!url) return null;
      
      // 标准格式: youtube.com/watch?v=VIDEO_ID
      let match = url.match(/(?:youtube\.com\/watch\?v=|youtube\.com\/watch\?.*&v=)([a-zA-Z0-9_-]{11})/);
      if (match) return match[1];
      
      // 短链接格式: youtu.be/VIDEO_ID
      match = url.match(/youtu\.be\/([a-zA-Z0-9_-]{11})/);
      if (match) return match[1];
      
      // 嵌入格式: youtube.com/embed/VIDEO_ID
      match = url.match(/youtube\.com\/embed\/([a-zA-Z0-9_-]{11})/);
      if (match) return match[1];
      
      // v/ 格式: youtube.com/v/VIDEO_ID
      match = url.match(/youtube\.com\/v\/([a-zA-Z0-9_-]{11})/);
      if (match) return match[1];
      
      return null;
    },
    // 确认插入 YouTube 视频
    insertYouTubeVideo() {
      if (!this.youtubeUrl || !this.youtubeUrl.trim()) {
        this.$message.warning('請輸入 YouTube 網址');
        return;
      }
      
      const videoId = this.extractYouTubeVideoId(this.youtubeUrl.trim());
      if (!videoId) {
        this.$message.error('無效的 YouTube 網址，請輸入正確的 YouTube 視頻網址');
        return;
      }
      
      // 验证并处理宽度
      let width = this.youtubeWidth ? this.youtubeWidth.trim() : '560';
      if (!width || isNaN(parseFloat(width)) || parseFloat(width) <= 0) {
        width = '560';
      }
      const widthUnit = this.youtubeWidthUnit || 'px';
      const widthValue = width + widthUnit;
      
      // 高度自动设置为 auto
      const heightValue = 'auto';
      
      // 插入到编辑器
      if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
        this.$refs.editorFrame.contentWindow.postMessage({
          type: 'insert-youtube',
          videoId: videoId,
          width: widthValue,
          height: heightValue,
          widthUnit: widthUnit,
          heightUnit: 'auto'
        }, '*');
        this.$message.success('YouTube 視頻已插入');
      }
      
      this.youtubeDialogVisible = false;
      this.youtubeUrl = '';
    },
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上傳文件大小不能超過 ${this.fileSize} MB!`);
          return false;
        }
      }
      return true;
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      // 如果上传成功
      if (res.code == 200) {
        // 获取图片URL
        const imageUrl = res.url || (process.env.VUE_APP_BASE_API + res.fileName);
        // 通知iframe插入图片
        if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
          this.$refs.editorFrame.contentWindow.postMessage({
            type: 'insert-image',
            url: imageUrl
          }, '*');
        }
      } else {
        this.$message.error(res.msg || "圖片插入失敗");
      }
    },
    // 上传失败回调
    handleUploadError() {
      this.$message.error("圖片插入失敗");
    },
    setContent(content, preserveSelection = false) {
      if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
        this.$refs.editorFrame.contentWindow.postMessage({
          type: 'set-content',
          content: content,
          preserveSelection: preserveSelection
        }, '*');
      }
    },
    getContent() {
      return new Promise((resolve) => {
        if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
          const handler = (event) => {
            if (event.data.type === 'content-response') {
              window.removeEventListener('message', handler);
              resolve(event.data.content);
            }
          };
          window.addEventListener('message', handler);
          this.$refs.editorFrame.contentWindow.postMessage({
            type: 'get-content'
          }, '*');
        } else {
          resolve('');
        }
      });
    },
    setReadonly(readonly) {
      if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
        this.$refs.editorFrame.contentWindow.postMessage({
          type: 'set-readonly',
          readonly: readonly
        }, '*');
      }
    },
    focus() {
      if (this.$refs.editorFrame && this.$refs.editorFrame.contentWindow) {
        this.$refs.editorFrame.contentWindow.focus();
      }
    }
  }
};
</script>

<style scoped>
.quill-editor-wrapper {
  width: 100%;
}

.quill-editor-iframe {
  display: block;
  background: #fff;
}
</style>

