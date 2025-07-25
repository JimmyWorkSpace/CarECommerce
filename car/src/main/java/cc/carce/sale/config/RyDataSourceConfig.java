package cc.carce.sale.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan(basePackages = "cc.carce.sale.mapper.manager", sqlSessionTemplateRef = "managerSqlSessionTemplate")
public class RyDataSourceConfig {

	@Bean(name = "managerDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.manager")
	public DataSource managerDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "managerSqlSessionFactory")
	public SqlSessionFactory managerSqlSessionFactory(@Qualifier("managerDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		// 设置tkMyBatis的配置
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/manager/*.xml"));
		return bean.getObject();
	}

	@Bean(name = DsConstants.tranManager)
	public DataSourceTransactionManager managerTransactionManager(
			@Qualifier("managerDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "managerSqlSessionTemplate")
	public SqlSessionTemplate managerSqlSessionTemplate(
			@Qualifier("managerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}