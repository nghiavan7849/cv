import React, { useEffect, useState } from 'react';
import Footer from './Footer';
import SideNav from './SideNav';
import HorizontalNav from './HorizontalNav';
import axios from 'axios';
import { toast } from 'react-toastify';
import { NavLink } from 'react-router-dom';
import '../../css/Admin/pagination.css'
import UploadImg from './UploadImg';
import { getDownloadURL, ref, uploadBytes } from 'firebase/storage';
import { storage } from '../Firebase';
import {v4} from 'uuid'
import ReactPaginate from 'react-paginate';
const urlApi = process.env.REACT_APP_API_URL;

const User = () => {

    const [cards,setCards] = useState([]);
    const [editting,setEditting] = useState(false);
    const [users,setUsers] = useState([]);
    const [pagination,setPagination] = useState([]);
    const [search, setSearch] = useState('');
    const [sort, setSort] = useState('Tên tài khoản');
    const [err, setErr] = useState({
        errName: '',
        errUsername: '',
        errPhone: '',
        errPass: '',
        errEmail: ''
    });

    //Chi tiết người dùng
    const [displayU,setDisplayU] = useState([]);
    const [displayUser,setDisplayUser] = useState([]);
    // const [statusList,setStatusList] = useState([]);
    // const [selectedStatus, setSelectedStatus] = useState("");
    const [imageFile, setImageFile] = useState(null);
    const [resetPreview,setResetPreview] = useState(false);
    const [imageUrls, setImageUrls] = useState({});

    const baseUrl = `${urlApi}/api/admin/quan-ly-nguoi-dung`;
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    const pages = Array.from({ length: pagination.totalPages }, (_, index) => index + 1);

    const fetchData = async(pageNo=1)=>{
        resetForm();
        try {
            const response = await axios.get(`${baseUrl}?pageNo=${pageNo}&sort=${sort}&search=${search}`,{
                headers:{
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            setCards(response.data.data.cards);
            setEditting(response.data.data.editting);
            setUsers(response.data.data.users);
            setPagination(response.data.data.pagination);
        } catch (error) {
            
        }finally{
            
        }
    }

    const fetchDetail = (maTaiKhoan)=>(
        async()=>{
            try {
                const response = await axios.get(baseUrl+"/"+maTaiKhoan,{
                    headers:{
                        Authorization: `Bearer ${sessionToken}`
                    }
                })
                setDisplayU(response.data.data.displayU);
                setDisplayUser(response.data.data.displayUser);
                setEditting(response.data.data.editting);
            } catch (error) {
                toast.error("Có lỗi xảy ra khi lấy dữ liệu user mới");
            }finally{
    
            }
        }
    )

    const handleSearchChange = (event) => {
        setSearch(event.target.value);
    }

    const handleSortChange = (event) => {
        setSort(event.target.value);
    }

    const handlePageClick = (data) => {
        let selectedPage = data.selected + 1; // Page index is 0-based in react-paginate
        fetchData(selectedPage);  // Gọi hàm fetchData với số trang đã chọn
      };

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        fetchData();
    }

    const enableFields = (value)=> {
        if(value==='disabled'){
            document.getElementById("tenDangNhap").disabled = true;
            document.getElementById("email").disabled = true;
            document.getElementById("soDienThoai").disabled = true;
        }
        else{
            document.getElementById("tenDangNhap").disabled = false;
            document.getElementById("email").disabled = false;
            document.getElementById("soDienThoai").disabled = false;
        }
    }


    //THÊM SỬA CHO USER
    const handlePost = async()=>{

        const payload = {
            // idForUpdate: displayU?.idForUpdate || null,  // Hidden input for update
            hoVaTen: displayU.hoVaTen || '',             // User's name
            tenDangNhap: displayU.tenDangNhap || '',     // Username
            matKhau: v4(),             // Password
            email: displayU.email || '',                 // Email
            vaiTro: displayU.vaiTro ,  // Role (Admin/User)
            trangThai: displayU.trangThai , // Status
            soDienThoai: displayU.soDienThoai || '',     // Phone number
            gioiTinh: displayU.gioiTinh ,  // Gender (Male/Female)
        };

        if (
            err.errName === '' && 
            err.errUsername === '' && 
            err.errPhone === '' && 
            err.errEmail === ''
        ){
            axios.post(baseUrl + "/create", payload, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            .then((response) => {
                toast.success(response.data.message);
                fetchData();
                setErr({
                    errEmail: "",
                    errName:"",
                    errPass:"",
                    errPhone:"",
                    errUsername:""
                })
            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message || "Đã xảy ra lỗi.";
                    setErr(error.response.data.data);
                    console.log(error)
                    toast.error(errorMessage);
                } else {
                    toast.error("Đã xảy ra lỗi không xác định.");
                }
            })
            .finally(() => {
                resetForm();
            });
        }
        else{
            toast.error("Vui lòng kiểm tra lỗi hiển thị!");
            console.log(Object.keys(err).length); // In ra số lượng lỗi
        }
    }

    const handlePut = ()=>{
        enableFields("disabled");
        const payload = {
            hoVaTen: displayU.hoVaTen || '',             // User's name
            tenDangNhap: displayU.tenDangNhap || '',     // Username
            email: displayU.email || '',                 // Email
            vaiTro: displayU.vaiTro ,  // Role (Admin/User)
            trangThai: displayU.trangThai , // Status
            soDienThoai: displayU.soDienThoai || '',     // Phone number
            gioiTinh: displayU.gioiTinh ,  // Gender (Male/Female)
        }

        if (
            err.errName === '' && 
            err.errUsername === '' && 
            err.errPhone === '' && 
            err.errEmail === ''
        ){
            axios.put(baseUrl + "/update/"+displayU.maTaiKhoan, payload, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            .then((response) => {
                toast.success(response.data.message);
                fetchData();
                setErr({
                    errEmail: "",
                    errName:"",
                    errPass:"",
                    errPhone:"",
                    errUsername:""
                })
            })
            .catch((error) => {
                if (error.response) {
                    const errorMessage = error.response.data.message || "Đã xảy ra lỗi.";
                    setErr(error.response.data.data);
                    console.log(error)
                    toast.error(errorMessage);
                    enableFields("disabled");
                } else {
                    toast.error("Đã xảy ra lỗi không xác định.");
                }
            })
            .finally(() => {
                resetForm();
            });
        }
        else{
            toast.error("Vui lòng kiểm tra lỗi hiển thị!");
            console.log(Object.keys(err).length); // In ra số lượng lỗi
        }
    }

    const resetForm = () => {
        setDisplayU({
            hoVaTen: '',
            tenDangNhap: '',
            matKhau: '', //Sử dụng method v4 từ thư viện uuid để generate một chuôi random
            email: '',
            vaiTro: true,  // Giá trị mặc định cho vai trò
            trangThai: true, // Giá trị mặc định cho trạng thái
            soDienThoai: '',
            gioiTinh: true // Giá trị mặc định cho giới tính
        });
        setResetPreview(true);  // Truyền giá trị true để reset preview
    };

    const basicValidation = (e,fieldName)=>{
        if(fieldName==="fullname"){
            if(e.target.value.length<=0){
                setErr({...err,errName:"Vui lòng nhập vào họ và tên!"})
            }
            else{
                setErr({...err,errName:""})
            }
            setDisplayU({...displayU,hoVaTen:e.target.value})
        }
        else if(fieldName==="username"){
            if(e.target.value.length<=0){
                setErr({...err,errUsername:"Vui lòng nhập vào tên tài khoản!"})
            }
            else{
                setErr({...err,errUsername:""})
            }
            setDisplayU({...displayU,tenDangNhap:e.target.value})
        }
        else if(fieldName==="email"){
            if(e.target.value===""){
                setErr({...err,errEmail:"Vui lòng nhập vào email!"})
            }
            else if(!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,}(\.[A-Za-z]{2,})?$/.test(e.target.value)){
                setErr({...err,errEmail:"Email không đúng định dạng!"})
            }
            else{
                setErr({...err,errEmail:""})
            }
            setDisplayU({...displayU,email:e.target.value})
        }
        else if(fieldName==="phone"){
            if(e.target.value===""){
                setErr({...err,errPhone:"Vui lòng nhập vào số điện thoại!"})
            }
            else if(!/^(0[3-9])\d{8}$/.test(e.target.value)){
                setErr({...err,errPhone:"Số điện thoại không đúng định dạng!"})
            }
            else{
                setErr({...err,errPhone:""})
            }
            setDisplayU({...displayU,soDienThoai:e.target.value})
        }
    }

    // Dùng useEffect để log giá trị của các state sau khi nó thay đổi
    // useEffect(() => {
    //     basicValidation()
    // }, [err]);

    useEffect(()=>{
        fetchData();
    },[sort, search]);

    return (
        <div className="wrapper">
            <SideNav></SideNav>
            <div className="main main-admin p-0">
                <HorizontalNav></HorizontalNav>
                <main className="content">
                    <div className="container-fluid p-0">
                        <h1 className="h3 mb-3">Danh sách người dùng</h1>
                        <div className="row">
                            <div className="col-md-6">
                                <div className="card">
                                    <div className="card-body" style={{borderRadius:8}}>
                                        <div className="row">
                                            <div className="col mt-0">
                                                <h5 className="card-title">Tổng số người dùng</h5>
                                            </div>

                                            <div className="col-auto">
                                                <div className="stat text-primary">
                                                    <i className="fa-solid fa-arrow-up-right-dots" style={{color: "#067a38"}}></i>
                                                </div>
                                            </div>
                                        </div>
                                        <h1 className="mt-1 mb-3 fs-2"> {cards.total} </h1>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="card">
                                    <div className="card-body" style={{borderRadius:8}}>
                                        <div className="row">
                                            <div className="col mt-0">
                                                <h5 className="card-title">Số người dùng bị khóa</h5>
                                            </div>

                                            <div className="col-auto">
                                                <div className="stat text-primary">
                                                    <i className="fa-solid fa-arrow-up-right-dots" style={{color: "#067a38"}}></i>
                                                </div>
                                            </div>
                                        </div>
                                        <h1 className="mt-1 mb-3 fs-2">

                                            {cards.inactive}

                                        </h1>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="userDetail" style={{marginTop:"20px"}}>
                            <h4> Chi tiết người dùng </h4>
                            <form>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Họ và tên:</label>
                                            <input value={displayU?.hoVaTen || ''} name="hoVaTen" type="text"
                                                className="form-control" placeholder="Nhập vào họ và tên"
                                                onChange={(e)=>(basicValidation(e,"fullname"))}
                                                />
                                            <em className='text-danger'>{err?.errName}</em>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Tên tài khoản:</label>
                                            <input id="tenDangNhap" disabled={editting} name="tenDangNhap"
                                                value={displayU?.tenDangNhap || ''} type="text"
                                                className="form-control" placeholder="Nhập vào tên tài khoản"
                                                onChange={(e)=>(basicValidation(e,"username"))}
                                                />
                                            <em className='text-danger'>{err?.errUsername}</em>
                                        </div>
                                    </div>
                                </div>

                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Vai trò:</label>
                                            <div className="form-check">
                                                <input checked={displayU.vaiTro === true} className="form-check-input"
                                                    value={true} type="radio" name="vaiTro" id="flexRadioDefault1"
                                                    onChange={(e)=>setDisplayU({...displayU,vaiTro: e.target.value === 'true'})}
                                                    />Admin
                                            </div>
                                            <div className="form-check">
                                                <input checked={displayU.vaiTro === false} className="form-check-input"
                                                    value={false} type="radio" name="vaiTro" id="flexRadioDefault2"
                                                    onChange={(e)=>setDisplayU({...displayU,vaiTro: e.target.value === 'true'})}
                                                    />User
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Trạng thái:</label>
                                            <div className="form-check">
                                                <input checked={displayU.trangThai === true} className="form-check-input"
                                                    type="radio" value={true} name="trangThai" id="flexRadioDefault3"
                                                    onChange={(e)=>setDisplayU({...displayU,trangThai: e.target.value === 'true'})}
                                                    />Hoạt động
                                            </div>
                                            <div className="form-check">
                                                <input checked={displayU.trangThai === false} className="form-check-input"
                                                    type="radio" value={false} name="trangThai" id="flexRadioDefault4"
                                                    onChange={(e)=>setDisplayU({...displayU,trangThai: e.target.value === 'true'})}
                                                    />Bị khóa
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Giới tính:</label>
                                            <div className="form-check">
                                                <input
                                                    checked={displayU.gioiTinh === true} 
                                                    className="form-check-input"
                                                    value={true} 
                                                    type="radio"
                                                    name="gioiTinh"
                                                    id="flexRadioDefault5"
                                                    onChange={(e) => setDisplayU({ ...displayU, gioiTinh: e.target.value === 'true' })}
                                                />Nam
                                            </div>
                                            <div className="form-check">
                                                <input
                                                    checked={displayU.gioiTinh === false} 
                                                    className="form-check-input"
                                                    value={false} 
                                                    type="radio"
                                                    name="gioiTinh"
                                                    id="flexRadioDefault6"
                                                    onChange={(e) => setDisplayU({ ...displayU, gioiTinh: e.target.value === 'true' })}
                                                />Nữ
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Email:</label>
                                            <input id="email" disabled={editting} name="email"
                                                value={displayU?.email || ''} type="email" className="form-control"
                                                placeholder="Nhập vào email"
                                                onChange={(e)=>(basicValidation(e,"email"))}
                                                />
                                            <em className='text-danger'>{err?.errEmail}</em>
                                        </div>
                                    </div>
                                </div>

                                <div className="row">
                                    <div className="col-md-12">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Số điện thoại:</label>
                                            <input id="soDienThoai" disabled={editting}
                                                value={displayU?.soDienThoai || ''} name="soDienThoai"
                                                type="text" className="form-control"
                                                placeholder='Nhập vào số điện thoại'
                                                onChange={(e)=>(basicValidation(e,"phone"))}
                                                />
                                            <em className='text-danger'>{err?.errPhone}</em>
                                        </div>
                                    </div>
                                </div>

                                <button type='button' disabled={editting} className="btn btn-success me-2" onClick={handlePost}>Thêm</button>
                                <button type="button" onClick={handlePut} disabled={!editting} className="btn btn-primary me-2">Cập nhật</button>
                                <button type='button' onClick={resetForm} className="btn me-2" style={{ backgroundColor: '#656565' }}>Làm mới</button>

                            </form>
                        </div>

                        <div className="userList" style={{marginTop:"20px"}}>
                            <h4 className="text-center">Danh sách người dùng</h4>
                            <form>
                                <div className="row">
                                    <div className="col-md-4 col-xxl-8">
                                        <div className="input-group mb-3">
                                            <select onChange={handleSortChange} name="sort" className="form-select" aria-label="Default select example">
                                                <option value="Tên tài khoản">Tên tài khoản</option>
                                                <option value="Số điện thoại">Số điện thoại</option>
                                                <option value="Email">Email</option>
                                                <option value="Họ và tên">Họ và tên</option>
                                            </select>
                                            <input value={search} name="search" type="search" className="form-control"
                                                placeholder="Tìm kiếm" aria-describedby="basic-addon2" onChange={handleSearchChange}/>
                                            {/* <button type="submit" className="input-group-text btn btn-success" id="basic-addon2">Tìm
                                                kiếm</button> */}
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <table className="table table-striped table-hover table-light">
                                <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff',fontSize:'0.8rem' }}>
                                    <th>Mã tài khoản</th>
                                    <th>Tên tài khoản</th>
                                    <th>Họ và tên</th>
                                    <th>Email</th>
                                    <th>Số ĐT</th>
                                    <th>Vai trò</th>
                                    <th>Trạng thái</th>
                                    <th>Giới tính</th>
                                    <th>Hành động</th>
                                </thead>
                                <tbody className='text-center'>
                                    {users.map((user) => (
                                        <tr key={user.maTaiKhoan}>
                                            <td>{user.maTaiKhoan}</td>
                                            <td>{user.tenDangNhap}</td>
                                            <td>{user.hoVaTen}</td>
                                            <td>{user.email}</td>
                                            <td>{user.soDienThoai}</td>
                                            <td>{user.vaiTro === true ? 'Admin' : 'Khách hàng'}</td>
                                            <td>
                                                <span className={`badge ${user.trangThai === true ? 'bg-success' : 'bg-danger' }`}>{user.trangThai
                                                    === true ? 'Hoạt động' : 'Bị khóa' }</span>
                                            </td>
                                            <td>{user.gioiTinh ? 'Nam' : 'Nữ'}</td>
                                            <td>
                                                <i className="fa-solid fa-pen-to-square" onClick={fetchDetail(user.maTaiKhoan)}></i>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>

                            <div className="text-center" hidden={pagination.totalPages!==0}>
                                <p className="fw-bold">Không tìm thấy sản phẩm tương ứng</p>
                            </div>

                            <div hidden={pagination.totalPages===0} className="d-flex justify-content-between" style={{marginTop:"25px"}}>
                                {/* Vị trí hiển thị số trang */}
                                <p className="fw-bold">Đang xem trang {pagination.currentPage} / {pagination.totalPages}</p>

                                {/* React Paginate */}
                                <ReactPaginate
                                    previousLabel={"«"}
                                    nextLabel={"»"}
                                    breakLabel={"..."}
                                    breakClassName={"page-item"} // Áp dụng style cho phần tử 3 chấm
                                    breakLinkClassName={"page-link"} // Áp dụng style cho link của 3 chấm
                                    pageCount={pagination.totalPages}
                                    marginPagesDisplayed={2}
                                    pageRangeDisplayed={5}  // Số lượng trang hiển thị
                                    onPageChange={handlePageClick}
                                    containerClassName={"pagination"}
                                    pageClassName={"page-item"}
                                    pageLinkClassName={"page-link"}
                                    previousClassName={"page-item"}
                                    previousLinkClassName={"page-link"}
                                    nextClassName={"page-item"}
                                    nextLinkClassName={"page-link"}
                                    activeClassName={"active"}
                                    disabledClassName={"disabled"}
                                    forcePage={pagination.currentPage > 0 ? pagination.currentPage - 1 : 0}  // Chuyển currentPage thành 0-based index cho ReactPaginate
                                />
                                <p className="fw-bold">5 sản phẩm / 1 trang</p>
                            </div>

                        </div>

                    </div>
                </main>
                <Footer></Footer>
            </div>
        </div>
    );
};

export default User;