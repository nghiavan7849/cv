import React, { useEffect, useState } from 'react';
import '../css/orderhistory.css';
import Navbar from './menu';
import Footer from './footer';
import axios from 'axios';
import { Modal, Button } from 'react-bootstrap';
import { NavLink, useNavigate } from 'react-router-dom';
import formatVND from '../Service/FormatVND';

const urlApi = process.env.REACT_APP_API_URL;
const getLichSu = (page, maTaiKhoan, trangThai, token) => {
  return axios.get(`${urlApi}/api/lich-su-mua-hang?page=${page}&size=5&maTaiKhoan=${maTaiKhoan}&trangThai=${trangThai}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
}
const getChiTietLichSu = (page, maTaiKhoan, trangThai, maHoaDon, token) => {
  return axios.get(`${urlApi}/api/lich-su-mua-hang/chi-tiet?size=100&page=${page}&maTaiKhoan=${maTaiKhoan}&trangThai=${trangThai}&maHoaDon=${maHoaDon}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
}
const createGioHang = (data, token) => {
  return axios.post(`${urlApi}/api/chi-tiet-gio-hang/create`, data, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
};

const updateTrangThai = (maHoaDon, trangThai, lyDoHuy, token) => {
  return axios.put(`${urlApi}/api/hoa-don/cap-nhat-trang-thai?maHoaDon=${maHoaDon}&trangThai=${trangThai}&lyDoHuy=${lyDoHuy}`, null, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
};


const OrderHistory = () => {
  const [listHoaDon, setListHoaDon] = useState([]);
  const [listChiTietLichSu, setListChiTietLichSu] = useState([]);
  const [activeTab, setActiveTab] = useState('Đã Đặt Hàng'); // default tab
  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [showModalOrderHistory, setShowModalOrderHistory] = useState(false);
  const [lyDoHuy, setLyDoHuy] = useState("");
  const [messageLyDoHuy, setMessageLyDoHuy] = useState("");
  const [hoaDonHuy, setHoaDonHuy] = useState({});
  const [sanPhamDanhGia, setSanPhamDanhGia] = useState([{ maSanPham: '', check: true }]);
  const listLyDo = [
    { id: 1, lyDo: "Tôi không có nhu cầu mua nữa" },
    { id: 2, lyDo: "Tôi muốn cập nhật địa chỉ/sđt nhận hàng" },
    { id: 3, lyDo: "Tôi tìm thấy chỗ mua khác tốt hơn" },
    { id: 4, lyDo: "Tôi muốn thay đổi sản phẩm" },
    { id: 5, lyDo: "Khác" },
  ]
  const [lyDoKhac, isLyDoKhac] = useState(false);
  const [selectedLyDo, setSelectedLyDo] = useState("");
  const sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan")) || { maTaiKhoan: 1 };
  const [tongTienSanPham, setTongTienSanPham] = useState(0);
  const [tongTien, setTongTien] = useState(0);
  let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));
  const navigate = useNavigate();
  const loadPage = async () => {
    let res = await getLichSu(page, sessionTaiKhoan.maTaiKhoan, getTrangThai(activeTab), sessionToken);
    setTotalPage(res.data.totalPage);
    setListHoaDon(prevList => [...prevList, ...res.data.data.lichSu]);
  }

  const getTrangThai = (trangThai) => {
    if (trangThai === 'Đã Đặt Hàng' || trangThai === 'Đã đặt hàng') {
      return 'dadathang';
    } else if (trangThai === 'Đã Xác Nhận' || trangThai === 'Đã xác nhận') {
      return 'daxacnhan';
    } else if (trangThai === 'Đang Xử Lý' || trangThai === 'Đang xử lý') {
      return 'dangxuly';
    } else if (trangThai === 'Đang Vận Chuyển' || trangThai === 'Đang vận chuyển') {
      return 'dangvanchuyen';
    } else if (trangThai === 'Giao Thành Công' || trangThai === 'Giao thành công') {
      return 'giaothanhcong';
    } else if (trangThai === 'Đã Hủy' || trangThai === 'Đã Hủy') {
      return 'dahuy';
    }
  }

  const clickChiTietLichSu = async (order) => {
    let res = await getChiTietLichSu(1, sessionTaiKhoan.maTaiKhoan, getTrangThai(order.tenTrangThai), order.maHoaDon, sessionToken);
    setListChiTietLichSu(res.data.data.lichSu);

    // let response = await getChiTietLichSu(1, sessionTaiKhoan.maTaiKhoan, getTrangThai(order.tenTrangThai), order.maHoaDon, sessionToken);
    const maTaiKhoan = sessionTaiKhoan.maTaiKhoan;
    const updateSanPhamDanhGia = [];
    let tongTienSanPham = 0;
    for (const ct of res.data.data.lichSu) {
      updateSanPhamDanhGia.push(ct.maSanPham);
      tongTienSanPham += ct.tongTien;
    }
    setTongTienSanPham(tongTienSanPham);
    try {
      let res = await axios.post(`${urlApi}/api/danh-gia/check-list?maTaiKhoan=${maTaiKhoan}`, {
        listIds: updateSanPhamDanhGia
      });
      setSanPhamDanhGia(res.data.listCheck);
      return res.data.check;
    } catch (error) {
      console.log('error', error)
    }
  }

  const clickTabStatus = (status) => {
    setActiveTab(status);
    setListHoaDon([]);
    setListChiTietLichSu([]);
    setPage(1);
  }

  const clickMuaLai = async (maSanPham, soLuong) => {
    let maTaiKhoan = sessionTaiKhoan.maTaiKhoan;
    let res = await createGioHang({
      "soLuong": soLuong,
      "maTaiKhoan": maTaiKhoan,
      "maSanPham": maSanPham
    }, sessionToken);
    if (res) {
      //  toast.success("Thêm vào giỏ hàng thành công");
      sessionStorage.setItem("listCTGH", JSON.stringify([res.data.data.chiTietGioHang]));
      navigate("/home/pay-details", { state: [res.data.data.chiTietGioHang] });
    }
  }

  const clickDaNhanDuocHang = async (hd) => {
    let res = await updateTrangThai(hd.maHoaDon, 'giaothanhcong', "", sessionToken);
    if (res) {
      loadPage();
      setListChiTietLichSu([]);
    }
  }

  useEffect(() => {
    loadPage();
  }, [page, activeTab]);


  const clickHuy = (hd) => {
    setShowModal(true);
    setHoaDonHuy(hd);
    isLyDoKhac(false);
    setLyDoHuy("Tôi không có nhu cầu mua nữa");
    setSelectedLyDo("Tôi không có nhu cầu mua nữa");
  }

  const clickXacNhanHuy = async () => {
    if (lyDoHuy === "") {
      setMessageLyDoHuy("Vui lòng nhập lý do hủy đơn hàng");
      document.getElementById("lyDoHuy").classList.add("error-validate");
    } else {
      setShowModal(false);
      let res = await updateTrangThai(hoaDonHuy.maHoaDon, "dahuy", lyDoHuy, sessionToken);
      if (res) {
        loadPage();
        setListChiTietLichSu([]);
      }
    }
  }

  const onChangeLyDoHuy = (e) => {
    setLyDoHuy(e.target.value)

    setMessageLyDoHuy("");
    document.getElementById("lyDoHuy").classList.remove("error-validate");
  }

  const handleClose = () => {
    setShowModal(false);
  }
  const handleCloseOrderHistory = () => {
    setShowModalOrderHistory(false);
  }
  const clickXemChiTiet = (order) => {
    setTongTien(order.tongTien);
    clickChiTietLichSu(order);
    setShowModalOrderHistory(true);
  }
  const clickChonLyDo = (event) => {
    const value = event.target.value;
    setSelectedLyDo(value);
    if (value === "Khác") {
      isLyDoKhac(true);
      setLyDoHuy("");
    } else {
      isLyDoKhac(false);
      setLyDoHuy(value);
      setMessageLyDoHuy("");
    }
  }

  const clickXemThem = () => {
    setPage(page + 1);
  }

  return (
    <>
      <Navbar />
      <ul className="nav nav-tabs mb-4" id="myTab" role="tablist" style={{ marginTop: 80 }}>
        {['Đã Đặt Hàng', 'Đã Xác Nhận', 'Đang Xử Lý', 'Đang Vận Chuyển', 'Giao Thành Công', 'Đã Hủy'].map(status => (
          <li className="nav-item" key={status}>
            <span
              className={`nav-link ${activeTab === status ? 'active' : ''}`}
              onClick={() => { clickTabStatus(status) }}
            >
              {status}
            </span>
          </li>
        ))}
      </ul>
      <hr />

      <div className="container order-history-container mb-5">
        <div style={{ minHeight: '300px' }}>
          <h3 className="cart-header mt-4">Hóa đơn mua hàng</h3>
          <div className="table-responsive mb-2">
            <table className="table">
              <thead>
                <tr>
                  <th>Mã hóa đơn</th>
                  <th>Tổng Sản phẩm</th>
                  <th>Địa Chỉ</th>
                  <th>Trạng Thái</th>
                  <th>Tổng Tiền</th>
                  {activeTab === 'Đã Hủy' ? (
                    <th>Lý do hủy</th>
                  ) : (
                    <></>
                  )}
                  <th>Thao Tác</th>
                </tr>
              </thead>
              <tbody>
                {
                  listHoaDon.length === 0 ? (
                    <tr>
                      <td colSpan="6" className='text-center'>Không có dữ liệu để hiển thị.</td>
                    </tr>
                  ) : (
                    listHoaDon.map(order => {
                      return (
                        <tr key={order.maHoaDon + Math.random()}>
                          <td>{order.maHoaDon}</td>
                          <td>{order.tongSanPham}</td>
                          <td>{order.diaChiNhan}</td>
                          <td>{order.tenTrangThai}</td>
                          <td style={{ color: 'red', fontWeight: 'bold' }}>{formatVND(order.tongTien)}</td>
                          {
                            order.tenTrangThai === 'Đã Hủy' ? (
                              <td>{order.lyDoHuy}</td>
                            ) : (
                              <></>
                            )
                          }
                          <td className='d-flex justify-content-center'>
                            <div>
                              {order.tenTrangThai === 'Đang vận chuyển' ? (
                                <button className="btn btn-sm btn-success ms-1 me-1"
                                  onClick={() => clickDaNhanDuocHang(order)}
                                >Đã nhận được hàng
                                </button>
                              ) : (
                                <></>
                              )
                              }
                              {order.tenTrangThai === 'Đã đặt hàng' ? (
                                <button className="btn btn-sm btn-danger ms-1 me-1"
                                  onClick={() => clickHuy(order)}
                                >Hủy đơn hàng
                                </button>
                              ) : (
                                <></>
                              )
                              }
                              <button
                                onClick={() => clickXemChiTiet(order)}
                                className="btn btn-info btn-sm ms-1 me-1"
                              >
                                Xem Chi Tiết
                              </button>
                            </div>
                          </td>

                        </tr>
                      )
                    })
                  )
                }
              </tbody>
            </table>
          </div>
          {(totalPage > 1 && page < totalPage) && (
            <div className='d-flex justify-content-center'>
              <button className='btn btn-success px-3' onClick={() => clickXemThem()}>Xem thêm</button>
            </div>
          )
          }

        </div>
      </div>
      <Modal show={showModal} onHide={handleClose} >
        <Modal.Header closeButton className='d-flex justify-content-end '>
          <Modal.Title className='fw-bold fs-3'>Lý do hủy</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className='ms-4 me-4 mt-2 mb-2'>
            <select className="form-select mb-4 py-2" id="ward" value={selectedLyDo} onChange={clickChonLyDo}>
              {listLyDo.map((item) => {
                return <option key={item.id} value={item.lyDo}>{item.lyDo}</option>
              })}
            </select>
            {lyDoKhac ? (<><textarea class="form-control" id="lyDoHuy" rows="3" onChange={(e) => onChangeLyDoHuy(e)}></textarea>
              <span className='text-danger'>{messageLyDoHuy}</span></>
            ) : (
              <></>
            )}

          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} style={{ width: '120px' }}>
            Thoát
          </Button>
          <Button variant="success" onClick={() => clickXacNhanHuy()} style={{ width: '120px' }}>
            Xác nhận
          </Button>
        </Modal.Footer>
      </Modal>
      {/* Modal xem chi tiết */}
      <Modal show={showModalOrderHistory} onHide={handleCloseOrderHistory} size="lg" scrollable="true">
        <Modal.Header closeButton className='d-flex justify-content-end'>
          <Modal.Title className='fw-bold fs-3'>Chi tiết mua hàng</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className='ms-1 me-1 mb-2'>
            <div className="tab-content" id="myTabContent">
              <div className="tab-pane fade show active" id="purchased" role="tabpanel" aria-labelledby="purchased-tab">
                <div className="table-responsive">
                  <table className="table table-order-history">
                    <thead>
                      <tr>
                        <th>Mã hóa đơn</th>
                        <th>Hình ảnh</th>
                        <th>Sản phẩm</th>
                        <th>Số Lượng</th>
                        <th>Tổng Tiền</th>
                        <th>Thao Tác</th>
                      </tr>
                    </thead>
                    <tbody>
                      {
                        listChiTietLichSu.map((detail) => (
                          <tr key={detail.maSanPham}>
                            <td>{detail.maHoaDon}</td>
                            <td><img src={detail.hinhAnh} alt={detail.tenSanPham} width="50" height="50" /></td>
                            <td>{detail.tenSanPham}</td>
                            <td>{detail.soLuong}</td>
                            <td style={{ color: 'red' }}>{formatVND(detail.tongTien)}</td>
                            <td className="align-content-center">
                              {
                                (activeTab === "Giao thành công" || activeTab === "Giao Thành Công") ? (
                                  <>
                                    <NavLink className="btn btn-sm btn-buy-again btn-success me-2" onClick={() => { clickMuaLai(detail.maSanPham, detail.soLuong) }}>Mua Lại</NavLink>
                                    {sanPhamDanhGia.find(sp => sp.maSanPham === detail.maSanPham && !sp.check) ? (
                                      <NavLink
                                        className="btn btn-sm btn-review btn-warning"
                                        to={`/home/evaluate/${detail.maSanPham}/${detail.soLuong}/${detail.maHoaDon}`}
                                      >
                                        Đánh giá
                                      </NavLink>
                                    ) : null}
                                  </>
                                ) : (
                                  <></>
                                )

                              }

                            </td>
                          </tr>
                        ))
                      }
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div className='row'>
              <div className='col-md-6'></div>
              <div className='col-md-3'>Tổng tiền sản phẩm:</div>
              <div className='col-md-3 text-end'>{formatVND(tongTienSanPham)}</div>
              {/* <div className='col-md-3'></div> */}

              <div className='col-md-6'></div>
              <div className='col-md-3'>Phí vận chuyển:</div>
              <div className='col-md-3 text-end'>{formatVND(tongTien - tongTienSanPham)}</div>
              {/* <div className='col-md-3'></div> */}

              <div className='col-md-6'></div>
              <div className='col-md-3'>Tổng tiền:</div>
              <div className='col-md-3 text-end fw-medium text-danger'>{formatVND(tongTien)}</div>
              {/* <div className='col-md-3'></div> */}

            </div>
          </div>
        </Modal.Body>
        {/* <Modal.Footer>Hihi</Modal.Footer> */}
      </Modal>
      <Footer />
    </>
  );
};

export default OrderHistory;
