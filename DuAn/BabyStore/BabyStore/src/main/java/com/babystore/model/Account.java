
package com.babystore.model;

import java.util.List;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "Accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String userName;
	private String password;
	private String email;
	@Nationalized
	private String fullName;
	private String avata;
	private boolean role;
	private boolean isDelete;

	@OneToOne(mappedBy = "account")
	private CartDetail cart;

	@OneToMany(mappedBy = "account")
	private List<Order> orders;

	@OneToMany(mappedBy = "account")
	private List<Address> addresses;

	@OneToMany(mappedBy = "account")
	private List<Avaluation> avaluations;

	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions;

	public boolean getRole() {
		return role;
	}
}
