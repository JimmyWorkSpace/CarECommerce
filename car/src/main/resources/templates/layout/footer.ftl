<!-- 通用页脚组件 -->
<#assign footerLinks = footerLinks!{}>
<#-- 辅助函数：判断链接是否有效 -->
<#function isValidLink link>
    <#return link?? && link != '' && link != '#'>
</#function>
<footer class="footer">
    <div class="container">
        <div class="row">
            <!-- 第一列：車主專區 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車主專區</h4>
                <ul class="footer-links">
                    <#assign link = footerLinks['footer_owner_verification']!''>
                    <li><#if isValidLink(link)><a href="${link}">車輛驗證認證</a><#else><span>車輛驗證認證</span></#if></li>
                    <#assign link = footerLinks['footer_owner_terms']!''>
                    <li><#if isValidLink(link)><a href="${link}">車主使用條款與規範</a><#else><span>車主使用條款與規範</span></#if></li>
                    <#assign link = footerLinks['footer_owner_contract']!''>
                    <li><#if isValidLink(link)><a href="${link}">車勢買賣車公版合約</a><#else><span>車勢買賣車公版合約</span></#if></li>
                    <#assign link = footerLinks['footer_owner_privacy']!''>
                    <li><#if isValidLink(link)><a href="${link}">隱私權政策</a><#else><span>隱私權政策</span></#if></li>
                </ul>
            </div>
            
            <!-- 第二列：車勢週邊服務 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車勢週邊服務</h4>
                <ul class="footer-links">
                    <#assign link = footerLinks['footer_service_warranty']!''>
                    <li><#if isValidLink(link)><a href="${link}">汽車保固</a><#else><span>汽車保固</span></#if></li>
                    <#assign link = footerLinks['footer_service_insurance']!''>
                    <li><#if isValidLink(link)><a href="${link}">汽車保險</a><#else><span>汽車保險</span></#if></li>
                    <#assign link = footerLinks['footer_service_loan']!''>
                    <li><#if isValidLink(link)><a href="${link}">汽車貸款</a><#else><span>汽車貸款</span></#if></li>
                    <#assign link = footerLinks['footer_service_supplies']!''>
                    <li><#if isValidLink(link)><a href="${link}">汽車用品</a><#else><span>汽車用品</span></#if></li>
                    <#assign link = footerLinks['footer_service_coating']!''>
                    <li><#if isValidLink(link)><a href="${link}">汽車美容鍍膜</a><#else><span>汽車美容鍍膜</span></#if></li>
                    <#assign link = footerLinks['footer_service_maintenance']!''>
                    <li><#if isValidLink(link)><a href="${link}">汽車保養維修</a><#else><span>汽車保養維修</span></#if></li>
                </ul>
            </div>
            
            <!-- 第三列：車廠店家專區 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車廠店家專區</h4>
                <ul class="footer-links">
                    <#assign link = footerLinks['footer_dealer_join']!''>
                    <li><#if isValidLink(link)><a href="${link}">優質車廠加盟</a><#else><span>優質車廠加盟</span></#if></li>
                    <#assign link = footerLinks['footer_dealer_partner']!''>
                    <li><#if isValidLink(link)><a href="${link}">周邊企業加入</a><#else><span>周邊企業加入</span></#if></li>
                    <#assign link = footerLinks['footer_dealer_contract']!''>
                    <li><#if isValidLink(link)><a href="${link}">車勢買賣車公版合約</a><#else><span>車勢買賣車公版合約</span></#if></li>
                </ul>
            </div>
            
            <!-- 第四列：車勢資訊 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車勢資訊</h4>
                <ul class="footer-links">
                    <#assign link = footerLinks['footer_info_facebook']!''>
                    <li><#if isValidLink(link)><a href="${link}" target="_blank">Facebook</a><#else><span>Facebook</span></#if></li>
                    <#assign link = footerLinks['footer_info_youtube']!''>
                    <li><#if isValidLink(link)><a href="${link}" target="_blank">YouTube</a><#else><span>YouTube</span></#if></li>
                    <#assign link = footerLinks['footer_info_about']!''>
                    <li><#if isValidLink(link)><a href="${link}">關於我們</a><#else><span>關於我們</span></#if></li>
                </ul>
            </div>
            
            <!-- 第五列：聯繫車勢 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">聯繫車勢</h4>
                <div class="footer-line-buttons">
                    <#assign link = footerLinks['footer_contact_line_public']!''>
                    <#if isValidLink(link)><a href="${link}" class="line-button">民眾專用LINE</a><#else><span class="line-button" style="cursor: default; opacity: 0.6;">民眾專用LINE</span></#if>
                    <#assign link = footerLinks['footer_contact_line_dealer']!''>
                    <#if isValidLink(link)><a href="${link}" class="line-button">車行專用LINE</a><#else><span class="line-button" style="cursor: default; opacity: 0.6;">車行專用LINE</span></#if>
                </div>
            </div>
        </div>
    </div>
</footer>
