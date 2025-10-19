<style>
    .channel-content-text {
        line-height: 1.8;
        color: #333;
    }
    
    .channel-content-text h3 {
        color: #333;
        font-weight: 600;
        margin-bottom: 20px;
        font-size: 1.5rem;
    }
    
    .channel-content-text h4 {
        color: #5ACFC9;
        font-weight: 600;
        margin: 25px 0 10px 0;
        font-size: 1.2rem;
    }
    
    .channel-content-text p {
        color: #666;
        line-height: 1.8;
        margin-bottom: 15px;
        font-size: 1rem;
    }
    
    /* 响应式设计 */
    @media (max-width: 768px) {
        .channel-content-text h3 {
            font-size: 1.3rem;
        }
        
        .channel-content-text h4 {
            font-size: 1.1rem;
        }
        
        .channel-content-text p {
            font-size: 0.95rem;
        }
    }
</style>

<div class="container">
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <#if channelContent??>
                        <!-- 频道内容 -->
                        <div class="channel-content-text">
                            ${channelContent.content!''}
                        </div>
                    <#else>
                        <div class="alert alert-info" role="alert">
                            <h4 class="alert-heading">频道内容</h4>
                            <p>暂无频道内容，请联系管理员添加。</p>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
