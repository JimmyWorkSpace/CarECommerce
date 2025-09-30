package cc.carce.sale.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class SmsService {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.account}")
    private String account;

    @Value("${sms.password}")
    private String password;

    private final RestTemplate restTemplate = new RestTemplate();

    public SmsResponse sendSms(String mobile, String message) {
        try {
            // 編碼簡訊內容，避免中文亂碼
            String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());

            // 組裝 POST 參數
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("username", account);
            params.add("password", password);
            params.add("dstaddr", mobile);
            params.add("smbody", encodedMsg);
            params.add("CharsetURL", "UTF8"); // ★ 指定 UTF8，避免亂碼

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // 發送 POST 請求
            String response = restTemplate.postForObject(smsUrl, request, String.class);

            log.info("SMS API response: {}", response);

            // 解析回傳結果
            return parseResponse(response);

        } catch (Exception e) {
            log.error("Failed to send SMS to {}", mobile, e);
            SmsResponse errorResp = new SmsResponse();
            errorResp.setStatuscode("-1");
            errorResp.setMsgid("N/A");
            errorResp.setRawResponse("Error: " + e.getMessage());
            return errorResp;
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
}
