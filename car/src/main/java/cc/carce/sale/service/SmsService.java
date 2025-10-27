package cc.carce.sale.service;

import java.io.DataOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cc.carce.sale.entity.CarSmsLogEntity;
import cc.carce.sale.mapper.manager.CarSmsLogMapper;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Data;
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
    
    // 存储发送时间的Map，key为手机号，value为发送时间戳
    private static final Map<String, Long> SMS_SEND_TIME_MAP = new ConcurrentHashMap<>();

    // 验证码有效期（分钟）
    private static final int CODE_EXPIRE_MINUTES = 5;
    
    // 发送间隔时间（秒）
    private static final int SEND_INTERVAL_SECONDS = 25;

    // 开发环境固定验证码
    private static final String DEV_FIXED_CODE = "123456";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 发送短信验证码
     * 
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
        
        // 记录发送时间
        long currentTime = System.currentTimeMillis();
        SMS_SEND_TIME_MAP.put(phoneNumber, currentTime);

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
                SmsResponse result = sendSms(phoneNumber, message);
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
     * 
     * @param phoneNumber 手机号
     * @param code        验证码
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
     * 检查手机号是否可以重新发送验证码
     * 
     * @param phoneNumber 手机号
     * @return 是否可以重新发送
     */
    public boolean canResendCode(String phoneNumber) {
        Long lastSendTime = SMS_SEND_TIME_MAP.get(phoneNumber);
        if (lastSendTime == null) {
            return true; // 从未发送过，可以发送
        }

        // 检查距离上次发送是否已超过25秒
        long currentTime = System.currentTimeMillis();
        long timeSinceLastSend = currentTime - lastSendTime;
        
        if (timeSinceLastSend < SEND_INTERVAL_SECONDS * 1000) {
            log.info("手机号 {} 距离上次发送验证码不足 {} 秒，剩余 {} 秒", 
                    phoneNumber, SEND_INTERVAL_SECONDS, 
                    (SEND_INTERVAL_SECONDS * 1000 - timeSinceLastSend) / 1000);
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取手机号剩余等待时间（秒）
     * 
     * @param phoneNumber 手机号
     * @return 剩余等待时间，如果为0表示可以发送
     */
    public int getRemainingWaitTime(String phoneNumber) {
        Long lastSendTime = SMS_SEND_TIME_MAP.get(phoneNumber);
        if (lastSendTime == null) {
            return 0; // 从未发送过，可以发送
        }

        long currentTime = System.currentTimeMillis();
        long timeSinceLastSend = currentTime - lastSendTime;
        long remainingTime = SEND_INTERVAL_SECONDS * 1000 - timeSinceLastSend;
        
        return remainingTime > 0 ? (int) (remainingTime / 1000) : 0;
    }

    public SmsResponse sendSms(String mobile, String message) {
        CarSmsLogEntity carSmsLog = new CarSmsLogEntity();
        carSmsLog.setPhoneNumber(mobile);
        carSmsLog.setSmsContent(message);
        carSmsLog.setSendTime(new Date());
        carSmsLogMapper.insert(carSmsLog);

        try {

            StringBuffer reqUrl = new StringBuffer();
            reqUrl.append(smsUrl + "?CharsetURL=UTF-8");
            StringBuffer params = new StringBuffer();
            params.append("username=" + account);
            params.append("&password=" + password);
            params.append("&dstaddr=" + mobile);
            params.append("&smbody=" + message);
            URL url = new URL(reqUrl.toString());
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
            dos.write(params.toString().getBytes("utf-8"));
            dos.flush();
            dos.close();

            // 读取响应
            StringBuilder response = new StringBuilder();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
            }

            String responseStr = response.toString();
            log.info("SMS API response: {}", responseStr);
            carSmsLog.setSmsResponse(responseStr);

            // 解析回傳結果
            return parseResponse(responseStr);

        } catch (Exception e) {
            log.error("Failed to send SMS to {}", mobile, e);
            SmsResponse errorResp = new SmsResponse();
            errorResp.setStatuscode("-1");
            errorResp.setMsgid("N/A");
            errorResp.setRawResponse("Error: " + e.getMessage());
            carSmsLog.setSmsResponse(ExceptionUtil.stacktraceToString(e));
            return errorResp;
        } finally {
            carSmsLogMapper.updateByPrimaryKey(carSmsLog);
        }
    }

    // 解析回傳字串
    private SmsResponse parseResponse(String response) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setRawResponse(response);

        if (response == null) {
            smsResponse.setStatuscode("-1");
            return smsResponse;
        }

        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.startsWith("msgid=")) {
                smsResponse.setMsgid(line.replace("msgid=", "").trim());
            } else if (line.startsWith("statuscode=")) {
                smsResponse.setStatuscode(line.replace("statuscode=", "").trim());
            }
        }
        return smsResponse;
    }

    // 簡訊回傳物件
    @Data
    public static class SmsResponse {
        private String msgid;
        private String statuscode;
        private String rawResponse;

        public boolean isSuccess() {
            return "0".equals(statuscode); // 0 = 成功
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
