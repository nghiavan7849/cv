import React, { useEffect, useState } from 'react';
import '../css/Confirmforgotpassword.css';
import bgLogin from '../images/backgrounddangnhap.png';
import { Button } from 'react-bootstrap';
import { NavLink, useLocation } from 'react-router-dom';
import axios from "axios";
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

const urlApi = process.env.REACT_APP_API_URL;

const postForgotPasswordChange = (data) => {
    return axios.post(`${urlApi}/api/tai-khoan/forgot-password/change`,data);
}

export const ConfirmForgotPassword = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { state } = location;
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");
    const [confirmPasswordMessage, setConfirmPasswordMessage] = useState("");

    const clickXacNhan = async () => {
        if(checkValidate()){
            const data = {
                "email":state.email,
                "matKhau":password,
                "maXacNhan":state.codeOTP  
            }
            let res = await postForgotPasswordChange(data);
            if(res.data.status === 'success'){
                toast.success(res.data.message);
                setTimeout(() => {
                    navigate("/login", {replace:true});
                }, 3000);
            }
        }
    };
    const checkValidate = () => {
        if (!password) {
            setPasswordMessage('Mật khẩu mới không được để trống.');
            return false;
        }
        if (!confirmPassword) {
            setConfirmPasswordMessage('Xác nhận mật khẩu mới không được để trống.');
            return false;
        }
        if(confirmPassword !== password ){
            setConfirmPasswordMessage("Xác nhận mật khẩu không khớp");
            return false;
        }

        return true;
    }
    const onChangePassword = (e) => {
        setPassword(e.target.value);
        setPasswordMessage("");
    }
    const onChangeConfirmPassword = (e) => {
        setConfirmPassword(e.target.value);
        setConfirmPasswordMessage("");
    }

    useEffect(() => {
        console.log(state);
    }, []);

    return (
        <div className="confirmforgotpassword">
            <div className="div-image">
                <img src={bgLogin} alt='Login background' />
            </div>
            <div className="div-main">
                <div className="d-flex justify-content-center">
                    <div className="card">
                        <div className="card-body">
                            <div className="text-center fs-2 fw-bold">Quên mật khẩu</div>
                            <div className="mt-4">
                                <form>
                                    <div className="d-flex justify-content-center div-input">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-lock"></i>
                                                <input
                                                    type="password"
                                                    placeholder="Mật khẩu mới"
                                                    value={password}
                                                    onChange={(e) => onChangePassword(e)}
                                                />
                                                <div className="text-danger mt-1">{passwordMessage}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center div-input mt-1">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-lock"></i>
                                                <input
                                                    type="password"
                                                    placeholder="Xác nhận mật khẩu mới"
                                                    value={confirmPassword}
                                                    onChange={(e) => onChangeConfirmPassword(e)}
                                                />
                                            </div>
                                            <div className="text-danger mt-1">{confirmPasswordMessage}</div>
                                        </div>

                                    </div>
                                    <div className="d-flex justify-content-center mt-1 div-button">
                                        <div className="d-flex justify-content-center mt-3">
                                            <Button type="button" className="btn btn-primary " onClick={clickXacNhan} >Tiếp tục</Button>
                                        </div>
                                        <div className="d-flex justify-content-center mt-3">
                                            <NavLink className="btn  nav-link" to={"/login"}>Trở lại</NavLink>
                                        </div>
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
