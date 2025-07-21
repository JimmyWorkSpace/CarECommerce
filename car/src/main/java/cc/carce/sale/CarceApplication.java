package cc.carce.sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@SpringBootApplication
public class CarceApplication extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CarceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CarceApplication.class, args);
        System.out.println("二手车销售平台启动成功！");
    }
}
