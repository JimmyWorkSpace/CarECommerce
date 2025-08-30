package cc.carce.sale.common;

import lombok.Data;

@Data
public class BasePageForm {

	protected Integer pageNum = 1;
	
	protected Integer pageSize = 10;
}
