<!-- 错误页面 -->
<div class="error-page">
    <div class="error-container">
        <div class="error-icon">
            <i class="bi bi-exclamation-triangle"></i>
        </div>
        <h1 class="error-title">页面加载失败</h1>
        <p class="error-message">
            <#if error?? && error?has_content>
                ${error}
            <#else>
                抱歉，页面内容无法正常显示。
            </#if>
        </p>
        <div class="error-actions">
            <a href="/" class="btn btn-primary">
                <i class="bi bi-house"></i>
                返回首页
            </a>
            <button onclick="history.back()" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left"></i>
                返回上页
            </button>
        </div>
    </div>
</div>

<style>
.error-page {
    min-height: 60vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 40px 20px;
}

.error-container {
    text-align: center;
    max-width: 500px;
    padding: 40px;
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.error-icon {
    font-size: 4rem;
    color: #ff6b6b;
    margin-bottom: 20px;
}

.error-title {
    color: #333;
    font-size: 1.8rem;
    font-weight: 600;
    margin-bottom: 15px;
}

.error-message {
    color: #666;
    font-size: 1.1rem;
    margin-bottom: 30px;
    line-height: 1.5;
}

.error-actions {
    display: flex;
    gap: 15px;
    justify-content: center;
    flex-wrap: wrap;
}

.error-actions .btn {
    padding: 12px 24px;
    border-radius: 8px;
    font-weight: 600;
    transition: all 0.3s ease;
}

.error-actions .btn i {
    margin-right: 8px;
}

.btn-primary {
    background: linear-gradient(135deg, #5ACFC9 0%, #4AB8B2 100%);
    border: none;
    color: white;
}

.btn-primary:hover {
    background: linear-gradient(135deg, #4AB8B2 0%, #3AA7A1 100%);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(90, 207, 201, 0.3);
}

.btn-outline-secondary {
    border: 2px solid #6c757d;
    color: #6c757d;
    background: transparent;
}

.btn-outline-secondary:hover {
    background: #6c757d;
    color: white;
    transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 768px) {
    .error-container {
        padding: 30px 20px;
    }
    
    .error-icon {
        font-size: 3rem;
    }
    
    .error-title {
        font-size: 1.5rem;
    }
    
    .error-message {
        font-size: 1rem;
    }
    
    .error-actions {
        flex-direction: column;
        align-items: center;
    }
    
    .error-actions .btn {
        width: 100%;
        max-width: 200px;
    }
}
</style> 