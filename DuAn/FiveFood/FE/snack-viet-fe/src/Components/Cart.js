import '../css/Cart.css';
import imgCart from '../images/anhgiohang.png'
import Navbar from './menu';
import Footer from './footer';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useEffect, useRef, useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { FourSquare, OrbitProgress } from 'react-loading-indicators';
import formatVND from '../Service/FormatVND';
import { All } from '@tensorflow/tfjs';

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

const updateSoLuongCart = (maCTGH, soLuong, token) => {
    return axios.put(`${urlApi}/api/chi-tiet-gio-hang/update?maChiTietGioHang=${maCTGH}&soLuong=${soLuong}`,
        null, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    }
    );
}
const deleteCart = (maChiTietGioHang, token) => {
    return axios.delete(`${urlApi}/api/chi-tiet-gio-hang/delete/${maChiTietGioHang}`,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        }
    );
}

export const Cart = () => {
    const navRef = useRef();

    let sessionTaiKhoan = JSON.parse(sessionStorage.getItem("sessionTaiKhoan"));
    let sessionToken = JSON.parse(sessionStorage.getItem("sessionTokenTK"));

    const [listCart, setListCart] = useState([]);
    const [modalItemDelete, setModalItemDelete] = useState({});
    const [showModalDelete, setShowModalDelete] = useState(false);
    const [showModalDeleteAll, setShowModalDeleteAll] = useState(false);
    const [listItemClickChon, setListItemClickChon] = useState([]);
    const [totalProductSelect, setTotalProductSelect] = useState(0);
    const [totalAmount, setTotalAmount] = useState(0);
    const [loading, isLoading] = useState(false);
    const [loadingDeleteAll, isLoadingDeleteAll] = useState(false);
    const [loadingDeleteItemCart, isLoadingDeleteItemCart] = useState(false);

    const navigate = useNavigate();
    // load dữ liệu khi mới vào
    const loadPage = async () => {
        let res = await getCardByTaiKhoan(sessionTaiKhoan.maTaiKhoan, sessionToken);
        setListCart(res.data.data.chiTietGioHang);
        isLoading(true);
    }

    // Click Có khi hỏi có đồng ý xóa không
    const clickAgreeDelete = async (item) => {
        isLoadingDeleteItemCart(true);
        let res = await deleteCart(item.maChiTietGioHang, sessionToken);
        if (res) {
            setModalItemDelete({});
            setShowModalDelete(false);
            isLoadingDeleteItemCart(false);
            if(navRef.current){
                navRef.current.loadListCartMenu(sessionTaiKhoan, sessionToken);
            }
            toast.success("Xóa sản phẩm ra khỏi giỏ hàng thành công");
            loadPage();
        }
    }
    // Click để hiện modal hỏi có xóa sản phẩm không
    const clickDeleteCart = (item) => {
        setShowModalDelete(true);
        setModalItemDelete(item);
    }
    // Click để tắt modal
    const handleClose = () => {
        setShowModalDelete(false);
    }
    const handleCloseDeleteAll = () => {
        setShowModalDeleteAll(false);
    }
    const clickAgreeDeleteAll = async () => {
        isLoadingDeleteAll(true);
        await Promise.all(
            listCart.map(item => deleteCart(item.maChiTietGioHang, sessionToken))
        )
        // listCart.forEach(item => {
        //     deleteCart(item.maChiTietGioHang, sessionToken);
        // })
        setShowModalDeleteAll(false);
        isLoadingDeleteAll(false);
        if(navRef.current){
            navRef.current.loadListCartMenu(sessionTaiKhoan, sessionToken);
        }
        toast.success("Xóa tất cả sản phẩm ra khỏi giỏ hàng thành công");
        loadPage();
    }
    // Click để trừ số lượng sản phẩm
    const clickTruSoLuong = async (item) => {
        if (item.soLuong > 1) {
            var soLuong = +item.soLuong - 1;
            let res = await updateSoLuongCart(item.maChiTietGioHang, soLuong, sessionToken);
            if (res) {
                loadPage();
                listItemClickChon.forEach((i) => {
                    if (i.maChiTietGioHang === item.maChiTietGioHang) {
                        setTotalAmount(preTotalAmout => preTotalAmout - item.sanPhamGH.gia);
                    }
                });
            }
        }
    }
    // Click để cộng số lượng sản phẩm
    const clickCongSoLuong = async (item) => {
        if (item.soLuong < 10) {
            var soLuong = +item.soLuong + 1;
            let res = await updateSoLuongCart(item.maChiTietGioHang, soLuong, sessionToken);
            if (res) {
                loadPage();
                listItemClickChon.forEach((i) => {
                    if (i.maChiTietGioHang === item.maChiTietGioHang) {
                        setTotalAmount(preTotalAmout => preTotalAmout + item.sanPhamGH.gia);
                    }
                })
            }
        }
    }

    // Click tích chọn sản phẩm
    const clickChonSanPham = (event, item) => {
        var checkboxes = document.querySelectorAll(".checkboxClass");
        if (event.target.checked) {
            let tong = 0;
            checkboxes.forEach(checkbox => {
                if (checkbox.checked) {
                    tong += 1;
                }
            });
            if (tong === listCart.length) {
                document.getElementById("checkboxAll").checked = true;
            }
            setListItemClickChon([item, ...listItemClickChon]);
            setTotalProductSelect(totalProductSelect + 1);
            setTotalAmount(totalAmount + item.sanPhamGH.gia * item.soLuong)
        } else {
            setListItemClickChon(listItemClickChon.filter(i => i.maChiTietGioHang !== item.maChiTietGioHang));
            setTotalProductSelect(totalProductSelect - 1);
            setTotalAmount(totalAmount - item.sanPhamGH.gia * item.soLuong);
            let tong = 0;
            checkboxes.forEach(checkbox => {
                if (checkbox.checked) {
                    tong -= 1;
                }
            });
            if (tong < listCart.length) {
                document.getElementById("checkboxAll").checked = false;
            }
        }

    }

    //Click chọn tất cả
    const clickSelectAll = (event) => {
        // Checkbox chọn tất cả
        var checkboxAll = document.getElementById("checkboxAll");
        var checkboxes = document.querySelectorAll(".checkboxClass");
        checkboxes.forEach(function (checkbox) {
            if (checkboxAll.checked) {
                checkbox.checked = true;
            } else {
                checkbox.checked = false;
            }
        });
        if (checkboxAll.checked) {
            setTotalProductSelect(listCart.length);
            listCart.forEach((i) => {
                setTotalAmount(preTotalAmout => preTotalAmout + i.sanPhamGH.gia * i.soLuong);
            });
            setListItemClickChon(listCart);
        } else {
            setTotalProductSelect(0);
            setTotalAmount(0);
            setListItemClickChon([]);
        }
        // if(checkboxAll.checked){
        //     total = '${totalGioHang}';
        //     tongSanPham = checkboxes.length;
        // } else {
        //     total = 0;
        //     tongSanPham = 0;
        // }
        // tongSanPhamChon.innerText = tongSanPham;
        // document.getElementById('total').innerText =  parseFloat(total).toLocaleString('vi-VN');
    }

    //Click nút mua hàng
    const clickMuaHang = () => {
        if (listItemClickChon.length > 0) {
            sessionStorage.setItem("listCTGH", JSON.stringify(listItemClickChon));
            sessionStorage.setItem('checkBuyNow', JSON.stringify(false));
            navigate("/home/pay-details", { state: listItemClickChon });
        }
    }


    useEffect(() => {
        loadPage();
    }, [])

    return (
        <div style={{ marginTop: 65 }}>
            <Navbar ref={navRef}/>
            {loading ? (
                <>
                    <div className="container mt-5 cart-container" style={{ marginBottom: '75px', minHeight: '430px' }}>
                        <h2 className="text-center cart-header pb-2">Giỏ Hàng</h2>
                        {
                            listCart.length === 0 ? (
                                <div>
                                    <div className="d-flex justify-content-center mt-5"><img alt={imgCart} src={imgCart} width='130' /></div>
                                    <div className="text-center fs-4">Chưa có sản phẩm</div>
                                </div>
                            ) : (
                                <div>
                                    <div className="table-responsive">
                                        <table className="table">
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th>Sản phẩm</th>
                                                    <th>Đơn Giá</th>
                                                    <th>Số Lượng</th>
                                                    <th>Số Tiền</th>
                                                    <th className="d-flex justify-content-center">Thao Tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {
                                                    listCart.map((item) => {
                                                        return (
                                                            <tr className="cart-item" key={`cart-${item.maChiTietGioHang}`}>
                                                                <td className="align-content-center">
                                                                    <div className="d-flex justify-content-center">
                                                                        <input type="checkbox"
                                                                            className="checkboxClass"
                                                                            onClick={(event) => { clickChonSanPham(event, item) }}
                                                                        />
                                                                    </div>
                                                                </td>
                                                                <td className="align-content-center">
                                                                    <div className="product-info">
                                                                        <img src={item.sanPhamGH.hinhAnh} alt="" />
                                                                        {item.sanPhamGH.tenSanPham}
                                                                    </div>
                                                                </td>
                                                                <td className="align-content-center">{formatVND(item.sanPhamGH.gia)}</td>
                                                                <td className="align-content-center">
                                                                    <div>
                                                                        <div className="quantity-control">
                                                                            <button type="button" className="btn btn-sm" onClick={() => { clickTruSoLuong(item) }} >-</button>
                                                                            <input type="text" className="form-control" name="soLuong" value={item.soLuong} min="1" style={{ width: '80px' }} id="soLuong" disabled={true} />
                                                                            <button type="button" className="btn btn-sm" onClick={() => { clickCongSoLuong(item) }} >+</button>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                                <td className="align-content-center">{formatVND(item.sanPhamGH.gia * item.soLuong)}</td>
                                                                <td className="align-content-center "><span onClick={() => { clickDeleteCart(item) }} className="remove-link d-flex justify-content-center"><i className="fa-solid fa-trash-can " style={{ color: '#ff0000', fontSize: '22px' }}></i></span></td>
                                                            </tr>
                                                        )
                                                    })
                                                }
                                            </tbody>
                                        </table>
                                    </div>
                                    <div className="cart-footer">
                                        <table style={{ width: '100%', padding: '0 20px' }}>
                                            <tbody >
                                                <tr className="row" >
                                                    <td className="col-md-2 align-content-center"><input className="ms-1" type="checkbox" id="checkboxAll" onClick={(event) => { clickSelectAll(event) }} />&nbsp;<label htmlFor='checkboxAll'> Chọn Tất Cả ({totalProductSelect})</label></td>
                                                    <td className="col-md-4 align-content-center"><span className="remove-link" style={{ textDecoration: 'none' }} onClick={() => setShowModalDeleteAll(true)} >Xóa tất cả</span></td>
                                                    <td className="col-md-4 align-content-center"><div className="d-flex justify-content-end">Tổng thanh toán (<span id="tongSanPhamChon" className="ms-1 me-1">{totalProductSelect}</span> sản phẩm): <span className="total-amount"> <span id="total" className="ms-1 me-1">{totalAmount === 0 ? "0 ₫" : formatVND(totalAmount)}</span> </span> </div> </td>
                                                    <td className="col-md-2 align-content-center d-flex justify-content-center">
                                                        <button
                                                            type="submit"
                                                            className="btn btn-success"
                                                            id="muaHang"
                                                            onClick={clickMuaHang}
                                                            disabled={listItemClickChon.length === 0 ? true : false}
                                                        >Mua Hàng
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            )
                        }
                    </div>
                </>
            ) : (
                <>
                    <div className='d-flex justify-content-center align-items-center' style={{ minHeight: 510 }}>
                        <FourSquare color="#067A38" size="large" text="" textColor="" />
                    </div>
                </>
            )}



            <Modal show={showModalDelete} onHide={handleClose}>
                <Modal.Header closeButton className='d-flex justify-content-end '>
                    <Modal.Title className='fw-bold fs-3'>Xóa sản phẩm</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='ms-4 me-4 mt-2 mb-2'>
                        Bạn có chắc muốn xóa sản phẩm <span className='fw-bold'>{Object.keys(modalItemDelete).length === 0 ? '' : modalItemDelete.sanPhamGH.tenSanPham}</span> này không?
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose} style={{ width: '80px' }}>
                        Không
                    </Button>
                    <Button variant="danger" onClick={() => clickAgreeDelete(modalItemDelete)} style={{ width: '80px' }}>
                        Có
                    </Button>
                </Modal.Footer>
                {
                    loadingDeleteItemCart && (
                        <div className='position-absolute bg-black loading-modal-delete-all d-flex justify-content-center align-items-center' style={{ opacity: 0.4 }}>
                            <div>
                                <OrbitProgress variant="disc" color="#32cd32" size="small" text="" textColor="" />
                            </div>
                        </div>
                    )
                }
            </Modal>
            <Modal show={showModalDeleteAll} onHide={handleCloseDeleteAll}>
                <Modal.Header closeButton className='d-flex justify-content-end '>
                    <Modal.Title className='fw-bold fs-3'>Xóa tất cả sản phẩm</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className='ms-4 me-4 mt-2 mb-2'>
                        Bạn có chắc muốn xóa tất cả sản phẩm có trong giỏ hàng không?
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseDeleteAll} style={{ width: '80px' }}>
                        Không
                    </Button>
                    <Button variant="danger" onClick={() => clickAgreeDeleteAll()} style={{ width: '80px' }}>
                        Có
                    </Button>
                </Modal.Footer>
                {
                    loadingDeleteAll && (
                        <div className='position-absolute bg-black loading-modal-delete-all d-flex justify-content-center align-items-center' style={{ opacity: 0.4 }}>
                            <div>
                                <OrbitProgress variant="disc" color="#32cd32" size="small" text="" textColor="" />
                            </div>
                        </div>
                    )
                }

            </Modal>
            <Footer />
        </div>

    )
}