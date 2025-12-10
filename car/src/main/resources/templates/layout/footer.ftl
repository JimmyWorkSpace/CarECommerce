<!-- 通用页脚组件 -->
<#assign footerLinks = footerLinks!{}>
<footer class="footer">
    <div class="container">
        <div class="row">
            <!-- 第一列：車主專區 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車主專區</h4>
                <ul class="footer-links">
                    <li><a href="${footerLinks['footer_owner_verification']!'#'}">車輛驗證認證</a></li>
                    <li><a href="${footerLinks['footer_owner_terms']!'#'}">車主使用條款與規範</a></li>
                    <li><a href="${footerLinks['footer_owner_contract']!'#'}">車勢買賣車公版合約</a></li>
                    <li><a href="${footerLinks['footer_owner_privacy']!'#'}">隱私權政策</a></li>
                </ul>
            </div>
            
            <!-- 第二列：車勢週邊服務 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車勢週邊服務</h4>
                <ul class="footer-links">
                    <li><a href="${footerLinks['footer_service_warranty']!'#'}">汽車保固</a></li>
                    <li><a href="${footerLinks['footer_service_insurance']!'#'}">汽車保險</a></li>
                    <li><a href="${footerLinks['footer_service_loan']!'#'}">汽車貸款</a></li>
                    <li><a href="${footerLinks['footer_service_supplies']!'#'}">汽車用品</a></li>
                    <li><a href="${footerLinks['footer_service_coating']!'#'}">汽車美容鍍膜</a></li>
                    <li><a href="${footerLinks['footer_service_maintenance']!'#'}">汽車保養維修</a></li>
                </ul>
            </div>
            
            <!-- 第三列：車廠店家專區 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車廠店家專區</h4>
                <ul class="footer-links">
                    <li><a href="${footerLinks['footer_dealer_join']!'#'}">優質車廠加盟</a></li>
                    <li><a href="${footerLinks['footer_dealer_partner']!'#'}">周邊企業加入</a></li>
                    <li><a href="${footerLinks['footer_dealer_contract']!'#'}">車勢買賣車公版合約</a></li>
                </ul>
            </div>
            
            <!-- 第四列：車勢資訊 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">車勢資訊</h4>
                <ul class="footer-links">
                    <li><a href="${footerLinks['footer_info_facebook']!'#'}" target="_blank">Facebook</a></li>
                    <li><a href="${footerLinks['footer_info_youtube']!'#'}" target="_blank">YouTube</a></li>
                    <li><a href="${footerLinks['footer_info_about']!'#'}">關於我們</a></li>
                </ul>
            </div>
            
            <!-- 第五列：聯繫車勢 -->
            <div class="col-12 col-md-6 col-lg footer-column">
                <h4 class="footer-title">聯繫車勢</h4>
                <div class="footer-line-buttons">
                    <a href="${footerLinks['footer_contact_line_public']!'#'}" class="line-button">民眾專用LINE</a>
                    <a href="${footerLinks['footer_contact_line_dealer']!'#'}" class="line-button">車行專用LINE</a>
                </div>
            </div>
        </div>
    </div>
</footer>
