-- 删除现有的频道内容
DELETE FROM `car_rich_content` WHERE `contentType` = 2;

-- 插入简单的频道富文本内容
INSERT INTO `car_rich_content` (
    `title`, 
    `content`, 
    `contentType`, 
    `showOrder`, 
    `delFlag`, 
    `createTime`
) VALUES 
-- 賣車頻道
(
    '賣車頻道',
    '<div class="channel-content">
        <div class="content-header">
            <h2>賣車頻道</h2>
            <p class="subtitle">專業賣車服務，讓您的愛車快速變現</p>
        </div>
        
        <div class="content-body">
            <div class="service-item">
                <h3>免費評估</h3>
                <p>專業評估師免費上門評估，3分鐘快速報價，讓您了解愛車的真實價值。</p>
            </div>
            
            <div class="service-item">
                <h3>安全保障</h3>
                <p>資金安全保障，交易過程透明，讓您放心賣車，無後顧之憂。</p>
            </div>
            
            <div class="service-item">
                <h3>快速成交</h3>
                <p>海量買家資源，最快當天成交，價格更優，服務更貼心。</p>
            </div>
            
            <div class="service-item">
                <h3>專業服務</h3>
                <p>一站式服務，從評估到過戶，全程專業指導，讓賣車更簡單。</p>
            </div>
        </div>
        
        <div class="contact-section">
            <h3>立即開始賣車</h3>
            <p>專業團隊為您提供優質服務</p>
            <div class="contact-info">
                <span class="phone">客服熱線：400-123-4567</span>
                <span class="time">服務時間：9:00-18:00</span>
            </div>
        </div>
    </div>
    
    <style>
        .channel-content {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .content-header {
            text-align: center;
            margin-bottom: 40px;
            padding: 30px;
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB3AD 100%);
            color: white;
            border-radius: 10px;
        }
        
        .content-header h2 {
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .content-body {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        
        .service-item {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 3px 10px rgba(90, 207, 201, 0.1);
            border-left: 4px solid #5ACFC9;
            transition: transform 0.3s ease;
        }
        
        .service-item:hover {
            transform: translateY(-3px);
        }
        
        .service-item h3 {
            color: #5ACFC9;
            font-size: 1.3em;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .service-item p {
            color: #666;
            line-height: 1.6;
            margin: 0;
        }
        
        .contact-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            border: 2px solid #5ACFC9;
        }
        
        .contact-section h3 {
            color: #5ACFC9;
            margin-bottom: 10px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 20px;
        }
        
        .contact-info {
            display: flex;
            justify-content: center;
            gap: 30px;
            flex-wrap: wrap;
        }
        
        .phone, .time {
            color: #5ACFC9;
            font-weight: 500;
        }
        
        @media (max-width: 768px) {
            .content-header h2 {
                font-size: 2em;
            }
            
            .content-body {
                grid-template-columns: 1fr;
            }
            
            .contact-info {
                flex-direction: column;
                gap: 15px;
            }
        }
    </style>',
    2,
    1,
    0,
    NOW()
),

-- 購車服務
(
    '購車服務',
    '<div class="channel-content">
        <div class="content-header">
            <h2>購車服務</h2>
            <p class="subtitle">專業購車指導，讓您輕鬆買到心儀愛車</p>
        </div>
        
        <div class="content-body">
            <div class="service-item">
                <h3>專業選車顧問</h3>
                <p>資深汽車專家一對一服務，根據您的需求和預算，為您推薦最適合的車型。</p>
            </div>
            
            <div class="service-item">
                <h3>全程陪同看車</h3>
                <p>專業顧問全程陪同，從看車到試駕，從驗車到議價，每個環節都有專業指導。</p>
            </div>
            
            <div class="service-item">
                <h3>金融貸款服務</h3>
                <p>與多家銀行和金融機構合作，為您提供最優惠的貸款方案，讓購車更輕鬆。</p>
            </div>
            
            <div class="service-item">
                <h3>過戶代辦服務</h3>
                <p>專業過戶代辦，確保手續齊全，過戶無憂，讓您省心省力。</p>
            </div>
        </div>
        
        <div class="contact-section">
            <h3>立即預約購車服務</h3>
            <p>專業顧問24小時內聯繫您，為您提供專屬購車服務</p>
            <div class="contact-info">
                <span class="phone">客服熱線：400-123-4567</span>
                <span class="time">服務時間：9:00-18:00</span>
            </div>
        </div>
    </div>
    
    <style>
        .channel-content {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .content-header {
            text-align: center;
            margin-bottom: 40px;
            padding: 30px;
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB3AD 100%);
            color: white;
            border-radius: 10px;
        }
        
        .content-header h2 {
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .content-body {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        
        .service-item {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 3px 10px rgba(90, 207, 201, 0.1);
            border-left: 4px solid #5ACFC9;
            transition: transform 0.3s ease;
        }
        
        .service-item:hover {
            transform: translateY(-3px);
        }
        
        .service-item h3 {
            color: #5ACFC9;
            font-size: 1.3em;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .service-item p {
            color: #666;
            line-height: 1.6;
            margin: 0;
        }
        
        .contact-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            border: 2px solid #5ACFC9;
        }
        
        .contact-section h3 {
            color: #5ACFC9;
            margin-bottom: 10px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 20px;
        }
        
        .contact-info {
            display: flex;
            justify-content: center;
            gap: 30px;
            flex-wrap: wrap;
        }
        
        .phone, .time {
            color: #5ACFC9;
            font-weight: 500;
        }
        
        @media (max-width: 768px) {
            .content-header h2 {
                font-size: 2em;
            }
            
            .content-body {
                grid-template-columns: 1fr;
            }
            
            .contact-info {
                flex-direction: column;
                gap: 15px;
            }
        }
    </style>',
    2,
    2,
    0,
    NOW()
),

-- 消費者保障
(
    '消費者保障',
    '<div class="channel-content">
        <div class="content-header">
            <h2>消費者保障</h2>
            <p class="subtitle">全方位保障體系，讓您購車無憂</p>
        </div>
        
        <div class="content-body">
            <div class="service-item">
                <h3>車況保障</h3>
                <p>每輛車都經過專業檢測，確保車況真實，如有隱瞞，全額退款。</p>
            </div>
            
            <div class="service-item">
                <h3>價格保障</h3>
                <p>明碼標價，無隱性費用，價格透明，讓您購車更放心。</p>
            </div>
            
            <div class="service-item">
                <h3>售後保障</h3>
                <p>完善的售後服務體系，讓您購車後無後顧之憂。</p>
            </div>
            
            <div class="service-item">
                <h3>過戶保障</h3>
                <p>專業過戶服務，確保手續齊全，過戶無憂。</p>
            </div>
        </div>
        
        <div class="guarantee-section">
            <h3>保障承諾</h3>
            <div class="guarantee-items">
                <div class="guarantee-item">
                    <span class="guarantee-number">100%</span>
                    <span class="guarantee-text">全額退款保障</span>
                </div>
                <div class="guarantee-item">
                    <span class="guarantee-number">7天</span>
                    <span class="guarantee-text">無理由退車</span>
                </div>
                <div class="guarantee-item">
                    <span class="guarantee-number">30天</span>
                    <span class="guarantee-text">質保服務</span>
                </div>
                <div class="guarantee-item">
                    <span class="guarantee-number">24小時</span>
                    <span class="guarantee-text">客服響應</span>
                </div>
            </div>
        </div>
        
        <div class="contact-section">
            <h3>遇到問題？聯繫我們</h3>
            <p>專業客服團隊，為您解決購車過程中的任何問題</p>
            <div class="contact-info">
                <span class="phone">客服熱線：400-123-4567</span>
                <span class="time">服務時間：24小時在線</span>
            </div>
        </div>
    </div>
    
    <style>
        .channel-content {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .content-header {
            text-align: center;
            margin-bottom: 40px;
            padding: 30px;
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB3AD 100%);
            color: white;
            border-radius: 10px;
        }
        
        .content-header h2 {
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .content-body {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        
        .service-item {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 3px 10px rgba(90, 207, 201, 0.1);
            border-left: 4px solid #5ACFC9;
            transition: transform 0.3s ease;
        }
        
        .service-item:hover {
            transform: translateY(-3px);
        }
        
        .service-item h3 {
            color: #5ACFC9;
            font-size: 1.3em;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .service-item p {
            color: #666;
            line-height: 1.6;
            margin: 0;
        }
        
        .guarantee-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            margin-bottom: 30px;
            border: 2px solid #5ACFC9;
        }
        
        .guarantee-section h3 {
            color: #5ACFC9;
            margin-bottom: 25px;
        }
        
        .guarantee-items {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 20px;
        }
        
        .guarantee-item {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(90, 207, 201, 0.1);
        }
        
        .guarantee-number {
            display: block;
            font-size: 2em;
            font-weight: bold;
            color: #5ACFC9;
            margin-bottom: 8px;
        }
        
        .guarantee-text {
            color: #666;
            font-size: 0.9em;
        }
        
        .contact-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            border: 2px solid #5ACFC9;
        }
        
        .contact-section h3 {
            color: #5ACFC9;
            margin-bottom: 10px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 20px;
        }
        
        .contact-info {
            display: flex;
            justify-content: center;
            gap: 30px;
            flex-wrap: wrap;
        }
        
        .phone, .time {
            color: #5ACFC9;
            font-weight: 500;
        }
        
        @media (max-width: 768px) {
            .content-header h2 {
                font-size: 2em;
            }
            
            .content-body {
                grid-template-columns: 1fr;
            }
            
            .guarantee-items {
                grid-template-columns: repeat(2, 1fr);
            }
            
            .contact-info {
                flex-direction: column;
                gap: 15px;
            }
        }
    </style>',
    2,
    3,
    0,
    NOW()
),

-- 加盟專區
(
    '加盟專區',
    '<div class="channel-content">
        <div class="content-header">
            <h2>加盟專區</h2>
            <p class="subtitle">攜手共創汽車銷售新未來</p>
        </div>
        
        <div class="content-body">
            <div class="service-item">
                <h3>品牌優勢</h3>
                <p>知名品牌，市場認知度高，客戶信任度強，為您的事業保駕護航。</p>
            </div>
            
            <div class="service-item">
                <h3>技術支持</h3>
                <p>專業培訓體系，技術支持，運營指導，讓您快速上手。</p>
            </div>
            
            <div class="service-item">
                <h3>盈利保障</h3>
                <p>成熟的盈利模式，穩定的收入來源，讓您的投資更有保障。</p>
            </div>
            
            <div class="service-item">
                <h3>風險控制</h3>
                <p>完善的風險控制體系，降低經營風險，讓您經營更安心。</p>
            </div>
        </div>
        
        <div class="requirements-section">
            <h3>加盟條件</h3>
            <div class="requirements-grid">
                <div class="requirement-item">
                    <h4>資金要求</h4>
                    <ul>
                        <li>加盟費：10萬元</li>
                        <li>保證金：5萬元</li>
                        <li>流動資金：50萬元</li>
                        <li>總投資：100萬元起</li>
                    </ul>
                </div>
                
                <div class="requirement-item">
                    <h4>場地要求</h4>
                    <ul>
                        <li>展廳面積：200㎡以上</li>
                        <li>停車位：20個以上</li>
                        <li>地理位置：交通便利</li>
                        <li>裝修標準：按總部要求</li>
                    </ul>
                </div>
            </div>
        </div>
        
        <div class="contact-section">
            <h3>立即申請加盟</h3>
            <p>專業招商團隊，為您提供詳細的加盟諮詢服務</p>
            <div class="contact-info">
                <span class="phone">招商熱線：400-123-4567</span>
                <span class="time">服務時間：9:00-18:00</span>
            </div>
        </div>
    </div>
    
    <style>
        .channel-content {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
        .content-header {
            text-align: center;
            margin-bottom: 40px;
            padding: 30px;
            background: linear-gradient(135deg, #5ACFC9 0%, #4AB3AD 100%);
            color: white;
            border-radius: 10px;
        }
        
        .content-header h2 {
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .content-body {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        
        .service-item {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 3px 10px rgba(90, 207, 201, 0.1);
            border-left: 4px solid #5ACFC9;
            transition: transform 0.3s ease;
        }
        
        .service-item:hover {
            transform: translateY(-3px);
        }
        
        .service-item h3 {
            color: #5ACFC9;
            font-size: 1.3em;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .service-item p {
            color: #666;
            line-height: 1.6;
            margin: 0;
        }
        
        .requirements-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            border: 2px solid #5ACFC9;
        }
        
        .requirements-section h3 {
            color: #5ACFC9;
            text-align: center;
            margin-bottom: 25px;
        }
        
        .requirements-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
        }
        
        .requirement-item {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(90, 207, 201, 0.1);
        }
        
        .requirement-item h4 {
            color: #5ACFC9;
            margin-bottom: 15px;
            text-align: center;
        }
        
        .requirement-item ul {
            color: #666;
            padding-left: 20px;
            margin: 0;
        }
        
        .requirement-item li {
            margin-bottom: 8px;
            line-height: 1.5;
        }
        
        .contact-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 10px;
            text-align: center;
            border: 2px solid #5ACFC9;
        }
        
        .contact-section h3 {
            color: #5ACFC9;
            margin-bottom: 10px;
        }
        
        .contact-section p {
            color: #666;
            margin-bottom: 20px;
        }
        
        .contact-info {
            display: flex;
            justify-content: center;
            gap: 30px;
            flex-wrap: wrap;
        }
        
        .phone, .time {
            color: #5ACFC9;
            font-weight: 500;
        }
        
        @media (max-width: 768px) {
            .content-header h2 {
                font-size: 2em;
            }
            
            .content-body {
                grid-template-columns: 1fr;
            }
            
            .requirements-grid {
                grid-template-columns: 1fr;
            }
            
            .contact-info {
                flex-direction: column;
                gap: 15px;
            }
        }
    </style>',
    2,
    4,
    0,
    NOW()
);
