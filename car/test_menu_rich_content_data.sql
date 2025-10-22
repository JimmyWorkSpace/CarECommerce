-- 测试菜单富文本功能数据
-- 插入一个富文本类型的菜单项

INSERT INTO car_menu (title, showOrder, delFlag, createTime, isShow, canDel, linkUrl, linkType, content) VALUES
('服務條款', 6, 0, NOW(), 1, 1, '', 1, 
'<h1>服務條款</h1>
<h2>1. 服務說明</h2>
<p>歡迎使用車勢汽車交易網（以下簡稱「本平台」）。本平台提供二手車買賣服務，致力於為用戶提供安全、便捷的汽車交易體驗。</p>

<h2>2. 用戶責任</h2>
<p>用戶在使用本平台服務時，應當：</p>
<ul>
    <li>提供真實、準確的個人信息</li>
    <li>遵守相關法律法規</li>
    <li>不得發布虛假信息</li>
    <li>保護個人賬戶安全</li>
</ul>

<h2>3. 平台責任</h2>
<p>本平台承諾：</p>
<ul>
    <li>保護用戶隱私信息</li>
    <li>提供優質的客戶服務</li>
    <li>維護平台交易安全</li>
    <li>及時處理用戶投訴</li>
</ul>

<h2>4. 免責聲明</h2>
<p>本平台僅提供信息發布和交易撮合服務，不對以下事項承擔責任：</p>
<ul>
    <li>車輛的實際狀況與描述不符</li>
    <li>交易過程中產生的糾紛</li>
    <li>第三方服務的質量問題</li>
</ul>

<h2>5. 聯繫我們</h2>
<p>如有任何問題，請聯繫我們的客服團隊：</p>
<div class="highlight-box">
    <p><strong>客服電話：</strong> 400-123-4567</p>
    <p><strong>客服郵箱：</strong> service@carce.cc</p>
    <p><strong>服務時間：</strong> 週一至週五 9:00-18:00</p>
</div>

<p>本服務條款自發布之日起生效，如有更新將及時通知用戶。</p>');

-- 插入另一個富文本類型的菜單項
INSERT INTO car_menu (title, showOrder, delFlag, createTime, isShow, canDel, linkUrl, linkType, content) VALUES
('隱私政策', 7, 0, NOW(), 1, 1, '', 1, 
'<h1>隱私政策</h1>
<h2>1. 信息收集</h2>
<p>我們收集以下類型的信息：</p>

<h3>1.1 個人信息</h3>
<ul>
    <li>姓名、電話號碼、電子郵件地址</li>
    <li>身份證明文件信息</li>
    <li>銀行賬戶信息（用於交易）</li>
</ul>

<h3>1.2 車輛信息</h3>
<ul>
    <li>車輛基本信息（品牌、型號、年份等）</li>
    <li>車輛照片和視頻</li>
    <li>車輛檢測報告</li>
</ul>

<h2>2. 信息使用</h2>
<p>我們使用收集的信息用於：</p>
<ul>
    <li>提供和改進我們的服務</li>
    <li>處理交易和支付</li>
    <li>與用戶溝通</li>
    <li>防止欺詐和確保安全</li>
</ul>

<h2>3. 信息保護</h2>
<p>我們採取以下措施保護您的信息：</p>
<div class="content-grid">
    <div class="content-item">
        <h4>數據加密</h4>
        <p>使用SSL加密技術保護數據傳輸</p>
    </div>
    <div class="content-item">
        <h4>訪問控制</h4>
        <p>限制員工對個人信息的訪問權限</p>
    </div>
    <div class="content-item">
        <h4>定期審計</h4>
        <p>定期檢查和更新安全措施</p>
    </div>
</div>

<h2>4. 信息共享</h2>
<p>我們不會向第三方出售、交易或轉讓您的個人信息，除非：</p>
<ul>
    <li>獲得您的明確同意</li>
    <li>法律要求或法院命令</li>
    <li>保護我們的權利和財產</li>
</ul>

<h2>5. 您的權利</h2>
<p>您有權：</p>
<ul>
    <li>查看和更新您的個人信息</li>
    <li>要求刪除您的賬戶</li>
    <li>選擇不接收營銷信息</li>
    <li>提出投訴或建議</li>
</ul>

<h2>6. 聯繫我們</h2>
<p>如果您對本隱私政策有任何疑問，請聯繫我們：</p>
<div class="highlight-box">
    <p><strong>隱私保護專員：</strong> privacy@carce.cc</p>
    <p><strong>電話：</strong> 400-123-4567</p>
    <p><strong>地址：</strong> 台北市信義區信義路五段7號</p>
</div>

<p>本隱私政策最後更新時間：2024年1月1日</p>');

-- 查詢菜單數據驗證
SELECT id, title, showOrder, linkType, 
       CASE 
           WHEN linkType = 0 THEN '普通鏈接'
           WHEN linkType = 1 THEN '富文本內容'
           ELSE '未知類型'
       END as linkTypeDesc,
       CASE 
           WHEN linkType = 1 THEN LEFT(content, 50) + '...'
           ELSE linkUrl
       END as content_preview
FROM car_menu 
WHERE delFlag = 0 AND isShow = 1 
ORDER BY showOrder;
