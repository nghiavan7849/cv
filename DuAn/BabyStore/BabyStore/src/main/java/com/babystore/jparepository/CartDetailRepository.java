package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Account;
import com.babystore.model.CartDetail;
import com.babystore.model.ProductDetail;

public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {

	List<CartDetail> findAllByAccount(Account account);

	CartDetail findFirstByAccountAndProductDetail(Account account, ProductDetail productDetail);
}
