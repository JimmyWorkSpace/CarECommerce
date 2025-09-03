//package cc.carce.sale.config;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
//import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
//import com.alibaba.druid.util.Utils;
//
//import javax.sql.DataSource;
//
///**
// * 数据源配置类
// * 确保使用Druid连接池
// */
//@Configuration
//@ConditionalOnClass(DruidDataSource.class)
//public class DataSourceConfig {
//
//    /**
//     * 主数据源配置
//     */
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.carcecloud")
//    public DataSource carcecloudDataSource() {
//        return new DruidDataSource();
//    }
//
//    /**
//     * 管理数据源配置
//     */
//    @Bean
//    @ConfigurationProperties("spring.datasource.manager")
//    public DataSource managerDataSource() {
//        return new DruidDataSource();
//    }
//
//    /**
//     * 配置Druid监控过滤器
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public FilterRegistrationBean<DruidStatProperties.WebStatFilter> druidWebStatFilterRegistrationBean(
//            DruidStatProperties properties) {
//        DruidStatProperties.WebStatFilter webStatFilter = properties.getWebStatFilter();
//        FilterRegistrationBean<DruidStatProperties.WebStatFilter> registrationBean = new FilterRegistrationBean<>(webStatFilter);
//        registrationBean.addUrlPatterns(webStatFilter.getUrlPattern());
//        registrationBean.addInitParameter("exclusions", webStatFilter.getExclusions());
//        registrationBean.addInitParameter("sessionStatEnable", webStatFilter.getSessionStatEnable());
//        registrationBean.addInitParameter("sessionStatMaxCount", webStatFilter.getSessionStatMaxCount());
//        registrationBean.addInitParameter("principalSessionName", webStatFilter.getPrincipalSessionName());
//        registrationBean.addInitParameter("principalCookieName", webStatFilter.getPrincipalCookieName());
//        registrationBean.addInitParameter("profileEnable", webStatFilter.getProfileEnable());
//        return registrationBean;
//    }
//
//    /**
//     * 配置Druid监控Servlet
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public ServletRegistrationBean<DruidStatProperties.StatViewServlet> druidStatViewServletRegistrationBean(
//            DruidStatProperties properties) {
//        DruidStatProperties.StatViewServlet statViewServlet = properties.getStatViewServlet();
//        ServletRegistrationBean<DruidStatProperties.StatViewServlet> registrationBean = new ServletRegistrationBean<>(statViewServlet);
//        registrationBean.addUrlMappings(statViewServlet.getUrlPattern());
//        registrationBean.addInitParameter("allow", statViewServlet.getAllow());
//        registrationBean.addInitParameter("deny", statViewServlet.getDeny());
//        registrationBean.addInitParameter("loginUsername", statViewServlet.getLoginUsername());
//        registrationBean.addInitParameter("loginPassword", statViewServlet.getLoginPassword());
//        registrationBean.addInitParameter("resetEnable", statViewServlet.getResetEnable());
//        return registrationBean;
//    }
//}
