// import '../css/pay.css';
import Navbar from './menu';
import Footer from './footer';
import axios from 'axios';
import { format } from "date-fns";
import { useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';
import Numeral from 'react-numeral';
import { FourSquare } from 'react-loading-indicators';
import formatVND from '../Service/FormatVND';

const urlApi = process.env.REACT_APP_API_URL;
const getChiTietHoaDon = (maHoaDon, token) => {
    return axios.get(`${urlApi}/api/chi-tiet-hoa-don/list-by-mahd?maHoaDon=${maHoaDon}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const updateTrangThai = (maHoaDon, trangThai, token) => {
    return axios.put(`${urlApi}/api/hoa-don/cap-nhat-trang-thai?maHoaDon=${maHoaDon}&trangThai=${trangThai}&lyDoHuy=`, null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
};

const updateStatusPaymet = (maHoaDon, status, token) => {
    return axios.put(`${urlApi}/api/hoa-don/update-status-payment/${maHoaDon}?status=${status}`, null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const getPayment = (id, token) => {
    return axios.get(`${urlApi}/api/payos/get-payment/${id}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    });
}

const createGiaoDich = (data, token) => {
    return axios.post(`${urlApi}/api/giao-dich/create`, data,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
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

export const Pay = () => {
    const location = useLocation();
    const { state } = location;
    const navigate = useNavigate();
    const [hoaDon, setHoaDon] = useState(null);
    const [listChiTietHoaDon, setListChiTietHoaDon] = useState([]);
    const isLoaded = useRef(false); // useRef để kiểm soát trạng thái
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    const [loading, isLoading] = useState(false);


    useEffect(() => {
        const url = new URL(window.location.href);
        const orderCode = url.searchParams.get('orderCode');

        const loadPage = async () => {

            if (isLoaded.current) return; // Ngăn không cho request lặp lại

            // Đánh dấu là đã bắt đầu load dữ liệu để ngăn duplicate requests
            isLoaded.current = true;

            let paymentMethod = sessionStorage.getItem("paymentMethod") ? JSON.parse(sessionStorage.getItem("paymentMethod")) : null;
            if (paymentMethod === true) {
                setHoaDon(state.hoaDon);
                let res = await getChiTietHoaDon(state.hoaDon.maHoaDon, sessionToken);
                setListChiTietHoaDon(res.data.data.chiTietHoaDon);
                isLoading(true);
            } else if (paymentMethod === false) {
                let paymentMethodType = sessionStorage.getItem("paymentMethodType") ? JSON.parse(sessionStorage.getItem("paymentMethodType")) : null;
                if (paymentMethodType === true) {
                    // Xử lý kết quả khi chọn VNPAY
                    const queryParams = new URLSearchParams(location.search);
                    const requestParams = {};
                    for (const [key, value] of queryParams.entries()) {
                        requestParams[key] = value;
                    }

                    try {

                        const sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
                        const listCTGH = JSON.parse(sessionStorage.getItem("listCTGH"))

                        // Tạo mảng chứa mã chi tiết giỏ hàng
                        const maCTGH = listCTGH.map(item => item.maChiTietGioHang);

                        const payload = {
                            ...requestParams,
                            phiVanChuyen: JSON.parse(sessionStorage.getItem("phiVanChuyen")),
                            maTaiKhoan: sessionTaiKhoan.maTaiKhoan,
                            maCTGH: maCTGH,
                        }

                        const response = await axios.post(`${urlApi}/api/vnpay/save-order`, payload, {
                            headers: {
                                'Authorization': `Bearer ${sessionToken}`,
                            },
                        });
                        console.log(response.data);
                        setHoaDon(response.data);
                        // Đánh dấu rằng request đã được gửi

                        // const res = await getChiTietHoaDon(response.data.maHoaDon, sessionToken);
                        // setListChiTietHoaDon(res.data.data.chiTietHoaDon);
                        // console.log(listChiTietHoaDon);
                        isLoading(true);
                    } catch (error) {
                        console.error("Payment failed", error);
                        navigate('/home');
                    }
                } else if (paymentMethodType === false) {
                    // Xử lý kết quả khi chọn QR Code
                    try {
                        const dataHD = JSON.parse(sessionStorage.getItem("dataHoaDon"));
                        const listItemClickChon = JSON.parse(sessionStorage.getItem("listItemClickChon"));
                        const checkBuyNow = JSON.parse(sessionStorage.getItem("checkBuyNow"));
                        if (orderCode) {
                            let resPayMent = await getPayment(orderCode, sessionToken);
                            if (resPayMent.data.data.status === "PAID") {
                                dataHD.trangThai = true;
                                let resCreateHD = await creatHoaDon(dataHD, sessionToken);
                                if (resCreateHD) {
                                    setHoaDon(resCreateHD.data.data.hoaDon);
                                    // Tạo các chi tiết hóa đơn song song
                                    const chiTietPromises = listItemClickChon.map(async (item) => {
                                        const dataCTHD = {
                                            "soLuong": item.soLuong,
                                            "gia": item.sanPhamGH.gia,
                                            "maHoaDon": resCreateHD.data.data.hoaDon.maHoaDon,
                                            "maSanPham": item.sanPhamGH.maSanPham
                                        };
                                        const promise = [creatChiTietHoaDon(dataCTHD, sessionToken)];
                                        if(checkBuyNow === false){
                                            promise.push(deleteChiTietGioHang(item.maChiTietGioHang, sessionToken));
                                        }
                                        return Promise.all(promise);
                                    });
                                    await Promise.all(chiTietPromises);
                                    let resCTHD = await getChiTietHoaDon(resCreateHD.data.data.hoaDon.maHoaDon, sessionToken);
                                    setListChiTietHoaDon(resCTHD.data.data.chiTietHoaDon);
                                    const data = {
                                        "codeGiaoDich": orderCode,
                                        "soTaiKhoan": resPayMent.data.data.transactions[0].counterAccountNumber,
                                        "hoVaTen": resPayMent.data.data.transactions[0].counterAccountName,
                                        "ngayGiaoDich": resPayMent.data.data.transactions[0].transactionDateTime,
                                        "trangThai": true,
                                        "soTien": resPayMent.data.data.transactions[0].amount,
                                        "maTaiKhoan": sessionTaiKhoan.maTaiKhoan,
                                        "maHoaDon": resCreateHD.data.data.hoaDon.maHoaDon
                                    }
                                    await createGiaoDich(data, sessionToken);
                                    sessionStorage.removeItem("listItemClickChon");
                                    sessionStorage.removeItem("dataHoaDon");
                                    sessionStorage.removeItem("paymentMethod");
                                    sessionStorage.removeItem("paymentMethodType");
                                    navigate("/home/pay");
                                    isLoading(true);
                                }
                            }
                        }
                    } catch (error) {
                        console.log('Error: ', error)
                    }
                }
            }
            sessionStorage.removeItem("paymentMethod");
            sessionStorage.removeItem("paymentMethodType");
        }
        loadPage();
    }, []);



    return (
        <>
            <Navbar />
            <div className="container container-pay pt-2 pb-5" style={{ minHeight: 510, marginTop:60 }}>
                {loading ? (
                    <>
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item"><a href="https://www.google.com.vn/?hl=vi">Giỏ hàng</a></li>
                                <li className="breadcrumb-item "><a href="https://www.google.com.vn/?hl=vi">Chi tiết thanh toán</a></li>
                                <li className="breadcrumb-item active" aria-current="page">Thanh toán thành công</li>
                            </ol>
                        </nav>

                        <h3 className="payment-header">Thanh toán thành công</h3>

                        {hoaDon && (
                            <div className="row">
                                <div className="col-md-8">
                                    <div className="order-details">
                                        <h4>CHI TIẾT ĐƠN HÀNG</h4>
                                        <table className="table table-borderless">
                                            <tbody>
                                                {listChiTietHoaDon.map(item => (
                                                    <tr key={item.maChiTietHoaDon}>
                                                        <td>{item.sanPhamCT.tenSanPham} × {item.soLuong}</td>
                                                        <td className="text-right">{formatVND(item.gia * item.soLuong)}</td>
                                                    </tr>
                                                ))}
                                                <tr>
                                                    <td>Tạm tính:</td>
                                                    <td className="text-right">{formatVND(hoaDon.tongTien - hoaDon.phiVanChuyen)}</td>  
                                                </tr>
                                                <tr>
                                                    <td>Phí vận chuyển:</td>
                                                    <td className="text-right">{formatVND(hoaDon.phiVanChuyen)}</td>
                                                </tr>
                                                <tr>
                                                    <td>Phương thức thanh toán:</td>
                                                    {hoaDon.phuongThucThanhToan ? (
                                                        <td className="text-right">Thanh toán khi nhận hàng (COD)</td>
                                                    ) : (
                                                        <td className="text-right">Thanh toán online</td>
                                                    )}
                                                </tr>
                                                <tr>
                                                    <td className="total-amount">Tổng cộng:</td>
                                                    <td className="text-right total-amount">{formatVND(hoaDon.tongTien)}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <p className="note">Lưu ý: {hoaDon.ghiChu}</p>
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className="order-summary">
                                        <h4>Cảm ơn bạn. Đơn hàng của bạn đã được nhận.</h4>
                                        <p><strong>Mã đơn hàng:</strong> {hoaDon.maHoaDon}</p>
                                        <p><strong>Ngày:</strong> {format(new Date(hoaDon.ngayThanhToan), "dd-MM-yyyy")}</p>
                                        <p><strong>Số điện thoại:</strong> {hoaDon.dcSoDienThoai}</p>
                                        <p><strong>Địa chỉ:</strong> {hoaDon.diaChiNhan}</p>
                                        <p><strong>Phương thức thanh toán:</strong>
                                            {hoaDon.phuongThucThanhToan ? (
                                                <span className="text-right">Thanh toán khi nhận hàng (COD)</span>
                                            ) : (
                                                <span className="text-right">Thanh toán online</span>
                                            )}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        )}
                    </>
                ) : (
                    <>
                        <div className='d-flex justify-content-center align-items-center' style={{minHeight:510}}>
                            <FourSquare color="#067A38" size="large" text="" textColor="" />
                        </div>
                    </>
                )}

            </div>
            <Footer />
        </>
    );
}
