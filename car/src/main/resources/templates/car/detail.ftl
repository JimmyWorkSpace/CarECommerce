<link href="/css/car-detail.css" rel="stylesheet">
<link rel="canonical" href="${ogUrl!''}">
<div class="car-detail" id="app">
    <div class="main-title-container">
        <div class="first-title">
            {{ dealerInfo.dealerName || '優質二手車商' }} /
        </div>
        <div class="second-title">
            {{ carInfo.saleTitle || (carInfo.manufactureYear + '年 ' + carInfo.brand + ' ' + carInfo.customModel) || '車輛詳情' }}
        </div>
    </div>
    
    <!-- 第一行：图片和信息 -->
    <div class="row mb-4">
        <!-- 左侧图片轮播 -->
        <div class="col-md-6">
            <div class="swiper-wrapper-container" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
                <div class="swiper-container">
                    <div class="swiper-wrapper">
                        <div v-for="(image, index) in images" 
                             :key="index"
                             class="swiper-slide"
                             :class="{ active: currentImageIndex === index }">
                            <div class="image-container">
                                <img class="swiper_image" 
                                     :src="image" 
                                     :alt="carInfo.saleTitle || '汽車圖片'"
                                     @click="openLightbox(index)"
                                     @error="handleImageError">
                            </div>
                        </div>
                        <!-- 如果没有图片，显示默认图片 -->
                        <div v-if="!images || images.length === 0" class="swiper-slide active">
                            <div class="image-container">
                                <img class="swiper_image" 
                                     src="/img/car/car4.jpg" 
                                     alt="汽車圖片"
                                     @error="handleImageError">
                            </div>
                        </div>
                    </div>
                    <!-- 輪播圖导航按钮 -->
                    <div class="swiper-button-prev" v-if="images && images.length > 1" @click="prevImage"></div>
                    <div class="swiper-button-next" v-if="images && images.length > 1" @click="nextImage"></div>
                    <!-- 輪播圖指示器 -->
                    <div class="swiper-pagination" v-if="images && images.length > 1">
                        <span v-for="(image, index) in images" 
                              :key="index"
                              class="swiper-pagination-bullet"
                              :class="{ active: currentImageIndex === index }"
                              @click="changeImage(index)"></span>
                    </div>
                </div>
                <div class="thumbnail-container" v-if="images && images.length > 1">
                    <img v-for="(image, index) in images" 
                         :key="index"
                         :src="getThumbnailUrl(image)" 
                         class="thumbnail" 
                         :class="{ active: currentImageIndex === index }"
                         @click="changeImage(index)"
                         alt="縮略圖">
                </div>
            </div>
            <!-- Line分享按钮 -->
            <div class="share-container">
                <div class="line-it-button" data-lang="zh_Hant" data-type="share-a" data-env="REAL"
                    data-og-url="https://sale.carce.cc/car/detail/{{ saleId }}"
                    data-url="https://sale.carce.cc/detail/{{ saleId }}"
                    data-color="default" data-size="large"
                    data-count="false" data-ver="3" style="display: none;"></div>
            </div>
        </div>
        
        <!-- 右侧信息 -->
        <div class="col-md-6">
            <h2 class="car-title">
                {{ carInfo.saleTitle || (carInfo.manufactureYear + '年 ' + carInfo.brand + ' ' + carInfo.customModel) || '車輛詳情' }}
            </h2>
            <div class="price mb-3">
                <span class="h4">$</span><span class="h4">
                    {{ carInfo.salePrice ? formatPrice(carInfo.salePrice) : '面議' }}
                </span>
            </div>
            <div class="specs mb-3">
                <div class="row">
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">年份</div>
                        <div class="spec-value">{{ carInfo.manufactureYear || '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">里程</div>
                        <div class="spec-value">{{ carInfo.mileage ? formatMileage(carInfo.mileage) + ' km' : '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">排量</div>
                        <div class="spec-value">{{ carInfo.displacement || '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">變速箱</div>
                        <div class="spec-value">{{ carInfo.transmission || '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">燃料</div>
                        <div class="spec-value">{{ carInfo.fuelSystem || '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">顏色</div>
                        <div class="spec-value">{{ carInfo.color || '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">車門</div>
                        <div class="spec-value">{{ carInfo.doorCount ? carInfo.doorCount + '門' : '--' }}</div>
                    </div>
                    <div class="col-6 col-sm-6 col-md-4 col-lg-3 mb-3">
                        <div class="spec-name text-muted">座位</div>
                        <div class="spec-value">{{ carInfo.passengerCount ? carInfo.passengerCount + '座' : '--' }}</div>
                    </div>
                </div>
            </div>
            <div class="action-buttons d-flex w-100 mb-3">
                <button class="btn btn-line flex-fill ms-2" @click="openLineContact">
                    <i class="bi bi-chat-dots"></i> 加LINE
                </button>
                <button class="btn btn-phone flex-fill ms-2" @click="openPhoneContact">
                    <i class="bi bi-telephone"></i> 電話聯絡
                </button>
                <button class="btn btn-appointment flex-fill ms-2" @click="openAppointmentModal">
                    <i class="bi bi-calendar-check"></i> 預約看車
                </button>
                <button class="btn btn-report flex-fill ms-2" @click="openReportModal">
                    <i class="bi bi-exclamation-triangle"></i> 檢舉店家
                </button>
            </div>
            <div class="dealer-info mt-3 text-left">
                <div class="dealer-contact">聯絡人： {{ dealerInfo.contactPerson || '張經理' }}</div>
                <div class="dealer-address">
                    賞車地址： {{ dealerInfo.publicAddress || '台北市信義區信義路五段7號' }}
                    📍<a :href="'https://www.google.com/maps/search/?api=1&query=' + encodeURIComponent(dealerInfo.publicAddress || '台北市信義區信義路五段7號')" target="_blank">查看地圖</a>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 第三行：功能块和广告块 -->
    <div class="features-ads-section mb-4">
        <div class="container-fluid">
            <div class="row">
                <!-- 左侧功能块区域 (2/3) -->
                <div class="col-md-8" style="padding: 2px">
                    <div class="features-section">
                        <div class="row">
                            <div class="col-12 mb-3" 
                                 v-for="(feature, index) in features" :key="feature.id">
                                <div class="feature-card">
                                    <!-- titleType=0 时显示图片和标题 -->
                                    <template v-if="feature.titleType === 0">
                                        <div class="feature-image-container">
                                            <img :src="feature.imageUrl" :alt="feature.title" class="feature-image" 
                                                 @error="handleImageError($event, feature.title)">
                                            <h3 class="feature-title" v-text="feature.title"></h3>
                                        </div>
                                    </template>
                                    <!-- titleType=1 时显示HTML内容 -->
                                    <template v-else-if="feature.titleType === 1">
                                        <div class="feature-html-container">
                                            <div class="feature-html-content" v-html="feature.titleHtml"></div>
                                        </div>
                                    </template>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 右侧广告块区域 (1/3) -->
                <div class="col-md-4">
                    <div class="ad-section">
                        <div class="row">
                            <div class="col-12 mb-3" 
                                 v-for="(ad, index) in advertisements" :key="ad.id">
                                <div class="ad-card" :data-ad-id="ad.id">
                                    <!-- 連結類型广告 -->
                                    <a v-if="ad.isLink === 1" :href="ad.linkUrl" class="ad-link" target="_blank">
                                        <img :src="ad.imageUrl" :alt="ad.title" class="ad-image" 
                                             @error="handleImageError($event, ad.title)">
                                        <div v-if="ad.title" class="ad-title-overlay" style="display: none;" v-text="ad.title"></div>
                                        <div v-if="ad.title" class="ad-title-bottom" v-text="ad.title"></div>
                                    </a>
                                    <!-- 內容類型广告 -->
                                    <div v-else class="ad-content-link" @click="showAdContent(ad.id, ad.title, ad.content)">
                                        <img :src="ad.imageUrl" :alt="ad.title" class="ad-image"
                                             @error="handleImageError($event, ad.title)">
                                        <div v-if="ad.title" class="ad-title-overlay" style="display: none;" v-text="ad.title"></div>
                                        <div v-if="ad.title" class="ad-title-bottom" v-text="ad.title"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 第四行：Tab页 -->
    <div class="tabs">
        <ul class="nav nav-tabs">
            <li class="nav-item" v-for="(tab, index) in tabs" :key="index">
                <a class="nav-link" :class="{ active: activeTab.code === tab.code }" @click="activeTab = tab">
                    {{ tab.title }}
                </a>
            </li>
        </ul>
        <div class="tab-content p-3 border border-top-0 rounded-bottom">
            <div v-show="activeTab.code === 'car_equipments'">
                <div class="equipment-header">
                    車輛配備
                </div>
                <div class="equipment-section" v-for="(equipment, index) in carEquipments" :key="index">
                    <div class="equipment-row d-flex flex-column flex-md-row">
                        <div class="equipment-name">{{ equipment.name }}</div>
                        <div class="equipment-tags">
                            <span class="equipment-tag" v-for="(tag, tagIndex) in equipment.tags" :key="tagIndex">
                                <i class="iconfont">&#xe632;</i> {{ tag }}
                            </span>
                        </div>
                    </div>
                </div>
                <div class="equipment-header">
                    賣家保證
                </div>
                <div class="guarantee-tags">
                    <div class="guarantee-section" v-for="(gur, index) in carGuarantees" :key="index">
                        <div class="guarantee-name"><i class="iconfont">&#xe632;</i>{{ gur.itemName }}</div>
                        <div class="guarantee-desc">
                            {{ gur.description }}
                        </div>
                    </div>
                </div>
                <div class="equipment-header">
                    車輛描述
                </div>
                <iframe ref="contentFrame" class="content-frame" :srcdoc="getHtmlContent(carInfo.saleDescription)"
                    frameborder="0" width="100%"></iframe>
                <div style="height: 1em;"></div>
            </div>
            
            <div v-show="activeTab.code === 'dealer_intro'">
                <div class="dealer-container">
                    <div class="dealer-intro">
                        <div class="dealer-intro-row">
                            <div class="dealer-title">店家名稱</div>
                            <div class="dealer-content">{{ dealerInfo.dealerName || '--' }}</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">店家地址</div>
                            <div class="dealer-content">{{ dealerInfo.publicAddress || '--' }}</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">聯絡人</div>
                            <div class="dealer-content">{{ dealerInfo.contactPerson || '--' }}</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">店家電話</div>
                            <div class="dealer-content">{{ dealerInfo.companyPhone || '--' }}</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">手機</div>
                            <div class="dealer-content">{{ dealerInfo.companyMobile || '--' }}</div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">LINE</div>
                            <div class="dealer-content">
                                <button class="btn-line-id" @click="contactLine" @mouseenter="lineIdHover = true"
                                    @mouseleave="lineIdHover = false" v-if="dealerInfo.lineId">
                                    <i class="bi bi-chat-dots"></i> {{ lineIdHover ? dealerInfo.lineId : 'LINE聯絡我' }}
                                </button>
                                <span v-else>--</span>
                            </div>
                        </div>
                        <div class="dealer-intro-row">
                            <div class="dealer-title">網址</div>
                            <div class="dealer-content">{{ dealerInfo.website || '--' }}</div>
                        </div>
                    </div>
                    <div class="dealer-cover">
                        <img :src="dealerInfo.coverImage" alt="店家封面">
                    </div>
                </div>
                <iframe ref="dealerContentFrame" class="content-frame"
                    :srcdoc="getHtmlContent(dealerInfo.description)" frameborder="0" width="100%"></iframe>
            </div>
        </div>
    </div>
    
    <!-- 预约看车弹窗 -->
    <div class="modal fade" id="appointmentModal" tabindex="-1" aria-labelledby="appointmentModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-start" id="appointmentModalLabel">
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
                                   placeholder="請輸入您的手機號碼" 
                                   value="<#if user?? && user?has_content>${user.phone!''}</#if>" required>
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
                    <button type="button" class="btn btn-success" @click="submitAppointment">
                        <i class="bi bi-check-circle me-2"></i>提交預約
                    </button>
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

    <!-- 檢舉弹窗 -->
    <div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="reportModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-start" id="reportModalLabel">
                        <i class="bi bi-flag me-2"></i>檢舉車輛
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="reportForm">
                        <div class="mb-3">
                            <label for="reportReason" class="form-label">
                                <i class="bi bi-exclamation-triangle me-2"></i>檢舉原因
                            </label>
                            <select class="form-control" id="reportReason" required>
                                <option value="">請選擇檢舉原因</option>
                                <option value="price_mismatch">價格與現場不符</option>
                                <option value="false_info">資料虛假</option>
                                <option value="fraud_suspicion">詐騙嫌疑</option>
                                <option value="other">其它</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="reportDescription" class="form-label">
                                <i class="bi bi-chat-text me-2"></i>詳細說明
                            </label>
                            <textarea class="form-control" id="reportDescription" rows="4" 
                                      placeholder="請詳細描述檢舉原因，提供更多資訊有助於我們處理您的檢舉" required></textarea>
                        </div>
                        <div class="mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="reportAnonymous">
                                <label class="form-check-label" for="reportAnonymous">
                                    匿名檢舉
                                </label>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-danger" @click="submitReport">
                        <i class="bi bi-flag me-2"></i>提交檢舉
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- SEO描述 -->
    <section class="seo-description" style="display:none">
        <p><#if carInfo.saleDescription??>${carInfo.saleDescription?replace('<[^>]*>', '', 'r')?replace('\\s+', ' ', 'r')?trim}</#if><#if carInfo.saleDescription?? && ogDescription??>,</#if><#if ogDescription??>${ogDescription?replace('<[^>]*>', '', 'r')?replace('\\s+', ' ', 'r')?trim}</#if></p>
    </section>
</div>
<script type="application/ld+json">
{
  "@context": "https://schema.org/",
  "@type": "Product",
  "name": "${ogTitle}",
  "image": "https://testcloud.carce.cc/img/car_sale/sell_car_68d4ac5db7cc01.55690246.jpg",
  "description": "<#if carInfo.saleDescription??>${carInfo.saleDescription?replace('<[^>]*>', '', 'r')?replace('\\s+', ' ', 'r')?trim}</#if><#if carInfo.saleDescription?? && ogDescription??>,</#if><#if ogDescription??>${ogDescription?replace('<[^>]*>', '', 'r')?replace('\\s+', ' ', 'r')?trim}</#if>",
  "brand": {
    "@type": "Brand",
    "name": "${carInfo.brand}"
  },
  "offers": {
    "@type": "Offer",
    "url": "${ogUrl}",
    "priceCurrency": "TWD",
    "price": "50000",
    "availability": "https://schema.org/InStock",
    "seller": {
      "@type": "Organization",
      "name": "小明二手車"
    }
  }
}
</script>
<script>
console.log('Vue版本:', Vue.version);
console.log('开始初始化Vue实例...');

try {
    new Vue({
    el: '#app',
    data: {
        carInfo: <#if carInfoJson??>${carInfoJson}<#else>{
            saleTitle: '',
            brand: '',
            customModel: '',
            manufactureYear: '',
            salePrice: null,
            mileage: null,
            displacement: '',
            transmission: '',
            fuelSystem: '',
            color: '',
            doorCount: null,
            passengerCount: null,
            saleDescription: '',
            coverImage: ''
        }</#if>,
        images: <#if imagesJson??>${imagesJson}<#else>[]</#if>,
        equipments: <#if equipmentsJson??>${equipmentsJson}<#else>[]</#if>,
        guarantees: <#if guaranteesJson??>${guaranteesJson}<#else>[]</#if>,
        dealerInfo: <#if dealerInfoJson??>${dealerInfoJson}<#else>{}</#if>,
        saleId: <#if saleId??>${saleId}<#else>null</#if>,
        activeTab: { code: 'car_equipments', title: '車輛配備' },
        loading: false,
        currentImageIndex: 0,
        tabs: [
            { code: 'car_equipments', title: '車輛配備' },
            { code: 'dealer_intro', title: '店家介紹' }
        ],
        carEquipments: <#if equipmentsJson??>${equipmentsJson}<#else>[]</#if>,
        carGuarantees: <#if guaranteesJson??>${guaranteesJson}<#else>[]</#if>,
        lineIdHover: false,
        autoPlayTimer: null,
        autoPlayInterval: 5000,
        features: [],
        advertisements: []
    },
    mounted() {
        console.log('车辆详情页Vue实例已挂载');
        console.log('Vue实例数据:', this.$data);
        console.log('Vue实例方法:', Object.keys(this.$options.methods));
        
        
        this.initializePage();
        this.startAutoPlay();
        this.getFeatures();
        this.getAdvertisements();
        
        // 测试Vue实例是否正常工作
        setTimeout(() => {
            console.log('Vue实例测试 - 3秒后执行');
            const testButton = document.querySelector('[data-test="vue-test"]');
            if (testButton) {
                console.log('找到测试按钮');
            }
        }, 3000);
    },
    computed: {
        // 获取纯文本描述
        plainTextDescription() {
            if (!this.carInfo.saleDescription) return '';
            // 创建临时div元素来解析HTML
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = this.carInfo.saleDescription;
            return tempDiv.textContent || tempDiv.innerText || '';
        }
    },
    methods: {
        // 初始化页面
        initializePage() {
            // 设置默认日期为明天
            const appointmentDateInput = document.getElementById('appointmentDate');
            if (appointmentDateInput) {
                const tomorrow = new Date();
                tomorrow.setDate(tomorrow.getDate() + 1);
                appointmentDateInput.value = tomorrow.toISOString().split('T')[0];
            }
            
            // 初始化縮略圖点击事件
            this.initializeThumbnails();
        },
        
        // 初始化縮略圖
        initializeThumbnails() {
            // 这个方法现在由Vue的@click事件处理，不再需要手动绑定事件
        },
        
        // 切换图片
        changeImage(index) {
            this.currentImageIndex = index;
        },
        
        // 上一张图片
        prevImage() {
            if (this.images && this.images.length > 0) {
                this.currentImageIndex = this.currentImageIndex > 0 ? this.currentImageIndex - 1 : this.images.length - 1;
            }
        },
        
        // 下一张图片
        nextImage() {
            if (this.images && this.images.length > 0) {
                this.currentImageIndex = this.currentImageIndex < this.images.length - 1 ? this.currentImageIndex + 1 : 0;
            }
        },
        
        // 开始自动轮播
        startAutoPlay() {
            if (this.images && this.images.length > 1) {
                this.autoPlayTimer = setInterval(() => {
                    this.nextImage();
                }, this.autoPlayInterval);
            }
        },
        
        // 停止自动轮播
        stopAutoPlay() {
            if (this.autoPlayTimer) {
                clearInterval(this.autoPlayTimer);
                this.autoPlayTimer = null;
            }
        },
        
        // 打开灯箱
        openLightbox(index) {
            // 简单的图片查看功能，可以后续扩展为更复杂的灯箱组件
            if (this.images && this.images.length > 0) {
                window.open(this.images[index], '_blank');
            }
        },
        
        // 设置活动标签页
        setActiveTab(tab) {
            this.activeTab = tab;
        },
        
        // 获取HTML内容
        getHtmlContent(htmlContent) {
            if (!htmlContent) return '';
            return htmlContent;
        },
        
        // 获取纯文本内容（去除HTML标签）
        getPlainText(htmlContent) {
            if (!htmlContent) return '';
            // 创建临时div元素来解析HTML
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = htmlContent;
            return tempDiv.textContent || tempDiv.innerText || '';
        },
        
        // 联系Line
        contactLine() {
            if (this.dealerInfo.lineId) {
                window.open('https://line.me/ti/p/' + this.dealerInfo.lineId, '_blank');
            }
        },
        
        // 打开LINE联系
        openLineContact() {
            if (!this.dealerInfo.lineId) {
                alert('店家未提供LINE ID');
                return;
            }
            
            // 检测设备类型
            const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
            
            if (isMobile) {
                // 移动设备：直接打开LINE应用
                window.location.href = 'https://line.me/ti/p/' + this.dealerInfo.lineId;
            } else {
                // PC设备：打开LINE网页版
                window.open('https://line.me/ti/p/' + this.dealerInfo.lineId, '_blank');
            }
        },
        
        // 打开电话联系
        openPhoneContact() {
            if (!this.dealerInfo.companyMobile) {
                alert('店家未提供联系电话');
                return;
            }
            
            // 检测设备类型
            const isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
            
            if (isMobile) {
                // 移动设备：直接拨号
                window.location.href = 'tel:' + this.dealerInfo.companyMobile;
            } else {
                // PC设备：显示电话号码
                alert('联系电话：' + this.dealerInfo.companyMobile);
            }
        },
        
        // 处理图片加载失败
        handleImageError(event) {
            console.log('图片加载失败，使用默认图片');
            event.target.src = '/img/car/car4.jpg';
        },
        
        // 获取功能块数据
        getFeatures() {
            let _this = this;
            $.ajax({
                url: '/api/advertisement/list',
                method: 'GET',
                success: (data) => {
                    // 过滤出 advType=1 的数据，取前2个
                    const filteredFeatures = data.data.filter(ad => ad.advType === 1).slice(0, 2);
                    _this.features = filteredFeatures;
                },
                error: (error) => {
                    console.log('获取功能块数据失败:', error);
                }
            });
        },
        
        // 获取广告数据
        getAdvertisements() {
            let _this = this;
            $.ajax({
                url: '/api/advertisement/list',
                method: 'GET',
                success: (data) => {
                    // 过滤出 advType=0 的数据，取前2个
                    const filteredAds = data.data.filter(ad => ad.advType === 0).slice(0, 2);
                    _this.advertisements = filteredAds;
                },
                error: (error) => {
                    console.log('获取广告数据失败:', error);
                }
            });
        },
        
        // 顯示廣告內容
        showAdContent(adId, title, content) {
            // 檢查adId是否有效
            if (!adId || adId === 'null' || adId === '') {
                console.error('廣告ID無效:', adId);
                return;
            }
            // 打開新窗口顯示廣告內容頁面
            window.open('/ad-content/' + adId, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
        },
        
        // 获取缩略图URL
        getThumbnailUrl(originalUrl) {
            if (!originalUrl) return '';
            
            // 检查URL是否已经包含_90x90后缀
            if (originalUrl.includes('_90x90.')) {
                return originalUrl;
            }
            
            // 获取文件扩展名
            const lastDotIndex = originalUrl.lastIndexOf('.');
            if (lastDotIndex === -1) {
                return originalUrl;
            }
            
            // 在扩展名前插入_90x90
            const baseUrl = originalUrl.substring(0, lastDotIndex);
            const extension = originalUrl.substring(lastDotIndex);
            
            return baseUrl + '_90x90' + extension;
        },
        
        // 打开檢舉弹窗
        openReportModal() {
            // 检查用户是否已登录
            <#if user??>
                // 已登录，打开檢舉弹窗
                const modal = new bootstrap.Modal(document.getElementById('reportModal'));
                modal.show();
            <#else>
                // 未登录，提示用户先登录
                alert('請先登入後再進行檢舉');
                return;
            </#if>
        },
        
        // 提交檢舉
        async submitReport() {
            const reason = document.getElementById('reportReason').value;
            const description = document.getElementById('reportDescription').value;
            const anonymous = document.getElementById('reportAnonymous').checked;
            
            if (!reason) {
                alert('請選擇檢舉原因');
                return;
            }
            
            if (!description.trim()) {
                alert('請填寫詳細說明');
                return;
            }
            
            const reportData = {
                saleId: this.saleId,
                reason: reason,
                description: description.trim(),
                anonymous: anonymous,
                reporterId: <#if user??>${user.id}<#else>null</#if>
            };
            
            try {
                console.log('提交檢舉數據:', reportData);
                const response = await axios.post('/api/report/create', reportData);
                console.log('檢舉提交響應:', response.data);
                
                if (response.data.success) {
                    // 关闭檢舉弹窗
                    const reportModalElement = document.getElementById('reportModal');
                    if (reportModalElement) {
                        const reportModal = bootstrap.Modal.getInstance(reportModalElement);
                        if (reportModal) {
                            reportModal.hide();
                        } else {
                            // 备用方案：直接隐藏弹窗
                            reportModalElement.style.display = 'none';
                            document.body.classList.remove('modal-open');
                            const backdrop = document.querySelector('.modal-backdrop');
                            if (backdrop) {
                                backdrop.remove();
                            }
                        }
                    }
                    
                    // 清空表单
                    document.getElementById('reportForm').reset();
                    
                    // 显示成功提示
                    alert('檢舉已提交，我們會盡快處理您的檢舉');
                } else {
                    alert('檢舉提交失敗：' + (response.data.message || '未知錯誤'));
                }
            } catch (error) {
                console.error('檢舉提交錯誤:', error);
                alert('檢舉提交失敗，請稍後再試');
            }
        },
        
        // 格式化价格
        formatPrice(price) {
            if (!price) return '0';
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        
        // 格式化里程
        formatMileage(mileage) {
            if (!mileage) return '0';
            return mileage.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        
        
        // 打开预约弹窗
        openAppointmentModal() {
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
        },
        
        // 测试方法
        testMethod() {
            console.log('测试方法被调用');
            console.log('当前Vue实例:', this);
            console.log('saleId:', this.saleId);
            alert('Vue实例正常工作！saleId: ' + this.saleId);
        },
        
        // 提交预约
        async submitAppointment() {
            console.log('submitAppointment 方法被调用');
            
            const form = document.getElementById('appointmentForm');
            if (!form) {
                console.error('找不到预约表单');
                return;
            }
            
            // 验证表单
            if (!form.checkValidity()) {
                form.reportValidity();
                return;
            }
            
            // 获取表单数据
            const appointmentDate = document.getElementById('appointmentDate')?.value || '';
            const appointmentTime = document.getElementById('appointmentTime')?.value || '';
            const appointmentPhone = document.getElementById('appointmentPhone')?.value || '';
            const appointmentName = document.getElementById('appointmentNameDisplay')?.textContent?.trim() || '';
            const appointmentNote = document.getElementById('appointmentNote')?.value || '';
            
            console.log('表单数据:', {
                appointmentDate,
                appointmentTime,
                appointmentPhone,
                appointmentName,
                appointmentNote,
                saleId: this.saleId
            });
            
            // 组合日期和时间
            const appointmentDateTime = new Date(appointmentDate + 'T' + appointmentTime);
            
            const appointmentData = {
                carSaleId: this.saleId,
                appointmentName: appointmentName,
                appointmentPhone: appointmentPhone,
                appointmentTime: appointmentDateTime.toISOString(),
                appointmentNote: appointmentNote
            };
            
            console.log('准备提交的预约数据:', appointmentData);
            
            try {
                // 提交预约到后端
                const response = await axios.post('/appointment/api/create', appointmentData);
                console.log('预约提交响应:', response.data);
                
                if (response.data.success) {
                    // 关闭预约弹窗
                    const appointmentModalElement = document.getElementById('appointmentModal');
                    if (appointmentModalElement) {
                        const appointmentModal = bootstrap.Modal.getInstance(appointmentModalElement);
                        if (appointmentModal) {
                            appointmentModal.hide();
                        } else {
                            // 备用方案：直接隐藏弹窗
                            appointmentModalElement.style.display = 'none';
                            document.body.classList.remove('modal-open');
                            const backdrop = document.querySelector('.modal-backdrop');
                            if (backdrop) {
                                backdrop.remove();
                            }
                        }
                    }
                    
                    // 显示成功提示
                    alert('預約成功！我們會盡快與您聯繫確認預約詳情。');
                    
                    // 重置表单
                    form.reset();
                    
                    // 设置默认日期为明天
                    const appointmentDateInput = document.getElementById('appointmentDate');
                    if (appointmentDateInput) {
                        const tomorrow = new Date();
                        tomorrow.setDate(tomorrow.getDate() + 1);
                        appointmentDateInput.value = tomorrow.toISOString().split('T')[0];
                    }
                } else {
                    alert('预约失败：' + response.data.message);
                }
            } catch (error) {
                console.error('提交预约失败:', error);
                alert('预约失败，请稍后重试');
            }
        }
    }
});
} catch (error) {
    console.error('Vue实例初始化失败:', error);
    alert('页面初始化失败，请刷新页面重试');
}
</script>

<style>
/* 动作按钮容器样式 */
.action-buttons {
    display: flex;
    width: 100%;
    margin-bottom: 1rem;
}

/* 检车报告按钮样式 */
.btn-report {
    background-color: #6f42c1;
    border: none;
    color: white;
    border-radius: 10px;
    padding: 0.75rem 1rem;
    font-weight: 600;
    transition: all 0.3s ease;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
}

.btn-report:hover {
    background-color: #5a32a3;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(111, 66, 193, 0.4);
    color: white;
}

/* 预约按钮样式 - 使用网站主色调 */
.btn-appointment {
    background-color: #5ACFC9;
    border: none;
    color: white;
    border-radius: 10px;
    padding: 0.75rem 1rem;
    font-weight: 600;
    transition: all 0.3s ease;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
}

.btn-appointment:hover {
    background-color: #4AB8B2;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(90, 207, 201, 0.4);
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
    text-align: left !important;
    width: 100% !important;
    display: block !important;
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
 
 /* 响应式设计 */
@media (max-width: 768px) {
    .modal-dialog {
        margin: 1rem;
    }
    
    .action-buttons {
        flex-direction: column;
        gap: 0.75rem;
    }
    
    .action-buttons .flex-fill {
        width: 100%;
    }
    
    .action-buttons .me-2,
    .action-buttons .ms-2 {
        margin-left: 0 !important;
        margin-right: 0 !important;
    }
    
    .btn-appointment,
    .btn-report {
        padding: 0.75rem 1rem;
        font-size: 1rem;
        width: 100%;
        margin-bottom: 0;
    }
    
    .modal-xl {
        max-width: 95%;
    }
    
    .specs .row {
        margin: 0;
    }
    
    .specs .row > div {
        padding: 0.25rem 0.5rem;
    }
}

/* 功能块和广告块样式 */
.features-ads-section {
    padding: 20px 0;
}

.feature-card {
    text-align: center;
    padding: 8px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    transition: all 0.3s ease;
    height: 100%;
    border: 1px solid rgba(0,0,0,0.05);
    position: relative;
    overflow: visible;
}

.feature-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 20px rgba(0,0,0,0.15);
    background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
}

.feature-image-container {
    position: relative;
    overflow: hidden;
    height: 200px;
}

.feature-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
}

.feature-title {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: linear-gradient(transparent, rgba(0,0,0,0.7));
    color: white;
    padding: 1rem;
    margin: 0;
    font-size: 1.1rem;
    font-weight: bold;
    text-shadow: 1px 1px 2px rgba(0,0,0,0.8);
}

.feature-html-container {
    width: 100%;
    height: auto;
    margin: 0;
    padding: 15px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
}

.feature-html-content {
    width: 100%;
    height: auto;
    display: block;
    overflow: auto;
    text-align: left;
}

.feature-card:has(.feature-html-container) {
    height: auto;
    padding-bottom: 0;
}

.ad-card {
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
    height: 200px;
    position: relative;
    padding: 0;
}

.ad-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 15px rgba(0,0,0,0.2);
}

.ad-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.ad-title-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.8);
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.5rem;
    font-weight: bold;
    text-align: center;
    padding: 20px;
    opacity: 0;
    transition: opacity 0.3s ease;
}

.ad-card:hover .ad-title-overlay {
    opacity: 1;
}

.ad-title-bottom {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
    color: white;
    padding: 20px 15px 15px;
    font-size: 1.2rem;
    font-weight: bold;
    text-align: center;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
    transform: translateY(0);
    transition: all 0.3s ease;
}

.ad-card:hover .ad-title-bottom {
    transform: translateY(-5px);
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.9));
}

.ad-link {
    display: block;
    width: 100%;
    height: 100%;
    text-decoration: none;
    color: inherit;
    position: relative;
}

.ad-link:hover {
    text-decoration: none;
    color: inherit;
}

.ad-content-link {
    cursor: pointer;
    display: block;
    width: 100%;
    height: 100%;
}

@media (max-width: 576px) {
    .btn-appointment,
    .btn-report {
        font-size: 0.9rem;
        padding: 0.65rem 1rem;
    }
    
    .car-title {
        font-size: 1.5rem;
    }
    
    .price .h4 {
        font-size: 1.5rem;
    }
}
</style>

