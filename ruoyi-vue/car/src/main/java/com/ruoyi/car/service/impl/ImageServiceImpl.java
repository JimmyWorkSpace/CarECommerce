package com.ruoyi.car.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.ruoyi.car.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

  @Value("${carce.prefix}")
  private String imagePrefix;
  @Override
  public String replaceImagePrefix(String imageUrl) {
    try{
      if (imageUrl != null && !imageUrl.startsWith("http") && !imageUrl.startsWith("https")) {
        // 处理相对路径
        if (imageUrl.startsWith("/")) {
          // 绝对路径（相对于根目录）
          imageUrl = imagePrefix + imageUrl;
        } else {
          // 相对路径
          imageUrl = imagePrefix + "/" + imageUrl;
        }
      }
    }catch(Exception ex){
      log.error("处理圖片Url失败", ex);
    }
    return imageUrl;
  }

  @Override
  public String replaceImagePrefixInHtml(String html) {
    try{
// 解析HTML
      Document doc = Jsoup.parse(html);

      // 选择所有的img元素
      Elements imgElements = doc.select("img");

      for (Element img : imgElements) {
        String src = img.attr("src");

        // 检查src是否不是以http或https开头
        if (src != null && !src.startsWith("http") && !src.startsWith("https")) {
          // 处理相对路径
          if (src.startsWith("/")) {
            // 绝对路径（相对于根目录）
            img.attr("src", imagePrefix + src);
          } else {
            // 相对路径
            img.attr("src", imagePrefix + "/" + src);
          }
        }
      }

      return doc.toString();
    }catch(Exception ex){
      log.error("处理html中的圖片元素失败", ex);
      return html;
    }
  }
}
