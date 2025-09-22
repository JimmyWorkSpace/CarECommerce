package cc.carce.sale.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import cc.carce.sale.entity.CarSmsLogEntity;
import cc.carce.sale.mapper.manager.CarSmsLogMapper;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 短信验证码服务
 */
@Service
@Slf4j
public class SmsService {
    
	@Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.account}")
    private String account;

    @Value("${sms.password}")
    private String password;
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private CarSmsLogMapper carSmsLogMapper;
    
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
        
        // 构建短信内容
        String message = String.format("您的验证码是：%s，%d分钟内有效，请勿泄露给他人。", code, CODE_EXPIRE_MINUTES);
        
        try {
            if (isDevOrTest) {
                // 开发环境只打印到控制台，不实际发送短信
                log.info("=== 短信验证码发送模拟 ===");
                log.info("手机号: {}", phoneNumber);
                log.info("验证码: {}", code);
                log.info("有效期: {} 分钟", CODE_EXPIRE_MINUTES);
                log.info("环境: 开发环境 (固定验证码)");
                log.info("短信内容: {}", message);
                log.info("========================");
            } else {
                // 生产环境实际发送短信
                String result = sendSms(phoneNumber, message);
                log.info("=== 短信验证码发送结果 ===");
                log.info("手机号: {}", phoneNumber);
                log.info("验证码: {}", code);
                log.info("有效期: {} 分钟", CODE_EXPIRE_MINUTES);
                log.info("短信内容: {}", message);
                log.info("发送结果: {}", result);
                log.info("========================");
            }
        } catch (Exception e) {
            log.error("发送短信验证码失败，手机号: {}, 错误: {}", phoneNumber, e.getMessage(), e);
            // 发送失败时从缓存中移除验证码
            SMS_CODE_MAP.remove(phoneNumber);
            throw new RuntimeException("短信发送失败: " + e.getMessage());
        }
        
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
    
    public String sendSms(String mobile, String message) {
    	try {
            // 確保簡訊內容有正確編碼 (避免中文亂碼)
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());

            String url = smsUrl +
                    "?username=" + account +
                    "&password=" + password +
                    "&dstaddr=" + mobile +
                    "&smbody=" + encodedMsg;

            String result = HttpUtil.createGet(url).execute().body();
            
            // 记录短信发送日志到数据库
            try {
                CarSmsLogEntity smsLog = new CarSmsLogEntity();
                smsLog.setPhoneNumber(mobile);
                smsLog.setSmsContent(message);
                smsLog.setSendTime(new Date());
                carSmsLogMapper.insert(smsLog);
                log.info("短信发送记录已保存到数据库，手机号: {}", mobile);
            } catch (Exception logException) {
                log.error("保存短信发送记录失败，手机号: {}, 错误: {}", mobile, logException.getMessage(), logException);
                // 记录日志失败不影响短信发送结果
            }
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending SMS: " + e.getMessage();
        }
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
