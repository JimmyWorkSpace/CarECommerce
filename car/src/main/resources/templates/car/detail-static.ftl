<link href="/css/car-detail.css" rel="stylesheet">
<div class="car-detail">
    <div class="main-title-container">
        <div class="first-title">
            優質二手車商 /
        </div>
        <div class="second-title">
            2020年 Toyota Camry 2.5L 豪華版
        </div>
    </div>
    
    <!-- 第一行：图片和信息 -->
    <div class="row mb-4">
        <!-- 左侧图片轮播 -->
        <div class="col-md-6">
            <div class="swiper-wrapper-container">
                <div class="swiper-container">
                    <img style="width:100%;aspect-ratio: 3/2;" class="swiper_image" src="/img/car/car4.jpg" alt="汽车图片">
                </div>
                <div class="thumbnail-container">
                    <img src="/img/car/car4.jpg" class="thumbnail active" alt="缩略图">
                    <img src="/img/car/car5.jpg" class="thumbnail" alt="缩略图">
                    <img src="/img/car/car6.jpg" class="thumbnail" alt="缩略图">
                    <img src="/img/car/car7.jpg" class="thumbnail" alt="缩略图">
                    <img src="/img/car/car8.jpg" class="thumbnail" alt="缩略图">
                </div>
            </div>
            <!-- Line分享按钮 -->
            <div class="share-container">
                <div class="line-it-button" data-lang="zh_Hant" data-type="share-a" data-env="REAL"
                    data-og-url="https://sale.carce.cc/car/preview/static-demo"
                    data-url="https://sale.carce.cc/static-demo"
                    data-color="default" data-size="large"
                    data-count="false" data-ver="3" style="display: none;"></div>
            </div>
        </div>
        
        <!-- 右侧信息 -->
        <div class="col-md-6">
            <h2 class="car-title">2020年 Toyota Camry 2.5L 豪華版
            </h2>
            <div class="price mb-3">
                <span class="h4">$</span><span class="h4">850,000
                </span>
            </div>
            <div class="specs mb-3">
                <div class="row">
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">年份</div>
                        <div class="spec-value">2020</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">里程</div>
                        <div class="spec-value">45,000 km</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">排量</div>
                        <div class="spec-value">2.5L</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">變速箱</div>
                        <div class="spec-value">自動</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">燃料</div>
                        <div class="spec-value">汽油</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">顏色</div>
                        <div class="spec-value">白色</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">車門</div>
                        <div class="spec-value">4門</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">座位</div>
                        <div class="spec-value">5座</div>
                    </div>
                </div>
            </div>
            <div class="action-buttons">
                <button class="btn btn-report" onclick="openReportModal()">
                    <i class="bi bi-file-earmark-text"></i> 檢車報告
                </button>
                <button class="btn btn-appointment" onclick="openAppointmentModal()">
                    <i class="bi bi-calendar-check"></i> 預約看車
                </button>
            </div>
            <div class="dealer-info mt-3 text-left">
                <div class="dealer-contact">聯絡人： 張經理
                </div>
                <div class="dealer-address">
                    賞車地址： 台北市信義區信義路五段7號
                    📍<a href="https://www.google.com/maps/search/?api=1&query=台北市信義區信義路五段7號" target="_blank">查看地圖</a>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 第三行：Tab页 -->
    <div class="tabs">
        <ul class="nav nav-tabs car-detail-tabs">
            <li class="nav-item">
                <a class="nav-link active" href="#equipment-tab">車輛配備</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#dealer-tab">店家介紹</a>
            </li>
        </ul>
        <div class="tab-content p-3 border border-top-0 rounded-bottom">
            <div id="equipment-tab" class="tab-pane active">
                <div class="equipment-header">
                    車輛配備
                </div>
                <div class="equipment-section">
                    <div class="equipment-row d-flex flex-column flex-md-row">
                        <div class="equipment-name">安全配備</div>
                        <div class="equipment-tags">
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 安全氣囊
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> ABS防鎖死煞車
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 電子穩定控制
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 倒車影像
                            </span>
                        </div>
                    </div>
                </div>
                <div class="equipment-section">
                    <div class="equipment-row d-flex flex-column flex-md-row">
                        <div class="equipment-name">舒適配備</div>
                        <div class="equipment-tags">
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 電動座椅
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 雙區恆溫空調
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 全景天窗
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 真皮座椅
                            </span>
                        </div>
                    </div>
                </div>
                <div class="equipment-section">
                    <div class="equipment-row d-flex flex-column flex-md-row">
                        <div class="equipment-name">科技配備</div>
                        <div class="equipment-tags">
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 導航系統
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 藍牙連接
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 無線充電
                            </span>
                            <span class="equipment-tag">
                                <i class="iconfont">&#xe632;</i> 智能鑰匙
                            </span>
                        </div>
                    </div>
                </div>
                
                <div class="equipment-header">
                    賣家保證
                </div>
                <div class="guarantee-tags">
                    <div class="guarantee-section">
                        <div class="guarantee-name">
                            <i class="iconfont">&#xe632;</i>無重大事故
                        </div>
                        <div class="guarantee-desc">
                            本車無重大事故記錄，車身結構完整，安全性能良好。
                        </div>
                    </div>
                    <div class="guarantee-section">
                        <div class="guarantee-name">
                            <i class="iconfont">&#xe632;</i>里程真實
                        </div>
                        <div class="guarantee-desc">
                            里程數真實可靠，無調表記錄，可提供完整保養記錄。
                        </div>
                    </div>
                    <div class="guarantee-section">
                        <div class="guarantee-name">
                            <i class="iconfont">&#xe632;</i>品質保證
                        </div>
                        <div class="guarantee-desc">
                            提供3個月品質保證，如有問題可免費維修或更換。
                        </div>
                    </div>
                </div>
                
                <div class="equipment-header">
                    車輛描述
                </div>
                <div class="content-frame">
                    <p>這是一輛2020年出廠的Toyota Camry 2.5L豪華版，車況極佳，里程僅45,000公里。</p>
                    <p><strong>車況特點：</strong></p>
                    <ul>
                        <li>原廠保養，定期維護</li>
                        <li>無重大事故，車身完整</li>
                        <li>內裝乾淨，座椅無磨損</li>
                        <li>引擎運轉順暢，變速箱正常</li>
                        <li>空調系統良好，音響系統完整</li>
                    </ul>
                    <p><strong>配備特色：</strong></p>
                    <ul>
                        <li>2.5L直列四缸引擎，動力充沛</li>
                        <li>8速手自排變速箱，換檔順暢</li>
                        <li>雙區恆溫空調系統</li>
                        <li>全景天窗，採光良好</li>
                        <li>真皮座椅，舒適度佳</li>
                        <li>倒車影像系統，安全便利</li>
                        <li>藍牙音響系統，娛樂功能完整</li>
                    </ul>
                    <p><strong>適合對象：</strong></p>
                    <p>適合家庭使用，商務代步，長途旅行等各種用途。車況良好，值得信賴。</p>
                </div>
            </div>
            
            <div id="dealer-tab" class="tab-pane">
                <div class="dealer-container">
                    <div class="dealer-intro">
                        <div class="dealer-intro-row">
                            <div class="dealer-title">店家名稱</div>
                            <div class="dealer-content">優質二手車商</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">店家地址</div>
                            <div class="dealer-content">台北市信義區信義路五段7號</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">聯絡人</div>
                            <div class="dealer-content">張經理</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">店家電話</div>
                            <div class="dealer-content">02-2345-6789</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">手機</div>
                            <div class="dealer-content">0912-345-678</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">LINE</div>
                            <div class="dealer-content">
                                <button class="btn-line-id">
                                    <i class="bi bi-chat-dots"></i> LINE聯絡我
                                </button>
                            </div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">網址</div>
                            <div class="dealer-content">www.qualitycars.com.tw</div>
                        </div>
                    </div>
                    <div class="dealer-cover">
                        <img src="/img/car/car9.jpg" alt="店家封面">
                    </div>
                </div>
                <div class="content-frame">
                    <p><strong>關於我們</strong></p>
                    <p>優質二手車商成立於2010年，專注於中高級二手車的買賣業務。我們擁有專業的車輛檢測團隊，確保每輛車都經過嚴格的安全檢查。</p>
                    <p><strong>服務特色</strong></p>
                    <ul>
                        <li>專業車輛檢測，確保車況</li>
                        <li>提供完整保養記錄</li>
                        <li>3個月品質保證</li>
                        <li>免費試駕服務</li>
                        <li>靈活的分期付款方案</li>
                        <li>完善的售後服務</li>
                    </ul>
                    <p><strong>營業時間</strong></p>
                    <p>週一至週五：09:00-18:00<br>
                    週六：09:00-17:00<br>
                    週日：10:00-16:00</p>
                    <p><strong>交通資訊</strong></p>
                    <p>捷運信義安和站步行5分鐘即可到達，附近設有停車場，方便客戶前來賞車。</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 预约看车弹窗 -->
<div class="modal fade" id="appointmentModal" tabindex="-1" aria-labelledby="appointmentModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="appointmentModalLabel">
                    <i class="bi bi-calendar-check me-2"></i>預約看車
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="appointmentForm">
                    <div class="mb-3">
                        <label for="appointmentDate" class="form-label">
                            <i class="bi bi-calendar me-2"></i>預約日期
                        </label>
                        <input type="date" class="form-control" id="appointmentDate" required>
                    </div>
                    <div class="mb-3">
                        <label for="appointmentTime" class="form-label">
                            <i class="bi bi-clock me-2"></i>預約時間
                        </label>
                        <select class="form-control" id="appointmentTime" required>
                            <option value="">請選擇時間</option>
                            <option value="09:00">09:00</option>
                            <option value="10:00">10:00</option>
                            <option value="11:00">11:00</option>
                            <option value="14:00">14:00</option>
                            <option value="15:00">15:00</option>
                            <option value="16:00">16:00</option>
                            <option value="17:00">17:00</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="appointmentPhone" class="form-label">
                            <i class="bi bi-telephone me-2"></i>聯絡電話
                        </label>
                        <input type="tel" class="form-control" id="appointmentPhone" 
                               placeholder="請輸入您的手機號碼" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">
                            <i class="bi bi-person me-2"></i>聯絡人
                        </label>
                        <div class="form-control-plaintext" id="appointmentNameDisplay">
                            <#if user?? && user?has_content>
                                ${user.name!'未设置姓名'}
                            <#else>
                                <span class="text-danger">请先登录</span>
                            </#if>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="appointmentNote" class="form-label">
                            <i class="bi bi-chat-text me-2"></i>備註
                        </label>
                        <textarea class="form-control" id="appointmentNote" rows="3" 
                                  placeholder="如有特殊需求請在此說明"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="submitAppointment()">
                    <i class="bi bi-check-circle me-2"></i>提交預約
                </button>
            </div>
        </div>
    </div>
</div>

<!-- 检车报告弹窗 -->
<div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="reportModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportModalLabel">
                    <i class="bi bi-file-earmark-text me-2"></i>檢車報告
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="pdf-info-container">
                    <div class="text-center p-4">
                        <i class="bi bi-file-earmark-pdf text-primary" style="font-size: 4rem;"></i>
                        <h4 class="mt-3 mb-3">檢車報告</h4>
                        <p class="text-muted mb-4">這份檢車報告包含了車輛的詳細檢測信息，包括發動機、變速箱、底盤等各個系統的檢測結果。</p>
                        
                        <div class="row g-3">
                            <div class="col-md-6">
                                <a href="/api/pdf/inspection-report" target="_blank" class="btn btn-primary btn-lg w-100">
                                    <i class="bi bi-eye me-2"></i>在線查看
                                </a>
                            </div>
                            <div class="col-md-6">
                                <a href="/api/pdf/inspection-report" download="检车报告.pdf" class="btn btn-outline-primary btn-lg w-100">
                                    <i class="bi bi-download me-2"></i>下載報告
                                </a>
                            </div>
                        </div>
                        
                        <div class="mt-4">
                            <small class="text-muted">
                                <i class="bi bi-info-circle me-1"></i>
                                報告包含：發動機檢測、變速箱檢測、底盤檢測、電器系統檢測、外觀檢測等
                            </small>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
            </div>
        </div>
    </div>
</div>

<!-- 成功提示弹窗 -->
<div class="modal fade" id="successModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body text-center">
                <div class="success-icon mb-3">
                    <i class="bi bi-check-circle-fill"></i>
                </div>
                <h5 class="modal-title">預約成功！</h5>
                <p class="text-muted">我們會盡快與您聯繫確認預約詳情。</p>
                <button type="button" class="btn btn-primary" data-bs-dismiss="modal">確定</button>
            </div>
        </div>
    </div>
</div>

<script>
// 简单的Tab切换功能
document.addEventListener('DOMContentLoaded', function() {
    // 只选择详情页内的tab导航，避免与主布局的导航冲突
    const tabLinks = document.querySelectorAll('.car-detail-tabs .nav-link');
    const tabPanes = document.querySelectorAll('.tab-pane');
    
    tabLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // 移除所有active类
            tabLinks.forEach(l => l.classList.remove('active'));
            tabPanes.forEach(p => p.classList.remove('active'));
            
            // 添加active类到当前tab
            this.classList.add('active');
            const targetId = this.getAttribute('href').substring(1);
            const targetElement = document.getElementById(targetId);
            if (targetElement) {
                targetElement.classList.add('active');
            }
        });
    });
    
    // 缩略图点击切换
    const thumbnails = document.querySelectorAll('.thumbnail');
    const mainImage = document.querySelector('.swiper_image');
    
    if (thumbnails.length > 0 && mainImage) {
        thumbnails.forEach(thumb => {
            thumb.addEventListener('click', function() {
                // 移除所有active类
                thumbnails.forEach(t => t.classList.remove('active'));
                // 添加active类到当前缩略图
                this.classList.add('active');
                // 更新主图片
                mainImage.src = this.src;
            });
        });
    }
    
    // 设置默认日期为明天
    const appointmentDateInput = document.getElementById('appointmentDate');
    if (appointmentDateInput) {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        appointmentDateInput.value = tomorrow.toISOString().split('T')[0];
    }
});

// 检车报告功能
function openReportModal() {
    const modal = new bootstrap.Modal(document.getElementById('reportModal'));
    modal.show();
}

// 预约相关功能
function openAppointmentModal() {
    // 检查用户是否登录
    const userNameDisplay = document.getElementById('appointmentNameDisplay');
    if (!userNameDisplay) {
        console.error('找不到用户名称显示元素');
        return;
    }
    
    const isLoggedIn = !userNameDisplay.querySelector('.text-danger');
    
    if (!isLoggedIn) {
        // 未登录，显示提示并跳转到登录页
        if (confirm('您尚未登录，是否跳转到登录页面？')) {
            // 获取当前页面URL作为返回地址
            const currentUrl = window.location.pathname + window.location.search;
            window.location.href = '/login?returnUrl=' + encodeURIComponent(currentUrl);
        }
        return;
    }
    
    // 已登录，打开预约弹窗
    const modal = new bootstrap.Modal(document.getElementById('appointmentModal'));
    modal.show();
}

function submitAppointment() {
    const form = document.getElementById('appointmentForm');
    if (!form) {
        console.error('找不到预约表单');
        return;
    }
    
    const formData = new FormData(form);
    
    // 验证表单
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }
    
    // 获取表单数据
    const appointmentData = {
        date: document.getElementById('appointmentDate')?.value || '',
        time: document.getElementById('appointmentTime')?.value || '',
        phone: document.getElementById('appointmentPhone')?.value || '',
        name: document.getElementById('appointmentNameDisplay')?.textContent?.trim() || '',
        note: document.getElementById('appointmentNote')?.value || '',
        carTitle: '2020年 Toyota Camry 2.5L 豪华版'
    };
    
    // 模拟提交预约
    console.log('预约数据:', appointmentData);
    
    // 关闭预约弹窗
    const appointmentModalElement = document.getElementById('appointmentModal');
    if (appointmentModalElement) {
        const appointmentModal = bootstrap.Modal.getInstance(appointmentModalElement);
        if (appointmentModal) {
            appointmentModal.hide();
        }
    }
    
    // 显示成功提示
    const successModalElement = document.getElementById('successModal');
    if (successModalElement) {
        const successModal = new bootstrap.Modal(successModalElement);
        successModal.show();
    }
    
    // 重置表单
    form.reset();
    
    // 设置默认日期为明天
    const appointmentDateInput = document.getElementById('appointmentDate');
    if (appointmentDateInput) {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        appointmentDateInput.value = tomorrow.toISOString().split('T')[0];
    }
}
</script>

<style>
/* 检车报告按钮样式 */
.btn-report {
    background: linear-gradient(135deg, #6f42c1 0%, #5a32a3 100%);
    border: none;
    color: white;
    border-radius: 10px;
    padding: 0.75rem 1.5rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.btn-report:hover {
    background: linear-gradient(135deg, #5a32a3 0%, #4a2d8a 100%);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(111, 66, 193, 0.4);
    color: white;
}

/* 预约按钮样式 */
.btn-appointment {
    background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
    border: none;
    color: white;
    border-radius: 10px;
    padding: 0.75rem 1.5rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.btn-appointment:hover {
    background: linear-gradient(135deg, #20c997 0%, #17a2b8 100%);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(40, 167, 69, 0.4);
    color: white;
}

/* 预约弹窗样式 */
.modal-content {
    border-radius: 15px;
    border: none;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.modal-header {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    color: white;
    border-radius: 15px 15px 0 0;
    border: none;
}

.modal-title {
    font-weight: 600;
}

.btn-close {
    filter: invert(1);
}

/* 成功提示弹窗样式 */
.success-icon {
    font-size: 4rem;
    color: #28a745;
}

#successModal .modal-content {
    border: none;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

#successModal .modal-body {
    padding: 2rem;
}

/* 表单样式优化 */
.form-control:focus {
    border-color: #5ACFC9;
    box-shadow: 0 0 0 0.2rem rgba(90, 207, 201, 0.25);
}

.form-label {
    font-weight: 600;
    color: #333;
}

/* 联系人显示样式 */
.form-control-plaintext {
    background-color: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 0.75rem 1rem;
    color: #495057;
    font-weight: 500;
}

.form-control-plaintext .text-danger {
    font-weight: 600;
}

/* PDF信息容器样式 */
.pdf-info-container {
    min-height: 400px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.pdf-info-container .text-center {
    max-width: 500px;
}

.pdf-info-container i {
    color: #5ACFC9;
}

.pdf-info-container h4 {
    color: #495057;
    font-weight: 600;
}

.pdf-info-container .btn {
    border-radius: 10px;
    font-weight: 600;
    transition: all 0.3s ease;
}

.pdf-info-container .btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 响应式设计 */
@media (max-width: 768px) {
    .modal-dialog {
        margin: 1rem;
    }
    
    .btn-appointment,
    .btn-report {
        padding: 0.5rem 1rem;
        font-size: 0.9rem;
    }
    

    
    .modal-xl {
        max-width: 95%;
    }
}
</style> 