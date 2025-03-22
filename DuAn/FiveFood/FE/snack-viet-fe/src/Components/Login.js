import '../css/Login.css';
import bgLogin from '../images/backgrounddangnhap.png';

import axios from "axios";
import { useState, useContext } from 'react';
import { toast } from 'react-toastify';
import { useNavigate, useLocation } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import { NavLink } from 'react-router-dom';
import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google";
import { AuthContext } from '../Service/AuthContext';

const urlApi = process.env.REACT_APP_API_URL;
export const Login = () => {

    //Tạo function api login
    const getLogin = (tenDangNhap, matKhau) => {
        return axios.get(`${urlApi}/api/get-tai-khoan?tenDangNhap=${tenDangNhap}&matKhau=${matKhau}`);
    }
    // Tạo biến lưu trữ và hàm set để cập nhật cái biến đó
    const [tenDangNhap, setTenDangNhap] = useState("");
    const [matKhau, setMatKhau] = useState("");
    const [message, setMessage] = useState("");
    const { updateTaiKhoan } = useContext(AuthContext);

    const navigate = useNavigate();
    const location = useLocation();
    const toUrl = location.state?.from?.pathname || '/home';
    const googleClientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;


    //Nhấn đăng nhập
    const clickDangNhap = async () => {

        let res = await getLogin(tenDangNhap, matKhau);
        // Bắt dữ liệu trả về của api
        if (res.data.status === 'error') {
            sessionStorage.removeItem('sessionTaiKhoan');
            // Gán thông báo lỗi để in ra
            setMessage(res.data.message);
        } else {
            toast.success("Đăng nhập thành công");
            const token = jwtDecode(res.data.token)
            //Lưu dữ liệu vào session
            updateTaiKhoan(token.data.taiKhoan);
            sessionStorage.setItem('sessionTaiKhoan', JSON.stringify(token.data.taiKhoan));
            sessionStorage.setItem('sessionTokenTK', JSON.stringify(res.data.token));
            // Sau 3 giây khi sẽ chuyển sang trang home
            setTimeout(() => {
                navigate(toUrl, { replace: true });
            }, 3000);
        }
    }

    const handleSuccess = async (credentialResponse) => {
        // Gửi token nhận được từ Google về BE
        try {
            const res = await axios.post(`${urlApi}/api/tai-khoan/auth/google`, {
                tokenId: credentialResponse.credential,
            });
            if (res.data.status === 'error') {
                sessionStorage.removeItem('sessionTaiKhoan');
                // Gán thông báo lỗi để in ra
                setMessage(res.data.message);
            } else {
                toast.success("Đăng nhập thành công");
                const token = jwtDecode(res.data.token)
                //Lưu dữ liệu vào session
                sessionStorage.setItem('sessionTaiKhoan', JSON.stringify(token.data.taiKhoan));
                sessionStorage.setItem('sessionTokenTK', JSON.stringify(res.data.token));
                // Sau 3 giây khi sẽ chuyển sang trang home
                setTimeout(() => {
                    navigate(toUrl, { replace: true });
                }, 3000);
            }
        } catch (error) {
            console.error("Error during Google login:", error);
        }
    };

    const handleFailure = (error) => {
        toast.error("Đăng nhập bằng google thất bại");
    };
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
          // Thực hiện các hành động khi nhấn Enter
          clickDangNhap();
        }
      };
    return (
        <>
            <div className='login'>
                <div className='div-image'>
                    <img src={bgLogin} alt='Logo Login' />
                </div>
                <div className="div-main">
                    <div className="d-flex justify-content-center">
                        <div className="card">
                            <div className="card-body">
                                <div className="text-center fs-2 fw-bold">Đăng nhập</div>
                                <div className="mt-4">
                                    <div>
                                        <div className="d-flex justify-content-center div-input">
                                            <div className="input-box" >
                                                <div className='d-flex justify-content-center'>
                                                    <i className="fa-solid fa-user"></i>
                                                    <input type="text"
                                                        placeholder="Tài khoản"
                                                        className=''
                                                        value={tenDangNhap !== undefined ? tenDangNhap : ''}
                                                        onChange={(event) => { setTenDangNhap(event.target.value); setMessage(""); }}
                                                    />
                                                </div>
                                                <span className='text-danger'>{message}</span>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center div-input">
                                            <div className="input-box" >
                                                <div className='d-flex justify-content-center'>
                                                    <i className="fa-solid fa-lock"></i>
                                                    <input type="password"
                                                        placeholder="Mật khẩu"
                                                        className=''
                                                        value={matKhau !== undefined ? matKhau : ''}
                                                        onChange={(event) => { setMatKhau(event.target.value); setMessage(""); }}
                                                        onKeyDown={(event) => handleKeyDown(event)}
                                                    />
                                                </div>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-1">
                                            <div className="row div-cuoi">
                                                <div className="col-md-6">
                                                </div>
                                                <div className="col-md-6 d-flex justify-content-end">
                                                    <NavLink className="nav-link" to={"/forgot-password"}>Quên mật khẩu</NavLink>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-2 div-button">
                                            <div className="d-flex justify-content-center mt-1">
                                                <button className="btn btn-primary " onClick={() => clickDangNhap()}>Đăng nhập</button>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-1">
                                            Hoặc
                                        </div>
                                        <div className="d-flex justify-content-center mt-1 div-button">
                                            <div className="d-flex justify-content-center">
                                                <GoogleOAuthProvider clientId={googleClientId}>
                                                    <GoogleLogin
                                                        onSuccess={handleSuccess}
                                                        onError={handleFailure}
                                                        style={{ width: "210px" }}
                                                    />
                                                </GoogleOAuthProvider>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-3 div-button">
                                            Bạn chưa có tài khoản? &nbsp;<NavLink className="nav-link" to={"/register"}>Đăng ký</NavLink>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </>
    )
}