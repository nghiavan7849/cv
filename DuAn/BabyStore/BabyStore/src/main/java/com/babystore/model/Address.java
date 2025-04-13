package com.babystore.model;

import java.util.List;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Addresses")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Nationalized
	private String fullNameAddress;
	private String numberPhone;
	@Nationalized
	private String provinceID;
	@Nationalized
	private String districtCode;
	@Nationalized
	private String wardCode;
	private boolean status;

	@ManyToOne
	@JoinColumn(name = "idAccount")
	private Account account;

	@OneToMany(mappedBy = "address")
	private List<Order> orders;
}
