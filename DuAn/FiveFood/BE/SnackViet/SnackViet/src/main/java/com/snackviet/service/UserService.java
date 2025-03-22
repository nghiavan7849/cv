package com.snackviet.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
	TaiKhoanRepository taiKhoanRepository;
	
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(username);
		String password = tk.getMatKhau();
		Set<GrantedAuthority> authorities = new HashSet<>();
        if(tk.isVaiTro()){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
		return new User(username, password, authorities);
		
	}
}
