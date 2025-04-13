package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Account;
import com.babystore.model.Address;


public interface AddressRepository extends JpaRepository<Address, Integer> {
 	public List<Address> findByAccount(Account account);
}
