import React, { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [taiKhoan, setTaiKhoan] = useState(null);

  useEffect(() => {
    // Lấy thông tin từ sessionStorage khi ứng dụng load
    const storedTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    if (storedTaiKhoan) {
      setTaiKhoan(storedTaiKhoan);
    }
  }, []);

  // Hàm cập nhật thông tin tài khoản
  const updateTaiKhoan = (newTaiKhoan) => {
    setTaiKhoan(newTaiKhoan);
    sessionStorage.setItem("sessionTaiKhoan", JSON.stringify(newTaiKhoan));
  };

  return (
    <AuthContext.Provider value={{ taiKhoan, updateTaiKhoan }}>
      {children}
    </AuthContext.Provider>
  );
};
