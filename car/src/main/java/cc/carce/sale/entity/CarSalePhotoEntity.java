package cc.carce.sale.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Data
@Table(name = "car_sale_photos")
public class CarSalePhotoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "car_sales_id")
	private Long carSalesId;

	@Column(name = "id_garage")
	private Long idGarage;

	@Column(name = "photo_url")
	private String photoUrl;

	@Column(name = "is_cover")
	private Integer isCover;

	@Column(name = "photo_order")
	private Integer photoOrder;

	@Column(name = "c_dt")
	private Date cDt;
}
