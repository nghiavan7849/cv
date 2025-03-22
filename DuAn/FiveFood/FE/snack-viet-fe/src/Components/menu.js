import React, { forwardRef, useImperativeHandle } from 'react';
import '../css/menu.css';  // Đảm bảo đường dẫn chính xác
import { NavLink } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState, useContext } from 'react';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
import { AuthContext } from '../Service/AuthContext';
import formatVND from '../Service/FormatVND';
const urlApi = process.env.REACT_APP_API_URL;
const getCardByTaiKhoan = (maTaiKhoan, token) => {
    return axios.get(`${urlApi}/api/chi-tiet-gio-hang/list-by-tk?maTaiKhoan=${maTaiKhoan}`,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const Navbar = forwardRef((props, ref) => {
    useImperativeHandle(ref, ()=>({
        loadListCartMenu: loadListCart,
    }));

    const [listCart, setListCart] = useState([]);
    const [taiKhoan, setTaiKhoan] = useState({});
    const {updateTaiKhoan} = useContext(AuthContext);
    const [clickLogout, isClickLogout] = useState(false);
    const navigate = useNavigate();
    const clickDangXuat = () => {
        isClickLogout(true);
        sessionStorage.removeItem('sessionTaiKhoan');
        sessionStorage.removeItem('sessionTokenTK');
        updateTaiKhoan(null);
        setListCart([]);
        setTaiKhoan({});
        loadPage();
    }

    useEffect(() => {
        loadPage()
    }, [])

    const loadPage = () => {
        let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
        let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
        setTaiKhoan(sessionTaiKhoan);
        if (sessionToken !== null) {
            console.log('sessionToken', sessionToken)

            let token = jwtDecode(sessionToken);
            console.log('token', token)
            const issuedAt = new Date(token.iat * 1000); // Chuyển đổi từ giây sang mili giây
            console.log('issuedAt', issuedAt)
            const expiration = new Date(token.exp * 1000); // Chuyển đổi từ giây sang mili giây
            console.log('expiration', expiration)

            setTimeout(() => {
                sessionStorage.removeItem('sessionTaiKhoan');
                sessionStorage.removeItem('sessionTokenTK');
                updateTaiKhoan(null);
                navigate("/home");
            }, [expiration - issuedAt])

            loadListCart(sessionTaiKhoan, sessionToken);
        }
    }

    useEffect(() => {
        if (clickLogout) {
            navigate('/home');
        }
    }, [clickLogout]);

    const loadListCart = async (sessionTaiKhoan, sessionToken) => {
        let res = await getCardByTaiKhoan(sessionTaiKhoan.maTaiKhoan, sessionToken);
        if (res) {
            setListCart(res.data.data.chiTietGioHang);
        }
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-light fixed-top">
            <NavLink className="navbar-brand" to="/home" style={{ fontFamily: 'Rancho' }}>Five Food 24 Giờ</NavLink>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse justify-content-center" id="navbarNav">
                <ul className="navbar-nav">
                    <li className="nav-item active">
                        <NavLink className="nav-link" to="/home">Trang Chủ</NavLink>
                    </li>
                    <li className="nav-item">
                        <NavLink className="nav-link" to="/home/product">Đồ Ăn</NavLink>
                    </li>
                    <li className="nav-item">
                        <NavLink className="nav-link">Về Chúng Tôi</NavLink>
                    </li>
                    <li className="nav-item">
                        <NavLink className="nav-link">Liên Hệ</NavLink>
                    </li>
                </ul>
            </div>
            <div className="navbar-nav ml-auto">
                <li className="nav-item cart-dropdown ">
                    <NavLink className="nav-link cart-icon " to="/home/cart">
                        <i className="fas fa-shopping-cart position-relative">
                            {
                                listCart.length === 0 ? (
                                    <></>
                                ) : (
                                    <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                        {listCart.length}
                                    </span>
                                )
                            }

                        </i>
                    </NavLink>

                    {
                        listCart.length === 0 ? (
                            <div className="cart-dropdown-content" style={{ borderRadius:5 }}>
                                <p className='mt-3 ms-3'>Chưa Có Sản Phẩm</p>
                            </div>
                        ) : (
                            <div className="cart-dropdown-content" style={{ width: "350px", borderRadius:5 }}>
                                {
                                    listCart.map((item) => {
                                        return (
                                            <div className="row mt-1" key={item.sanPhamGH.maSanPham}>
                                                <div className="col-md-2"><img alt="" src={item.sanPhamGH.hinhAnh} height="40px" width="40px" /> </div>
                                                <div className="col-md-5 align-content-center" >{item.sanPhamGH.tenSanPham}</div>
                                                <div className="col-md-5 align-content-center">{item.soLuong} x {formatVND(item.sanPhamGH.gia)} <formatVND value={item.sanPhamGH.gia} format={"0.0"} /></div>
                                            </div>
                                        )
                                    })
                                }
                            </div>
                        )
                    }
                </li>
                <li className="nav-item cart-dropdown">
                    <NavLink className="nav-link cart-icon" id="userDropdown" role="button">
                        {taiKhoan !== null ? (
                            <>
                                { taiKhoan.hinhAnh !== null ? (
                                        <img className="img-menu" alt="" src={taiKhoan.hinhAnh} />
                                    ) : (
                                        <i className="fas fa-user"></i>
                                    )

                                }
                            </>
                        ) : (
                            <i className="fas fa-user"></i>
                        )}
                    </NavLink>
                    <div className="cart-dropdown-content" style={{ borderRadius:5 }} aria-labelledby="userDropdown">
                        {JSON.parse(sessionStorage.getItem('sessionTaiKhoan')) ? (
                            <>
                                <NavLink className="dropdown-item" to="/home/address">Địa chỉ</NavLink>
                                <NavLink className="dropdown-item" to="/home/order-history">Đơn mua</NavLink>
                                <NavLink className="dropdown-item" to="/home/change-password">Đổi mật khẩu</NavLink>
                                <NavLink className="dropdown-item" to="/home/edit-profile">Cập nhật tài khoản</NavLink>
                                {sessionStorage.getItem('sessionTaiKhoan') ? (
                                    <>
                                        {
                                            JSON.parse(sessionStorage.getItem('sessionTaiKhoan')).vaiTro && (
                                                <NavLink className="dropdown-item" to="/home-admin">Admin</NavLink>
                                            )
                                        }
                                    </>
                                ) : (
                                    <></>
                                )
                                }
                                <NavLink className="dropdown-item" onClick={() => clickDangXuat()}>Đăng xuất</NavLink>
                            </>
                        ) : (
                            <>
                                <NavLink className="dropdown-item" to="/login">Đăng Nhập</NavLink>
                                <NavLink className="dropdown-item" to="/register">Đăng Ký</NavLink>
                            </>
                        )}
                    </div>
                </li>
            </div>
        </nav>
    );
});

export default Navbar;
