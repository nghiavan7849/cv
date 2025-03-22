import { useEffect, useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import Navbar from './menu';
import Footer from './footer';
import imgCrush from '../images/comga.png';
import '../css/Updateaccount.css';
const urlApi = process.env.REACT_APP_API_URL;
export const UpdateAccount = () => {
    const [hoVaTen, setHoVaTen] = useState("");
    const [soDienThoai, setSoDienThoai] = useState("");
    const [email, setEmail] = useState("");
    const [gioiTinh, setGioiTinh] = useState(true);
    const [hinhAnh, setHinhAnh] = useState(null);

    const [messageHoVaTen, setMessageHoVaTen] = useState("");
    const [messageSoDienThoai, setMessageSoDienThoai] = useState("");
    const [messageEmail, setMessageEmail] = useState("");
    const [messageGioiTinh, setMessageGioiTinh] = useState("");
    const [messageHinhAnh, setMessageHinhAnh] = useState("");

    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));

    useEffect(() => {
        setHoVaTen(sessionTaiKhoan.hoVaTen)
        setSoDienThoai(sessionTaiKhoan.soDienThoai)
        setEmail(sessionTaiKhoan.email)
        setGioiTinh(sessionTaiKhoan.gioiTinh)
    }, [])

    const handleUpdateAccount = async (e) => {
        e.preventDefault();

        if (checkValidate()) {
            const data = {
                "hoVaTen": hoVaTen,
                "tenDangNhap": sessionTaiKhoan.tenDangNhap,
                "email": email,
                "soDienThoai": soDienThoai,
                "gioiTinh": gioiTinh

            }

            try {
                const res = await axios.put(`${urlApi}/api/tai-khoan/edit-profile`, data, {
                    headers: {

                        'Authorization': `Bearer ${sessionToken}`,
                    }

                });
                if (res.data.status === 'error') {
                    // Hiển thị thông báo lỗi nếu có
                    setMessageHoVaTen(res.data.message.hoVaTen || "");
                    setMessageSoDienThoai(res.data.message.soDienThoai || "");
                    setMessageEmail(res.data.message.email || "");
                    setMessageGioiTinh(res.data.message.gioiTinh || "");
                    setMessageHinhAnh(res.data.message.hinhAnh || "");
                } else {
                    toast.success("Cập nhật tài khoản thành công");
                }
            } catch (error) {
                console.error("Error updating account:", error);
                toast.error("Đã có lỗi xảy ra, vui lòng thử lại sau.");
            }
        }
    }

    const checkValidate = () => {
        let isValid = true;
        if (hoVaTen === "") {
            setMessageHoVaTen("Vui lòng nhập họ và tên");
            isValid = false;
        }
        if (soDienThoai === "") {
            setMessageSoDienThoai("Vui lòng nhập số điện thoại");
            isValid = false;
        }
        if (email === "") {
            setMessageEmail("Vui lòng nhập email");
            isValid = false;
        }
        if (gioiTinh === "") {
            setMessageGioiTinh("Vui lòng chọn giới tính");
            isValid = false;
        }
        if (!hinhAnh) {
            setMessageHinhAnh("Vui lòng chọn hình ảnh");
            isValid = false;
        }
        return isValid;
    }

    return (
        <>
            <Navbar />
            <main className='main-update-account' style={{paddingTop:40}}>
                <div className="row pt-5 ms-5 me-5">
                    <div className="col-md-6 d-flex justify-content-center">
                        <div className="card">
                            <div className="card-body">
                                <div className="div-title">
                                    <div className="text-center fs-2 fw-bold">Cập nhật tài khoản</div>
                                </div>
                                <div className="">
                                    <form onSubmit={handleUpdateAccount}>
                                        <div className="d-flex justify-content-center div-input">
                                            <div className="input-box">
                                                <div>
                                                    <i className="fa-solid fa-user"></i>
                                                    <input
                                                        type="text"
                                                        placeholder="Họ và tên"
                                                        className=''
                                                        value={hoVaTen} 
                                                        onChange={(e) => setHoVaTen(e.target.value)}
                                                    />
                                                </div>
                                                <span className="text-danger">{messageHoVaTen}</span>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center div-input mt-1">
                                            <div className="input-box">
                                                <div>
                                                    <i className="fa-solid fa-phone"></i>
                                                    <input
                                                        type="text"
                                                        placeholder="Số điện thoại"
                                                        className=''
                                                        value={soDienThoai}
                                                        onChange={(e) => setSoDienThoai(e.target.value)}
                                                    />
                                                </div>
                                                <span className="text-danger">{messageSoDienThoai}</span>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center div-input mt-1">
                                            <div className="input-box">
                                                <div>
                                                    <i className="fa-regular fa-envelope"></i>
                                                    <input
                                                        type="text"
                                                        placeholder="Email"
                                                        className=''
                                                        value={email}
                                                        onChange={(e) => setEmail(e.target.value)}
                                                    />
                                                </div>
                                                <span className="text-danger">{messageEmail}</span>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-1">
                                            <div className="d-flex justify-content-start" style={{ width: '350px', height: '51px' }}>
                                                <div>
                                                    <label className="fs-5" htmlFor="">Giới tính:</label>
                                                    <input
                                                        id='nam'
                                                        type="radio"
                                                        className="ms-3"
                                                        name="gioiTinh"
                                                        value="true"
                                                        checked={gioiTinh}
                                                        onChange={(e) => setGioiTinh(true)}
                                                    /> <label htmlFor="nam">Nam</label>
                                                    <input
                                                        id='nu'
                                                        type="radio"
                                                        className="ms-2"
                                                        name="gioiTinh"
                                                        value="false"
                                                        checked={!gioiTinh}
                                                        onChange={(e) => setGioiTinh(false)}
                                                    /> <label htmlFor="nu">Nữ</label>
                                                    <span className="text-danger">{messageGioiTinh}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-1">
                                            <div className="d-flex justify-content-start" style={{ width: '350px' }}>
                                                <div className="row">
                                                    <label className="fs-5 p-0" htmlFor="">Hình ảnh:</label>
                                                    <input
                                                        type="file"
                                                        className="form-control col-md-9"
                                                        onChange={(e) => setHinhAnh(e.target.files[0])}
                                                    />
                                                    <span className="text-danger p-0">{messageHinhAnh}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-4 mb-2 div-button">
                                            <div className="d-flex justify-content-end">
                                                <button type="submit" className="btn btn-success ">Lưu thay đổi</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-6">
                        <div className="d-flex justify-content-center">
                            <img src={imgCrush} alt="" className="hero-img"/>
                        </div>
                    </div>
                </div>
            </main>
            <Footer />
        </>
    )
}
