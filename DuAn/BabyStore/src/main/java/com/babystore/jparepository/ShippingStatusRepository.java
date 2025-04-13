package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.ShippingStatus;

public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Integer>{
	public List<ShippingStatus> findByNameIn(String[] names);
}
