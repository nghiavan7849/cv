package com.babystore.model;

import java.util.Date;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "Transactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String transactionCode;
	private String accountNumber;
	@Nationalized
	private String fullName;
	@Temporal(TemporalType.DATE)
	private Date transactionDate = new Date();
	private boolean transactionStatus;
	private double amountMoney;

	@ManyToOne
	@JoinColumn(name = "idAccount")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "idOrder")
	private Order order;
}
