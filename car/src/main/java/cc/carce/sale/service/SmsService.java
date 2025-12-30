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
    private CarSmsLogMapper carSmsLogMapper;
    
    @Autowired
    private Environment environment;

    // 存储验证码的Map，key为手机号，value为验证码和过期时间
    private static final Map<String, SmsCode> SMS_CODE_MAP = new ConcurrentHashMap<>();
    
    // 存储发送时间的Map，key为手机号，value为发送时间戳
    private static final Map<String, Long> SMS_SEND_TIME_MAP = new ConcurrentHashMap<>();

    // 验证码有效期（分钟）
    private static final int CODE_EXPIRE_MINUTES = 5;
    
    // 发送间隔时间（秒）
    private static final int SEND_INTERVAL_SECONDS = 25;

    /**
     * 发送短信验证码
     * 
     * @param phoneNumber 手机号
     * @return 验证码
     */
    public String sendSmsCode(String phoneNumber) {
        // 生成随机验证码
        String code = RandomUtil.randomNumbers(6);

        // 计算过期时间
        long expireTime = System.currentTimeMillis() + CODE_EXPIRE_MINUTES * 60 * 1000;
        
        // 记录发送时间
        long currentTime = System.currentTimeMillis();
        SMS_SEND_TIME_MAP.put(phoneNumber, currentTime);

        // 存储验证码
        SMS_CODE_MAP.put(phoneNumber, new SmsCode(code, expireTime));

        // 构建短信内容
        String message = String.format("您的驗證碼是：%s，%d分鐘內有效，請勿洩露給他人。", code, CODE_EXPIRE_MINUTES);

        // 检查当前环境是否为dev
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isDev = false;
        for (String profile : activeProfiles) {
            if ("dev".equals(profile)) {
                isDev = true;
                break;
            }
        }

        if (isDev) {
            // dev环境：不实际发送短信，只在控制台打印
            log.info("=== [DEV環境] 簡訊驗證碼（未實際發送） ===");
            log.info("手機號: {}", phoneNumber);
            log.info("驗證碼: {}", code);
            log.info("有效期: {} 分鐘", CODE_EXPIRE_MINUTES);
            log.info("簡訊內容: {}", message);
            log.info("==========================================");
        } else {
            // 非dev环境：实际发送短信
            try {
                SmsResponse result = sendSms(phoneNumber, message);
                log.info("=== 簡訊驗證碼發送結果 ===");
                log.info("手機號: {}", phoneNumber);
                log.info("驗證碼: {}", code);
                log.info("有效期: {} 分鐘", CODE_EXPIRE_MINUTES);
                log.info("簡訊內容: {}", message);
                log.info("發送結果: {}", result);
                log.info("========================");
            } catch (Exception e) {
                log.error("發送簡訊驗證碼失敗，手機號: {}, 錯誤: {}", phoneNumber, e.getMessage(), e);
                // 发送失败时从缓存中移除验证码
                SMS_CODE_MAP.remove(phoneNumber);
                throw new RuntimeException("發送簡訊驗證碼失敗: " + e.getMessage());
            }
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
            log.warn("手機號 {} 沒有發送過驗證碼", phoneNumber);
            return false;
        }

        // 检查验证码是否过期
        if (System.currentTimeMillis() > smsCode.getExpireTime()) {
            log.warn("手機號 {} 的驗證碼已過期", phoneNumber);
            SMS_CODE_MAP.remove(phoneNumber);
            return false;
        }

        // 检查验证码是否正确
        if (!smsCode.getCode().equals(code)) {
            log.warn("手機號 {} 的驗證碼不正確，輸入: {}, 正確: {}", phoneNumber, code, smsCode.getCode());
            return false;
        }

        // 验证成功后删除验证码
        SMS_CODE_MAP.remove(phoneNumber);
        log.info("手機號 {} 驗證碼驗證成功", phoneNumber);
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
            log.info("手機號 {} 距離上次發送驗證碼不足 {} 秒，剩余 {} 秒", 
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
