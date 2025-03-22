import axios from "axios";
import { useState } from 'react';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import '../css/Register.css';
import bgLogin from '../images/backgrounddangnhap.png';
import { NavLink } from 'react-router-dom';
const urlApi = process.env.REACT_APP_API_URL;
export const Register = () => {
    const getRegister = (data) => {
        return axios.post(`${urlApi}/api/tai-khoan/create`, data);
    }

    // Tạo biến lưu trữ và hàm set để cập nhật cái biến đó
    const [hoVaTen, setHoVaTen] = useState("");
    const [tenDangNhap, setTenDangNhap] = useState("");
    const [matKhau, setMatKhau] = useState("");
    const [email, setEmail] = useState("");
    const [soDienThoai, setSoDienThoai] = useState("");
    const [confirmMatKhau, setConfirmMatKhau] = useState("");

    const [message, setMessage] = useState("");

    const [messgaeHoVaTen, setMessgaeHoVaTen] = useState("");
    const [messageTenDangNhap, setMessgaeTenDangNhap] = useState("");
    const [messageMatKhau, setMessageMatKhau] = useState("");
    const [messageEmail, setMessageEmail] = useState("");
    const [messgaeSoDienThoai, setMessgaeSoDienThoai] = useState("");
    const [messageConfirmMatKhau, setMessageConfirmMatKhau] = useState("");

    const navigate = useNavigate();

    const clickDangKy = async (e) => {
        e.preventDefault();

        if (checkValidate()) {
            const data = {
                "hoVaTen": hoVaTen,
                "tenDangNhap": tenDangNhap,
                "matKhau": matKhau,
                "email": email,
                "soDienThoai": soDienThoai
            }

            console.log("Sending data to API:", data); // Kiểm tra dữ liệu gửi đi

            try {
                let res = await getRegister(data);
                console.log("API response:", res); // Kiểm tra phản hồi từ API

                // Bắt dữ liệu trả về của API
                if (res.data.status === 'error') {
                    // Gán thông báo lỗi để in ra
                    setMessage(res.data.message);
                } else {
                    toast.success("Đăng ký thành công");

                    // Sau 3 giây sẽ chuyển sang trang đăng nhập
                    setTimeout(() => {
                        navigate('/login');
                    }, 1000);
                }
            } catch (error) {
                console.error("Error calling API:", error);
                setMessage("Đã có lỗi xảy ra, vui lòng thử lại sau.");
            }
        }
    }

    // Xử lý sự kiện onchange cho của các input
    const onChangeTenDangNhap = (event) => {
        setTenDangNhap(event.target.value);
        setMessgaeTenDangNhap("");
    }
    const onChangeHoVaTen = (event) => {
        setHoVaTen(event.target.value);
        setMessgaeHoVaTen("");
    }

    const onChangeMatKhau = (event) => {
        setMatKhau(event.target.value);
        setMessageMatKhau("");
    } // Added closing bracket

    const onChangeEmail = (event) => {
        setEmail(event.target.value);
        setMessageEmail("");
    }
    const onChangeSoDienThoai = (event) => {
        setSoDienThoai(event.target.value);
        setMessgaeSoDienThoai("");
    }
    const onChangeConfirmMatKhau = (event) => {
        setConfirmMatKhau(event.target.value);
        setMessageConfirmMatKhau("");
    }

    const checkValidate = () => {
        let isValid = true;
        
        if (hoVaTen === "") {
            setMessgaeHoVaTen("Vui lòng nhập họ và tên");
            isValid = false;
        }
        if (tenDangNhap === "") {
            setMessgaeTenDangNhap("Vui lòng nhập tên đăng nhập");
            isValid = false;
        }
        if (soDienThoai === "") {
            setMessgaeSoDienThoai("Vui lòng nhập số điện thoại");
            isValid = false;
        }
        if (email === "") {
            setMessageEmail("Vui lòng nhập email");
            isValid = false;
        } else if (!/\S+@\S+\.\S+/.test(email)) {
            setMessageEmail("Email không hợp lệ");
            isValid = false;
        }
        if (matKhau === "") {
            setMessageMatKhau("Vui lòng nhập mật khẩu");
            isValid = false;
        } else if (matKhau.length < 8) {
            setMessageMatKhau("Mật khẩu phải có ít nhất 8 ký tự");
            isValid = false;
        }
        if (confirmMatKhau === "" || matKhau !== confirmMatKhau) {
            setMessageConfirmMatKhau("Mật khẩu xác nhận không khớp");
            isValid = false;
        }
        return isValid;
    }

    return (
        <div className="register">
            <div className="div-image">
                <img src={bgLogin} alt='Logo Login' />
            </div>
            <div className="div-main">
                <div className="d-flex justify-content-center mt-2">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-center fs-2 fw-bold">Đăng ký</div>
                            <div className="mt-4">
                                <form onSubmit={clickDangKy}>
                                    <div className="d-flex justify-content-center div-input">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-user"></i>
                                                <input type="text" placeholder="Họ và tên" className='' value={hoVaTen} onChange={onChangeHoVaTen} /><br />
                                                <span className="text-danger">{messgaeHoVaTen}</span>
                                            </div>
                                            <span className='text-danger'>{message}</span>
                                        </div>
                                    </div><div className="d-flex justify-content-center div-input mt-1">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-user"></i>
                                                <input type="text" placeholder="Tên đăng nhập" className='' value={tenDangNhap} onChange={onChangeTenDangNhap} /><br />
                                                <span className="text-danger">{messageTenDangNhap}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center div-input mt-1">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-phone"></i>
                                                <input type="text" placeholder="Số điện thoại" className='' value={soDienThoai} onChange={onChangeSoDienThoai} /><br />
                                                <span className="text-danger">{messgaeSoDienThoai}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center div-input mt-1">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-regular fa-envelope"></i>
                                                <input type="email" placeholder="Email" className='' value={email} onChange={onChangeEmail} /><br />
                                                <span className="text-danger">{messageEmail}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center div-input">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-lock"></i>
                                                <input type="password" placeholder="Mật khẩu" className='' value={matKhau} onChange={onChangeMatKhau} /><br />
                                                <span className="text-danger">{messageMatKhau}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center div-input">
                                        <div className="input-box"><div>
                                                <i className="fa-solid fa-lock"></i>
                                                <input type="password" placeholder="Xác Nhận Mật khẩu" className='' value={confirmMatKhau} onChange={onChangeConfirmMatKhau} /><br />
                                                <span className="text-danger">{messageConfirmMatKhau}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center div-button">
                                        <div className="d-flex justify-content-center mt-3">
                                            <button type="submit" className="btn btn-primary ">Đăng ký</button>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center mt-3 div-button">
                                        Bạn đã có tài khoản? &nbsp; <NavLink className="nav-link" to={"/login"}> Đăng nhập</NavLink>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
