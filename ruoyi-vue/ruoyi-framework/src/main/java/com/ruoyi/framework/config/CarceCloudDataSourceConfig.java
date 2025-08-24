package com.ruoyi.framework.config;

import com.ruoyi.common.constant.DsConstants;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@MapperScan(basePackages = "com.ruoyi.*.mapper.carcecloud", sqlSessionTemplateRef = "carceCloudSqlSessionTemplate")
public class CarceCloudDataSourceConfig {

	@Bean(name = "carceCloudDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.carcecloud")
	public DataSource carceCloudDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "carceCloudSqlSessionFactory")
	public SqlSessionFactory carceCloudSqlSessionFactory(@Qualifier("carceCloudDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setTypeAliasesPackage("com.ruoyi.car.domain");
		bean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
		// 设置tkMyBatis的配置
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/carcecloud/*.xml"));
		return bean.getObject();
	}

	@Bean(name = DsConstants.tranCarceCloud)
	public DataSourceTransactionManager carceCloudTransactionManager(
			@Qualifier("carceCloudDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "carceCloudSqlSessionTemplate")
	public SqlSessionTemplate carceCloudSqlSessionTemplate(
			@Qualifier("carceCloudSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}