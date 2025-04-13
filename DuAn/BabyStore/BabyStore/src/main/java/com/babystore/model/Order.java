package com.babystore.model;

import java.util.List;

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
@Table(name = "Orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private double total;
	private String orderDate;
	private boolean paymentStatus;
	private boolean paymentMethod;
	private double shippingFee;

	@ManyToOne
	@JoinColumn(name = "idAddress")
	private Address address;

	@ManyToOne
	@JoinColumn(name = "idAccount")
	private Account account;

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;

	@ManyToOne
	@JoinColumn(name = "idShippingStatus")
	private ShippingStatus shippingStatus;

	@OneToMany(mappedBy = "order")
	List<Transaction> transactions;
}
