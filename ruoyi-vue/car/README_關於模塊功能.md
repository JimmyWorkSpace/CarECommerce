# 關於模塊功能說明

## 功能概述
關於模塊用於維護網站的"關於我們"內容，存儲在 `CarRichContentEntity` 表中，通過 `contentType=1` 來標識。

## 功能特點
- 沒有列表頁面，直接編輯單一內容
- 沒有標題欄，簡潔的編輯界面
- 自動獲取 `contentType=1` 的第一條數據進行編輯
- 如果沒有數據，會自動創建默認內容
- 支持富文本編輯器

## 文件結構

### 後端文件
1. **控制器**: `car/src/main/java/com/ruoyi/car/controller/CarAboutController.java`
   - 提供獲取和修改關於內容的API接口
   - 權限控制：`car:about:query` 和 `car:about:edit`

2. **服務接口**: `car/src/main/java/com/ruoyi/car/service/ICarAboutService.java`
   - 定義獲取和修改關於內容的服務方法

3. **服務實現**: `car/src/main/java/com/ruoyi/car/service/impl/CarAboutServiceImpl.java`
   - 實現關於內容的業務邏輯
   - 自動創建默認內容（如果不存在）
   - 確保 `contentType=1`

### 前端文件
1. **API文件**: `ruoyi-ui/src/api/car/about.js`
   - 封裝關於模塊的API調用

2. **頁面組件**: `ruoyi-ui/src/views/car/about/index.vue`
   - 關於內容的編輯界面
   - 使用富文本編輯器
   - 表單驗證

### 數據庫文件
1. **菜單SQL**: `car/car_about_menu.sql`
   - 創建關於模塊的菜單和權限

## API接口

### 獲取關於內容
- **URL**: `GET /car/about`
- **權限**: `car:about:query`
- **返回**: 關於內容對象

### 修改關於內容
- **URL**: `PUT /car/about`
- **權限**: `car:about:edit`
- **參數**: 關於內容對象
- **返回**: 操作結果

## 數據結構
使用 `CarRichContentEntity` 實體：
- `id`: 主鍵ID
- `title`: 標題
- `content`: 內容（富文本）
- `contentType`: 內容類型（固定為1）
- `showOrder`: 排序
- `delFlag`: 刪除標記
- `createTime`: 創建時間

## 部署說明
1. 執行 `car/car_about_menu.sql` 創建菜單和權限
2. 重啟應用程序
3. 在系統菜單中會出現"關於"菜單項
4. 點擊進入即可編輯關於內容

## 使用說明
1. 進入"關於"菜單
2. 編輯標題和內容
3. 點擊"保存"按鈕
4. 系統會自動保存到數據庫

## 注意事項
- 關於內容只會有一條記錄（contentType=1）
- 如果沒有記錄，系統會自動創建默認內容
- 支持富文本格式的內容編輯
- 所有文字已轉換為繁體中文
