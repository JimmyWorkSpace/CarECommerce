package cc.carce.sale.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信验证码服务
 */
@Service
@Slf4j
public class SmsService {
    
    @Autowired
    private Environment environment;
    
    // 存储验证码的Map，key为手机号，value为验证码和过期时间
    private static final Map<String, SmsCode> SMS_CODE_MAP = new ConcurrentHashMap<>();
    
    // 验证码有效期（分钟）
    private static final int CODE_EXPIRE_MINUTES = 5;
    
    // 开发环境固定验证码
    private static final String DEV_FIXED_CODE = "123456";
    
    /**
     * 发送短信验证码
     * @param phoneNumber 手机号
     * @return 验证码
     */
    public String sendSmsCode(String phoneNumber) {
        String code;
        
        // 检查当前环境，如果是dev或test环境，使用固定验证码
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isDevOrTest = false;
        
        for (String profile : activeProfiles) {
            if ("dev".equals(profile) || "test".equals(profile)) {
                isDevOrTest = true;
                break;
            }
        }
        
        if (isDevOrTest) {
            // 开发环境使用固定验证码
            code = DEV_FIXED_CODE;
            log.info("=== 开发环境：使用固定验证码 {} ===", code);
        } else {
            // 生产环境生成随机验证码
            code = RandomUtil.randomNumbers(6);
        }
        
        // 计算过期时间
        long expireTime = System.currentTimeMillis() + CODE_EXPIRE_MINUTES * 60 * 1000;
        
        // 存储验证码
        SMS_CODE_MAP.put(phoneNumber, new SmsCode(code, expireTime));
        
        // 模拟发送短信，打印验证码到控制台
        log.info("=== 短信验证码发送模拟 ===");
        log.info("手机号: {}", phoneNumber);
        log.info("验证码: {}", code);
        log.info("有效期: {} 分钟", CODE_EXPIRE_MINUTES);
        if (isDevOrTest) {
            log.info("环境: 开发环境 (固定验证码)");
        } else {
            log.info("环境: 生产环境 (随机验证码)");
        }
        log.info("========================");
        
        return code;
    }
    
    /**
     * 验证短信验证码
     * @param phoneNumber 手机号
     * @param code 验证码
     * @return 是否验证成功
     */
    public boolean verifySmsCode(String phoneNumber, String code) {
        SmsCode smsCode = SMS_CODE_MAP.get(phoneNumber);
        
        if (smsCode == null) {
            log.warn("手机号 {} 没有发送过验证码", phoneNumber);
            return false;
        }
        
        // 检查验证码是否过期
        if (System.currentTimeMillis() > smsCode.getExpireTime()) {
            log.warn("手机号 {} 的验证码已过期", phoneNumber);
            SMS_CODE_MAP.remove(phoneNumber);
            return false;
        }
        
        // 检查验证码是否正确
        if (!smsCode.getCode().equals(code)) {
            log.warn("手机号 {} 的验证码不正确，输入: {}, 正确: {}", phoneNumber, code, smsCode.getCode());
            return false;
        }
        
        // 验证成功后删除验证码
        SMS_CODE_MAP.remove(phoneNumber);
        log.info("手机号 {} 验证码验证成功", phoneNumber);
        return true;
    }
    
    /**
     * 检查手机号是否已发送验证码且未过期
     * @param phoneNumber 手机号
     * @return 是否可以重新发送
     */
    public boolean canResendCode(String phoneNumber) {
        SmsCode smsCode = SMS_CODE_MAP.get(phoneNumber);
        if (smsCode == null) {
            return true;
        }
        
        // 如果距离上次发送不足1分钟，则不能重新发送
        long oneMinuteAgo = System.currentTimeMillis() - 60 * 1000;
        return smsCode.getExpireTime() < oneMinuteAgo;
    }
    
    /**
     * 短信验证码内部类
     */
    private static class SmsCode {
        private final String code;
        private final long expireTime;
        
        public SmsCode(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
        
        public String getCode() {
            return code;
        }
        
        public long getExpireTime() {
            return expireTime;
        }
    }
}
