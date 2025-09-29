<div class="container mt-4">
    <div class="row">
        <div class="col-12">
            <h2>拦截器测试页面</h2>
            <p>这个页面用于测试页面数据拦截器是否正常工作。</p>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h5>菜单数据测试</h5>
                </div>
                <div class="card-body">
                    <#if menus?? && menus?has_content>
                        <p>菜单数据已加载，共 ${menus?size} 个菜单项：</p>
                        <ul>
                            <#list menus as menu>
                                <li>${menu.title} - ${menu.linkUrl}</li>
                            </#list>
                        </ul>
                    <#else>
                        <p class="text-danger">菜单数据未加载</p>
                    </#if>
                </div>
            </div>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h5>页脚配置数据测试</h5>
                </div>
                <div class="card-body">
                    <#if configContent??>
                        <p>页脚配置数据已加载：</p>
                        <ul>
                            <li>客服热线：${configContent.kefu!'未设置'}</li>
                            <li>邮箱：${configContent.youxiang!'未设置'}</li>
                            <li>地址：${configContent.dizhi!'未设置'}</li>
                        </ul>
                    <#else>
                        <p class="text-danger">页脚配置数据未加载</p>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>
