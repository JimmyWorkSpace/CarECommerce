package cc.carce.sale.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cc.carce.sale.dto.CarShoppingCartDto;
import cc.carce.sale.entity.CarProductsEntity;
import cc.carce.sale.entity.CarShoppingCartEntity;
import cc.carce.sale.mapper.manager.CarShoppingCartMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarShoppingCartService {

	@Resource
	private CarShoppingCartMapper carShoppingCartMapper;
	
	@Resource
	private CarProductsService carProductsService;
	
	@Value("${carce.prefix}")
	private String imagePrefix;
	
	/**
	 * 获取用户的购物车列表
	 */
	public List<CarShoppingCartEntity> getUserCart(Long userId) {
		CarShoppingCartEntity query = new CarShoppingCartEntity();
		query.setUserId(userId);
		query.setDelFlag(false);
		return carShoppingCartMapper.select(query);
	}
	
	/**
	 * 获取用户的购物车详细信息列表
	 */
	public List<CarShoppingCartDto> getUserCartDetails(Long userId) {
		try {
			List<CarShoppingCartEntity> cartItems = getUserCart(userId);
			List<CarShoppingCartDto> cartDetails = new ArrayList<>();
			
			for (CarShoppingCartEntity item : cartItems) {
				CarShoppingCartDto dto = convertToDto(item);
				cartDetails.add(dto);
			}
			
			return cartDetails;
		} catch (Exception e) {
			log.error("获取购物车详细信息失败", e);
			return new ArrayList<>();
		}
	}
	
	/**
	 * 添加商品到购物车
	 */
	public boolean addToCart(CarShoppingCartEntity cartItem) {
		try {
			// 检查是否已存在相同商品
			CarShoppingCartEntity query = new CarShoppingCartEntity();
			query.setUserId(cartItem.getUserId());
			query.setProductId(cartItem.getProductId());
			query.setDelFlag(false);
			
			List<CarShoppingCartEntity> existing = carShoppingCartMapper.select(query);
			if (!existing.isEmpty()) {
				// 更新数量
				CarShoppingCartEntity existingItem = existing.get(0);
				existingItem.setProductAmount(existingItem.getProductAmount() + cartItem.getProductAmount());
				return carShoppingCartMapper.updateByPrimaryKey(existingItem) > 0;
			} else {
				// 新增
				cartItem.setDelFlag(false);
				return carShoppingCartMapper.insert(cartItem) > 0;
			}
		} catch (Exception e) {
			log.error("添加购物车失败", e);
			return false;
		}
	}
	
	/**
	 * 更新购物车商品数量
	 */
	public boolean updateQuantity(Long id, Integer quantity) {
		try {
			CarShoppingCartEntity cartItem = carShoppingCartMapper.selectByPrimaryKey(id);
			if (cartItem != null) {
				if (quantity <= 0) {
					// 数量为0时删除
					cartItem.setDelFlag(true);
				} else {
					cartItem.setProductAmount(quantity);
				}
				return carShoppingCartMapper.updateByPrimaryKey(cartItem) > 0;
			}
			return false;
		} catch (Exception e) {
			log.error("更新购物车数量失败", e);
			return false;
		}
	}
	
	/**
	 * 从购物车移除商品
	 */
	public boolean removeFromCart(Long id) {
		try {
			CarShoppingCartEntity cartItem = carShoppingCartMapper.selectByPrimaryKey(id);
			if (cartItem != null) {
				cartItem.setDelFlag(true);
				return carShoppingCartMapper.updateByPrimaryKey(cartItem) > 0;
			}
			return false;
		} catch (Exception e) {
			log.error("移除购物车商品失败", e);
			return false;
		}
	}
	
	/**
	 * 根据用户ID和商品ID从购物车移除商品
	 */
	public boolean removeFromCartByProductId(Long userId, Long productId) {
		try {
			CarShoppingCartEntity query = new CarShoppingCartEntity();
			query.setUserId(userId);
			query.setProductId(productId);
			query.setDelFlag(false);
			
			List<CarShoppingCartEntity> cartItems = carShoppingCartMapper.select(query);
			if (!cartItems.isEmpty()) {
				CarShoppingCartEntity cartItem = cartItems.get(0);
				cartItem.setDelFlag(true);
				return carShoppingCartMapper.updateByPrimaryKey(cartItem) > 0;
			}
			return true; // 如果商品不在购物车中，认为删除成功
		} catch (Exception e) {
			log.error("根据商品ID移除购物车商品失败", e);
			return false;
		}
	}
	
	/**
	 * 清空用户购物车
	 */
	public boolean clearUserCart(Long userId) {
		try {
			CarShoppingCartEntity query = new CarShoppingCartEntity();
			query.setUserId(userId);
			query.setDelFlag(false);
			
			List<CarShoppingCartEntity> cartItems = carShoppingCartMapper.select(query);
			for (CarShoppingCartEntity item : cartItems) {
				item.setDelFlag(true);
				carShoppingCartMapper.updateByPrimaryKey(item);
			}
			return true;
		} catch (Exception e) {
			log.error("清空购物车失败", e);
			return false;
		}
	}
	
	/**
	 * 将实体转换为DTO
	 */
	private CarShoppingCartDto convertToDto(CarShoppingCartEntity entity) {
		CarShoppingCartDto dto = new CarShoppingCartDto();
		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setProductId(entity.getProductId());
		dto.setProductAmount(entity.getProductAmount());
		dto.setProductName(entity.getProductName());
		dto.setCreateTime(entity.getCreateTime());
		
		// 查询商品详细信息
		CarProductsEntity product = carProductsService.getProductById(entity.getProductId());
		if (product != null) {
			dto.setSource(product.getSource());
			dto.setAlias(product.getAlias());
			dto.setModel(product.getModel());
			dto.setMarketPrice(product.getMarketPrice());
			dto.setProductPrice(product.getMarketPrice());
			dto.setBrand(product.getBrand());
			dto.setTag(product.getTag());
			// 设置商品图片路径
			dto.setProductImage(imagePrefix + "/img/product/" + product.getId() + "_90x90.jpg");
		} else {
			// 如果商品不存在，设置默认值
			dto.setSource("未知");
			dto.setAlias("未知");
			dto.setModel("未知");
			dto.setMarketPrice(0L);
			dto.setBrand("未知");
			dto.setTag("未知");
			dto.setProductImage(imagePrefix + "/img/product/default_90x90.jpg");
		}
		
		return dto;
	}
}
