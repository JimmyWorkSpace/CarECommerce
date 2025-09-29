package cc.carce.sale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import cc.carce.sale.service.SmsService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class CarceApplication extends SpringBootServletInitializer {
	
	@Resource
	private SmsService smsService;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CarceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CarceApplication.class, args);

    }
    
    public void testSms() {
    	smsService.sendSms("0975760203", "一条测试的信息");
    }
}
