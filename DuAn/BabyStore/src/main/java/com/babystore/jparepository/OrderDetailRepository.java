package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Order;
import com.babystore.model.OrderDetail;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	public List<OrderDetail> findByOrder(Order order);
}
