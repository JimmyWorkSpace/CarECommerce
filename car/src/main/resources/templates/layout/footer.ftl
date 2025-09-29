<!-- 通用页脚组件 -->
<footer class="footer">
    <div class="container">
        <div class="row">
            <div class="col-12 col-md-4 mb-4">
                <h4>聯繫我們</h4>
                <#if configContent??>
                    <p><i class="bi bi-telephone"></i> 客服熱線：${configContent.kefu!'---'}</p>
                    <p><i class="bi bi-envelope"></i> 郵箱：${configContent.youxiang!'---'}</p>
                    <p><i class="bi bi-geo-alt"></i> 地址：${configContent.dizhi!'---'}</p>
                <#else>
                    <p><i class="bi bi-telephone"></i> 客服熱線：---</p>
                    <p><i class="bi bi-envelope"></i> 郵箱：---</p>
                    <p><i class="bi bi-geo-alt"></i> 地址：---</p>
                </#if>
            </div>
            <div class="col-12 col-md-4 mb-4">
                <h4>服務時間</h4>
                <#if configContent??>
                    <p><i class="bi bi-briefcase"></i> 工作日：${configContent.fwsj1!'---'}</p>
                    <p><i class="bi bi-calendar"></i> 週末：${configContent.fwsj2!'---'}</p>
                    <p><i class="bi bi-calendar-event"></i> 節假日：${configContent.fwsj3!'---'}</p>
                <#else>
                    <p><i class="bi bi-briefcase"></i> 工作日：---</p>
                    <p><i class="bi bi-calendar"></i> 週末：---</p>
                    <p><i class="bi bi-calendar-event"></i> 節假日：---</p>
                </#if>
            </div>
            <div class="col-12 col-md-4 mb-4">
                <h4>關注我們</h4>
                <div class="social-links">
                    <a href="#" class="social-link"><i class="bi bi-facebook"></i></a>
                    <a href="#" class="social-link"><i class="bi bi-instagram"></i></a>
                    <a href="#" class="social-link"><i class="bi bi-twitter"></i></a>
                    <a href="#" class="social-link"><i class="bi bi-youtube"></i></a>
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2024 二手車銷售平台. 保留所有權利.</p>
        </div>
    </div>
</footer>
