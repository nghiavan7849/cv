package com.snackviet.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.DiaChi;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DiaChiRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.SessionService;

@Controller
@RequestMapping("dia-chi")
public class DiaChiController {
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired 
	DiaChiRepository diaChiRepository;
	
	@Autowired
	SessionService sessionService;
	DiaChi diaChi =  new DiaChi();
	String messageHoVaTen = "";
	String messageSoDienThoai = "";
	String messageProvince = "";
	String messageDistrict = "";
	String messageWard = "";
	String messageDiaChiCuThe = "";
	boolean checkValid = false;
	boolean checkBtn = false;
	
	@GetMapping
	public String diaChi(Model model) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan,Sort.by(Direction.DESC, "maDiaChi"));
	
		if(diaChi.getMaDiaChi() != null) {
			String address = diaChi.getDiaChi();
			String[] parts = address.split(", ");		
			model.addAttribute("valueHoVaTen", diaChi.getHoVaTen());
			model.addAttribute("valueSoDienThoai", diaChi.getSoDienThoai());
			model.addAttribute("valueDiaChiCuthe", parts[0]);
			model.addAttribute("valueProvince", parts[3]);
			model.addAttribute("valueDistrict", parts[2]);
			model.addAttribute("valueWard", parts[1]);
		}
		model.addAttribute("diaChi", diaChi);
		model.addAttribute("listDiaChi", listDiaChi);
		model.addAttribute("checkBtn", checkBtn);
		
		return "user/diachi";
	}
	
	@PostMapping("them-moi")
	public String themMoiDiaChi(Model model, @RequestParam("hoVaTen")String hoVaTen,
			@RequestParam("soDienThoai")String soDienThoai, @RequestParam("province") String province,
			@RequestParam("district") String district, @RequestParam("ward")String ward, @RequestParam("diaChiCuThe")String diaChiCuThe,
			@RequestParam(name = "macDinhDC", defaultValue = "false")boolean macDinhDC
			)  {
		checkValidForm(hoVaTen, soDienThoai, province, district, ward, diaChiCuThe);
		
		model.addAttribute("valueHoVaTen", hoVaTen);
		model.addAttribute("valueSoDienThoai", soDienThoai);
		model.addAttribute("valueDiaChiCuthe", diaChiCuThe);
		model.addAttribute("valueProvince", province);
		model.addAttribute("valueDistrict", district);
		model.addAttribute("valueWard", ward);
		model.addAttribute("messageHoVaTen", messageHoVaTen);
		model.addAttribute("messageSoDienThoai", messageSoDienThoai);
		model.addAttribute("messageProvince", messageProvince);
		model.addAttribute("messageDistrict", messageDistrict);
		model.addAttribute("messageWard", messageWard);
		model.addAttribute("messageDiaChiCuThe", messageDiaChiCuThe);
		if(this.checkValid) {
			return "user/diachi";
		} else {
			String StringDiaChi =  diaChiCuThe + ", " + ward + ", " + district + ", " + province;
			TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
			DiaChi diaChi = new DiaChi();
			diaChi.setHoVaTen(hoVaTen);
			diaChi.setSoDienThoai(soDienThoai);
			diaChi.setDiaChi(StringDiaChi);
			diaChi.setTaiKhoanDC(taiKhoan);
			List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
			if(macDinhDC) {
				for (DiaChi dc : listDiaChi) {
					if(dc.isTrangThai()) {
						dc.setTrangThai(false);
						diaChiRepository.saveAndFlush(dc);
						diaChi.setTrangThai(true);
						break;
					}
				}
			} else {
				diaChi.setTrangThai(macDinhDC);	
			}
			if(listDiaChi.equals(new ArrayList<DiaChi>())) {
				diaChi.setTrangThai(true);
			}
			System.out.println(listDiaChi);
			diaChiRepository.saveAndFlush(diaChi);
			diaChi = new DiaChi();
			return "redirect:/dia-chi";
		}

	}
	@GetMapping("edit/{id}")
	public String editDiaChi(Model model, @PathVariable("id")Integer id) {
		diaChi = diaChiRepository.findById(id).get();
		checkBtn = true;
		return "redirect:/dia-chi";
	}
	
	@PostMapping("cap-nhat")
	public String capNhatDiaChi(Model model, @RequestParam("hoVaTen")String hoVaTen,
			@RequestParam("soDienThoai")String soDienThoai, @RequestParam("province") String province,
			@RequestParam("district") String district, @RequestParam("ward")String ward, @RequestParam("diaChiCuThe")String diaChiCuThe,
			@RequestParam(name = "macDinhDC", defaultValue = "false")boolean macDinhDC
			)  {
		checkValidForm(hoVaTen, soDienThoai, province, district, ward, diaChiCuThe);
		
		model.addAttribute("valueHoVaTen", hoVaTen);
		model.addAttribute("valueSoDienThoai", soDienThoai);
		model.addAttribute("valueDiaChiCuthe", diaChiCuThe);
		model.addAttribute("valueProvince", province);
		model.addAttribute("valueDistrict", district);
		model.addAttribute("valueWard", ward);
		model.addAttribute("messageHoVaTen", messageHoVaTen);
		model.addAttribute("messageSoDienThoai", messageSoDienThoai);
		model.addAttribute("messageProvince", messageProvince);
		model.addAttribute("messageDistrict", messageDistrict);
		model.addAttribute("messageWard", messageWard);
		model.addAttribute("messageDiaChiCuThe", messageDiaChiCuThe);
		if(this.checkValid) {
			return "user/diachi";
		} else {
			String StringDiaChi =  diaChiCuThe + ", " + ward + ", " + district + ", " + province;
			TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
			DiaChi diaChi = new DiaChi();
			diaChi.setMaDiaChi(this.diaChi.getMaDiaChi());
			diaChi.setHoVaTen(hoVaTen);
			diaChi.setSoDienThoai(soDienThoai);
			diaChi.setDiaChi(StringDiaChi);
			diaChi.setTaiKhoanDC(taiKhoan);
			List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
			if(macDinhDC) {
				for (DiaChi dc : listDiaChi) {
					if(dc.isTrangThai()) {
						dc.setTrangThai(false);
						diaChiRepository.saveAndFlush(dc);
						diaChi.setTrangThai(true);
						break;
					}
				}
				
			} else {
				DiaChi dc = diaChiRepository.findById(this.diaChi.getMaDiaChi()).get();
				if(dc.isTrangThai()) {
					for (DiaChi dia : listDiaChi) {
						if(dia.getMaDiaChi() != this.diaChi.getMaDiaChi()) {
							dia.setTrangThai(true);
							diaChiRepository.saveAndFlush(dia);
							break;
						}
					}
				}
			}
			diaChi.setTrangThai(macDinhDC);
			
			diaChiRepository.saveAndFlush(diaChi);
			this.diaChi = new DiaChi();
			checkBtn = false;
			return "redirect:/dia-chi";
		}

	}
	
	@GetMapping("remove/{id}")
	public String removeDiaChi(Model model, @PathVariable("id")Integer id) {
		DiaChi diaChi = diaChiRepository.findById(id).get();
		if(diaChi.isTrangThai()) {
			List<DiaChi> list = diaChiRepository.findAll(Sort.by(Direction.DESC, "maDiaChi"));
			for (DiaChi dc : list) {
				if(!dc.isTrangThai()) {
					dc.setTrangThai(true);
					diaChiRepository.saveAndFlush(dc);
					break;
				}
			}
		}
		diaChiRepository.delete(diaChi);
		return "redirect:/dia-chi";
	}
	
	@GetMapping("lam-moi")
	public String resetDiaChi(Model model) {
		diaChi = new DiaChi();
		checkBtn = false;
		return "redirect:/dia-chi";
	}
	
	
	public void checkValidForm(String hoVaTen, String soDienThoai, String province, String district, String ward, String diaChiCuThe) {
		if(hoVaTen.isEmpty()) {
			messageHoVaTen = "Vui lòng nhập họ và tên!!!";
			checkValid = true;
		}else {
			messageHoVaTen = "";
		}
		String patternSDT = "^(0[3-9])\\d{8}$";
		if(soDienThoai.isEmpty()) {
			messageSoDienThoai = "Vui lòng nhập số điện thoại!!!";
			checkValid = true;
		} else { 
			try {
				long sdt = Long.parseLong(soDienThoai);
				if (sdt < 0) {
					messageSoDienThoai = "Số điện thoại phải là số dương!!!";
					checkValid = true;
				} else if ( soDienThoai.length() != 10) {
					messageSoDienThoai = "Số điện thoại phải là 10 số!!!";
					checkValid = true;
				} else if (!soDienThoai.matches(patternSDT)) { 
					messageSoDienThoai = "Số điện thoại không đúng định dạng!!!";
					checkValid = true;
				} else {
					messageSoDienThoai = "";
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
				messageSoDienThoai = "Số điện thoại phải là số!!!";
				checkValid = true;
			}
		}
		
		if(province.isEmpty()) {
			messageProvince = "Vui lòng chọn Tỉnh/TP!!!";
			checkValid = true;
		} else {
			messageProvince = "";
		}
		
		if(district.isEmpty()) {
			messageDistrict = "Vui lòng chọn Quận/Huyện!!!";
			checkValid = true;
		} else {
			messageDistrict = "";
		}
		
		if(ward.isEmpty()) {
			messageWard = "Vui lòng chọn Xã/Phường!!!";
			checkValid = true;
		} else {
			messageWard = "";
		}
		
		if(diaChiCuThe.isEmpty()) {
			messageDiaChiCuThe = "Vui lòng nhập địa chỉ cụ thể!!!";
			checkValid = true;
		} else {
			messageDiaChiCuThe = "";
		}
	}
}
