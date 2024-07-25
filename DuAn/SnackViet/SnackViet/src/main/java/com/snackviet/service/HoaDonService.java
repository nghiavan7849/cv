package com.snackviet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.HoaDon;
import com.snackviet.model.SanPham;
import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.SanPhamRepository;

@Service
public class HoaDonService {

	@Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    public int tinhTongSanPhamDaBan(Integer sanPhamId) {
        Integer tongSoLuong = chiTietHoaDonRepository.findTotalSoldBySanPhamId(sanPhamId);
        return tongSoLuong != null ? tongSoLuong : 0;
    }
}
