package com.ruoyi.car.task;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ruoyi.car.domain.CarSalePhotoEntity;
import com.ruoyi.car.mapper.carcecloud.CarSalePhotoMapper;
import com.ruoyi.framework.service.FtpService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成缩略图任务
 */
@Component
@Slf4j
public class GenImageThumbTask {
	
	@Value("${carce.prefix}")
	private String imagePrefix;
	
	@Resource
	private FtpService ftpService;

	@Resource
	private CarSalePhotoMapper carSalePhotoMapper;
	
	private ExecutorService es = ThreadUtil.newExecutor(3);
	
//	@PostConstruct
	public void execute() {
//		获取要生成缩略图的圖片信息
		List<CarSalePhotoEntity> list = carSalePhotoMapper.getNotThumbedImage();
		if(CollUtil.isNotEmpty(list)) {
			for(CarSalePhotoEntity photo : list) {
				// 使用多线程处理每张圖片
				es.submit(() -> processImage(photo));
			}
		}
	}
	
	/**
	 * 处理单张圖片，生成缩略图并上传
	 * 
	 * @param photo 圖片信息
	 */
	private void processImage(CarSalePhotoEntity photo) {
		try {
			String imageUrl = imagePrefix + photo.getPhotoUrl();
			log.info("开始处理圖片: {}", imageUrl);
			
			// 1. 检查圖片是否存在
			if (!isImageExists(imageUrl)) {
				log.warn("圖片不存在: {}", imageUrl);
				return;
			}
			
			// 2. 读取圖片并生成缩略图
			byte[] thumbnailBytes = generateThumbnail(imageUrl, 256);
			if (thumbnailBytes == null) {
				log.error("生成缩略图失败: {}", imageUrl);
				return;
			}
			
			// 3. 构造缩略图文件名和路径
			String originalPath = photo.getPhotoUrl();
			String fileName = getFileName(originalPath);
			String fileExtension = getFileExtension(fileName);
			String thumbnailFileName = getFileNameWithoutExtension(fileName) + ".thumb256." + fileExtension;
			String remoteDir = getDirectoryPath(originalPath);
			
			// 4. 上传缩略图到FTP
			try (InputStream inputStream = new ByteArrayInputStream(thumbnailBytes)) {
				String uploadedFileName = ftpService.uploadFile(inputStream, thumbnailFileName, remoteDir);
				log.info("缩略图上传成功: {} -> {}", imageUrl, uploadedFileName);
				photo.setIsThumbDo(1);
				carSalePhotoMapper.updateByPrimaryKey(photo);
			} catch (Exception e) {
				log.error("上传缩略图失败: {}", imageUrl, e);
			}
			
		} catch (Exception e) {
			log.error("处理圖片失败: {}", photo.getPhotoUrl(), e);
		}
	}
	
	/**
	 * 检查圖片URL是否存在
	 * 
	 * @param imageUrl 圖片URL
	 * @return 是否存在
	 */
	private boolean isImageExists(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			
			int responseCode = connection.getResponseCode();
			return responseCode == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			log.error("检查圖片存在性失败: {}", imageUrl, e);
			return false;
		}
	}
	
	/**
	 * 生成缩略图
	 * 
	 * @param imageUrl 原图URL
	 * @param targetWidth 目标宽度
	 * @return 缩略图字节数组
	 */
	private byte[] generateThumbnail(String imageUrl, int targetWidth) {
		try {
			// 读取原图
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(60000);
			
			BufferedImage originalImage = ImageIO.read(connection.getInputStream());
			if (originalImage == null) {
				log.error("无法读取圖片: {}", imageUrl);
				return null;
			}
			
			// 计算缩略图尺寸，保持宽高比
			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();
			
			// 如果原图宽度小于等于目标宽度，不需要缩放
			if (originalWidth <= targetWidth) {
				log.info("原图宽度({})小于等于目标宽度({}), 无需缩放: {}", originalWidth, targetWidth, imageUrl);
				return null;
			}
			
			double scale = (double) targetWidth / originalWidth;
			int targetHeight = (int) (originalHeight * scale);
			
			// 创建缩略图
			BufferedImage thumbnailImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = thumbnailImage.createGraphics();
			
			// 设置渲染质量
			g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			
			// 绘制缩放后的圖片
			Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
			g2d.drawImage(scaledImage, 0, 0, null);
			g2d.dispose();
			
			// 转换为字节数组
			String formatName = getImageFormat(imageUrl);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(thumbnailImage, formatName, baos);
			
			log.info("缩略图生成成功: {} -> {}x{}", imageUrl, targetWidth, targetHeight);
			return baos.toByteArray();
			
		} catch (Exception e) {
			log.error("生成缩略图失败: {}", imageUrl, e);
			return null;
		}
	}
	
	/**
	 * 获取圖片格式
	 * 
	 * @param imageUrl 圖片URL
	 * @return 圖片格式
	 */
	private String getImageFormat(String imageUrl) {
		String extension = getFileExtension(imageUrl).toLowerCase();
		switch (extension) {
			case "jpg":
			case "jpeg":
				return "jpg";
			case "png":
				return "png";
			case "gif":
				return "gif";
			case "bmp":
				return "bmp";
			default:
				return "jpg"; // 默认使用jpg格式
		}
	}
	
	/**
	 * 从路径中获取文件名
	 * 
	 * @param path 文件路径
	 * @return 文件名
	 */
	private String getFileName(String path) {
		if (path == null || path.isEmpty()) {
			return "";
		}
		int lastSlash = path.lastIndexOf('/');
		return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
	}
	
	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	private String getFileExtension(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return "";
		}
		int lastDot = fileName.lastIndexOf('.');
		return lastDot >= 0 ? fileName.substring(lastDot + 1) : "";
	}
	
	/**
	 * 获取不带扩展名的文件名
	 * 
	 * @param fileName 文件名
	 * @return 不带扩展名的文件名
	 */
	private String getFileNameWithoutExtension(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return "";
		}
		int lastDot = fileName.lastIndexOf('.');
		return lastDot >= 0 ? fileName.substring(0, lastDot) : fileName;
	}
	
	/**
	 * 从路径中获取目录路径
	 * 
	 * @param path 完整路径
	 * @return 目录路径
	 */
	private String getDirectoryPath(String path) {
		if (path == null || path.isEmpty()) {
			return "";
		}
		int lastSlash = path.lastIndexOf('/');
		return lastSlash >= 0 ? path.substring(0, lastSlash) : "";
	}
}
