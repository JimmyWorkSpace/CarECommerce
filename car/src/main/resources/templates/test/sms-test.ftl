<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h4>短信验证码登录测试</h4>
                </div>
                <div class="card-body">
                    <div class="alert alert-info">
                        <h5>测试说明：</h5>
                        <ul>
                            <li>输入手机号后点击"发送验证码"</li>
                            <li>验证码会打印在控制台，请查看控制台输出</li>
                            <li>输入验证码后点击"登录"</li>
                            <li>登录成功后会跳转到首页</li>
                        </ul>
                    </div>
                    
                    <form action="/login" method="post" id="testLoginForm">
                        <input type="hidden" name="returnUrl" value="/test/sms-test">
                        
                        <div class="mb-3">
                            <label for="phoneNumber" class="form-label">手机号码</label>
                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" 
                                   placeholder="请输入手机号码" required pattern="^1[3-9]\d{9}$" value="13800138000">
                        </div>
                        
                        <div class="mb-3">
                            <label for="smsCode" class="form-label">短信验证码</label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="smsCode" name="smsCode" 
                                       placeholder="请输入验证码" required maxlength="6" pattern="\d{6}">
                                <button type="button" class="btn btn-outline-primary" id="sendSmsBtn">
                                    <span id="sendSmsText">发送验证码</span>
                                </button>
                            </div>
                            <div class="form-text">验证码将发送到您的手机，有效期5分钟</div>
                        </div>
                        
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">登录</button>
                        </div>
                    </form>
                    
                    <div class="mt-4">
                        <h5>当前登录状态：</h5>
                        <#if user?? && user?has_content>
                            <div class="alert alert-success">
                                <strong>已登录</strong><br>
                                用户ID: ${user.id!''}<br>
                                用户名: ${user.username!''}<br>
                                昵称: ${user.nickname!''}<br>
                                手机号: ${user.phone!''}
                            </div>
                            <a href="/logout" class="btn btn-danger">退出登录</a>
                        <#else>
                            <div class="alert alert-warning">
                                <strong>未登录</strong>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const phoneNumberInput = document.getElementById('phoneNumber');
    const smsCodeInput = document.getElementById('smsCode');
    const sendSmsBtn = document.getElementById('sendSmsBtn');
    const sendSmsText = document.getElementById('sendSmsText');
    const testLoginForm = document.getElementById('testLoginForm');
    
    let countdown = 0;
    let countdownTimer = null;
    
    // 发送验证码
    sendSmsBtn.addEventListener('click', function() {
        const phoneNumber = phoneNumberInput.value.trim();
        
        // 验证手机号格式
        if (!/^1[3-9]\d{9}$/.test(phoneNumber)) {
            alert('请输入正确的手机号码格式');
            phoneNumberInput.focus();
            return;
        }
        
        // 发送验证码请求
        fetch('/api/sms/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                phoneNumber: phoneNumber
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 开始倒计时
                startCountdown();
                alert('验证码已发送，请查看控制台输出\n验证码: ' + data.code);
            } else {
                alert(data.message || '发送失败，请稍后重试');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('发送失败，请稍后重试');
        });
    });
    
    // 倒计时功能
    function startCountdown() {
        countdown = 60;
        sendSmsBtn.disabled = true;
        sendSmsText.textContent = `${countdown}秒后重发`;
        
        countdownTimer = setInterval(function() {
            countdown--;
            if (countdown <= 0) {
                clearInterval(countdownTimer);
                sendSmsBtn.disabled = false;
                sendSmsText.textContent = '发送验证码';
            } else {
                sendSmsText.textContent = `${countdown}秒后重发`;
            }
        }, 1000);
    }
    
    // 表单提交验证
    testLoginForm.addEventListener('submit', function(e) {
        const phoneNumber = phoneNumberInput.value.trim();
        const smsCode = smsCodeInput.value.trim();
        
        if (!/^1[3-9]\d{9}$/.test(phoneNumber)) {
            e.preventDefault();
            alert('请输入正确的手机号码格式');
            phoneNumberInput.focus();
            return;
        }
        
        if (!/^\d{6}$/.test(smsCode)) {
            e.preventDefault();
            alert('请输入6位数字验证码');
            smsCodeInput.focus();
            return;
        }
    });
    
    // 手机号输入时自动格式化
    phoneNumberInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) {
            value = value.substring(0, 11);
        }
        e.target.value = value;
    });
    
    // 验证码输入时只允许数字
    smsCodeInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 6) {
            value = value.substring(0, 6);
        }
        e.target.value = value;
    });
});
</script>
