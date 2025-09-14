package cc.carce.sale.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SmsService {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.account}")
    private String account;

    @Value("${sms.password}")
    private String password;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendSms(String mobile, String message) {
        try {
            // 確保簡訊內容有正確編碼 (避免中文亂碼)
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());

            String url = smsUrl +
                    "?username=" + account +
                    "&password=" + password +
                    "&dstaddr=" + mobile +
                    "&smbody=" + encodedMsg;

            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending SMS: " + e.getMessage();
        }
    }
}
