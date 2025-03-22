import { jwtDecode } from 'jwt-decode';
import React, { useEffect, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';

const HorizontalNav = () => {

    const navigate = useNavigate();
    const [hinhAnh,setHinhAnh] = useState("");
    const [hoTen,setHoTen] = useState("");
    const clickDangXuat = () => {
        sessionStorage.removeItem('sessionTaiKhoan');
        sessionStorage.removeItem('sessionTokenTK');
        setTimeout(() => {
            navigate("/home");
        }, [500])
    }
    useEffect(() => {
        let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
        let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

        setHinhAnh(sessionTaiKhoan.hinhAnh);
        setHoTen(sessionTaiKhoan.hoVaTen);

        if (sessionToken !== null) {
            console.log(sessionToken)
            let token = jwtDecode(sessionToken);
            const issuedAt = new Date(token.iat * 1000); // Chuyển đổi từ giây sang mili giây
            const expiration = new Date(token.exp * 1000); // Chuyển đổi từ giây sang mili giây

            setTimeout(() => {
                sessionStorage.removeItem('sessionTaiKhoan');
                sessionStorage.removeItem('sessionTokenTK');
                navigate("/home");
            }, [expiration - issuedAt])
        }
    }, [])

    return (
        <nav className="navbar navbar-expand navbar-light navbar-bg nav-admin">
                <NavLink style={{marginLeft:"20px"}} className="sidebar-toggle js-sidebar-toggle">
                    <i className="hamburger align-self-center"></i>
                </NavLink>

                {/* <div className="navbar-collapse collapse">
                    <ul className="navbar-nav navbar-align">
                        <li className="nav-item dropdown">
                            <NavLink className="nav-icon dropdown-toggle d-inline-block d-sm-none" href="#" data-bs-toggle="dropdown"> <i
                                    className="align-middle" data-feather="settings"></i></NavLink>
                            <NavLink className="nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-bs-toggle="dropdown">
                                <img src={hinhAnh}
                                    className="avatar img-fluid rounded me-1" alt="avatar" /> <span
                                    className="text-dark">{hoTen}
                                </span>
                            </NavLink>
                            <div className="dropdown-menu dropdown-menu-end">
                                <NavLink className="dropdown-item" to={"/home/edit-profile"}><i className="align-middle me-1"
                                        data-feather="user"></i> Thông tin cá nhân</NavLink>
                                <NavLink className="dropdown-item" to={"/home"}><i className="align-middle me-1"
                                        data-feather="user"></i> Về trang người dùng</NavLink>
                                <div className="dropdown-divider"></div>
                                <NavLink className="dropdown-item" onClick={() => clickDangXuat()}>Đăng xuất</NavLink>
                            </div>
                        </li>
                    </ul>
                </div> */}

                <div className="navbar-collapse collapse">
                    <ul className="navbar-nav navbar-align">
                        <li className="nav-item dropdown">
                            {/* Icon cho mobile */}
                            <NavLink className="nav-icon dropdown-toggle d-inline-block d-sm-none" href="#" data-bs-toggle="dropdown">
                                <i className="align-middle" data-feather="settings"></i>
                            </NavLink>

                            {/* Avatar và tên người dùng cho desktop */}
                            <NavLink className="custom-nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-bs-toggle="dropdown">
                                <img src={hinhAnh} className="avatar img-fluid rounded me-1" alt="avatar" />
                                <span className="text-dark">{hoTen}</span>
                            </NavLink>

                            {/* Menu drop-down */}
                            <div className="dropdown-menu dropdown-menu-end" style={{ zIndex: 1050 }}>
                                <NavLink className="dropdown-item" to="/home/edit-profile">
                                    <i className="align-middle me-1" data-feather="user"></i> Thông tin cá nhân
                                </NavLink>
                                <NavLink className="dropdown-item" to="/home">
                                    <i className="align-middle me-1" data-feather="user"></i> Về trang người dùng
                                </NavLink>
                                <div className="dropdown-divider"></div>
                                <NavLink className="dropdown-item" onClick={() => clickDangXuat()}>Log out</NavLink>
                            </div>
                        </li>
                    </ul>
                </div>

            </nav>
    );
};

export default HorizontalNav;