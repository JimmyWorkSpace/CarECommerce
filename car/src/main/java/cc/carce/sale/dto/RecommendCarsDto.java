package cc.carce.sale.dto;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "首页好车推荐")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendCarsDto {

	private Long id;

    private Long idGarage;

    private Long carId;

    private String uid;
}
