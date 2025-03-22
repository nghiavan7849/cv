import '../css/PayDetails.css';
import Navbar from './menu';
import Footer from './footer';
import { useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import { Modal } from 'react-bootstrap';
import { v4 } from 'uuid';
import formatVND from '../Service/FormatVND';
const urlApi = process.env.REACT_APP_API_URL;
const url = process.env.REACT_APP_URL;
const getListDiaChiByMaTK = (maTaiKhoan, token) => {
    return axios.get(`${urlApi}/api/dia-chi/list-by-matk?maTaiKhoan=${maTaiKhoan}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}


const postApiTinhPhiVanChuyen = (code) => {
    return axios.post('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services',
        {
            shop_id: 192476, // Thay bằng shop_id của bạn nếu cần
            from_district: 1574,
            to_district: parseInt(code)
        },
        {
            headers: {
                'Content-Type': 'application/json',
                'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead'
            }

        }
    );
};

const getPhiVanChuyen = (data) => {
    return axios.post(`https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee`, data,
        {
            headers: {
                'Content-Type': 'application/json',
                'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead',
                'ShopId': 192476
            }
        }
    );
}

const creatHoaDon = (data, token) => {
    return axios.post(`${urlApi}/api/hoa-don/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const creatChiTietHoaDon = (data, token) => {
    return axios.post(`${urlApi}/api/chi-tiet-hoa-don/create`, data, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}
const deleteChiTietGioHang = (maChiTietGioHang, token) => {
    return axios.delete(`${urlApi}/api/chi-tiet-gio-hang/delete/${maChiTietGioHang}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const createVnPayPaymentUrl = (orderData, token) => {
    return axios.post(`${urlApi}/api/vnpay/create-payment`, orderData, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const createPaymentLink = (data, token) => {
    return axios.post(`${urlApi}/api/payos/create-payment-link`, data,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

const getMaHDLast = (token) => {
    return axios.get(`${urlApi}/api/vnpay/get-ma-hd`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    })
}
export const PayDetails = () => {
    const url = process.env.REACT_APP_URL;
    const location = useLocation();
    const { state } = location;
    const [listItemClickChon, setListItemClickChon] = useState([]);
    const [listDiaChi, setListDiaChi] = useState([]);
    const [diaChi, setDiaChi] = useState({});
    const [phiVanChuyen, setPhiVanChuyen] = useState(0);
    const [total, setTotal] = useState(0);
    const [totalTemp, setTotalTemp] = useState(0);
    const [paymentMethod, isPaymentMethod] = useState(true);
    const [paymentMethodType, isPaymentMethodType] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [totalWeight, setTotalWeight] = useState(0);
    const [note, setNote] = useState("");
    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    const navigate = useNavigate();

    const loadPage = async () => {
        setListItemClickChon(state);
        console.log('state', state)
        let letTotalWeight = 0;
        let letTotalTam = 0;
        state.forEach(item => {
            letTotalWeight = letTotalWeight + item.sanPhamGH.trongLuong * item.soLuong;
            letTotalTam += item.sanPhamGH.gia * item.soLuong;
        })
        setTotalTemp(letTotalTam);
        setTotalWeight(letTotalWeight);
        let res = await getListDiaChiByMaTK(sessionTaiKhoan.maTaiKhoan, sessionToken);
        if (res) {
            setListDiaChi(res.data.data.diaChi.filter(item => item.trangThaiXoa === false && item.trangThai === false));
            res.data.data.diaChi.forEach(item => {
                if (item.trangThai) {
                    setDiaChi(item);
                    layNameDiaChi(item, letTotalWeight, letTotalTam);
                }
            })
        }

    }

    const handleClose = () => {
        setShowModal(false);
    }

    // Click để thay đổi địa chỉ
    const clickThayDoiDiaChi = () => {
        setShowModal(true);
    }
    const clickChonThayDoiDiaChi = (item) => {
        setDiaChi(item);
        setShowModal(false);
        setPhiVanChuyen(0);
        setTotal(0);
        layNameDiaChi(item, totalWeight, totalTemp);
    }

    const layNameDiaChi = async (item, ttWeight, ttTemp) => {
        let wardCodeTo = item.maXaPhuong;
        let districtIdTo = item.maQuanHuyen;
        let provinceIdTo = item.maTinhThanh;

        let service_id = 0;

        let res = await postApiTinhPhiVanChuyen(districtIdTo);
        if (res) {
            service_id = res.data.data[0].service_id;
            const data = {
                from_district_id: 1574,
                from_ward_code: '550307',
                service_id: res.data.data[0].service_id,
                service_type_id: res.data.data[0].service_type_id,
                to_district_id: districtIdTo,
                to_ward_code: String(wardCodeTo),
                weight: parseFloat(ttWeight)
            }
            let resPhi = await getPhiVanChuyen(data);
            setPhiVanChuyen(resPhi.data.data.total);
            setTotal(ttTemp + resPhi.data.data.total);

        }
    }

    // Click đặt hàng
    const clicDatHang = async () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // Tháng được tính từ 0-11 nên cần +1 và thêm '0' nếu cần
        const day = String(today.getDate()).padStart(2, '0'); // Thêm '0' nếu ngày nhỏ hơn 10

        const formattedDate = `${year}-${month}-${day}`; // Định dạng YYYY-MM-DD

        const data = {
            "phiVanChuyen": phiVanChuyen,
            "tongTien": total,
            "phuongThucThanhToan": paymentMethod,
            "ngayThanhToan": formattedDate,
            "ghiChu": note,
            "trangThai": false,
            "diaChiNhan": diaChi.diaChiChiTiet + ', ' + diaChi.diaChi,
            "dcHoTen": diaChi.hoVaTen,
            "dcSoDienThoai": diaChi.soDienThoai,
            "maTaiKhoan": sessionTaiKhoan.maTaiKhoan,
            "maTrangThaiHoaDon": 1
        }

        // Đặt khi chọn thanh toán khi nhận hàng

        if (paymentMethod) {
            data.trangThai = false;
            const checkBuyNow = JSON.parse(sessionStorage.getItem("checkBuyNow"));
            let res = await creatHoaDon(data, sessionToken);
            if (res) {
                toast.success("Đã đặt hàng thành công");
                listItemClickChon.forEach(item => {
                    const dataCTHD = {
                        "soLuong": item.soLuong,
                        "gia": item.sanPhamGH.gia,
                        "maHoaDon": res.data.data.hoaDon.maHoaDon,
                        "maSanPham": item.sanPhamGH.maSanPham
                    };
                    creatChiTietHoaDon(dataCTHD, sessionToken);
                    if(checkBuyNow === false) {
                        deleteChiTietGioHang(item.maChiTietGioHang, sessionToken);
                    }
                });
                // Lưu thông tin vào sessionStorage
                sessionStorage.setItem('phiVanChuyen', phiVanChuyen);
                sessionStorage.setItem('total', total);
                sessionStorage.setItem("paymentMethod", JSON.stringify(paymentMethod));
                navigate("/home/pay", { state: res.data.data })
            }
        } else {
            sessionStorage.setItem("paymentMethodType", JSON.stringify(paymentMethodType))
            if (paymentMethodType) {
                data.trangThai = true
                // Khi chọn thanh toán bằng VNPAY
                let res = await axios.get(`${urlApi}/api/vnpay/get-ma-hd`, {
                    headers: {
                        Authorization: `Bearer ${sessionToken}`
                    }
                })
                const dataCTHD2 = {
                    soLuong: [],
                    gia: null,
                    maHoaDon: res.data.maHD + 1,
                    maSanPham: [],
                    maChiTietGioHang: '',
                    txnRef: ''
                };
                listItemClickChon.forEach(item => {
                    dataCTHD2.soLuong.push(item.soLuong);
                    dataCTHD2.gia = item.sanPhamGH.gia;
                    dataCTHD2.maChiTietGioHang = item.maChiTietGioHang;
                    dataCTHD2.txnRef = v4();
                    dataCTHD2.maSanPham.push(item.sanPhamGH.maSanPham)
                });

                try {
                    // Gửi thông tin phiVanChuyen và total đến API để lưu vào session
                    await axios.post(`${urlApi}/api/vnpay/save-session-data`, {
                        phiVanChuyen: phiVanChuyen,
                        maTaiKhoan: sessionTaiKhoan.maTaiKhoan,
                        dataCTHD: dataCTHD2
                        
                    }, {
                        headers: {
                            'Authorization': `Bearer ${sessionToken}`,
                        },
                    }
                    )

                    // Lưu thông tin vào sessionStorage
                    sessionStorage.setItem('phiVanChuyen', phiVanChuyen);
                    sessionStorage.setItem('total', total);
                    sessionStorage.setItem("paymentMethod", paymentMethod.toString());
                    console.log('dataCTHD2', dataCTHD2)
                    const paymentUrlRes = await createVnPayPaymentUrl({ ...data, orderId: dataCTHD2.maHoaDon, urlReturn:`${url}/vnpay_return`}, sessionToken);
                    
                    if (paymentUrlRes.data && paymentUrlRes.data.paymentUrl) {
                        console.log('paymentUrlRes', paymentUrlRes)
                        window.location.href = paymentUrlRes.data.paymentUrl;
                    }

                } catch (error) {
                    if (error.response) {
                        // Lỗi từ máy chủ
                        console.error('Error status:', error.response.status);
                        console.error('Error details:', error.response.data);
                    } else {
                        // Lỗi khác (ví dụ: mạng)
                        console.error('Error:', error.message);
                    }
                }
            } else {
                // // Khi chọn thanh toán bằng QR Code
                // Lấy mã hóa đơn cuối 
                data.maTrangThaiHoaDon = 1; // Đã đặt hàng
                data.trangThai = true;
                let getMaHd = await getMaHDLast(sessionToken);
                if(getMaHd){
                    const dataPay = {
                        "productName": "Thanh toán hóa đơn",
                        "description": `HD${getMaHd.data.maHD + 1}`,
                        "returnUrl": `${url}/home/pay`,
                        "price": total,
                        "cancelUrl": `${url}/home`
                    }
                    // console.log('dataPay', dataPay);
                    let resPay = await createPaymentLink(dataPay, sessionToken);
                    if (resPay) {
                        sessionStorage.setItem("paymentMethod", JSON.stringify(paymentMethod));
                        sessionStorage.setItem("dataHoaDon", JSON.stringify(data));
                        sessionStorage.setItem("listItemClickChon", JSON.stringify(listItemClickChon));

                        window.location.href = resPay.data.data.checkoutUrl;
                    }
                }
            }
        }
    }

    useEffect(() => {
        loadPage();
    }, []);

    return (
        <>
            <Navbar />
            <div className='container-pay-details ' style={{paddingTop:60}}>
                <div className="container pt-2 pb-5">
                    <nav aria-label="breadcrumb">
                        <ol className="breadcrumb">
                            <li className="breadcrumb-item"><a href="https://www.google.com.vn/?hl=vi">Giỏ hàng</a></li>
                            <li className="breadcrumb-item"><span>Chi tiết thanh toán</span></li>
                        </ol>
                    </nav>

                    <h3 className="section-header">Thông tin thanh toán</h3>

                    <div className="row">
                        <div className="col-md-8">
                            <div className="order-details">
                                <h4>Thông Tin Thanh Toán</h4>

                                <span>Thay đổi địa chỉ <span className='text-danger fw-bold' style={{ cursor: 'pointer' }} onClick={() => clickThayDoiDiaChi()}>Tại đây</span> </span>
                                <div className='mt-1'>
                                    <div className="form-group">
                                        <label>Số điện thoại</label>
                                        <input type="text" className="form-control" id="soDienThoai" value={diaChi.soDienThoai} placeholder="Nhập địa chỉ số điện thoại" disabled="disabled" />
                                    </div>
                                    <div className="form-group">
                                        <label>Họ và Tên</label>
                                        <input type="text" className="form-control" id="name" value={diaChi.hoVaTen} placeholder="Nhập họ và tên" disabled="disabled" />
                                    </div>
                                    <div className="form-group">
                                        <label>Địa chỉ</label>
                                        <input type="text" className="form-control" id="address" value={diaChi.diaChiChiTiet + ', ' + diaChi.diaChi} placeholder="Nhập địa chỉ" disabled="disabled" />
                                    </div>
                                    <h4 className='mt-2'>Thông Tin Bổ Sung</h4>
                                    <div className="form-group">
                                        <label>Ghi chú đơn hàng (tùy chọn)</label>
                                        <textarea className="form-control" id="note" rows="3" placeholder="Nhập ghi chú về đơn hàng" onChange={(event) => setNote(event.target.value)} ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-4">
                            <div className="order-summary">
                                <h4>Đơn Hàng Của Bạn</h4>
                                <table className="table table-borderless">
                                    <tbody>
                                        {
                                            listItemClickChon.map((item) => {
                                                return (
                                                    <tr key={item.maChiTietGioHang}>
                                                        <td>{item.sanPhamGH.tenSanPham} × {item.soLuong}</td>
                                                        <td className="text-right"><span className='d-flex justify-content-end me-2'>{formatVND(item.sanPhamGH.gia * item.soLuong)}</span></td>
                                                    </tr>
                                                )
                                            })
                                        }

                                        <tr>
                                            <td>Tổng tiền sản phẩm</td>
                                            <td className="text-right"><span className='d-flex justify-content-end me-2'>{formatVND(totalTemp)}</span></td>
                                        </tr>
                                        <tr>
                                            <td>Phí vận chuyển</td>
                                            <td className="text-right"><span className='d-flex justify-content-end me-2'>{formatVND(phiVanChuyen)}</span></td>
                                        </tr>
                                        <tr>
                                            <td className="total-amount">Tổng thanh toán</td>
                                            <td className="text-right total-amount"><span className='d-flex justify-content-end me-2'>{formatVND(total)}</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div className="form-check">
                                    <input className="form-check-input" type="radio" name="paymentMethod" id="cod" value="cod" checked={paymentMethod} onChange={(event) => isPaymentMethod(true)} />
                                    <label className="form-check-label" htmlFor="cod">
                                        Thanh toán khi nhận hàng (COD)
                                    </label>
                                </div>
                                <div className="form-check">
                                    <input className="form-check-input" type="radio" name="paymentMethod" id="online" value="online" onChange={(event) => isPaymentMethod(false)} />
                                    <label className="form-check-label" htmlFor="online">
                                        Thanh toán online
                                    </label>
                                    {
                                        paymentMethod === false ? (
                                            <div className='ps-3'>
                                                <div>
                                                    <input className="form-check-input" type="radio" name="paymentMethodType" id="vnpay" value="vnpay" checked={paymentMethodType} onChange={(event) => isPaymentMethodType(true)} />
                                                    <label className="form-check-label" htmlFor="vnpay">
                                                        <img width={43} alt='' src='https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhLogo%2FLogo-VNPAY-QR-1.webp?alt=media&token=46e719a7-72ac-4de4-b6fc-118a16c3ab1b' /> VNPAY
                                                    </label>
                                                </div>
                                                <div>
                                                    <input className="form-check-input" type="radio" name="paymentMethodType" id="qrcode" value="qrcode" checked={!paymentMethodType} onChange={(event) => isPaymentMethodType(false)} />
                                                    <label className="form-check-label" htmlFor="qrcode">
                                                        <img width={43} alt='' src='https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhLogo%2Fpayos.png?alt=media&token=d61454d9-1abf-4a09-ba5a-3c382e7bf27c' /> Pay OS
                                                    </label>
                                                </div>
                                            </div>
                                        ) : (
                                            <></>
                                        )
                                    }

                                </div>
                                <button type="button" className="btn btn-success btn-block mt-4" id="xac-nhan-dat-hang" disabled={phiVanChuyen === 0 ? true : false} onClick={() => clicDatHang()}>Đặt Hàng</button>
                                <p className="note mt-3">Bằng cách nhấp vào Đặt hàng, bạn đồng ý với <a href="https://www.google.com.vn/?hl=vi">điều khoản và điều kiện</a> và <a href="https://www.google.com.vn/?hl=vi">chính sách riêng tư</a> của chúng tôi.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Modal size='xl' show={showModal} onHide={handleClose} dialogClassName="modal-90w" aria-labelledby="example-custom-modal-styling-title">
                <Modal.Header closeButton className='d-flex justify-content-end me-3'>
                    <Modal.Title className='fw-bold fs-3'>Thay đổi địa chỉ</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='ms-4 me-4 mt-2 mb-2'>
                        {listDiaChi && listDiaChi.length > 0 &&
                            listDiaChi.map((item, index) => {
                                return (
                                    <div className="row mt-2" key={`address-${index}`}>
                                        <div className="col-md-2 text-center ">{item.hoVaTen}</div>
                                        <div className="col-md-2 text-center ">{item.soDienThoai}</div>
                                        <div className="col-md-6 text-center ">{item.diaChiChiTiet + ', ' + item.diaChi}</div>
                                        <div className="col-md-2 text-center ">
                                            <button className="btn btn-info" onClick={() => { clickChonThayDoiDiaChi(item) }} >Thay đổi</button>
                                        </div>
                                    </div>
                                )
                            })
                        }
                    </div>

                </Modal.Body>
            </Modal>
            <Footer />
        </>
    )
}