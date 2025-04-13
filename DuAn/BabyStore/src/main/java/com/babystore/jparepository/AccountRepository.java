package com.babystore.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	


	 Account findByUserName(String userName);
	
    Account findByEmail(String email);

}

