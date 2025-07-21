package cc.carce.sale.service;

import cc.carce.sale.entity.CarSalesEntity;
import cc.carce.sale.mapper.carcecloud.CarSalesMapper;
import cn.hutool.core.util.StrUtil;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
//@DataSource(DataSourceType.SLAVE)
public class CarSalesService {

	@Resource
	private CarSalesMapper carSalesMapper;

	/**
	 * 根据id获取uid,  如果uid为空则生成一个
	 * @param id
	 * @return
	 */
	public String getUidById(Long id) {
		CarSalesEntity cs = carSalesMapper.getById(id);
		if (cs != null) {
			if (StrUtil.isBlank(cs.getUid())) {
				cs.setUid(generateShortCode());
				carSalesMapper.updateUidById(cs.getUid(), cs.getId());
			}
			return cs.getUid();
		}
		return null;
	}

	/**
	 * 生成短码
	 * @return
	 */
	private String generateShortCode() {
		String shortId = "";
		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		do {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 4; i++) {
				int index = (int) (Math.random() * letters.length());
				sb.append(letters.charAt(index));
			}
			shortId = sb.toString();
		}
		while (getByUid(shortId) != null);
		return shortId;
	}

	public CarSalesEntity getByUid(String uid) {
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria().andEqualTo("uid", uid);
		return carSalesMapper.selectOneByExample(example);
	}

	public CarSalesEntity getById(Long carSaleId) {
		Example example = new Example(CarSalesEntity.class);
		example.createCriteria().andEqualTo("id", carSaleId);
		return carSalesMapper.selectOneByExample(example);
	}
}
