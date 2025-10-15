package cc.carce.sale.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.carce.sale.dto.CarListDto;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Robots.txt 动态生成控制器
 * 
 * @author ruoyi
 * @date 2024-01-01
 */
@Slf4j
@RestController
public class RobotsController {

    @Resource
    private cc.carce.sale.service.CarSalesService carSalesService;

    /**
     * 动态生成robots.txt
     */
    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robots(HttpServletRequest request) {
        try {
            // 获取当前访问的域名和协议
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            
            // 构建基础URL
            String baseUrl = scheme + "://" + serverName;
            if ((scheme.equals("http") && serverPort != 80) || 
                (scheme.equals("https") && serverPort != 443)) {
                baseUrl += ":" + serverPort;
            }
            
            // 生成robots.txt内容
            StringBuilder robotsContent = new StringBuilder();
            
            robotsContent.append("User-agent: *\n");
            robotsContent.append("Allow: /\n\n");
            
            // 动态设置站点地图地址
            robotsContent.append("# 网站地图位置\n");
            robotsContent.append("Sitemap: ").append(baseUrl).append("/sitemap.xml\n\n");
            
            // 爬取延迟
            robotsContent.append("# 爬取延迟（秒）\n");
            robotsContent.append("Crawl-delay: 1\n");
            
            log.info("动态生成robots.txt，基础URL: {}", baseUrl);
            return robotsContent.toString();
            
        } catch (Exception e) {
            log.error("生成robots.txt失败", e);
            return "# robots.txt生成失败\nUser-agent: *\nDisallow: /";
        }
    }

    /**
     * 动态生成sitemap.xml
     */
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap(HttpServletRequest request) {
        try {
            // 获取当前访问的域名和协议
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            
            // 构建基础URL
            String baseUrl = scheme + "://" + serverName;
            if ((scheme.equals("http") && serverPort != 80) || 
                (scheme.equals("https") && serverPort != 443)) {
                baseUrl += ":" + serverPort;
            }
            
            // 获取当前时间
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            
            // 生成sitemap.xml内容
            StringBuilder sitemapContent = new StringBuilder();
            sitemapContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sitemapContent.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
            
            // 添加主要页面
            sitemapContent.append("  <url>\n");
            sitemapContent.append("    <loc>").append(baseUrl).append("/</loc>\n");
            sitemapContent.append("    <lastmod>").append(currentTime).append("</lastmod>\n");
            sitemapContent.append("    <changefreq>daily</changefreq>\n");
            sitemapContent.append("    <priority>1.0</priority>\n");
            sitemapContent.append("  </url>\n");
            
            sitemapContent.append("  <url>\n");
            sitemapContent.append("    <loc>").append(baseUrl).append("/buy-cars</loc>\n");
            sitemapContent.append("    <lastmod>").append(currentTime).append("</lastmod>\n");
            sitemapContent.append("    <changefreq>daily</changefreq>\n");
            sitemapContent.append("    <priority>0.9</priority>\n");
            sitemapContent.append("  </url>\n");
            
            sitemapContent.append("  <url>\n");
            sitemapContent.append("    <loc>").append(baseUrl).append("/mall</loc>\n");
            sitemapContent.append("    <lastmod>").append(currentTime).append("</lastmod>\n");
            sitemapContent.append("    <changefreq>daily</changefreq>\n");
            sitemapContent.append("    <priority>0.8</priority>\n");
            sitemapContent.append("  </url>\n");
            
            sitemapContent.append("  <url>\n");
            sitemapContent.append("    <loc>").append(baseUrl).append("/about</loc>\n");
            sitemapContent.append("    <lastmod>").append(currentTime).append("</lastmod>\n");
            sitemapContent.append("    <changefreq>monthly</changefreq>\n");
            sitemapContent.append("    <priority>0.7</priority>\n");
            sitemapContent.append("  </url>\n");
            
            sitemapContent.append("  <url>\n");
            sitemapContent.append("    <loc>").append(baseUrl).append("/channel</loc>\n");
            sitemapContent.append("    <lastmod>").append(currentTime).append("</lastmod>\n");
            sitemapContent.append("    <changefreq>weekly</changefreq>\n");
            sitemapContent.append("    <priority>0.6</priority>\n");
            sitemapContent.append("  </url>\n");
            
            // 添加汽车详情页面
            try {
                // 获取推荐车辆列表，限制数量避免sitemap过大
                List<CarListDto> cars = carSalesService.getAllCars();
                for (CarListDto car : cars) {
                    sitemapContent.append("  <url>\n");
                    sitemapContent.append("    <loc>").append(baseUrl).append("/detail/").append(car.getId()).append("</loc>\n");
                    sitemapContent.append("    <lastmod>").append(currentTime).append("</lastmod>\n");
                    sitemapContent.append("    <changefreq>weekly</changefreq>\n");
                    sitemapContent.append("    <priority>0.8</priority>\n");
                    sitemapContent.append("  </url>\n");
                }
            } catch (Exception e) {
                log.warn("获取汽车列表失败，跳过汽车详情页面", e);
            }
            
            sitemapContent.append("</urlset>");
            
            log.info("动态生成sitemap.xml，基础URL: {}", baseUrl);
            return sitemapContent.toString();
            
        } catch (Exception e) {
            log.error("生成sitemap.xml失败", e);
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n</urlset>";
        }
    }
}
