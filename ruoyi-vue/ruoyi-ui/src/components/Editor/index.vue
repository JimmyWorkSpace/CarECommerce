<template>
  <div>
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
      v-if="this.type == 'url'"
    >
    </el-upload>
    <div class="editor" ref="editor" :style="styles"></div>
    
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
        <el-form-item label="高度">
          <el-row :gutter="10">
            <el-col :span="10">
              <el-input
                v-model="youtubeHeight"
                placeholder="例如：315 或 56.25"
                type="number"
              />
            </el-col>
            <el-col :span="6">
              <el-select v-model="youtubeHeightUnit" style="width: 100%">
                <el-option label="px" value="px"></el-option>
                <el-option label="%" value="%"></el-option>
              </el-select>
            </el-col>
          </el-row>
          <div style="margin-top: 5px; color: #909399; font-size: 12px;">
            默認：315px 或 56.25%（16:9 比例，當寬度為 % 時建議使用 %）
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
import Quill from "quill";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { getToken } from "@/utils/auth";

export default {
  name: "Editor",
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
      default: "",
    },
    /* 高度 */
    height: {
      type: Number,
      default: null,
    },
    /* 最小高度 */
    minHeight: {
      type: Number,
      default: null,
    },
    /* 只读 */
    readOnly: {
      type: Boolean,
      default: false,
    },
    // 上传文件大小限制(MB)
    fileSize: {
      type: Number,
      default: 5,
    },
    /* 類型（base64格式、url格式） */
    type: {
      type: String,
      default: "url",
    }
  },
  data() {
    return {
      uploadUrl: process.env.VUE_APP_BASE_API + "/car/upload/image", // 上传的圖片服务器地址
      headers: {
        Authorization: "Bearer " + getToken()
      },
      uploadData: {
        dir: "/img/car_product/editor" // 编辑器图片上传目录
      },
      Quill: null,
      currentValue: "",
      youtubeDialogVisible: false,
      youtubeUrl: "",
      youtubeWidth: "560",
      youtubeWidthUnit: "px",
      youtubeHeight: "315",
      youtubeHeightUnit: "px",
      options: {
        theme: "snow",
        bounds: document.body,
        debug: "warn",
        modules: {
          // 工具栏配置
          toolbar: [
            ["bold", "italic", "underline", "strike"],       // 加粗 斜体 下划线 刪除线
            ["blockquote", "code-block"],                    // 引用  代码块
            [{ list: "ordered" }, { list: "bullet" }],       // 有序、无序列表
            [{ indent: "-1" }, { indent: "+1" }],            // 缩进
            [{ size: ["small", false, "large", "huge"] }],   // 字体大小
            [{ header: [1, 2, 3, 4, 5, 6, false] }],         // 標題
            [{ color: [] }, { background: [] }],             // 字体颜色、字体背景颜色
            [{ align: [] }],                                 // 对齐方式
            ["clean"],                                       // 清除文本格式
            ["link", "image", "video", "youtube"]            // 链接、圖片、视频、YouTube
          ],
        },
        placeholder: "请输入内容",
        readOnly: this.readOnly,
      },
    };
  },
  computed: {
    styles() {
      let style = {};
      if (this.minHeight) {
        style.minHeight = `${this.minHeight}px`;
      }
      if (this.height) {
        style.height = `${this.height}px`;
      }
      return style;
    },
  },
  watch: {
    value: {
      handler(val) {
        if (val !== this.currentValue) {
          this.currentValue = val === null ? "" : val;
          if (this.Quill) {
            this.Quill.pasteHTML(this.currentValue);
          }
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.registerYouTubeBlot();
    this.init();
  },
  beforeDestroy() {
    this.Quill = null;
  },
  methods: {
    init() {
      const editor = this.$refs.editor;
      this.Quill = new Quill(editor, this.options);
      // 如果设置了上传地址则自定义圖片上传事件
      if (this.type == 'url') {
        let toolbar = this.Quill.getModule("toolbar");
        toolbar.addHandler("image", (value) => {
          this.uploadType = "image";
          if (value) {
            this.$refs.upload.$children[0].$refs.input.click();
          } else {
            this.quill.format("image", false);
          }
        });
        // 添加 YouTube 按钮处理器
        toolbar.addHandler("youtube", () => {
          this.showYouTubeDialog();
        });
      }
      this.Quill.pasteHTML(this.currentValue);
      this.Quill.on("text-change", (delta, oldDelta, source) => {
        const html = this.$refs.editor.children[0].innerHTML;
        const text = this.Quill.getText();
        const quill = this.Quill;
        this.currentValue = html;
        this.$emit("input", html);
        this.$emit("on-change", { html, text, quill });
      });
      this.Quill.on("text-change", (delta, oldDelta, source) => {
        this.$emit("on-text-change", delta, oldDelta, source);
      });
      this.Quill.on("selection-change", (range, oldRange, source) => {
        this.$emit("on-selection-change", range, oldRange, source);
      });
      this.Quill.on("editor-change", (eventName, ...args) => {
        this.$emit("on-editor-change", eventName, ...args);
      });
    },
    // 注册 YouTube Blot（自定义格式）
    registerYouTubeBlot() {
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
            containerStyle += `width: ${widthStr}; min-width: 320px;`;
          } else {
            containerStyle += `width: ${widthStr}; min-width: 320px;`;
          }
          
          // 设置高度
          // 如果高度单位是 %，使用 padding-bottom 技巧（通常用于响应式 16:9 比例）
          // 如果高度单位是 px，使用固定高度
          if (heightUnit === '%') {
            // 当使用 % 时，通常使用 padding-bottom 来保持宽高比
            // 如果用户输入的是 56.25%，那就是标准的 16:9 比例
            containerStyle += `height: 0; padding-bottom: ${heightStr};`;
          } else {
            containerStyle += `height: ${heightStr};`;
          }
          
          container.style.cssText = containerStyle;
          
          const videoContainer = document.createElement('div');
          videoContainer.className = 'youtube-video-container';
          
          // 视频容器始终填充父容器
          videoContainer.style.cssText = 'position: absolute; top: 0; left: 0; width: 100%; height: 100%; overflow: hidden; background: #000;';
          
          const iframe = document.createElement('iframe');
          iframe.src = `https://www.youtube.com/embed/${videoId}?rel=0&amp;showinfo=0`;
          iframe.frameBorder = '0';
          iframe.allow = 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share';
          iframe.allowFullscreen = true;
          iframe.style.cssText = 'position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: none; display: block;';
          
          // 设置 iframe 的 width 和 height 属性（用于兼容性，但实际大小由 CSS 控制）
          const widthNum = typeof width === 'string' ? parseFloat(width) : width;
          const heightNum = typeof height === 'string' ? parseFloat(height) : height;
          iframe.width = widthNum || 560;
          iframe.height = heightNum || 315;
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
        }
      }
      
      YouTubeBlot.blotName = 'youtube';
      YouTubeBlot.tagName = 'div';
      YouTubeBlot.className = 'ql-youtube';
      
      Quill.register(YouTubeBlot, true);
    },
    // 显示 YouTube 输入对话框
    showYouTubeDialog() {
      this.youtubeUrl = '';
      this.youtubeWidth = '560';
      this.youtubeWidthUnit = 'px';
      this.youtubeHeight = '315';
      this.youtubeHeightUnit = 'px';
      this.youtubeDialogVisible = true;
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
      
      // 验证并处理高度
      let height = this.youtubeHeight ? this.youtubeHeight.trim() : '315';
      if (!height || isNaN(parseFloat(height)) || parseFloat(height) <= 0) {
        // 如果宽度是 px，根据 16:9 比例计算高度；如果是 %，使用 56.25%
        if (widthUnit === 'px') {
          height = String(Math.round(parseFloat(width) * 9 / 16));
        } else {
          height = '56.25';
        }
      }
      const heightUnit = this.youtubeHeightUnit || 'px';
      const heightValue = height + heightUnit;
      
      // 插入到编辑器
      if (this.Quill) {
        const length = this.Quill.getSelection(true).index;
        this.Quill.insertEmbed(length, 'youtube', {
          videoId: videoId,
          width: widthValue,
          height: heightValue,
          widthUnit: widthUnit,
          heightUnit: heightUnit
        });
        this.Quill.setSelection(length + 1);
        this.$message.success('YouTube 視頻已插入');
      }
      
      this.youtubeDialogVisible = false;
      this.youtubeUrl = '';
    },
    // 从 YouTube URL 中提取视频 ID
    extractYouTubeVideoId(url) {
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
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      return true;
    },
    handleUploadSuccess(res, file) {
      // 获取富文本组件实例
      let quill = this.Quill;
      // 如果上传成功
      if (res.code == 200) {
        // 获取光标所在位置
        let length = quill.getSelection().index;
        // 插入圖片  res.url为服务器返回的完整圖片地址（已包含前缀）
        const imageUrl = res.url || (process.env.VUE_APP_BASE_API + res.fileName);
        quill.insertEmbed(length, "image", imageUrl);
        // 调整光标到最后
        quill.setSelection(length + 1);
      } else {
        this.$message.error(res.msg || "圖片插入失败");
      }
    },
    handleUploadError() {
      this.$message.error("圖片插入失败");
    },
  },
};
</script>

<style>
.editor, .ql-toolbar {
  white-space: pre-wrap !important;
  line-height: normal !important;
}
.quill-img {
  display: none;
}
.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "標題1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "標題2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "標題3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "標題4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "標題5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "標題6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}

/* YouTube 视频样式 - 支持调整大小 */
.editor .youtube-video-wrapper {
  position: relative !important;
  display: inline-block !important;
  max-width: 100% !important;
  margin: 10px 0 !important;
  overflow: hidden !important;
  min-width: 320px !important;
  vertical-align: middle;
  visibility: visible !important;
  opacity: 1 !important;
  box-sizing: border-box !important;
}

/* 当使用 px 单位时，允许调整大小 */
.editor .youtube-video-wrapper[data-width-unit="px"] {
  resize: both;
  overflow: visible !important;
  min-height: 180px !important;
}

.editor .youtube-video-container {
  position: absolute !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
  overflow: hidden !important;
  background: #000 !important;
  display: block !important;
  visibility: visible !important;
}

.editor .youtube-video-container iframe {
  position: absolute !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
  border: none !important;
  display: block !important;
  visibility: visible !important;
  opacity: 1 !important;
}

/* YouTube 按钮样式 */
.ql-toolbar .ql-youtube::before {
  content: "YouTube";
  font-size: 12px;
  font-weight: bold;
}

.ql-toolbar button.ql-youtube {
  width: auto;
  padding: 0 8px;
}

.ql-toolbar button.ql-youtube:hover {
  color: #FF0000;
}

/* 响应式设计 */
@media (max-width: 600px) {
  .editor .youtube-video-wrapper {
    width: 100% !important;
    resize: none;
  }
}
</style>
