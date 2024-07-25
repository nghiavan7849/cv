package com.snackviet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.snackviet.model.SanPham;
import com.snackviet.repository.SanPhamRepository;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    public Page<SanPham> getAllSanPhams(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    public Page<SanPham> getSanPhamsByCategory(Integer categoryId, Pageable pageable) {
        return sanPhamRepository.findByLoaiSPMaLoai(categoryId, pageable);
    }

    public Page<SanPham> getSanPhamsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return sanPhamRepository.findByGiaBetween(minPrice, maxPrice, pageable);
    }

    public Page<SanPham> searchSanPhams(String keyword, Pageable pageable) {
        return sanPhamRepository.findByTenSanPhamContaining(keyword, pageable);
    }

    public Page<SanPham> getSortedSanPhams(Pageable pageable, String sort) {
        Sort sortOrder = Sort.by("tenSanPham").ascending(); // Default sort
        if (sort != null) {
            switch (sort) {
                case "best_selling":
                case "most_bought":
                    sortOrder = Sort.by("soLuongBan").descending();
                    break;
                case "price_asc":
                    sortOrder = Sort.by("gia").ascending();
                    break;
                case "price_desc":
                    sortOrder = Sort.by("gia").descending();
                    break;
                case "newest":
                    sortOrder = Sort.by("maSanPham").descending();
                    break;
                default:
                    sortOrder = Sort.by("tenSanPham").ascending();
                    break;
            }
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
        return sanPhamRepository.findAll(sortedPageable);
    }
}
