package com.snackviet.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.diachi.CreateDiaChiDTO;
import com.snackviet.dto.diachi.UpdateDiaChiDTO;
import com.snackviet.model.DiaChi;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DiaChiRepository;
import com.snackviet.repository.TaiKhoanRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin("*")
@RestController
public class ApiDiaChi {
    @Autowired
    DiaChiRepository diaChiRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/api/dia-chi/list")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("diaChi", diaChiRepository.findAll()));
        response.put("message", "Lấy dữ liệu địa chỉ thành công");
        return response;
    }

    @GetMapping("/api/dia-chi/{maDC}")
    public Map<String, Object> getOne(@PathVariable("maDC") Integer maDC) {
        DiaChi diaChi = diaChiRepository.findById(maDC).get();
        if (diaChi == null) {
            return response("error", diaChi, "Địa chỉ không tồn tại!!!");
        } else {
            return response("success", diaChi, "Lấy dữ liệu địa chỉ thành công");
        }

    }
    @GetMapping("/api/dia-chi/list-by-matk")
    public Map<String, Object> getListByMaTK(@RequestParam("maTaiKhoan")Integer maTaiKhoan) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTaiKhoan).get();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("diaChi", diaChiRepository.findByTaiKhoanDC(taiKhoan)));
        response.put("message", "Lấy dữ liệu địa chỉ thành công");
        return response;
    }
    @GetMapping("/api/dia-chi/page/{pageSize}")
    public Map<String, Object> getListPage(@PathVariable("pageSize") int pageSize,
            @RequestParam("maTaiKhoan") Integer id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).get();
        Pageable pageable = PageRequest.of(pageSize - 1, 5, Sort.by(Direction.DESC, "trangThai", "maDiaChi"));
        Page<DiaChi> pageDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageDiaChi.getTotalPages());
        response.put("data", Map.of("diaChi", pageDiaChi.getContent()));
        response.put("message", "Lấy dữ liệu địa chỉ thành công");

        return response;
    }

    @PostMapping("/api/dia-chi/create")
    public Map<String, Object> create(@RequestBody CreateDiaChiDTO createDiaChiDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(createDiaChiDTO.getMaTaiKhoan()).get();
        List<DiaChi> listDC = diaChiRepository.findByTaiKhoanDC(taiKhoan);
        DiaChi diaChi = new DiaChi();
        diaChi.setHoVaTen(createDiaChiDTO.getHoVaTen());
        diaChi.setSoDienThoai(createDiaChiDTO.getSoDienThoai());
        diaChi.setTrangThai(createDiaChiDTO.isTrangThai());
        diaChi.setDiaChi(createDiaChiDTO.getDiaChi());
        diaChi.setDiaChiChiTiet((createDiaChiDTO.getDiaChiChiTiet()));
        diaChi.setMaTinhThanh(createDiaChiDTO.getMaTinhThanh());
        diaChi.setMaQuanHuyen(createDiaChiDTO.getMaQuanHuyen());
        diaChi.setMaXaPhuong(createDiaChiDTO.getMaXaPhuong());
        diaChi.setTrangThaiXoa(false);
        diaChi.setTaiKhoanDC(taiKhoan);
        if(listDC.isEmpty()){
            diaChi.setTrangThai(true);
        } else if (createDiaChiDTO.isTrangThai()) {
            for (DiaChi dc : listDC) {
                if (dc.isTrangThai()) {
                    dc.setTrangThai(false);
                    diaChiRepository.saveAndFlush(dc);
                    break;
                }
            }
            diaChi.setTrangThai(createDiaChiDTO.isTrangThai());
        } else {
            List<DiaChi> list = diaChiRepository.findByTaiKhoanDCAndTrangThai(taiKhoan, true);
            if(list.isEmpty()){
                diaChi.setTrangThai(true);
            } else {
                diaChi.setTrangThai(createDiaChiDTO.isTrangThai());
            }
        }
        diaChiRepository.saveAndFlush(diaChi);
        return response("success", diaChi, "Thêm địa chỉ mới thành công");
    }

    @PutMapping("/api/dia-chi/update")
    public Map<String, Object> update(@RequestBody UpdateDiaChiDTO updateDiaChiDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(updateDiaChiDTO.getMaTaiKhoan()).get();
        List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan,Sort.by(Direction.DESC, "maDiaChi"));
        DiaChi diaChi = new DiaChi();
        diaChi.setMaDiaChi(updateDiaChiDTO.getMaDiaChi());
        diaChi.setDiaChiChiTiet(updateDiaChiDTO.getDiaChiChiTiet());
        diaChi.setHoVaTen(updateDiaChiDTO.getHoVaTen());
        diaChi.setSoDienThoai(updateDiaChiDTO.getSoDienThoai());
        diaChi.setDiaChi(updateDiaChiDTO.getDiaChi());
        diaChi.setMaTinhThanh(updateDiaChiDTO.getMaTinhThanh());
        diaChi.setMaQuanHuyen(updateDiaChiDTO.getMaQuanHuyen());
        diaChi.setMaXaPhuong(updateDiaChiDTO.getMaXaPhuong());
        diaChi.setTaiKhoanDC(taiKhoan); 

        if (updateDiaChiDTO.isTrangThai()) {
            for (DiaChi dc : listDiaChi) {
                if (dc.isTrangThai()) {
                    dc.setTrangThai(false);
                    diaChiRepository.saveAndFlush(dc);
                    break;
                }
            }
            diaChi.setTrangThai(updateDiaChiDTO.isTrangThai());
        } else {
            DiaChi dc = diaChiRepository.findById(updateDiaChiDTO.getMaDiaChi()).get();
            if (dc.isTrangThai()) {
                for (DiaChi dia : listDiaChi) {
                    if (dia.getMaDiaChi() != updateDiaChiDTO.getMaDiaChi()) {
                        dia.setTrangThai(true);
                        diaChiRepository.saveAndFlush(dia);
                        break;
                    }
                }
            }

                diaChi.setTrangThai(updateDiaChiDTO.isTrangThai());
        }
        diaChiRepository.saveAndFlush(diaChi);
        return response("success", diaChi, "Cập nhật địa chỉ thành công");
    }

    @DeleteMapping("/api/dia-chi/delete/{maDiaChi}")
    public Map<String, Object> delete(@PathVariable("maDiaChi") Integer id) {
        DiaChi diaChi = diaChiRepository.findById(id).get();
        diaChi.setTrangThaiXoa(true);
        diaChi.setTrangThai(false);
        diaChiRepository.saveAndFlush(diaChi);
        return response("success", new DiaChi(), "Xóa địa chỉ thành công");
    }

    public Map<String, Object> response(String status, DiaChi diaChi, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", diaChi == null ? null : Map.of("diaChi", diaChi));
        response.put("message", message);
        return response;
    }
}
