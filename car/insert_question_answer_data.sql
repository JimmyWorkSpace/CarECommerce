-- 为频道生成问答数据
-- 频道ID对应关系：
-- 10 - 賣車頻道
-- 11 - 購車服務  
-- 12 - 消費者保障
-- 13 - 加盟專區

INSERT INTO `car_question_answer` (`channelId`, `question`, `answer`, `showOrder`, `delFlag`, `createTime`) VALUES

-- 賣車頻道 (channelId=10) 问答
(10, '如何快速賣車？', '<div class="qa-answer">
    <h4>快速賣車流程</h4>
    <p>我們提供專業的賣車服務，讓您的愛車快速變現：</p>
    <ol>
        <li><strong>免費評估</strong>：專業評估師上門評估，3分鐘快速報價</li>
        <li><strong>安全保障</strong>：資金安全保障，交易過程透明</li>
        <li><strong>快速成交</strong>：海量買家資源，最快當天成交</li>
        <li><strong>專業服務</strong>：一站式服務，從評估到過戶全程指導</li>
    </ol>
    <div class="contact-info">
        <p><strong>立即開始：</strong>撥打客服熱線 400-123-4567</p>
    </div>
</div>', 1, 0, NOW()),

(10, '賣車需要準備什麼資料？', '<div class="qa-answer">
    <h4>賣車必備資料</h4>
    <p>為了確保交易順利進行，請準備以下資料：</p>
    <div class="documents-list">
        <div class="document-item">
            <h5>📄 車輛證件</h5>
            <ul>
                <li>車輛行駛證</li>
                <li>車輛登記證書</li>
                <li>車輛購置稅完稅證明</li>
            </ul>
        </div>
        <div class="document-item">
            <h5>👤 個人證件</h5>
            <ul>
                <li>車主身份證</li>
                <li>車輛保險單</li>
            </ul>
        </div>
    </div>
    <div class="note">
        <p><strong>注意：</strong>我們會協助您辦理所有手續，讓賣車更簡單！</p>
    </div>
</div>', 2, 0, NOW()),

(10, '車輛評估是否收費？', '<div class="qa-answer">
    <h4>免費評估服務</h4>
    <div class="highlight-box">
        <p><strong>✅ 完全免費</strong> - 我們的車輛評估服務不收取任何費用！</p>
    </div>
    <h5>評估服務內容：</h5>
    <ul>
        <li>專業評估師上門服務</li>
        <li>詳細車輛檢測</li>
        <li>準確市場報價</li>
        <li>專業建議諮詢</li>
    </ul>
    <div class="benefits">
        <h5>評估優勢：</h5>
        <p>• 專業設備檢測<br>• 市場行情分析<br>• 透明報價流程<br>• 無隱藏費用</p>
    </div>
</div>', 3, 0, NOW()),

(10, '賣車價格如何確定？', '<div class="qa-answer">
    <h4>價格評估標準</h4>
    <p>我們採用專業的評估標準來確定車輛價格：</p>
    <div class="price-factors">
        <div class="factor-item">
            <h5>🚗 車輛狀況</h5>
            <p>車齡、里程數、保養記錄、事故歷史</p>
        </div>
        <div class="factor-item">
            <h5>📊 市場行情</h5>
            <p>同款車型市場價格、供需關係</p>
        </div>
        <div class="factor-item">
            <h5>🔧 配置等級</h5>
            <p>車型配置、選裝件、改裝情況</p>
        </div>
    </div>
    <div class="guarantee">
        <p><strong>價格保障：</strong>我們承諾提供市場最優價格，如有更高報價，我們會相應調整。</p>
    </div>
</div>', 4, 0, NOW()),

-- 購車服務 (channelId=11) 问答
(11, '如何選擇合適的車輛？', '<div class="qa-answer">
    <h4>專業選車指導</h4>
    <p>我們的專業選車顧問會根據您的具體情況推薦最適合的車型：</p>
    <div class="selection-criteria">
        <div class="criteria-item">
            <h5>💰 預算考量</h5>
            <p>根據您的預算範圍推薦性價比最高的車型</p>
        </div>
        <div class="criteria-item">
            <h5>🎯 使用需求</h5>
            <p>家庭用車、商務用車、代步工具等不同需求</p>
        </div>
        <div class="criteria-item">
            <h5>🏠 使用環境</h5>
            <p>城市道路、高速公路、鄉村道路等路況考慮</p>
        </div>
    </div>
    <div class="service-features">
        <h5>我們的服務：</h5>
        <ul>
            <li>一對一專業諮詢</li>
            <li>多款車型對比分析</li>
            <li>試駕安排</li>
            <li>購車方案定制</li>
        </ul>
    </div>
</div>', 1, 0, NOW()),

(11, '購車流程是怎樣的？', '<div class="qa-answer">
    <h4>完整購車流程</h4>
    <div class="process-steps">
        <div class="step">
            <div class="step-number">1</div>
            <div class="step-content">
                <h5>需求諮詢</h5>
                <p>了解您的購車需求和預算</p>
            </div>
        </div>
        <div class="step">
            <div class="step-number">2</div>
            <div class="step-content">
                <h5>車型推薦</h5>
                <p>專業顧問推薦適合的車型</p>
            </div>
        </div>
        <div class="step">
            <div class="step-number">3</div>
            <div class="step-content">
                <h5>預約看車</h5>
                <p>安排時間實地看車驗車</p>
            </div>
        </div>
        <div class="step">
            <div class="step-number">4</div>
            <div class="step-content">
                <h5>車輛檢測</h5>
                <p>專業檢測師進行詳細檢測</p>
            </div>
        </div>
        <div class="step">
            <div class="step-number">5</div>
            <div class="step-content">
                <h5>價格談判</h5>
                <p>協助您獲得最優價格</p>
            </div>
        </div>
        <div class="step">
            <div class="step-number">6</div>
            <div class="step-content">
                <h5>合同簽署</h5>
                <p>簽署購車合同，辦理手續</p>
            </div>
        </div>
        <div class="step">
            <div class="step-number">7</div>
            <div class="step-content">
                <h5>過戶辦理</h5>
                <p>專業代辦過戶手續</p>
            </div>
        </div>
    </div>
    <div class="service-guarantee">
        <p><strong>全程陪同：</strong>我們會全程陪同指導，讓您購車無憂！</p>
    </div>
</div>', 2, 0, NOW()),

(11, '是否提供貸款服務？', '<div class="qa-answer">
    <h4>金融貸款服務</h4>
    <p>我們與多家銀行和金融機構合作，為您提供最優惠的貸款方案：</p>
    <div class="loan-options">
        <div class="loan-item">
            <h5>🏦 銀行貸款</h5>
            <ul>
                <li>低息貸款方案</li>
                <li>快速審批流程</li>
                <li>靈活還款方式</li>
            </ul>
        </div>
        <div class="loan-item">
            <h5>💳 金融公司</h5>
            <ul>
                <li>門檻較低</li>
                <li>審批速度快</li>
                <li>手續簡便</li>
            </ul>
        </div>
    </div>
    <div class="loan-benefits">
        <h5>貸款優勢：</h5>
        <div class="benefit-grid">
            <div class="benefit-item">✅ 利率優惠</div>
            <div class="benefit-item">✅ 快速放款</div>
            <div class="benefit-item">✅ 專業指導</div>
            <div class="benefit-item">✅ 售後服務</div>
        </div>
    </div>
    <div class="contact-loan">
        <p><strong>立即諮詢：</strong>撥打 400-123-4567 了解最新貸款政策</p>
    </div>
</div>', 3, 0, NOW()),

(11, '購車後有哪些售後服務？', '<div class="qa-answer">
    <h4>完善售後服務</h4>
    <p>購車後我們提供全方位的售後服務保障：</p>
    <div class="after-sales">
        <div class="service-category">
            <h5>🔧 維修保養</h5>
            <ul>
                <li>定期保養提醒</li>
                <li>維修服務推薦</li>
                <li>原廠配件供應</li>
                <li>技術支援服務</li>
            </ul>
        </div>
        <div class="service-category">
            <h5>📞 客服支援</h5>
            <ul>
                <li>24小時客服熱線</li>
                <li>多種聯絡方式</li>
                <li>專業諮詢服務</li>
                <li>問題追蹤處理</li>
            </ul>
        </div>
        <div class="service-category">
            <h5>🛡️ 質保服務</h5>
            <ul>
                <li>30天質保期</li>
                <li>7天無理由退換</li>
                <li>終身免費檢測</li>
                <li>售後保障承諾</li>
            </ul>
        </div>
    </div>
    <div class="service-promise">
        <p><strong>服務承諾：</strong>讓您購車後無後顧之憂，享受優質的售後服務！</p>
    </div>
</div>', 4, 0, NOW()),

-- 消費者保障 (channelId=12) 问答
(12, '如何保障車輛品質？', '<div class="qa-answer">
    <h4>全方位品質保障</h4>
    <p>我們提供嚴格的品質保障體系，確保每輛車都經過專業檢測：</p>
    <div class="quality-assurance">
        <div class="assurance-item">
            <h5>🔍 專業檢測</h5>
            <ul>
                <li>專業檢測師檢測</li>
                <li>詳細檢測報告</li>
                <li>車況透明展示</li>
                <li>問題預警提醒</li>
            </ul>
        </div>
        <div class="assurance-item">
            <h5>🛡️ 品質承諾</h5>
            <ul>
                <li>重大事故車全額退款</li>
                <li>調表車雙倍賠償</li>
                <li>隱瞞車況全額退款</li>
                <li>品質問題免費維修</li>
            </ul>
        </div>
    </div>
    <div class="detection-process">
        <h5>檢測流程：</h5>
        <ol>
            <li>外觀檢測</li>
            <li>內飾檢測</li>
            <li>發動機檢測</li>
            <li>底盤檢測</li>
            <li>電路檢測</li>
            <li>路試檢測</li>
        </ol>
    </div>
    <div class="quality-guarantee">
        <p><strong>品質保證：</strong>每輛車都經過嚴格檢測，品質有保障！</p>
    </div>
</div>', 1, 0, NOW()),

(12, '如果車輛有問題怎麼辦？', '<div class="qa-answer">
    <h4>問題處理保障</h4>
    <p>如果車輛出現問題，我們提供完善的解決方案：</p>
    <div class="problem-solutions">
        <div class="solution-item">
            <h5>🚨 緊急處理</h5>
            <ul>
                <li>24小時客服熱線</li>
                <li>緊急救援服務</li>
                <li>快速響應機制</li>
                <li>專業技術支援</li>
            </ul>
        </div>
        <div class="solution-item">
            <h5>🔧 維修服務</h5>
            <ul>
                <li>免費檢測診斷</li>
                <li>專業維修服務</li>
                <li>原廠配件供應</li>
                <li>維修質保服務</li>
            </ul>
        </div>
        <div class="solution-item">
            <h5>💰 賠償保障</h5>
            <ul>
                <li>7天無理由退車</li>
                <li>30天質保服務</li>
                <li>問題車輛全額退款</li>
                <li>雙倍賠償承諾</li>
            </ul>
        </div>
    </div>
    <div class="contact-support">
        <h5>聯繫方式：</h5>
        <p>📞 客服熱線：400-123-4567<br>📧 郵箱：service@example.com<br>💬 在線客服：24小時在線</p>
    </div>
    <div class="support-promise">
        <p><strong>服務承諾：</strong>遇到問題不要慌，我們會第一時間為您解決！</p>
    </div>
</div>', 2, 0, NOW()),

(12, '過戶手續誰來辦理？', '<div class="qa-answer">
    <h4>專業過戶服務</h4>
    <p>我們提供專業的過戶代辦服務，讓您省心省力：</p>
    <div class="transfer-service">
        <div class="service-item">
            <h5>📋 代辦服務</h5>
            <ul>
                <li>專業過戶代辦</li>
                <li>手續齊全保證</li>
                <li>過戶進度跟蹤</li>
                <li>全程服務指導</li>
            </ul>
        </div>
        <div class="service-item">
            <h5>🛡️ 過戶保障</h5>
            <ul>
                <li>過戶失敗全額退款</li>
                <li>手續問題免費重辦</li>
                <li>時間承諾保障</li>
                <li>專業團隊服務</li>
            </ul>
        </div>
    </div>
    <div class="transfer-process">
        <h5>過戶流程：</h5>
        <ol>
            <li>資料準備</li>
            <li>預約辦理</li>
            <li>現場辦理</li>
            <li>證件領取</li>
            <li>完成過戶</li>
        </ol>
    </div>
    <div class="transfer-benefits">
        <h5>服務優勢：</h5>
        <div class="benefit-list">
            <span class="benefit-tag">✅ 專業代辦</span>
            <span class="benefit-tag">✅ 快速辦理</span>
            <span class="benefit-tag">✅ 手續齊全</span>
            <span class="benefit-tag">✅ 過戶無憂</span>
        </div>
    </div>
    <div class="transfer-guarantee">
        <p><strong>過戶承諾：</strong>專業團隊代辦，確保過戶順利完成！</p>
    </div>
</div>', 3, 0, NOW()),

(12, '售後服務包括哪些？', '<div class="qa-answer">
    <h4>完善售後服務體系</h4>
    <p>我們提供全方位的售後服務，讓您購車後無後顧之憂：</p>
    <div class="after-sales-services">
        <div class="service-category">
            <h5>🔧 維修保養</h5>
            <ul>
                <li>定期保養提醒</li>
                <li>維修服務推薦</li>
                <li>原廠配件供應</li>
                <li>技術支援服務</li>
                <li>上門維修服務</li>
            </ul>
        </div>
        <div class="service-category">
            <h5>📞 客服支援</h5>
            <ul>
                <li>24小時客服熱線</li>
                <li>多種聯絡方式</li>
                <li>專業諮詢服務</li>
                <li>問題追蹤處理</li>
                <li>投訴建議處理</li>
            </ul>
        </div>
        <div class="service-category">
            <h5>🛡️ 質保服務</h5>
            <ul>
                <li>30天質保期</li>
                <li>7天無理由退換</li>
                <li>終身免費檢測</li>
                <li>售後保障承諾</li>
                <li>延長質保服務</li>
            </ul>
        </div>
    </div>
    <div class="service-features">
        <h5>服務特色：</h5>
        <div class="feature-grid">
            <div class="feature-item">🚀 快速響應</div>
            <div class="feature-item">🎯 專業服務</div>
            <div class="feature-item">💯 品質保證</div>
            <div class="feature-item">🔄 持續跟進</div>
        </div>
    </div>
    <div class="service-commitment">
        <p><strong>服務承諾：</strong>讓您享受專業、貼心、放心的售後服務！</p>
    </div>
</div>', 4, 0, NOW()),

-- 加盟專區 (channelId=13) 问答
(13, '加盟需要什麼條件？', '<div class="qa-answer">
    <h4>加盟條件要求</h4>
    <p>加入我們的加盟體系，需要滿足以下條件：</p>
    <div class="requirements">
        <div class="requirement-category">
            <h5>💰 資金要求</h5>
            <ul>
                <li>加盟費：10萬元</li>
                <li>保證金：5萬元</li>
                <li>流動資金：50萬元</li>
                <li>總投資：100萬元起</li>
            </ul>
        </div>
        <div class="requirement-category">
            <h5>🏢 場地要求</h5>
            <ul>
                <li>展廳面積：200㎡以上</li>
                <li>停車位：20個以上</li>
                <li>地理位置：交通便利</li>
                <li>裝修標準：按總部要求</li>
            </ul>
        </div>
        <div class="requirement-category">
            <h5>👥 人員要求</h5>
            <ul>
                <li>負責人：大專以上學歷</li>
                <li>銷售團隊：5人以上</li>
                <li>技術人員：2人以上</li>
                <li>管理人員：1人以上</li>
            </ul>
        </div>
        <div class="requirement-category">
            <h5>📋 其他要求</h5>
            <ul>
                <li>合法經營資質</li>
                <li>良好的商業信譽</li>
                <li>當地市場資源</li>
                <li>合作經營理念</li>
            </ul>
        </div>
    </div>
    <div class="application-process">
        <h5>申請流程：</h5>
        <ol>
            <li>提交申請表</li>
            <li>資質審核</li>
            <li>實地考察</li>
            <li>簽署合同</li>
            <li>培訓開業</li>
        </ol>
    </div>
    <div class="contact-join">
        <p><strong>立即申請：</strong>撥打招商熱線 400-123-4567 了解更多詳情</p>
    </div>
</div>', 1, 0, NOW()),

(13, '加盟後有哪些支持？', '<div class="qa-answer">
    <h4>全方位加盟支持</h4>
    <p>加盟後我們提供全方位的支持服務：</p>
    <div class="support-services">
        <div class="support-item">
            <h5>🏷️ 品牌支持</h5>
            <ul>
                <li>知名品牌授權</li>
                <li>品牌形象統一</li>
                <li>市場推廣支持</li>
                <li>品牌宣傳材料</li>
            </ul>
        </div>
        <div class="support-item">
            <h5>🎓 培訓支持</h5>
            <ul>
                <li>業務技能培訓</li>
                <li>服務標準培訓</li>
                <li>管理培訓</li>
                <li>持續教育</li>
            </ul>
        </div>
        <div class="support-item">
            <h5>💻 技術支持</h5>
            <ul>
                <li>線上交易平台</li>
                <li>管理系統</li>
                <li>技術培訓</li>
                <li>系統維護</li>
            </ul>
        </div>
        <div class="support-item">
            <h5>📈 運營支持</h5>
            <ul>
                <li>運營指導</li>
                <li>市場分析</li>
                <li>營銷策略</li>
                <li>業績提升</li>
            </ul>
        </div>
    </div>
    <div class="support-benefits">
        <h5>支持優勢：</h5>
        <div class="benefit-grid">
            <div class="benefit-item">✅ 專業團隊</div>
            <div class="benefit-item">✅ 成熟模式</div>
            <div class="benefit-item">✅ 持續支持</div>
            <div class="benefit-item">✅ 成功保障</div>
        </div>
    </div>
    <div class="support-commitment">
        <p><strong>支持承諾：</strong>讓您的加盟事業快速起步，穩步發展！</p>
    </div>
</div>', 2, 0, NOW()),

(13, '加盟費用是多少？', '<div class="qa-answer">
    <h4>加盟費用明細</h4>
    <p>我們提供透明的加盟費用結構：</p>
    <div class="fee-structure">
        <div class="fee-item">
            <h5>💼 加盟費用</h5>
            <div class="fee-details">
                <div class="fee-row">
                    <span class="fee-label">加盟費：</span>
                    <span class="fee-amount">10萬元</span>
                </div>
                <div class="fee-row">
                    <span class="fee-label">保證金：</span>
                    <span class="fee-amount">5萬元</span>
                </div>
                <div class="fee-row">
                    <span class="fee-label">流動資金：</span>
                    <span class="fee-amount">50萬元</span>
                </div>
                <div class="fee-row total">
                    <span class="fee-label">總投資：</span>
                    <span class="fee-amount">100萬元起</span>
                </div>
            </div>
        </div>
    </div>
    <div class="fee-explanation">
        <h5>費用說明：</h5>
        <ul>
            <li><strong>加盟費：</strong>品牌使用費，一次性繳納</li>
            <li><strong>保證金：</strong>履約保證，合同期滿可退還</li>
            <li><strong>流動資金：</strong>日常運營資金，可靈活使用</li>
            <li><strong>總投資：</strong>包含場地、裝修、設備等</li>
        </ul>
    </div>
    <div class="fee-benefits">
        <h5>投資回報：</h5>
        <div class="return-info">
            <p>• 成熟商業模式<br>• 穩定收入來源<br>• 專業運營支持<br>• 快速回本週期</p>
        </div>
    </div>
    <div class="fee-contact">
        <p><strong>詳細諮詢：</strong>撥打 400-123-4567 了解具體投資方案</p>
    </div>
</div>', 3, 0, NOW()),

(13, '加盟流程是怎樣的？', '<div class="qa-answer">
    <h4>加盟申請流程</h4>
    <p>我們提供清晰的加盟流程，讓您輕鬆加入：</p>
    <div class="join-process">
        <div class="process-step">
            <div class="step-number">1</div>
            <div class="step-content">
                <h5>提交申請</h5>
                <p>填寫加盟申請表，提交相關資料</p>
                <ul>
                    <li>個人/企業基本資料</li>
                    <li>資金證明</li>
                    <li>場地證明</li>
                </ul>
            </div>
        </div>
        <div class="process-step">
            <div class="step-number">2</div>
            <div class="step-content">
                <h5>資質審核</h5>
                <p>總部審核加盟資質，評估合作條件</p>
                <ul>
                    <li>資料完整性檢查</li>
                    <li>資質符合性評估</li>
                    <li>合作條件確認</li>
                </ul>
            </div>
        </div>
        <div class="process-step">
            <div class="step-number">3</div>
            <div class="step-content">
                <h5>實地考察</h5>
                <p>總部派員實地考察，確認合作意向</p>
                <ul>
                    <li>場地實地查看</li>
                    <li>當地市場調研</li>
                    <li>合作意向確認</li>
                </ul>
            </div>
        </div>
        <div class="process-step">
            <div class="step-number">4</div>
            <div class="step-content">
                <h5>簽署合同</h5>
                <p>簽署加盟合同，繳納相關費用</p>
                <ul>
                    <li>合同條款確認</li>
                    <li>費用繳納</li>
                    <li>正式合作開始</li>
                </ul>
            </div>
        </div>
        <div class="process-step">
            <div class="step-number">5</div>
            <div class="step-content">
                <h5>培訓開業</h5>
                <p>接受專業培訓，正式開業運營</p>
                <ul>
                    <li>業務技能培訓</li>
                    <li>系統操作培訓</li>
                    <li>開業指導</li>
                </ul>
            </div>
        </div>
    </div>
    <div class="process-timeline">
        <h5>時間安排：</h5>
        <p>整個流程約需 <strong>15-30天</strong>，我們會全程跟進，確保順利完成。</p>
    </div>
    <div class="process-support">
        <p><strong>全程支持：</strong>專業團隊全程指導，讓您輕鬆完成加盟流程！</p>
    </div>
</div>', 4, 0, NOW());
