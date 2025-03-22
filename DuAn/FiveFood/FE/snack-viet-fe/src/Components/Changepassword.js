import React, { useState } from 'react';
import axios from 'axios';
import '../css/Changepassword.css';
import Navbar from './menu';
import Footer from './footer';
import imgCrush from '../images/comga.png';

const urlApi = process.env.REACT_APP_API_URL;

export const ChangePassword = () => {
    const [matKhauCu, setMatKhauCu] = useState('');
    const [matKhauMoi, setMatKhauMoi] = useState('');
    const [confirmMatKhau, setConfirmMatKhau] = useState('');
    const [errors, setErrors] = useState({
        matKhauCu: '',
        matKhauMoi: '',
        confirmMatKhau: ''
    });
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));


    const validatePassword = (password) => {
        // Add more complex password validation if needed
        if (password.length < 8) {
            return 'Mật khẩu phải có ít nhất 8 ký tự.';
        }
        return '';
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        let formIsValid = true;
        const newErrors = { matKhauCu: '', matKhauMoi: '', confirmMatKhau: '' };

        if (!matKhauCu) {
            newErrors.matKhauCu = 'Mật khẩu cũ không được để trống.';
            formIsValid = false;
        }

        const newMatKhauMoiError = validatePassword(matKhauMoi);
        if (newMatKhauMoiError) {
            newErrors.matKhauMoi = newMatKhauMoiError;
            formIsValid = false;
        }

        if (matKhauMoi !== confirmMatKhau) {
            newErrors.confirmMatKhau = 'Xác nhận mật khẩu mới không khớp.';
            formIsValid = false;
        }

        setErrors(newErrors);

        if (formIsValid) {
            const data = { 
                "tenDangNhap": sessionTaiKhoan.tenDangNhap,
                "matKhau": matKhauCu,
                "changeMatKhau":matKhauMoi
            }
            try {
                const response = await axios.put(`${urlApi}/api/tai-khoan/change-password`,data, 
                   
                {
                    headers: {
                    'Authorization': `Bearer ${sessionToken}`,
                  },
                }
                );

                if (response.data.status === 'success') {
                    setSuccessMessage('Mật khẩu đã được thay đổi thành công.');
                    setErrorMessage('');
                } else {
                    setErrorMessage(response.data.message || 'Đã có lỗi xảy ra. Vui lòng thử lại.');
                }
            } catch (error) {
                setErrorMessage(error.response?.data?.message || 'Đã có lỗi xảy ra. Vui lòng thử lại.');
            }
        }
    };

    return (
        <>
            <Navbar />
            <main className='main-change-pass' style={{marginTop:20}}>
                <div className="row pt-5 pb-5 ms-5 me-5">
                    <div className="col-md-6 d-flex justify-content-center mt-5">
                        <div className="card">
                            <div className="card-body">
                                <div className="text-center fs-2 fw-bold">Đổi Mật Khẩu</div>
                                <div className="mt-4">
                                    <form onSubmit={handleSubmit}>
                                        <div className="d-flex justify-content-center div-input">
                                            <div className="input-box">
                                                <div>
                                                    <i className="fa-solid fa-lock"></i>
                                                    <input
                                                        type="password"
                                                        placeholder="Mật khẩu cũ"
                                                        value={matKhauCu}
                                                        onChange={(e) => setMatKhauCu(e.target.value)}
                                                    />
                                                </div>
                                                {errors.matKhauCu && <span className="text-danger">{errors.matKhauCu}</span>}
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center div-input mt-1">
                                            <div className="input-box">
                                                <div>
                                                    <i className="fa-solid fa-lock"></i>
                                                    <input
                                                        type="password"
                                                        placeholder="Mật khẩu mới"
                                                        value={matKhauMoi}
                                                        onChange={(e) => setMatKhauMoi(e.target.value)}
                                                    />
                                                </div>
                                                {errors.matKhauMoi && <span className="text-danger">{errors.matKhauMoi}</span>}
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center div-input mt-1">
                                            <div className="input-box">
                                                <div>
                                                    <i className="fa-solid fa-lock"></i>
                                                    <input
                                                        type="password"
                                                        placeholder="Xác nhận mật khẩu mới"
                                                        value={confirmMatKhau}
                                                        onChange={(e) => setConfirmMatKhau(e.target.value)}
                                                    />
                                                </div>
                                                {errors.confirmMatKhau && <span className="text-danger">{errors.confirmMatKhau}</span>}
                                                {successMessage && <span className="text-success mt-2">{successMessage}</span>}
                                                {errorMessage && <span className="text-danger mt-2">{errorMessage}</span>}
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-3 div-button">
                                            <div className="d-flex justify-content-end">
                                                <button type="submit" className="btn btn-success">Xác nhận</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-6 mt-2">
                        <div className="d-flex justify-content-center">
                            <img src={imgCrush} alt="Crush Chips" className="hero-img" />
                        </div>
                    </div>
                </div>
            </main>
            <Footer />
        </>
    );
};
