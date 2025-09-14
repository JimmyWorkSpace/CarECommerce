-- 插入关于页面的富文本内容
INSERT INTO `car_rich_content` (
    `title`, 
    `content`, 
    `contentType`, 
    `showOrder`, 
    `delFlag`, 
    `createTime`
) VALUES (
    '关于我们',
    '<div class="about-page">
        <div class="hero-section">
            <h1>关于我们</h1>
            <p class="subtitle">专业的汽车销售平台，为您提供优质的购车服务</p>
        </div>
        
        <div class="content-section">
            <h2>公司简介</h2>
            <p>我们是一家专业的汽车销售平台，成立于2020年，致力于为客户提供优质的汽车购买服务。我们拥有丰富的汽车行业经验和专业的服务团队，为客户提供从选车、看车到购车的全程专业指导。</p>
            
            <h2>我们的优势</h2>
            <ul>
                <li><strong>专业团队：</strong>拥有多年汽车行业经验的专业销售顾问</li>
                <li><strong>优质车源：</strong>严格筛选，确保每一辆车都经过专业检测</li>
                <li><strong>透明价格：</strong>明码标价，无隐性费用，让您购车更放心</li>
                <li><strong>贴心服务：</strong>从选车到提车，全程跟踪服务</li>
                <li><strong>售后保障：</strong>完善的售后服务体系，让您用车无忧</li>
            </ul>
            
            <h2>服务理念</h2>
            <p>我们始终坚持以客户为中心的服务理念，秉承"诚信、专业、贴心"的服务宗旨，为每一位客户提供最优质的购车体验。我们相信，只有客户满意，才是我们最大的成功。</p>
            
            <h2>联系我们</h2>
            <div class="contact-info">
                <p><strong>客服热线：</strong>400-123-4567</p>
                <p><strong>邮箱地址：</strong>service@example.com</p>
                <p><strong>营业时间：</strong>周一至周日 9:00-18:00</p>
                <p><strong>公司地址：</strong>北京市朝阳区汽车销售中心</p>
            </div>
        </div>
    </div>
    
    <style>
        .about-page {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            font-family: "Microsoft YaHei", Arial, sans-serif;
            line-height: 1.6;
        }
        
        .hero-section {
            text-align: center;
            padding: 40px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            margin-bottom: 30px;
        }
        
        .hero-section h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            font-weight: 300;
        }
        
        .subtitle {
            font-size: 1.2em;
            opacity: 0.9;
        }
        
        .content-section {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .content-section h2 {
            color: #333;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
            margin-top: 30px;
            margin-bottom: 20px;
        }
        
        .content-section p {
            color: #666;
            margin-bottom: 15px;
            text-align: justify;
        }
        
        .content-section ul {
            color: #666;
            margin-bottom: 20px;
        }
        
        .content-section li {
            margin-bottom: 8px;
        }
        
        .contact-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        
        .contact-info p {
            margin-bottom: 10px;
        }
        
        @media (max-width: 768px) {
            .about-page {
                padding: 10px;
            }
            
            .hero-section h1 {
                font-size: 2em;
            }
            
            .content-section {
                padding: 20px;
            }
        }
    </style>',
    1,
    1,
    0,
    NOW()
);
