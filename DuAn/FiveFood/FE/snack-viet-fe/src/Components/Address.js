import '../css/Address.css';
import imgCrush from '../images/crush-chips.png';
import Navbar from './menu';
import Footer from './footer';

import axios from "axios";
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { Modal, Button } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';
import { OrbitProgress } from 'react-loading-indicators';

const urlApi = process.env.REACT_APP_API_URL;

export const Address = () => {

    const [listAddress, setListAddress] = useState([]);
    // const [listProvinces, setListProvinces] = useState([]);
    const listProvinces = [
        { ProvinceID: 220, ProvinceName: "Cần Thơ" }
    ];
    const [selectProvince, setSelectProvince] = useState(
        { "provinceId": "220", "provinceName": "Cần Thơ" }
    );
    const [provinceName, setProvinceName] = useState("Cần Thơ");
    // const [listDistrict, setListDistrict] = useState([]);
    const listDistrict = [
        { DistrictID: 1574, DistrictName: "Quận Cái Răng" },
        { DistrictID: 1572, DistrictName: "Quận Ninh Kiều" }
    ];
    const [selectDistrict, setSelectDistrict] = useState(
        { "districtId": "", "districtName": "" }
    );
    const [districtName, setDistrictName] = useState("");
    const [listWard, setListWard] = useState([]);
    const [selectWard, setSelectWard] = useState(
        { "wardCode": "", "wardName": "" }
    );
    const [wardName, setWardName] = useState("");
    const [totalPage, setTotalPages] = useState(0);

    const [maDiaChi, setMaDiaChi] = useState("");
    const [hoVaTen, setHoVaTen] = useState("");
    const [soDienThoai, setSoDienThoai] = useState("");
    const [diaChiCuThe, setDiaChiCuThe] = useState("");
    const [defaultAddress, isDefaultAddress] = useState(false);
    const [checkBtnThem, setCheckBtnThem] = useState(false);
    const [checkBtnEdit, setCheckBtnEdit] = useState(true);

    const [messgaeHoVaTen, setMessgaeHoVaTen] = useState("");
    const [messgaeSoDienThoai, setMessgaeSoDienThoai] = useState("");
    const [messgaeDiaChiCuThe, setMessgaeDiaChiCuThe] = useState("");

    const [showModal, setShowModal] = useState(false);
    const [modalItem, setModalItem] = useState({});
    const [loadingDeleteAddress, isLoadingDeleteAddress] = useState(false);

    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    const loadPage = async (page) => {
        if (page === undefined) {
            page = 0;
        }
        let res = await getListByAccount(page + 1, sessionTaiKhoan.maTaiKhoan, sessionToken);

        if (res.data) {
            setListAddress(res.data.data.diaChi);
            setTotalPages(res.data.totalPage);
        }

    }
    // // Tạo function load lấy dữ liệu từ api và xử lý
    // const loadApiProvinces = async () => {
    //     let res = await getProvinces();
    //     // Xử lý code trong đây
    //     if (res.data) {
    //         setListProvinces(res.data.data);
    //         console.log(res.data.data);
    //     }
    // }
    // const loadApiDistrict = async (provinceId) => {
    //     let res = await getDistrict(provinceId);
    //     // Xử lý code trong đây
    //     if (res.data) {
    //         setListDistrict(res.data.data);
    //         console.log(res.data.data);
    //     }
    // }
    const loadApiWard = async (districtID) => {
        let res = await getWard(districtID);
        // Xử lý code trong đây
        if (res.data) {
            setListWard(res.data.data);
        }
    }


    const handleProvinces = (event) => {
        let value = JSON.parse(event.target.value);
        // setSelectProvince(value);
        setProvinceName(value.provinceName);
        // loadApiDistrict(value.provinceId);
    }
    const handleDistrict = (event) => {
        let value = JSON.parse(event.target.value);
        setSelectDistrict(value);
        setDistrictName(value.districtName);
        loadApiWard(value.districtId);
    }
    const handleWard = (event) => {
        let value = JSON.parse(event.target.value);
        setSelectWard(value);
        setWardName(value.wardName);
    }

    // Xử lý sự kiện onchange cho của các input
    const onChangeHoVaTen = (event) => {
        setHoVaTen(event.target.value);
        setMessgaeHoVaTen("");
    }
    const onChangeSoDienThoai = (event) => {
        setSoDienThoai(event.target.value);
        setMessgaeSoDienThoai("");
    }
    const onChangeDiaChiCuThe = (event) => {
        setDiaChiCuThe(event.target.value);
        setMessgaeDiaChiCuThe("");
    }
    // Xử lý sự kiện click button
    // Click thêm mới
    const clickAddNewAddress = async () => {

        if (!checkValidate()) {
            let diaChi = `${wardName}, ${districtName}, ${provinceName}`;
            let res = await createAddress({
                "hoVaTen": hoVaTen,
                "soDienThoai": soDienThoai,
                "trangThai": defaultAddress,
                "diaChi": diaChi,
                "diaChiChiTiet": diaChiCuThe,
                "maTinhThanh": selectProvince.provinceId,
                "maQuanHuyen": selectDistrict.districtId,
                "maXaPhuong": selectWard.wardCode,
                "maTaiKhoan": sessionTaiKhoan.maTaiKhoan
            }, sessionToken);
            toast.success(res.data.message);
            loadPage();
            clickResetAddress();
        }
    }
    //Click cập nhật
    const clickUpdateAddress = async () => {
        if (!checkValidate()) {
            let diaChi = `${wardName}, ${districtName}, ${provinceName}`;
            let res = await updateAddress({
                "maDiaChi": maDiaChi,
                "hoVaTen": hoVaTen,
                "soDienThoai": soDienThoai,
                "trangThai": defaultAddress,
                "diaChi": diaChi,
                "diaChiChiTiet": diaChiCuThe,
                "maTinhThanh": selectProvince.provinceId,
                "maQuanHuyen": selectDistrict.districtId,
                "maXaPhuong": selectWard.wardCode,
                "maTaiKhoan": sessionTaiKhoan.maTaiKhoan
            }, sessionToken);
            toast.success(res.data.message);
            loadPage();
            clickResetAddress();
        }
    }
    // Clikk edit
    const clickEditAddress = async (item) => {
        setCheckBtnThem(true);
        setCheckBtnEdit(false);
        setMaDiaChi(item.maDiaChi);
        setHoVaTen(item.hoVaTen);
        setSoDienThoai(item.soDienThoai);
        setDiaChiCuThe(item.diaChiChiTiet);
        isDefaultAddress(item.trangThai);
        // const dc = item.diaChi;
        // var dcSplit = dc.split(', ');
        // if (dcSplit.length > 0) {
        // setWardName(dcSplit[1]);
        // setDistrictName(dcSplit[2]);
        // setProvinceName(dcSplit[3]);
        // Fill tự chọn tỉnh thành lên select
        // const timProvince = listProvinces.find(itemList => itemList.ProvinceID === item.maTinhThanh);
        // setSelectProvince({ 'provinceId': timProvince.ProvinceID, 'provinceName': timProvince.ProvinceName });

        // // Fill tự chọn quận huyện lên select
        // let getDistrictApi = await getDistrict(item.maTinhThanh);
        // const listQuan = getDistrictApi.data.data;
        // setListDistrict(listQuan);
        const timDistrict = listDistrict.find(itemList => itemList.DistrictID === item.maQuanHuyen);
        console.log(timDistrict)
        setSelectDistrict({ "districtId": timDistrict.DistrictID, "districtName": timDistrict.DistrictName });

        // Fill tự chọn phường xã lên select
        let getWardApi = await getWard(item.maQuanHuyen);
        const listXa = getWardApi.data.data;
        setListWard(listXa)
        const timWard = listXa.find(itemList => itemList.WardCode === String(item.maXaPhuong));
        setSelectWard({ "wardCode": timWard.WardCode, "wardName": timWard.WardName });

        // }

    }
    // Clicl reset
    const clickResetAddress = () => {
        setMaDiaChi("");
        setHoVaTen("");
        setSoDienThoai("");
        setDiaChiCuThe("");
        isDefaultAddress(false);
        // setSelectProvince({ "provinceId": "", "provinceName": "" });
        setSelectDistrict({ "districtId": "", "districtName": "" });
        setSelectWard({ "wardCode": "", "wardName": "" });
        // setListDistrict([]);
        setListWard([]);
        setCheckBtnThem(false);
        setCheckBtnEdit(true);
    }

    const handlePageClick = (event) => {
        loadPage(+event.selected);
    }
    // Kiểm tra lỗi nhập vào trên input
    const checkValidate = () => {
        var check = false;
        if (hoVaTen === "") {
            setMessgaeHoVaTen("Vui lòng nhập họ và tên");
            check = true;
        }
        if (soDienThoai === "") {
            setMessgaeSoDienThoai("Vui lòng nhập số điện thoại");
            check = true;
        }
        if (diaChiCuThe === "") {
            setMessgaeDiaChiCuThe("Vui lòng nhập địa chỉ cụ thể");
            check = true;
        }
        return check;
    }

    const clickDeleteAddress = (item) => {
        setShowModal(true);
        setModalItem(item);
    }
    const handleClose = () => {
        setShowModal(false);
    }
    const clickAgreeDelete = async (item) => {
        isLoadingDeleteAddress(true);
        let res = await deleteAddress(item.maDiaChi, sessionToken);
        if (res) {
            toast.success(res.data.message);
            setModalItem({});
            setShowModal(false);
            isLoadingDeleteAddress(false);
            loadPage();
        }
    }


    useEffect(() => {
        loadPage();
        // loadApiProvinces();
    }, []);


    return (
        <>
            <Navbar />
            <main className='main-address' style={{marginTop:50}}>
                <div className="row pt-5 ms-5 me-5">
                    <div className="col-md-8 d-flex justify-content-center mt-4">
                        <div className="card card-form">
                            <div className="card-body">
                                <div className="text-center fs-2 fw-bold">Địa chỉ</div>
                                <div className="mt-4">
                                    <div>
                                        <div className="d-flex justify-content-center">
                                            <div className="d-flex justify-content-between div-input">
                                                <div className="input-box-address">
                                                    <div className="input-dau">
                                                        <i className="fa-solid fa-user"></i>
                                                        <input className='input-address' type="text" placeholder="Họ và tên" name="hoVaTen" value={hoVaTen} onChange={onChangeHoVaTen} /><br />
                                                        <span className="text-danger">{messgaeHoVaTen}</span>
                                                    </div>
                                                </div>
                                                <div className="input-box-address ">
                                                    <div className="input-dau">
                                                        <i className="fa-solid fa-phone"></i>
                                                        <input className='input-address' type="text" placeholder="Số điện thoại" name="soDienThoai" value={soDienThoai} onChange={onChangeSoDienThoai} /><br />
                                                        <span className="text-danger">{messgaeSoDienThoai}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center ">
                                            <div className="d-flex justify-content-between div-input">
                                                <div className="input-box-address">
                                                    <div className="input-giua">
                                                        <select className="form-select" id="province" value={JSON.stringify(selectProvince)} onChange={handleProvinces}>
                                                            {/* <option value={JSON.stringify({ "provinceId": "", "provinceName": "" })}>Tỉnh/Thành Phố</option> */}
                                                            {
                                                                listProvinces.map((item, index) => {
                                                                    return (
                                                                        <option
                                                                            key={item.ProvinceID}
                                                                            value={JSON.stringify({ "provinceId": item.ProvinceID, "provinceName": item.ProvinceName })}
                                                                        >{item.ProvinceName}
                                                                        </option>
                                                                    )
                                                                })
                                                            }
                                                        </select>
                                                        <input type="hidden" name="province" id="inputProvince" />
                                                        <span className="text-danger"></span>
                                                    </div>
                                                </div>
                                                <div className="input-box-address ">
                                                    <div className="input-giua">
                                                        <select className="form-select" id="district" value={JSON.stringify(selectDistrict)} onChange={handleDistrict} >
                                                            <option selected value={JSON.stringify({ "districtId": "", "districtName": "" })}>Quận/Huyện</option>
                                                            {
                                                                listDistrict.map((item, index) => {
                                                                    return (
                                                                        <option key={item.DistrictID} value={JSON.stringify({ "districtId": item.DistrictID, "districtName": item.DistrictName })} >{item.DistrictName}</option>
                                                                    )
                                                                })
                                                            }
                                                        </select>
                                                        <input type="hidden" name="district" id="inputDistrict" />
                                                        <span className="text-danger"></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center">
                                            <div className="d-flex justify-content-between div-input">
                                                <div className="input-box-address ">
                                                    <div className="input-giua">
                                                        <select className="form-select" id="ward" value={JSON.stringify(selectWard)} onChange={handleWard}>
                                                            <option selected value={JSON.stringify({ "wardCode": "", "wardName": "" })}>Xã/Phường</option>
                                                            {
                                                                listWard.map((item, index) => {
                                                                    return (
                                                                        <option key={item.WardCode} value={JSON.stringify({ "wardCode": item.WardCode, "wardName": item.WardName })} >{item.WardName}</option>
                                                                    )
                                                                })
                                                            }
                                                        </select>
                                                        <input type="hidden" name="ward" id="inputWard" />
                                                        <span className="text-danger"></span>
                                                    </div>
                                                </div>
                                                <div className="input-box-address ">
                                                    <div className="input-dau">
                                                        <i className="fa-solid fa-house"></i>
                                                        <input className='input-address' type="text" placeholder="Địa chỉ cụ thể" name="diaChiCuThe" value={diaChiCuThe} onChange={onChangeDiaChiCuThe} /><br />
                                                        <span className="text-danger">{messgaeDiaChiCuThe}</span>
                                                    </div>
                                                </div>               
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center">
                                            <div style={{ width: '750px' }}>
                                                <input id='checkDefault' type="checkbox" name="macDinhDC"
                                                    value={defaultAddress}
                                                    onChange={(event) => { isDefaultAddress(event.target.checked) }}
                                                    checked={defaultAddress}
                                                /> <label htmlFor='checkDefault'>Đặt làm địa chỉ mặc định</label>
                                            </div>
                                        </div>
                                        <div className="d-flex justify-content-center mt-3 mb-3 div-button">
                                            <div className="d-flex justify-content-start">
                                                <button className="btn btn-success" disabled={checkBtnThem} onClick={clickAddNewAddress}>Thêm mới</button>
                                                <button className="btn btn-primary" disabled={checkBtnEdit} onClick={clickUpdateAddress} >Cập nhật</button>
                                                <button className="btn text-light" style={{ backgroundColor: '#656565' }} onClick={clickResetAddress}>Làm mới</button>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4 d-flex justify-content-center align-items-center">
                        <div className="">
                            <img className='img-logo-address' src="https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhLogo%2Fmap-google-removebg-preview.png?alt=media&token=cd424f82-437d-46fb-a98c-19b421fea23c" alt="" />
                        </div>
                    </div>
                    <div className="col-md-12 ms-5 me-4 p-0 mt-4 mb-5">
                        <div className="fs-2">Danh sách địa chỉ </div>
                        <div className="card card-list">
                            <div className="card-body">
                                <table className="table table-address">
                                    <thead>
                                        <tr className="row">
                                            <th className="col-md-3 text-center">Họ và Tên</th>
                                            <th className="col-md-2 text-center">Số điện thoại</th>
                                            <th className="col-md-4 text-center">Địa chỉ</th>
                                            <th className="col-md-1 text-center"></th>
                                            <th className="col-md-2 text-center"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {listAddress && listAddress.length > 0 &&
                                            listAddress
                                                .filter(item => item.trangThaiXoa === false)
                                                .map((item, index) => {
                                                    return (
                                                        <tr className="row" key={`address-${index}`}>
                                                            <td className="col-md-3 text-center ">{item.hoVaTen}</td>
                                                            <td className="col-md-2 text-center ">{item.soDienThoai}</td>
                                                            <td className="col-md-4 text-center ">{item.diaChiChiTiet + ", " + item.diaChi}</td>
                                                            <td className="col-md-1 text-center ">
                                                                {item.trangThai ? (<button className="btn btn-success">Mặc định</button>) : ''}

                                                            </td>
                                                            <td className="col-md-2 text-center ">
                                                                <span onClick={() => { clickEditAddress(item) }}><i className="fa-solid fa-pencil" style={{ color: '#0091ff' }}></i></span> <i>|</i>
                                                                <span onClick={() => { clickDeleteAddress(item) }}><i className="fa-solid fa-trash-can" style={{ color: '#ff0000' }}></i></span>
                                                            </td>
                                                        </tr>
                                                    )
                                                })
                                        }
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div className='d-flex justify-content-center' style={{ marginRight: '72px' }}>
                            <ReactPaginate
                                breakLabel="..."
                                nextLabel={<i className="fa-solid fa-forward-step"></i>}
                                onPageChange={handlePageClick}
                                pageRangeDisplayed={3}
                                pageCount={totalPage}
                                previousLabel={<i className="fa-solid fa-backward-step"></i>}
                                renderOnZeroPageCount={null}

                                pageClassName='page-item  page-address'
                                pageLinkClassName='page-link'
                                previousClassName='page-item page-address'
                                previousLinkClassName='page-link'
                                nextClassName='page-item page-address'
                                nextLinkClassName='page-link'
                                breakClassName='page-item'
                                breakLinkClassName='page-link'
                                containerClassName='pagination'
                                activeClassName='active'
                            />
                        </div>
                    </div>
                </div>
            </main>
            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton className='d-flex justify-content-end '>
                    <Modal.Title className='fw-bold fs-3'>Xóa địa chỉ</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='ms-4 me-4 mt-2 mb-2'>
                        Bạn có chắc muốn xóa địa chỉ <span className='fw-bold'>{modalItem.diaChi}</span> này không?
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose} style={{ width: '80px' }}>
                        Không
                    </Button>
                    <Button variant="danger" onClick={() => clickAgreeDelete(modalItem)} style={{ width: '80px' }}>
                        Có
                    </Button>
                </Modal.Footer>
                {
                    loadingDeleteAddress&& (
                        <div className='position-absolute bg-black loading-modal-delete-all d-flex justify-content-center align-items-center' style={{ opacity: 0.4 }}>
                            <div>
                                <OrbitProgress variant="disc" color="#32cd32" size="small" text="" textColor="" />
                            </div>
                        </div>
                    )
                }
            </Modal>
            <Footer />

        </>
    )
}

// API
// Tạo function return về data của Api
const getListByAccount = (page, maTaiKhoan, token) => {
    console.log(token);
    return axios.get(`${urlApi}/api/dia-chi/page/${page}?maTaiKhoan=${maTaiKhoan}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const createAddress = (data, token) => {
    return axios.post(`${urlApi}/api/dia-chi/create`, data,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const updateAddress = (data, token) => {
    return axios.put(`${urlApi}/api/dia-chi/update`, data,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const deleteAddress = (id, token) => {
    return axios.delete(`${urlApi}/api/dia-chi/delete/` + id,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const getProvinces = async () => {
    try {
        const res = await axios.get("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province", {
            headers: {
                "Token": "bab9fb8c-2323-11ef-a951-76299e96dead"
            }
        })
        return res;
    } catch (error) {
        console.log(error);
    }
}

const getDistrict = async (provinceId) => {
    try {
        const res = await axios.get("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" + provinceId, {
            headers: {
                "Token": "bab9fb8c-2323-11ef-a951-76299e96dead"
            }
        })
        return res;
    } catch (error) {
        console.log(error);
    }
}

const getWard = async (DistrictID) => {
    try {
        const res = await axios.get("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + DistrictID, {
            headers: {
                "Token": "bab9fb8c-2323-11ef-a951-76299e96dead"
            }
        })
        return res;
    } catch (error) {
        console.log(error);
    }
}