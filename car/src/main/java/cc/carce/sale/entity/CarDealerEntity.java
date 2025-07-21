package cc.carce.sale.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "car_dealers")
public class CarDealerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "id_garage")
	private Long idGarage;

	@Column(name = "dealer_name")
	private String dealerName;

	@Column(name = "registered_name")
	private String registeredName;

	@Column(name = "tax_id")
	private String taxId;

	@Column(name = "line_id")
	private String lineId;

	@Column(name = "owner")
	private String owner;

	@Column(name = "registered_address")
	private String registeredAddress;

	@Column(name = "public_address")
	private String publicAddress;

	@Column(name = "company_phone")
	private String companyPhone;

	@Column(name = "company_mobile")
	private String companyMobile;

	@Column(name = "contact_person")
	private String contactPerson;

	@Column(name = "website")
	private String website;

	@Column(name = "business_hours")
	private String businessHours;

	@Column(name = "description")
	private String description;

	@Column(name = "c_dt")
	private Date cDt;

	@Column(name = "u_dt")
	private Date uDt;
}