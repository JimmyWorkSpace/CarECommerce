package com.ruoyi.framework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BaseServiceImpl {

	@Value("${spring.datasource.carcecloud.db-name}")
	protected String carDbName;
	
	@Value("${spring.datasource.master.db-name}")
	protected String mgrDbName;
}
