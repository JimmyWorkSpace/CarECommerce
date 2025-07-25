package com.ruoyi.car.controller;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.car.dto.CarBaseInfoDto;
import com.ruoyi.car.service.CarDealerService;
import com.ruoyi.car.service.CarSalesService;
import com.ruoyi.car.service.CarService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("")
public class OgController {

  @Value("${carce.webUrl}")
  private String webUrl;

  @Resource
  private CarService carService;

  @Resource
  private CarSalesService carSalesService;

  @Resource
  private CarDealerService carDealerService;

  private static final String html = "<!DOCTYPE html>\n" +
          "<html>\n" +
          "\n" +
          "<head>\n" +
          "    <meta charset=\"UTF-8\">\n" +
          "    <meta property=\"og:title\" content=\"#title#\" />\n" +
          "    <meta property=\"og:type\" content=\"website\" />\n" +
          "    <meta property=\"og:description\" content=\"#description#\" />\n" +
          "    <meta property=\"og:url\" content=\"#url#\" />\n" +
          "    <meta property=\"og:image\" content=\"#image#\" />\n" +
          "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
          "    <title>#title#</title>\n" +
          "</head>\n" +
          "\n" +
          "<body>\n" +
          "    <script type=\"text/javascript\">\n" +
          "        window.onload = function () {\n" +
          "            location.href = \"#redirectUrl#\";\n" +
          "        }\n" +
          "    </script>\n" +
          "</body>\n" +
          "\n" +
          "</html>";

  @GetMapping(value = "preview/{uid}", produces = "text/html")
  @ResponseBody
  public String index(@PathVariable("uid") String uid, HttpServletRequest request, HttpServletResponse response) {
    String title = "", description = "", image  = "", url = "", redirectUrl = "";
    List<String> images = carService.getImagesByUid(uid);
    CarBaseInfoDto car = carService.getBaseInfoByUidId(uid);
    if(car != null){
      title = CollUtil.join(CollUtil.newArrayList(car.getBrand(), car.getCustomModel(), car.getManufactureYear()), " ");
      description = car.getSaleTitle();

      redirectUrl = webUrl + "/" + uid;
      url = webUrl + "/preview/" + uid;
      if(CollUtil.isNotEmpty(images)){
        image = images.get(0);
      }

      String outerHtml = html.replaceAll("#title#", title)
              .replaceAll("#description#", description)
              .replaceAll("#url#", url)
              .replaceAll("#image#", image)
              .replaceAll("#redirectUrl#", redirectUrl);
      return outerHtml;
    }
    return "";
  }

  @GetMapping(value = "share/{id}", produces = "text/html")
  @ResponseBody
  public RedirectView shareRedirect(@PathVariable("id") Long id) {
    String uid = carSalesService.getUidById(id);
    if (uid != null) {
      return new RedirectView(webUrl + "/" + uid);
    }
    return new RedirectView("/error"); // 跳转到错误页
  }
}
