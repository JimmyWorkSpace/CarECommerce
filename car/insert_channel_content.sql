-- 插入频道页面的富文本内容
INSERT INTO `car_rich_content` (
    `title`, 
    `content`, 
    `contentType`, 
    `showOrder`, 
    `delFlag`, 
    `createTime`
) VALUES 
-- 卖车频道
(
    '卖车频道',
    '<div class="channel-section">
        <div class="hero-banner">
            <h1>卖车频道</h1>
            <p class="subtitle">专业卖车服务，让您的爱车快速变现</p>
        </div>
        
        <div class="service-grid">
            <div class="service-card">
                <div class="service-icon">
                    <i class="bi bi-car-front"></i>
                </div>
                <h3>免费评估</h3>
                <p>专业评估师免费上门评估，3分钟快速报价</p>
                <a href="#" class="btn btn-primary">立即评估</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="bi bi-shield-check"></i>
                </div>
                <h3>安全保障</h3>
                <p>资金安全保障，交易过程透明，让您放心卖车</p>
                <a href="#" class="btn btn-primary">了解详情</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="bi bi-lightning"></i>
                </div>
                <h3>快速成交</h3>
                <p>海量买家资源，最快当天成交，价格更优</p>
                <a href="#" class="btn btn-primary">开始卖车</a>
            </div>
        </div>
        
        <div class="process-section">
            <h2>卖车流程</h2>
            <div class="process-steps">
                <div class="step">
                    <div class="step-number">1</div>
                    <h4>在线评估</h4>
                    <p>填写车辆信息，获得初步报价</p>
                </div>
                <div class="step">
                    <div class="step-number">2</div>
                    <h4>上门检测</h4>
                    <p>专业评估师上门检测车况</p>
                </div>
                <div class="step">
                    <div class="step-number">3</div>
                    <h4>确认价格</h4>
                    <p>根据检测结果确定最终价格</p>
                </div>
                <div class="step">
                    <div class="step-number">4</div>
                    <h4>完成交易</h4>
                    <p>签署合同，完成过户，收到车款</p>
                </div>
            </div>
        </div>
    </div>
    
    <style>
        .channel-section {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .hero-banner {
            text-align: center;
            padding: 60px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            margin-bottom: 40px;
        }
        
        .hero-banner h1 {
            font-size: 3em;
            margin-bottom: 15px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.3em;
            opacity: 0.9;
        }
        
        .service-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }
        
        .service-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s ease;
        }
        
        .service-card:hover {
            transform: translateY(-5px);
        }
        
        .service-icon {
            font-size: 3em;
            color: #667eea;
            margin-bottom: 20px;
        }
        
        .service-card h3 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .service-card p {
            color: #666;
            margin-bottom: 20px;
            line-height: 1.6;
        }
        
        .process-section {
            background: #f8f9fa;
            padding: 40px;
            border-radius: 15px;
            text-align: center;
        }
        
        .process-section h2 {
            color: #333;
            margin-bottom: 40px;
        }
        
        .process-steps {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 30px;
        }
        
        .step {
            background: white;
            padding: 30px 20px;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        
        .step-number {
            width: 50px;
            height: 50px;
            background: #667eea;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5em;
            font-weight: bold;
            margin: 0 auto 20px;
        }
        
        .step h4 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .step p {
            color: #666;
            font-size: 0.9em;
        }
        
        @media (max-width: 768px) {
            .hero-banner h1 {
                font-size: 2em;
            }
            
            .service-grid {
                grid-template-columns: 1fr;
            }
            
            .process-steps {
                grid-template-columns: 1fr;
            }
        }
    </style>',
    2,
    1,
    0,
    NOW()
),

-- 购车服务
(
    '购车服务',
    '<div class="channel-section">
        <div class="hero-banner">
            <h1>购车服务</h1>
            <p class="subtitle">专业购车指导，让您轻松买到心仪爱车</p>
        </div>
        
        <div class="service-features">
            <div class="feature-row">
                <div class="feature-content">
                    <h3>专业选车顾问</h3>
                    <p>资深汽车专家一对一服务，根据您的需求和预算，为您推荐最适合的车型。我们了解每款车的优缺点，帮您做出明智选择。</p>
                    <ul>
                        <li>免费咨询，专业建议</li>
                        <li>车型对比分析</li>
                        <li>价格行情指导</li>
                        <li>购车方案定制</li>
                    </ul>
                </div>
                <div class="feature-image">
                    <i class="bi bi-person-check" style="font-size: 5em; color: #667eea;"></i>
                </div>
            </div>
            
            <div class="feature-row reverse">
                <div class="feature-content">
                    <h3>全程陪同看车</h3>
                    <p>专业顾问全程陪同，从看车到试驾，从验车到议价，每个环节都有专业指导，确保您买到放心车。</p>
                    <ul>
                        <li>陪同看车验车</li>
                        <li>试驾指导</li>
                        <li>价格谈判协助</li>
                        <li>合同审核把关</li>
                    </ul>
                </div>
                <div class="feature-image">
                    <i class="bi bi-car-front-fill" style="font-size: 5em; color: #667eea;"></i>
                </div>
            </div>
            
            <div class="feature-row">
                <div class="feature-content">
                    <h3>金融贷款服务</h3>
                    <p>与多家银行和金融机构合作，为您提供最优惠的贷款方案，让购车更轻松。</p>
                    <ul>
                        <li>低息贷款方案</li>
                        <li>快速审批流程</li>
                        <li>灵活还款方式</li>
                        <li>专业金融顾问</li>
                    </ul>
                </div>
                <div class="feature-image">
                    <i class="bi bi-credit-card" style="font-size: 5em; color: #667eea;"></i>
                </div>
            </div>
        </div>
        
        <div class="contact-section">
            <h2>立即预约购车服务</h2>
            <p>专业顾问24小时内联系您，为您提供专属购车服务</p>
            <div class="contact-buttons">
                <a href="tel:400-123-4567" class="btn btn-primary btn-lg">
                    <i class="bi bi-telephone"></i> 400-123-4567
                </a>
                <a href="#" class="btn btn-outline-primary btn-lg">
                    <i class="bi bi-chat-dots"></i> 在线咨询
                </a>
            </div>
        </div>
    </div>
    
    <style>
        .channel-section {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .hero-banner {
            text-align: center;
            padding: 60px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            margin-bottom: 40px;
        }
        
        .hero-banner h1 {
            font-size: 3em;
            margin-bottom: 15px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.3em;
            opacity: 0.9;
        }
        
        .service-features {
            margin-bottom: 50px;
        }
        
        .feature-row {
            display: flex;
            align-items: center;
            gap: 40px;
            margin-bottom: 60px;
            padding: 30px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        }
        
        .feature-row.reverse {
            flex-direction: row-reverse;
        }
        
        .feature-content {
            flex: 1;
        }
        
        .feature-content h3 {
            color: #333;
            font-size: 1.8em;
            margin-bottom: 20px;
        }
        
        .feature-content p {
            color: #666;
            line-height: 1.6;
            margin-bottom: 20px;
        }
        
        .feature-content ul {
            color: #666;
            padding-left: 20px;
        }
        
        .feature-content li {
            margin-bottom: 8px;
        }
        
        .feature-image {
            flex: 0 0 200px;
            text-align: center;
        }
        
        .contact-section {
            background: #f8f9fa;
            padding: 40px;
            border-radius: 15px;
            text-align: center;
        }
        
        .contact-section h2 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 30px;
        }
        
        .contact-buttons {
            display: flex;
            gap: 20px;
            justify-content: center;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 15px 30px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
            border: none;
        }
        
        .btn-primary:hover {
            background: #5a6fd8;
            transform: translateY(-2px);
        }
        
        .btn-outline-primary {
            background: transparent;
            color: #667eea;
            border: 2px solid #667eea;
        }
        
        .btn-outline-primary:hover {
            background: #667eea;
            color: white;
        }
        
        @media (max-width: 768px) {
            .hero-banner h1 {
                font-size: 2em;
            }
            
            .feature-row {
                flex-direction: column;
                text-align: center;
            }
            
            .feature-row.reverse {
                flex-direction: column;
            }
            
            .contact-buttons {
                flex-direction: column;
                align-items: center;
            }
        }
    </style>',
    2,
    2,
    0,
    NOW()
),

-- 消费者保障
(
    '消费者保障',
    '<div class="channel-section">
        <div class="hero-banner">
            <h1>消费者保障</h1>
            <p class="subtitle">全方位保障体系，让您购车无忧</p>
        </div>
        
        <div class="guarantee-grid">
            <div class="guarantee-card">
                <div class="guarantee-icon">
                    <i class="bi bi-shield-check"></i>
                </div>
                <h3>车况保障</h3>
                <p>每辆车都经过专业检测，确保车况真实，如有隐瞒，全额退款</p>
                <div class="guarantee-details">
                    <ul>
                        <li>专业检测报告</li>
                        <li>车况透明展示</li>
                        <li>重大事故车全额退款</li>
                        <li>调表车双倍赔偿</li>
                    </ul>
                </div>
            </div>
            
            <div class="guarantee-card">
                <div class="guarantee-icon">
                    <i class="bi bi-currency-dollar"></i>
                </div>
                <h3>价格保障</h3>
                <p>明码标价，无隐性费用，价格透明，让您购车更放心</p>
                <div class="guarantee-details">
                    <ul>
                        <li>明码标价，无隐性费用</li>
                        <li>价格对比，确保合理</li>
                        <li>议价透明，公平交易</li>
                        <li>价格保护，买贵退差</li>
                    </ul>
                </div>
            </div>
            
            <div class="guarantee-card">
                <div class="guarantee-icon">
                    <i class="bi bi-tools"></i>
                </div>
                <h3>售后保障</h3>
                <p>完善的售后服务体系，让您购车后无后顾之忧</p>
                <div class="guarantee-details">
                    <ul>
                        <li>7天无理由退车</li>
                        <li>30天质保服务</li>
                        <li>终身免费检测</li>
                        <li>24小时客服热线</li>
                    </ul>
                </div>
            </div>
            
            <div class="guarantee-card">
                <div class="guarantee-icon">
                    <i class="bi bi-file-earmark-check"></i>
                </div>
                <h3>过户保障</h3>
                <p>专业过户服务，确保手续齐全，过户无忧</p>
                <div class="guarantee-details">
                    <ul>
                        <li>专业过户代办</li>
                        <li>手续齐全保证</li>
                        <li>过户失败全额退款</li>
                        <li>过户进度实时跟踪</li>
                    </ul>
                </div>
            </div>
        </div>
        
        <div class="compensation-section">
            <h2>保障承诺</h2>
            <div class="compensation-grid">
                <div class="compensation-item">
                    <div class="compensation-amount">100%</div>
                    <div class="compensation-desc">全额退款保障</div>
                </div>
                <div class="compensation-item">
                    <div class="compensation-amount">7天</div>
                    <div class="compensation-desc">无理由退车</div>
                </div>
                <div class="compensation-item">
                    <div class="compensation-amount">30天</div>
                    <div class="compensation-desc">质保服务</div>
                </div>
                <div class="compensation-item">
                    <div class="compensation-amount">24小时</div>
                    <div class="compensation-desc">客服响应</div>
                </div>
            </div>
        </div>
        
        <div class="contact-section">
            <h2>遇到问题？联系我们</h2>
            <p>专业客服团队，为您解决购车过程中的任何问题</p>
            <div class="contact-info">
                <div class="contact-item">
                    <i class="bi bi-telephone"></i>
                    <span>客服热线：400-123-4567</span>
                </div>
                <div class="contact-item">
                    <i class="bi bi-envelope"></i>
                    <span>邮箱：service@example.com</span>
                </div>
                <div class="contact-item">
                    <i class="bi bi-chat-dots"></i>
                    <span>在线客服：24小时在线</span>
                </div>
            </div>
        </div>
    </div>
    
    <style>
        .channel-section {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .hero-banner {
            text-align: center;
            padding: 60px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            margin-bottom: 40px;
        }
        
        .hero-banner h1 {
            font-size: 3em;
            margin-bottom: 15px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.3em;
            opacity: 0.9;
        }
        
        .guarantee-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }
        
        .guarantee-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s ease;
        }
        
        .guarantee-card:hover {
            transform: translateY(-5px);
        }
        
        .guarantee-icon {
            font-size: 3em;
            color: #667eea;
            margin-bottom: 20px;
        }
        
        .guarantee-card h3 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .guarantee-card p {
            color: #666;
            margin-bottom: 20px;
            line-height: 1.6;
        }
        
        .guarantee-details ul {
            text-align: left;
            color: #666;
            padding-left: 20px;
        }
        
        .guarantee-details li {
            margin-bottom: 8px;
        }
        
        .compensation-section {
            background: #f8f9fa;
            padding: 40px;
            border-radius: 15px;
            text-align: center;
            margin-bottom: 40px;
        }
        
        .compensation-section h2 {
            color: #333;
            margin-bottom: 30px;
        }
        
        .compensation-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 30px;
        }
        
        .compensation-item {
            background: white;
            padding: 30px 20px;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        
        .compensation-amount {
            font-size: 2.5em;
            font-weight: bold;
            color: #667eea;
            margin-bottom: 10px;
        }
        
        .compensation-desc {
            color: #666;
            font-size: 1.1em;
        }
        
        .contact-section {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .contact-section h2 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 30px;
        }
        
        .contact-info {
            display: flex;
            justify-content: center;
            gap: 40px;
            flex-wrap: wrap;
        }
        
        .contact-item {
            display: flex;
            align-items: center;
            gap: 10px;
            color: #666;
        }
        
        .contact-item i {
            color: #667eea;
            font-size: 1.2em;
        }
        
        @media (max-width: 768px) {
            .hero-banner h1 {
                font-size: 2em;
            }
            
            .guarantee-grid {
                grid-template-columns: 1fr;
            }
            
            .compensation-grid {
                grid-template-columns: repeat(2, 1fr);
            }
            
            .contact-info {
                flex-direction: column;
                gap: 20px;
            }
        }
    </style>',
    2,
    3,
    0,
    NOW()
),

-- 加盟专区
(
    '加盟专区',
    '<div class="channel-section">
        <div class="hero-banner">
            <h1>加盟专区</h1>
            <p class="subtitle">携手共创汽车销售新未来</p>
        </div>
        
        <div class="join-benefits">
            <h2>加盟优势</h2>
            <div class="benefits-grid">
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="bi bi-graph-up"></i>
                    </div>
                    <h3>品牌优势</h3>
                    <p>知名品牌，市场认知度高，客户信任度强</p>
                </div>
                
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="bi bi-people"></i>
                    </div>
                    <h3>技术支持</h3>
                    <p>专业培训体系，技术支持，运营指导</p>
                </div>
                
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="bi bi-cash-stack"></i>
                    </div>
                    <h3>盈利保障</h3>
                    <p>成熟的盈利模式，稳定的收入来源</p>
                </div>
                
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="bi bi-shield-check"></i>
                    </div>
                    <h3>风险控制</h3>
                    <p>完善的风险控制体系，降低经营风险</p>
                </div>
            </div>
        </div>
        
        <div class="join-process">
            <h2>加盟流程</h2>
            <div class="process-timeline">
                <div class="timeline-item">
                    <div class="timeline-number">1</div>
                    <div class="timeline-content">
                        <h4>提交申请</h4>
                        <p>填写加盟申请表，提交相关资料</p>
                    </div>
                </div>
                
                <div class="timeline-item">
                    <div class="timeline-number">2</div>
                    <div class="timeline-content">
                        <h4>资质审核</h4>
                        <p>总部审核加盟资质，评估合作条件</p>
                    </div>
                </div>
                
                <div class="timeline-item">
                    <div class="timeline-number">3</div>
                    <div class="timeline-content">
                        <h4>实地考察</h4>
                        <p>总部派员实地考察，确认合作意向</p>
                    </div>
                </div>
                
                <div class="timeline-item">
                    <div class="timeline-number">4</div>
                    <div class="timeline-content">
                        <h4>签署合同</h4>
                        <p>签署加盟合同，缴纳相关费用</p>
                    </div>
                </div>
                
                <div class="timeline-item">
                    <div class="timeline-number">5</div>
                    <div class="timeline-content">
                        <h4>培训开业</h4>
                        <p>接受专业培训，正式开业运营</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="requirements-section">
            <h2>加盟条件</h2>
            <div class="requirements-grid">
                <div class="requirement-item">
                    <h4>资金要求</h4>
                    <ul>
                        <li>加盟费：10万元</li>
                        <li>保证金：5万元</li>
                        <li>流动资金：50万元</li>
                        <li>总投资：100万元起</li>
                    </ul>
                </div>
                
                <div class="requirement-item">
                    <h4>场地要求</h4>
                    <ul>
                        <li>展厅面积：200㎡以上</li>
                        <li>停车位：20个以上</li>
                        <li>地理位置：交通便利</li>
                        <li>装修标准：按总部要求</li>
                    </ul>
                </div>
                
                <div class="requirement-item">
                    <h4>人员要求</h4>
                    <ul>
                        <li>负责人：大专以上学历</li>
                        <li>销售团队：5人以上</li>
                        <li>技术人员：2人以上</li>
                        <li>管理人员：1人以上</li>
                    </ul>
                </div>
                
                <div class="requirement-item">
                    <h4>其他要求</h4>
                    <ul>
                        <li>合法经营资质</li>
                        <li>良好的商业信誉</li>
                        <li>当地市场资源</li>
                        <li>合作经营理念</li>
                    </ul>
                </div>
            </div>
        </div>
        
        <div class="contact-section">
            <h2>立即申请加盟</h2>
            <p>专业招商团队，为您提供详细的加盟咨询服务</p>
            <div class="contact-buttons">
                <a href="tel:400-123-4567" class="btn btn-primary btn-lg">
                    <i class="bi bi-telephone"></i> 招商热线：400-123-4567
                </a>
                <a href="#" class="btn btn-outline-primary btn-lg">
                    <i class="bi bi-file-earmark-text"></i> 下载加盟资料
                </a>
            </div>
            
            <div class="contact-info">
                <div class="contact-item">
                    <i class="bi bi-envelope"></i>
                    <span>邮箱：join@example.com</span>
                </div>
                <div class="contact-item">
                    <i class="bi bi-geo-alt"></i>
                    <span>地址：北京市朝阳区汽车销售中心</span>
                </div>
            </div>
        </div>
    </div>
    
    <style>
        .channel-section {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .hero-banner {
            text-align: center;
            padding: 60px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            margin-bottom: 40px;
        }
        
        .hero-banner h1 {
            font-size: 3em;
            margin-bottom: 15px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.3em;
            opacity: 0.9;
        }
        
        .join-benefits {
            margin-bottom: 50px;
        }
        
        .join-benefits h2 {
            text-align: center;
            color: #333;
            margin-bottom: 40px;
        }
        
        .benefits-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
        }
        
        .benefit-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s ease;
        }
        
        .benefit-card:hover {
            transform: translateY(-5px);
        }
        
        .benefit-icon {
            font-size: 3em;
            color: #667eea;
            margin-bottom: 20px;
        }
        
        .benefit-card h3 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .benefit-card p {
            color: #666;
            line-height: 1.6;
        }
        
        .join-process {
            background: #f8f9fa;
            padding: 40px;
            border-radius: 15px;
            margin-bottom: 50px;
        }
        
        .join-process h2 {
            text-align: center;
            color: #333;
            margin-bottom: 40px;
        }
        
        .process-timeline {
            display: flex;
            flex-direction: column;
            gap: 30px;
        }
        
        .timeline-item {
            display: flex;
            align-items: center;
            gap: 20px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        
        .timeline-number {
            width: 50px;
            height: 50px;
            background: #667eea;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5em;
            font-weight: bold;
            flex-shrink: 0;
        }
        
        .timeline-content h4 {
            color: #333;
            margin-bottom: 5px;
        }
        
        .timeline-content p {
            color: #666;
            margin: 0;
        }
        
        .requirements-section {
            margin-bottom: 50px;
        }
        
        .requirements-section h2 {
            text-align: center;
            color: #333;
            margin-bottom: 40px;
        }
        
        .requirements-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
        }
        
        .requirement-item {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        }
        
        .requirement-item h4 {
            color: #333;
            margin-bottom: 20px;
            text-align: center;
        }
        
        .requirement-item ul {
            color: #666;
            padding-left: 20px;
        }
        
        .requirement-item li {
            margin-bottom: 8px;
        }
        
        .contact-section {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .contact-section h2 {
            color: #333;
            margin-bottom: 15px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 30px;
        }
        
        .contact-buttons {
            display: flex;
            gap: 20px;
            justify-content: center;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 15px 30px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
            border: none;
        }
        
        .btn-primary:hover {
            background: #5a6fd8;
            transform: translateY(-2px);
        }
        
        .btn-outline-primary {
            background: transparent;
            color: #667eea;
            border: 2px solid #667eea;
        }
        
        .btn-outline-primary:hover {
            background: #667eea;
            color: white;
        }
        
        .contact-info {
            display: flex;
            justify-content: center;
            gap: 40px;
            flex-wrap: wrap;
        }
        
        .contact-item {
            display: flex;
            align-items: center;
            gap: 10px;
            color: #666;
        }
        
        .contact-item i {
            color: #667eea;
            font-size: 1.2em;
        }
        
        @media (max-width: 768px) {
            .hero-banner h1 {
                font-size: 2em;
            }
            
            .benefits-grid {
                grid-template-columns: 1fr;
            }
            
            .timeline-item {
                flex-direction: column;
                text-align: center;
            }
            
            .requirements-grid {
                grid-template-columns: 1fr;
            }
            
            .contact-buttons {
                flex-direction: column;
                align-items: center;
            }
            
            .contact-info {
                flex-direction: column;
                gap: 20px;
            }
        }
    </style>',
    2,
    4,
    0,
    NOW()
);
