package com.babystore.jparepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babystore.model.Order;


public interface OrderRepository extends JpaRepository<Order, Integer>{
	public Page<Order> findById(Integer id, Pageable pageable);
	@Query("SELECT o FROM Order o "
			+ "JOIN o.address a "
			+ "WHERE a.fullNameAddress LIKE %:name%")
	public Page<Order> findByNameAddress(@Param("name")String name, Pageable pageable);
	@Query("SELECT o FROM Order o "
			+ "JOIN o.account a "
			+ "WHERE a.fullName LIKE %:name% ")
	public Page<Order> findByFullNameAccount(@Param("name")String name, Pageable pageable);
	@Query("SELECT o FROM Order o "
			+ "JOIN o.shippingStatus s "
			+ "WHERE s.name LIKE %:status%")
	public Page<Order> findByStatusOrder(@Param("status")String status, Pageable pageable);
	 
	public Page<Order> findByOrderDate(String orderDate, Pageable pageable);
}
