package com.babystore.controller.client;

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

import com.babystore.jparepository.AccountRepository;
import com.babystore.jparepository.AddressRepository;
import com.babystore.model.Account;
import com.babystore.model.Address;
import com.babystore.service.SessionService;

@Controller
@RequestMapping("dia-chi")
public class AddressController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AddressRepository addressRepository;

	@Autowired
	SessionService sessionService;
	Address address = new Address();
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

		sessionService.setAttribute("sessionTaiKhoan", "Trần Văn Nghĩa");
		Account account = accountRepository.findById(1).get();
		// Account account = sessionService.getAttribute("sessionTaiKhoan");
		List<Address> listDiaChi = addressRepository.findByAccount(account);

		if (address.getId() != null) {
			String addres = address.getFullNameAddress();
			String[] parts = addres.split(", ");
			// model.addAttribute("valueHoVaTen", address.getHoVaTen());
			model.addAttribute("valueSoDienThoai", address.getNumberPhone());
			model.addAttribute("valueDiaChiCuthe", parts[0]);
			model.addAttribute("valueProvince", parts[3]);
			model.addAttribute("valueDistrict", parts[2]);
			model.addAttribute("valueWard", parts[1]);
		}
		model.addAttribute("diaChi", address);
		model.addAttribute("listDiaChi", listDiaChi);
		model.addAttribute("checkBtn", checkBtn);

		return "clients/address";
	}

	@PostMapping("them-moi")
	public String themMoiDiaChi(Model model, @RequestParam("hoVaTen") String hoVaTen,
			@RequestParam("soDienThoai") String soDienThoai, @RequestParam("province") String province,
			@RequestParam("district") String district, @RequestParam("ward") String ward,
			@RequestParam("diaChiCuThe") String diaChiCuThe,
			@RequestParam(name = "macDinhDC", defaultValue = "false") boolean macDinhDC) {
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
		if (this.checkValid) {
			return "clients/address";
		} else {
			String StringDiaChi = diaChiCuThe + ", " + ward + ", " + district + ", " + province;
			Account account = accountRepository.findById(1).get();
			// Account account = sessionService.getAttribute("sessionTaiKhoan");
			Address address = new Address();
			// diaChi.setHoVaTen(hoVaTen);
			address.setNumberPhone(soDienThoai);
			address.setFullNameAddress(StringDiaChi);
			address.setAccount(account);
			address.setDistrictCode(district);
			address.setProvinceID(province);
			address.setWardCode(ward);
			List<Address> listDiaChi = addressRepository.findByAccount(account);
			if (macDinhDC) {
				for (Address dc : listDiaChi) {
					if (dc.isStatus()) {
						dc.setStatus(false);
						addressRepository.saveAndFlush(dc);
						address.setStatus(true);
						break;
					}
				}
			} else {
				address.setStatus(macDinhDC);
			}
			if (listDiaChi.equals(new ArrayList<Address>())) {
				address.setStatus(true);
			}
			addressRepository.saveAndFlush(address);
			address = new Address();
			return "redirect:/dia-chi";
		}

	}

	@GetMapping("edit/{id}")
	public String editDiaChi(Model model, @PathVariable("id") Integer id) {
		address = addressRepository.findById(id).get();
		checkBtn = true;
		return "redirect:/dia-chi";
	}

	@PostMapping("cap-nhat")
	public String capNhatDiaChi(Model model, @RequestParam("hoVaTen") String hoVaTen,
			@RequestParam("soDienThoai") String soDienThoai, @RequestParam("province") String province,
			@RequestParam("district") String district, @RequestParam("ward") String ward,
			@RequestParam("diaChiCuThe") String diaChiCuThe,
			@RequestParam(name = "macDinhDC", defaultValue = "false") boolean macDinhDC) {
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
		if (this.checkValid) {
			return "clients/address";
		} else {
			String StringDiaChi = diaChiCuThe + ", " + ward + ", " + district + ", " + province;
			Account account = accountRepository.findById(1).get();
			// Account account = sessionService.getAttribute("sessionTaiKhoan");
			Address address = new Address();
			address.setId(this.address.getId());
			// address.setHoVaTen(hoVaTen);
			address.setNumberPhone(soDienThoai);
			address.setFullNameAddress(StringDiaChi);
			address.setAccount(account);
			address.setDistrictCode(district);
			address.setProvinceID(province);
			address.setWardCode(ward);
			List<Address> listDiaChi = addressRepository.findByAccount(account);
			if (macDinhDC) {
				for (Address dc : listDiaChi) {
					if (dc.isStatus()) {
						dc.setStatus(false);
						addressRepository.saveAndFlush(dc);
						address.setStatus(true);
						break;
					}
				}

			} else {
				Address dc = addressRepository.findById(this.address.getId()).get();
				if (dc.isStatus()) {
					for (Address dia : listDiaChi) {
						if (dia.getId() != this.address.getId()) {
							dia.setStatus(true);
							addressRepository.saveAndFlush(dia);
							break;
						}
					}
				}
			}
			address.setStatus(macDinhDC);

			addressRepository.saveAndFlush(address);
			this.address = new Address();
			checkBtn = false;
			return "redirect:/dia-chi";
		}

	}

	@GetMapping("remove/{id}")
	public String removeDiaChi(Model model, @PathVariable("id") Integer id) {
		Address diaChi = addressRepository.findById(id).get();
		if (diaChi.isStatus()) {
			List<Address> list = addressRepository.findAll(Sort.by(Direction.DESC, "maDiaChi"));
			for (Address dc : list) {
				if (!dc.isStatus()) {
					dc.setStatus(true);
					addressRepository.saveAndFlush(dc);
					break;
				}
			}
		}
		addressRepository.delete(diaChi);
		return "redirect:/dia-chi";
	}

	@GetMapping("lam-moi")
	public String resetDiaChi(Model model) {
		address = new Address();
		checkBtn = false;
		return "redirect:/dia-chi";
	}

	public void checkValidForm(String hoVaTen, String soDienThoai, String province, String district, String ward,
			String diaChiCuThe) {
		// if(hoVaTen.isEmpty()) {
		// messageHoVaTen = "Vui lòng nhập họ và tên!!!";
		// checkValid = true;
		// }else {
		// messageHoVaTen = "";
		// }
		String patternSDT = "^(0[3-9])\\d{8}$";
		if (soDienThoai.isEmpty()) {
			messageSoDienThoai = "Vui lòng nhập số điện thoại!!!";
			checkValid = true;
		} else {
			try {
				long sdt = Long.parseLong(soDienThoai);
				if (sdt < 0) {
					messageSoDienThoai = "Số điện thoại phải là số dương!!!";
					checkValid = true;
				} else if (soDienThoai.length() != 10) {
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

		if (province.isEmpty()) {
			messageProvince = "Vui lòng chọn Tỉnh/TP!!!";
			checkValid = true;
		} else {
			messageProvince = "";
		}

		if (district.isEmpty()) {
			messageDistrict = "Vui lòng chọn Quận/Huyện!!!";
			checkValid = true;
		} else {
			messageDistrict = "";
		}

		if (ward.isEmpty()) {
			messageWard = "Vui lòng chọn Xã/Phường!!!";
			checkValid = true;
		} else {
			messageWard = "";
		}

		if (diaChiCuThe.isEmpty()) {
			messageDiaChiCuThe = "Vui lòng nhập địa chỉ cụ thể!!!";
			checkValid = true;
		} else {
			messageDiaChiCuThe = "";
		}
	}
}
