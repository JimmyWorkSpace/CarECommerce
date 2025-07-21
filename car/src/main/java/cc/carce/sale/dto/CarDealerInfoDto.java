package cc.carce.sale.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CarDealerInfoDto {
  private String dealerName;

  private String registeredName;

  private String taxId;

  private String lineId;

  private String owner;

  private String registeredAddress;

  private String publicAddress;

  private String companyPhone;

  private String companyMobile;

  private String contactPerson;

  private String website;

  private String businessHours;

  private String description;

  private List<String> photos = new ArrayList<String>();
}
