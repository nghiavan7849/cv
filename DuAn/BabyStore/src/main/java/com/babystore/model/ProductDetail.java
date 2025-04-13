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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ProductDetails")
public class ProductDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private int quantity;
	private double price;
	private double height;
	private double width;
	private double length;
	private double weight;

	@ManyToOne
	@JoinColumn(name = "idColor")
	private Color color;

	@ManyToOne
	@JoinColumn(name = "idProduct")
	private Product product;

	@OneToMany(mappedBy = "productDetail")
	List<ImageProduct> imageProduct;
	
	@OneToMany(mappedBy = "productDetail")
	List<OrderDetail> orderDetails;
}
