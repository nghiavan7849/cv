import React, { useEffect, useState } from 'react';
import Footer from './Footer';
import SideNav from './SideNav';
import HorizontalNav from './HorizontalNav';
import axios from 'axios';
import { toast } from 'react-toastify';
import { NavLink } from 'react-router-dom';
import '../../css/Admin/pagination.css'
import Modal from 'react-bootstrap/Modal'
import ReactPaginate from 'react-paginate';

const urlApi = process.env.REACT_APP_API_URL;
const Order = () => {
    const [cards,setCards] = useState([]);
    const [listTrangThai,setListTrangThai] = useState([]);
    const [editting,setEditting] = useState(false);
    const [invoices,setInvoices] = useState([]);
    const [pagination,setPagination] = useState([]);
    const [search, setSearch] = useState('');
    const [sort, setSort] = useState('Mã hóa đơn');
    const [ngayFrom,setNgayFrom] = useState(new Date());
    const [ngayTo,setNgayTo] = useState(new Date());
    const [reason,setReason] = useState("")

    //hiển thị modal
    const [show,setShow] = useState(false);

    //Chi tiết hóa đơn
    const [displayI,setDisplayI] = useState([]);
    const [displayHoaDon,setDisplayHoaDon] = useState([]);
    const [statusList,setStatusList] = useState([]);
    const [selectedStatus, setSelectedStatus] = useState("");

    const baseUrl = `${urlApi}/api/admin/quan-ly-hoa-don`;
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    const pages = Array.from({ length: pagination.totalPages }, (_, index) => index + 1);

    const formatDate = (date) => {
        //Chuyển đổi đối tượng Date thành chuỗi ISO với toISOString()
        //Sử dụng split('T')[0] để lấy phần ngày từ chuỗi
        return new Date(date).toISOString().split('T')[0];
    }

    const fetchData = async(pageNo=1)=>{
        try {
            const formattedNgayFrom = formatDate(ngayFrom);
            const formattedNgayTo = formatDate(ngayTo);
            const response = await axios.get(`${baseUrl}?pageNo=${pageNo}&sort=${sort}&search=${search}&ngayFrom=${formattedNgayFrom}&ngayTo=${formattedNgayTo}`,{
                headers:{
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            setCards(response.data.data.cards);
            setListTrangThai(response.data.data.listTrangThai);
            setInvoices(response.data.data.invoices);
            setPagination(response.data.data.pagination);
        } catch (error) {
            
        }finally{

        }
    }

    const handleShow = (maHoaDon,status)=>{
        if(selectedStatus==='Đã Hủy'){
            setShow(true);
        }
        else{
            updateOrder(maHoaDon,status);
        }
        
    }

    const handleClose = ()=>{
        setShow(false);
        setEditting(true);
    }

    const handleSend = (maHoaDon,status)=>{
        setShow(true);
        updateOrder(maHoaDon,status)
    }

    const fetchDetail = async(maHoaDon)=>{
        try {
            const response = await axios.get(baseUrl+"/"+maHoaDon,{
                headers:{
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            setDisplayI(response.data.data.displayI);
            setDisplayHoaDon(response.data.data.displayHoaDon);
            setStatusList(response.data.data.listTrangThai);
            setEditting(response.data.data.editting);
            console.log(displayI);
        } catch (error) {
            toast.error(error);
        }finally{

        }
    }

    const toggleSearchAndDateFields = ()=>{
        var sortSelect = document.getElementById("sortSelect");
	    var searchField = document.getElementById("searchField");
	    var dateFields = document.getElementById("dateFields");
	        
	    if (sortSelect && sortSelect.value === "Ngày thanh toán") {
            searchField.style.display = "none";
            dateFields.style.display = "flex"; // Keep elements in the same row
        } else if (sortSelect) {
            searchField.style.display = "flex"; // Keep elements in the same row
            dateFields.style.display = "none";
        }
    }

    const formattedMoney = (amount) => (
        amount.toLocaleString('vi-VN',{
            style: 'currency',
            currency: 'VND'
        })
    )

    const handlePageClick = (data) => {
        let selectedPage = data.selected + 1; // Page index is 0-based in react-paginate
        fetchData(selectedPage);  // Gọi hàm fetchData với số trang đã chọn
      };

    const handleSearchChange = (event) => {
        setSearch(event.target.value);
    }

    const handleSortChange = (event) => {
        setSort(event.target.value);
        toggleSearchAndDateFields();
    }

    const handleDateChange = (event) => {
        const {name,value} = event.target;
        if(name === 'ngayFrom'){
            setNgayFrom(new Date(value));
        }else if(name === 'ngayTo'){
            setNgayTo(new Date(value));
        }
    }

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        fetchData();
    }


    // Hàm xử lý khi người dùng chọn một trạng thái
    const handleStatusChange = (event) => {
        setSelectedStatus(event.target.value); // Lưu giá trị trạng thái vào state
        setDisplayHoaDon({...displayHoaDon,trangThaiHoaDon:{tenTrangThai:event.target.value}});
    };

    // Dùng useEffect để log giá trị của các state sau khi nó thay đổi
    useEffect(() => {
        console.log(selectedStatus);
        console.log(search)
    }, [selectedStatus,search]); // useEffect sẽ chạy mỗi khi selectedStatus thay đổi

    const updateOrder = (maHoaDon, status)=>{
        axios.put(`${baseUrl}/update/${maHoaDon}`, 
            { trangThai: status,
              lyDo: reason || ''
            },
            {
                headers: {
                    Authorization: `Bearer ${sessionToken}`,  
                }
            }
        )
        .then(async(response) => {
            toast.success(response.data.message);
            if(status === 'Giao thành công' || status === 'Đã hủy'){
                setEditting(false);
            }
            fetchData();  // Gọi lại fetchData để load lại dữ liệu sau khi cập nhật thành công
            await fetchDetail(maHoaDon);
            setShow(false);
        })
        .catch(error => {
            toast.error("Có lỗi xảy ra khi cập nhật");
            // Xử lý lỗi
        });
    }

    useEffect(()=>{
        fetchData();
        // fetchDetail();
        toggleSearchAndDateFields();
    },[ngayFrom, ngayTo, sort, search]);

    return (
        <div className="wrapper">

            <SideNav></SideNav>

            <div className="main main-admin p-0">

                <HorizontalNav></HorizontalNav>

                <main className="content">
                    <div className="container-fluid p-0">

                        <h1 className="h3 mb-3">Danh sách hóa đơn</h1>

                        <div className="row">
                            <div className="col-md-6">
                                <div className="card">
                                    <div className="card-body" style={{borderRadius:8}}>
                                        <div className="row">
                                            <div className="col mt-0">
                                                <h5 className="card-title">Tổng số hóa đơn</h5>
                                            </div>

                                            <div className="col-auto">
                                                <div className="stat text-primary">
                                                    <i className="fa-solid fa-file-invoice" style={{color: "#067a38"}}></i>
                                                </div>
                                            </div>
                                        </div>
                                        <h1 className="mt-1 mb-3 fs-2">{cards.total}</h1>
                                    </div>
                                </div>
                            </div>
                            <div className="col-md-6">
                                <div className="card">
                                    <div className="card-body" style={{borderRadius:8}}>
                                        <div className="row">
                                            <div className="col mt-0">
                                                <h5 className="card-title">Số hóa đơn bị hủy</h5>
                                            </div>

                                            <div className="col-auto">
                                                <div className="stat text-primary">
                                                    <i className="fa-solid fa-file-invoice" style={{color:"#067a38"}}></i>
                                                </div>
                                            </div>
                                        </div>
                                        <h1 className="mt-1 mb-3 fs-2">{cards.canceled}</h1>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="invoiceDetail" style={{marginTop:"20px"}}>
                            <h4> Hóa đơn chi tiết </h4>
                            <form>
                                <div className="row">
                                    <div className="col-md-8 col-xxl-8">
                                        <table className="table table-striped table-hover table-light">
                                            <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff',fontSize: '0.8rem' }}>                                                
                                                <th>Tên sản phẩm</th>
                                                <th>Số lượng</th>
                                                <th>Tên khách hàng</th>
                                                <th>Địa chỉ</th>
                                                <th>Số điện thoại</th>
                                            </thead>

                                            <tbody className="text-center">
                                                {displayI.map((detail,index)=>(
                                                    <tr key={index}>
                                                        <td>{detail.sanPhamCT.tenSanPham}</td>
                                                        <td>{detail.soLuong}</td>
                                                        <td>{detail.hoaDonCT.dcHoTen}</td>
                                                        <td>{detail.hoaDonCT.diaChiNhan}</td>
                                                        <td>{detail.hoaDonCT.dcSoDienThoai}</td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>

                                    <div className="col-md-4">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Trạng thái:</label>
                                            <select value={selectedStatus} onChange={handleStatusChange} name="trangThaiHoaDon.tenTrangThai" className="form-select"
                                                aria-label="Default select example">
                                                    {statusList!==null ? (
                                                        statusList.map((item, index) => (
                                                            <option value={item.tenTrangThai} key={index}>{item.tenTrangThai}</option>
                                                        ))
                                                        ) : (
                                                        <option value={displayHoaDon.trangThaiHoaDon.tenTrangThai}>{displayHoaDon.trangThaiHoaDon.tenTrangThai}</option>
                                                    )}
                                            </select>
                                            {/* <em className="text-danger" th:text="${errType}"></em> */}
                                        </div>

                                        <button 
                                            disabled={!editting}
                                            onClick={() => handleShow(displayHoaDon.maHoaDon, displayHoaDon?.trangThaiHoaDon ? displayHoaDon.trangThaiHoaDon.tenTrangThai : '')} 
                                            className="btn btn-primary" 
                                            type="button"
                                            >
                                            Cập nhật trạng thái đơn hàng
                                        </button>     
                                        {/* updateOrder(displayHoaDon.maHoaDon,displayHoaDon.trangThaiHoaDon.tenTrangThai)          */}
                                        <Modal show={show} onHide={handleClose} backdrop="static" aria-labelledby="contained-modal-title-vcenter" centered>
                                            <Modal.Header closeButton>
                                                <Modal.Title id="contained-modal-title-vcenter">Lý do hủy</Modal.Title>
                                            </Modal.Header>
                                            <Modal.Body>
                                                <div className='mb-3'>
                                                    <textarea onChange={(event)=>setReason(event.target.value)} placeholder='Nhập vào lý do hủy' className='form-control' name="" id="" cols={3} rows={6}></textarea>
                                                </div>
                                            </Modal.Body>
                                            <Modal.Footer>
                                            <button className='btn btn-primary' onClick={()=>handleSend(displayHoaDon.maHoaDon,displayHoaDon?.trangThaiHoaDon ? displayHoaDon.trangThaiHoaDon.tenTrangThai : '')}>
                                                Gửi
                                            </button>
                                            </Modal.Footer>
                                        </Modal>
                                    </div>                
                                </div>
                            </form>
                        </div>

                        <div className="invoiceList" style={{marginTop:"20px"}}>
                            <h4 className="text-center"> Danh sách hóa đơn </h4>
                            <form onSubmit={handleSearchSubmit}>
                                <div className="row">
                                    <div className="col-md-4 col-xxl-6">
                                        <div className="input-group mb-3">
                                            <select id="sortSelect" name="sort" onChange={toggleSearchAndDateFields&&handleSortChange}
                                                className="form-select" aria-label="Default select example">
                                                {/* <option th:value="${displaySelected ?: 'Chọn bộ lọc'}" selected hidden
                                                    th:text="${displaySelected ?: 'Chọn bộ lọc'}">Chọn bộ lọc</option> */}
                                                <option value="Mã hóa đơn">Mã hóa đơn</option>
                                                <option value="Tên khách hàng">Tên khách hàng</option>
                                                <option value="Ngày thanh toán">Ngày thanh toán</option>
                                                <option value="Trạng thái">Trạng thái</option>
                                            </select>
                                            <div id="searchField">
                                                <input name="search" type="search" className="form-control"
                                                    placeholder="Tìm kiếm" value={search} onChange={handleSearchChange}></input>
                                                {/* <button type="submit" className="btn btn-success">Tìm kiếm</button> */}
                                            </div>
                                        </div>
                                    </div>

                                    <div id="dateFields" className="col-md-8 col-xxl-6" style={{display:"none"}}>
                                        <div className="input-group mb-3">
                                            <input name="ngayFrom" type="date"
                                                className="form-control" placeholder="Từ" onChange={handleDateChange}></input> 
                                            {/* th:value="${#dates.format(ngayFrom, 'yyyy-MM-dd')}" */}
                                            <input name="ngayTo" type="date"
                                                className="form-control" placeholder="Đến" onChange={handleDateChange}></input>
                                            {/* th:value="${#dates.format(ngayTo, 'yyyy-MM-dd')}" */}
                                            <button type="submit" className="btn btn-success">Tìm kiếm</button>
                                        </div>
                                    </div>                
                                </div>
                            </form>

                            <table className="table table-striped table-hover table-light">
                                <thead className="text-center align-middle" style={{ backgroundColor: '#067a38', color: '#fff',fontSize:'0.8rem' }}>
                                    <th>Mã hóa đơn</th>
                                    <th>Ngày thanh toán</th>
                                    <th>Trạng thái</th>
                                    <th>Tổng tiền sản phẩm</th>
                                    <th>Trạng thái thanh toán</th>
                                    <th>Ghi chú</th>
                                    <th>Phí vận chuyển</th>
                                    <th>Phương thức thanh toán</th>
                                    <th>Hành động</th>
                                </thead>    

                                <tbody className="text-center">
                                    {invoices.map((invoice) => (
                                        <tr key={invoice.maHoaDon}>
                                            <td>{invoice.maHoaDon}</td>
                                            <td>{invoice.ngayThanhToan}</td>
                                            <td>
                                                <span className={`badge ${invoice.trangThaiHoaDon.tenTrangThai === 'Đã Hủy' ? 'bg-danger' : invoice.trangThaiHoaDon.tenTrangThai === 'Đang xử lý' ? 'bg-warning' : invoice.trangThaiHoaDon.tenTrangThai === 'Đang vận chuyển' ? 'bg-info' : 'bg-success'}`}>
                                                    {invoice.trangThaiHoaDon.tenTrangThai}
                                                </span>
                                            </td>
                                            <td>
                                                <span>{formattedMoney(invoice.tongTien)}</span>
                                            </td>
                                            <td>{invoice.trangThai ? 'Đã thanh toán' : 'Chưa thanh toán'}</td>
                                            <td>{invoice.ghiChu !== null ? invoice.ghiChu : 'Không có'}</td>
                                            <td>
                                                <span>{formattedMoney(invoice.phiVanChuyen)}</span>
                                            </td>
                                            <td>{invoice.phuongThucThanhToan ? 'Thanh toán khi nhận hàng' : 'Thanh toán online'}</td>
                                            <td>
                                                <i className="fa-solid fa-pen-to-square" onClick={()=>fetchDetail(invoice.maHoaDon)}></i>
                                                {/* th:href="@{/adminIndex/quan-ly-hoa-don/{id}(id=${invoice.maHoaDon})}" */}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>  

                            <div className="text-center" hidden={`${pagination.totalPages===0}`}>
                                <p className="fw-bold">Không tìm thấy hóa đơn tương ứng</p>
                            </div>

                            <div hidden={`${pagination.totalPages===0}`} className="d-flex justify-content-between" style={{marginTop:"25px"}}>
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
                                    forcePage={pagination.currentPage - 1}  // Chuyển currentPage thành 0-based index cho ReactPaginate
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

export default Order;