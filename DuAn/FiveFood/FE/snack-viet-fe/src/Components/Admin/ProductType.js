import React, { useEffect, useState } from 'react';
import SideNav from './SideNav';
import HorizontalNav from './HorizontalNav';
import { NavLink } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';
import ReactPaginate from 'react-paginate';
import Footer from './Footer';
const urlApi = process.env.REACT_APP_API_URL;

const ProductType = () => {

    const [editting, setEditting] = useState(false);
    const [displayT, setDisplayT] = useState([]);
    const [search, setSearch] = useState("");
    const [sort, setSort] = useState("Tên loại")
    const [pagination, setPagination] = useState([]);
    const [types, setTypes] = useState([]);
    const [displayType, setDisplayType] = useState([]);
    const [err, setErr] = useState({
        errTypeName: ""
    });
    const [checkStatus, isCheckStatus] = useState(true);

    const baseUrl = `${urlApi}/api/admin/quan-ly-loai-san-pham`
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    const pages = Array.from({ length: pagination.totalPages }, (_, index) => index + 1);

    const handleSearchChange = (event) => {
        setSearch(event.target.value);
    }

    const fetchData = async (pageNo = 1) => {
        resetForm();
        try {
            const response = await axios.get(`${baseUrl}?pageNo=${pageNo}&sort=${sort}&search=${search}`, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
            setEditting(response.data.data.editting);
            setTypes(response.data.data.types);
            setPagination(response.data.data.pagination);
        } catch (error) {

        } finally {

        }
    }

    const editLoaiSP = (item) => (
        async () => {
            console.log(item);
            try {
                const response = await axios.get(baseUrl + "/" + item.maLoai, {
                    headers: {
                        Authorization: `Bearer ${sessionToken}`
                    }
                })
                setDisplayT(response.data.data.displayT);
                setDisplayType(response.data.data.displayType);
                setEditting(response.data.data.editting);
                isCheckStatus(response.data.data.displayType.trangThai);
            } catch (error) {
                toast.error("Có lỗi xảy ra khi lấy dữ liệu sản phẩm mới");
            } finally {

            }
        }
    )

    // const editLoaiSP = (item) => {
    //     console.log('item', item)
    //     setDisplayT(item.trangThai);
    //     // setDisplayType(item.displayType);
    //     setEditting(true);
    // }

    const handlePageClick = (data) => {
        let selectedPage = data.selected + 1; // Page index is 0-based in react-paginate
        console.log('Selected page:', selectedPage); // Log để kiểm tra
        fetchData(selectedPage);  // Gọi hàm fetchData với số trang đã chọn
    };

    const basicValidation = (e, fieldName) => {
        if (fieldName === "typeName") {
            if (e.target.value.length <= 0) {
                setErr({ ...err, errTypeName: "Vui lòng nhập vào tên loại sản phẩm!" })
            }
            else {
                setErr({ ...err, errTypeName: "" })
            }
            setDisplayT({ ...displayT, tenLoai: e.target.value })
        }

    }

    //THÊM SỬA CHO PRODUCT YPE
    const handlePost = async () => {

        const payload = {
            // idForUpdate: displayU?.idForUpdate || null,  // Hidden input for update
            tenLoai: displayT.tenLoai || "",
            trangThai: displayT.trangThai || ''
        };


        if (
            err.errTypeName === ''
        ) {
            axios.post(baseUrl + "/create", payload, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
                .then((response) => {
                    toast.success(response.data.message);
                    fetchData();
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
        else {
            toast.error("Vui lòng kiểm tra lỗi hiển thị!");
            console.log(Object.keys(err).length); // In ra số lượng lỗi
        }
    }

    const handlePut = async () => {
        
        const payload = {
            // idForUpdate: displayU?.idForUpdate || null,  // Hidden input for update
            tenLoai: displayT.tenLoai || "",
            trangThai: displayT.trangThai
        };
        if (
            err.errTypeName === ''
        ) {
            axios.put(baseUrl + "/update/" + displayT.maLoai, payload, {
                headers: {
                    Authorization: `Bearer ${sessionToken}`
                }
            })
                .then((response) => {
                    toast.success(response.data.message);
                    fetchData();
                })
                .catch((error) => {
                    if (error.response) {
                        const errorMessage = error.response.data.message || "Đã xảy ra lỗi.";
                        setErr(error.response.data.data);
                        toast.error(errorMessage);
                    } else {
                        toast.error("Đã xảy ra lỗi không xác định.");
                    }
                })
                .finally(() => {
                    resetForm();
                });
        }
        else {
            toast.error("Vui lòng kiểm tra lỗi hiển thị!");
            console.log(Object.keys(err).length); // In ra số lượng lỗi
        }
    }

    const resetForm = () => {
        setDisplayT({
            tenSanPham: '',
            gia: 0,
            soLuong: 0,
            trongLuong: 0,
            loai: 'Chọn loại',  // Giá trị mặc định cho loại
            trangThai: true, // Giá trị mặc định cho trạng thái
        });
        isCheckStatus(true);
    };

    useEffect(() => {
        fetchData();
    }, [sort, search]);

    return (
        <div className="wrapper">
            <SideNav></SideNav>
            <div className="main main-admin p-0">
                <HorizontalNav></HorizontalNav>
                <main className="content">
                    <div className="container-fluid p-0">
                        <div className='userDetail' style={{ marginTop: "20px" }}>
                            <h4> Chi tiết loại sản phẩm </h4>
                            {/* th:object="${loaiSanPham}" */}
                            <form className="mt-3">
                                <div className="row mb-3">
                                    <div className="col-md-12">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Tên loại:</label>
                                            <input type="text" value={displayT?.tenLoai || ''}
                                                className="form-control"
                                                placeholder="Nhập vào tên loại sản phẩm"
                                                onChange={(e) => basicValidation(e, "typeName")}
                                            />
                                            <em className='text-danger'>{err?.errTypeName}</em>
                                        </div>
                                    </div>
                                    <div className="col-md-12">
                                        <div className="mb-3">
                                            <label className="form-label fw-bold">Trạng thái:</label>
                                            <div className="form-check">
                                                <input type="radio" checked={checkStatus}
                                                    className="form-check-input" name="trangThai"
                                                    id="flexRadioDefault1"
                                                    onChange={(e) => {isCheckStatus(true);displayT.trangThai = true}}
                                                />Hiển thị
                                            </div>
                                            <div className="form-check">
                                                <input checked={!checkStatus}
                                                    className="form-check-input" type="radio" name="trangThai"
                                                    id="flexRadioDefault2"
                                                    onChange={(e) => {isCheckStatus(false);displayT.trangThai = false}} />Ẩn
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <button onClick={handlePost} type='button' disabled={editting} className="btn btn-success me-2">Thêm</button>
                                <button onClick={handlePut} type='button' disabled={!editting} className="btn btn-primary me-2">Cập nhật</button>
                                <button onClick={resetForm} type='button' className="btn me-2" style={{ backgroundColor: '#656565' }}>Làm mới</button>
                            </form>
                        </div>

                        <div className="userList" style={{ marginTop: "20px" }}>
                            <h4 className="text-center"> Danh sách loại sản phẩm </h4>
                            <form>
                                <div className="row">
                                    <div className="col-md-4">
                                        <div className="input-group mb-3">
                                            <input value={search} name="search" type="search" className="form-control" placeholder="Tìm kiếm" onChange={handleSearchChange} />
                                            {/* <button className="btn btn-success">Tìm kiếm</button> */}
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <table className="table table-striped table-hover table-light">
                                <thead className="text-center" style={{ backgroundColor: '#067a38', color: '#fff',fontSize:'0.8rem' }}>
                                    <th>Mã loại sản phẩm</th>
                                    <th>Tên loại sản phẩm</th>
                                    <th>Tổng sản phẩm</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </thead>
                                <tbody className="text-center">
                                    {types.map((item, index) => (
                                        <tr key={index}>
                                            <td>{item.maLoai}</td>
                                            <td>{item.tenLoai}</td>
                                            <td>{item.totalProducts}</td>
                                            <td>
                                                <span className={`badge ${item.trangThai === false ? 'bg-danger' : 'bg-success'}`}>
                                                    {item.trangThai === true ? 'Hiển thị' : 'Ẩn'}
                                                </span>
                                            </td>
                                            <td>
                                                <i className="fa-solid fa-pen-to-square" onClick={editLoaiSP(item)}></i>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>

                            <div className="text-center" hidden={pagination.totalPages !== 0}>
                                <p className="fw-bold">Không tìm thấy loại sản phẩm tương ứng</p>
                            </div>

                            <div hidden={pagination.totalPages === 0} className="d-flex justify-content-between" style={{ marginTop: "25px" }}>
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

export default ProductType;