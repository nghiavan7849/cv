package com.snackviet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.snackviet.repository.ChiTietHoaDonRepository;
@Service
public class HoaDonService {

	@Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    public int tinhTongSanPhamDaBan(Integer sanPhamId) {
        Integer tongSoLuong = chiTietHoaDonRepository.findTotalSoldBySanPhamId(sanPhamId);
        return tongSoLuong != null ? tongSoLuong : 0;
    }
}
