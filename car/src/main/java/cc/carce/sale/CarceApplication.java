package cc.carce.sale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import cc.carce.sale.dto.ECPayResultDto;
import cc.carce.sale.service.ECPayService;
import cc.carce.sale.service.SmsService;
import cc.carce.sale.service.SmsService.SmsResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@Slf4j
public class CarceApplication extends SpringBootServletInitializer {
	
	@Resource
	private SmsService smsService;

    @Resource
    private ECPayService ecPayService;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CarceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CarceApplication.class, args);

    }
    
    // @PostConstruct
    public void testSms() {
    	SmsResponse result = smsService.sendSms("0975760203", "一条测试的信息");
    	System.out.println(result);
    }

    // @PostConstruct
    public void testECPay() {
        ECPayResultDto  dto = ecPayService.queryOrderStatusFromECPay("202509181022115593");
        log.info("ECPayResultDto: {}", JSONUtil.toJsonStr(dto));
    }
}
