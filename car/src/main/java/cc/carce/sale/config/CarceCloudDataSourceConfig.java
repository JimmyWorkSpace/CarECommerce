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

import com.alibaba.druid.pool.DruidDataSource;

import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan(basePackages = "cc.carce.sale.mapper.carcecloud", sqlSessionTemplateRef = "carceCloudSqlSessionTemplate")
public class CarceCloudDataSourceConfig {

	@Bean(name = "carceCloudDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.carcecloud")
	@Primary
	public DataSource carceCloudDataSource() {
		return DataSourceBuilder.create()
				.type(DruidDataSource.class)
				.build();
	}

	@Bean(name = "carceCloudSqlSessionFactory")
	@Primary
	public SqlSessionFactory carceCloudSqlSessionFactory(@Qualifier("carceCloudDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		// 设置tkMyBatis的配置
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/carcecloud/*.xml"));
		return bean.getObject();
	}

	@Bean(name = DsConstants.tranCarce)
	@Primary
	public DataSourceTransactionManager carceCloudTransactionManager(
			@Qualifier("carceCloudDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "carceCloudSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate carceCloudSqlSessionTemplate(
			@Qualifier("carceCloudSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}