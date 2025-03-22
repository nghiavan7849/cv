package com.snackviet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.QuanHuyen;
import com.snackviet.model.XaPhuong;
import java.util.List;


@Repository
public interface XaPhuongRepository extends JpaRepository<XaPhuong, Integer> {
    public List<XaPhuong> findByQuanHuyenId(QuanHuyen quanHuyenId);
    public List<XaPhuong> findByWardCode(Integer wardCode);
    public List<XaPhuong> findByWardName(String wardName);
}
