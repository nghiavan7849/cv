import '../css/Forgotpassword.css';
import bgLogin from '../images/backgrounddangnhap.png';
import { Card, Button, Form } from 'react-bootstrap';
import { useState, } from 'react';
import { NavLink } from 'react-router-dom';
import { toast } from 'react-toastify';
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import bcrypt from 'bcryptjs';

const urlApi = process.env.REACT_APP_API_URL;

const sendOTP = (email) => {
    return axios.post(`${urlApi}/api/tai-khoan/forgot-password/send-otp?email=`+email);
}

export const Forgotpassword = () => {
    const [email, setEmail] = useState("");
    const [codeOTP, setCodeOTP] = useState("");
    const [resultOTP, setResultOTP] = useState("");
    const [emailMessage, setEmailMessage] = useState("");
    const [codeOTPMessage, setCodeOTPMessage] = useState("");
    const [checkSendEmail, setCheckSendEmail] = useState(false);
    const navigate = useNavigate();

    const clickSendOTP = async () => {
        if(checkValidateEmail()){
            let res = await sendOTP(email);
            if(res.data.status === 'success'){
                setResultOTP(String(res.data.data.code));
                setCheckSendEmail(true);
                toast.success("Đã gửi mã xác nhận");
            } else if(res.data.status === 'error') {
                setEmailMessage(res.data.message);
            }
        }
        
    };
    const checkValidateEmail = () => {
        if (!email) {
            setEmailMessage("Vui lòng nhập email");
            return false;
        } 
        return true;
    }
    const clickNext = async () => {
        if (!codeOTP) {
            setCodeOTPMessage("Vui lòng nhập mã xác nhận");
        } else {
            const isCheckCode = bcrypt.compare(codeOTP,resultOTP);
            if(isCheckCode) {
                const data = {
                    'email': email,
                    'codeOTP':codeOTP 
                }
                navigate("/forgot-password/confirm", { state: data });
            }
        }
    };
    const onChangeInputEmail = (e) => {
        setEmailMessage("");
        setEmail(e.target.value)
    }
    const onChangeInputCodeOTP = (e) => {
        setCodeOTPMessage("");
        setCodeOTP(e.target.value)
    }
    return (
        <div className="quenmatkhau">
            <div className="div-image">
                <img src={bgLogin} alt='Logo Login' />
            </div>
            <div className="div-main">
                <div className="d-flex justify-content-center">
                    <Card>
                        <Card.Body>
                            <div className="text-center fs-2 fw-bold">Quên mật khẩu</div>
                            <div className="mt-4">
                                <Form>
                                    <div className="d-flex justify-content-center div-input">
                                        <div className="input-box">
                                            <div>
                                                <i className="fa-solid fa-envelope"></i>
                                                <input 
                                                    type="text" 
                                                    placeholder="Email" 
                                                    name="email" 
                                                    className=""
                                                    value={email}
                                                    onChange={(e) => onChangeInputEmail(e)}
                                                />
                                            </div>
                                            <span className="text-danger">{emailMessage}</span>
                                        </div>
                                    </div>
                                    <div className="d-flex justify-content-center">
                                        <div className="d-flex justify-content-between div-input" style={{ width: '320px' }}>
                                            <div className="input-box">
                                                <div className="input-dau">
                                                    <i className="fa-solid fa-key"></i>
                                                    <input 
                                                        type="text" 
                                                        placeholder="Mã xác nhận" 
                                                        name="OTP" 
                                                        className=""
                                                        value={codeOTP}
                                                        onChange={(e) => onChangeInputCodeOTP(e)}
                                                    />
                                                </div>
                                                <span className="text-danger">{codeOTPMessage}</span>
                                            </div>
                                            <div className="input-box">
                                                <div className="input-dau">
                                                    <Button className="btn btn-primary " onClick={clickSendOTP}>Gửi</Button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="d-flex justify-content-center mt-1 div-button">
                                        <div className="d-flex justify-content-center mt-3">
                                            <Button type="button" className="btn btn-primary " onClick={clickNext} disabled={checkSendEmail?false:true}>Tiếp tục</Button>
                                        </div>
                                        <div className="d-flex justify-content-center mt-3">
                                            <NavLink className="btn btn-secondary " to={"/login"}>Trở lại</NavLink>
                                        </div>
                                    </div>
                                </Form>
                            </div>
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </div>
    )
}
