package com.snackviet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snackviet.model.DanhGia;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public boolean isReviewed(String maSanPham, String maTaiKhoan) {
        SanPham sanPham = sanPhamRepository.findById(Integer.valueOf(maSanPham)).get();
        TaiKhoan taiKhoan = taiKhoanRepository.findById(Integer.valueOf(maTaiKhoan)).get();

        if (sanPham.getMaSanPham() != null && taiKhoan.getMaTaiKhoan() != null) {
            List<DanhGia> list = danhGiaRepository.findByTaiKhoanDGAndSanPhamDG(taiKhoan, sanPham);
            System.out.println(list);
            System.out.println(list.size());
            if(!list.isEmpty() || list.size() != 0){
                return true;
            }
        }

        return false; // Nếu sản phẩm hoặc tài khoản không tồn tại
    }

}
