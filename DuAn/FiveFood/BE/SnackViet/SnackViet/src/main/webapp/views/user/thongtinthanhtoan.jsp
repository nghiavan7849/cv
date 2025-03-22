<%@page import="com.snackviet.model.ChiTietGioHang"%>
<%@page import="com.snackviet.repository.ChiTietHoaDonRepository"%>
<%@page import="com.snackviet.repository.ChiTietGioHangRepository"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chi Tiết Đơn Hàng</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/menu.css">
	<link rel="stylesheet" href="${request.contextPath}/css/footer.css">
    <style>
        body {
            background-color: #F5F0DC;
            color: #333;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .breadcrumb {
            background-color: transparent;
            padding: 0;
            margin-bottom: 20px;
        }
        .breadcrumb-item + .breadcrumb-item::before {
            content: '>';
            color: #6c757d;
            padding: 0 5px;
        }
        .breadcrumb-item a {
            color: #6c757d;
            text-decoration: none;
            transition: color 0.3s;
        }
        .breadcrumb-item a:hover {
            color: #000;
            text-decoration: underline;
        }
        .breadcrumb-item.active {
            color: #000;
        }
        .section-header {
            font-size: 1.75rem;
            margin-top: 20px;
            margin-bottom: 20px;
            color: #333;
            font-weight: bold;
            text-align: center;
        }
        .order-details, .order-summary {
            background-color: #fff;
            padding: 20px;
            margin-top: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .order-details:hover, .order-summary:hover {
            transform: translateY(-10px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
        .order-details h4, .order-summary h4 {
            margin-bottom: 20px;
            color: #000;
            font-weight: bold;
            text-align: center;
        }
        .order-summary p {
            margin-bottom: 10px;
        }
        .table-borderless td, .table-borderless th {
            border: 0;
        }
        .total-amount {
            font-size: 1.2em;
            font-weight: bold;
        }
        .note {
            color: #6c757d;
            text-align: center;
        }
        .form-check-label {
            margin-bottom: 10px;
            font-weight: normal;
        }
        .form-check-input {
            margin-right: 10px;
        }
        .btn-success {
            background-color: #067A38;
            border: none;
            padding: 15px;
            font-size: 1.1em;
            font-weight: bold;
            width: 100%;
            transition: background-color 0.3s, transform 0.3s;
        }
        .btn-success:hover {
            background-color: #056530;
            transform: translateY(-2px);
        }
        .btn-success:focus {
            outline: none;
            box-shadow: 0 0 0 3px rgba(6, 122, 56, 0.5);
        }
        input.form-control, textarea.form-control {
            border: 2px solid #e5e5e5;
            transition: border-color 0.3s;
        }
        input.form-control:focus, textarea.form-control:focus {
            border-color: #067A38;
            box-shadow: none;
        }
    </style>
</head>
<body>
	
	<!-- Menu -->
   <%@ include file="menu.jsp" %>
   <!-- End Menu -->

	<div class="container mt-3 mb-5">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${request.contextPath}/gio-hang">Giỏ hàng</a></li>
                <li class="breadcrumb-item"><a>Chi tiết thanh toán</a></li>
            </ol>
        </nav>

        <h3 class="section-header">Thông tin thanh toán</h3>

        <div class="row">
            <div class="col-md-8">
                <div class="order-details">
                    <h4>Thông Tin Thanh Toán</h4>
                       <c:if test="${!empty message}">
                        <span class="text-danger">${message} Tại đây</span>
                    	</c:if>
                    <form>
                        <div class="form-group">
                            <label for="email">Số điện thoại</label>
                            <input type="text" class="form-control" id="soDienThoai" value="${diaChi.soDienThoai}" placeholder="Nhập địa chỉ số điện thoại" disabled="disabled">
                        </div>
                        <div class="form-group">
                            <label for="name">Họ và Tên</label>
                            <input type="text" class="form-control" id="name" value="${diaChi.hoVaTen}" placeholder="Nhập họ và tên" disabled="disabled">
                        </div>
                        <div class="form-group">
                            <label for="address">Địa chỉ</label>
                            <input type="text" class="form-control" id="address" value="${diaChi.diaChi}" placeholder="Nhập địa chỉ" disabled="disabled">
                        </div>
                        <h4>Thông Tin Bổ Sung</h4>
                        <div class="form-group">
                            <label for="note">Ghi chú đơn hàng (tùy chọn)</label>
                            <textarea class="form-control" id="note" rows="3" placeholder="Nhập ghi chú về đơn hàng" ></textarea>
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-md-4">
                <div class="order-summary">
                    <h4>Đơn Hàng Của Bạn</h4>
                    <table class="table table-borderless">
                        <tbody>
                       		<c:forEach var="item" items="${listChiTietGioHang}">
	                            <tr>
	                                <td>${item.sanPhamGH.tenSanPham} × ${item.soLuong }</td>
	                                <td class="text-right"><fmt:formatNumber>${item.sanPhamGH.gia * item.soLuong}</fmt:formatNumber>  đ</td>
	                            </tr>
                            </c:forEach>
                            <tr>
                                <td>Tạm tính</td>
                                <td class="text-right"><fmt:formatNumber>${total}</fmt:formatNumber> đ</td>
                            </tr>
                            <tr>
                                <td>Phí vận chuyển</td>
                                <td class="text-right"><span id="phi-van-chuyen"></span> đ</td>
                            </tr>
                            <tr>
                                <td class="total-amount">Tổng</td>
                                <td class="text-right total-amount"><span id="tong-tien"></span> đ</td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="cod" checked>
                        <label class="form-check-label" for="cod">
                            Thanh toán khi nhận hàng (COD)
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="online" value="online">
                        <label class="form-check-label" for="online">
                            Thanh toán online
                        </label>
                    </div>
                    <button type="button" class="btn btn-success btn-block mt-4" id="xac-nhan-dat-hang" >Đặt Hàng</button>
                    <p class="note mt-3">Bằng cách nhấp vào Đặt hàng, bạn đồng ý với <a href="#">điều khoản và điều kiện</a> và <a href="#">chính sách riêng tư</a> của chúng tôi.</p>
                </div>
            </div>
        </div>
    </div>
	 <!-- Footer -->
    <%@ include file="footer.jsp" %>

    <!-- End Footer -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    
    <script type="text/javascript">
        let phiVanChuyen = 0;
    	document.addEventListener('DOMContentLoaded', (event) => {
    		document.getElementById('xac-nhan-dat-hang').setAttribute('disabled','disabled');
  
    		// Api
    		const url = 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee';
            const token = 'bab9fb8c-2323-11ef-a951-76299e96dead';
            const shopId = '192522';
            let address = '${diaChi.diaChi}';
            let parts = address.split(',');
            let wardTo = parts[1].trim();
            let districtTo = parts[2].trim();
            let city = parts[3].trim();

            let codeWard = 0;
            let codeDistrict = 0;
            let codeCity = 0;
            
            // Api tỉnh
            fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', {
                headers: {
                    'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead' // Replace with your actual API key
                }
            })
                .then(response => response.json())
                .then(data => {
                    let provinces = data.data;
                    provinces.map(value => {
                        if (city === value.ProvinceName) {
                            codeCity = value.ProvinceID;
                        }
                    });
                    // Api quận huyện
                    fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=' + codeCity, {
                        headers: {
                            'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead' // Replace with your actual API key
                        }
                    })
                        .then(response => response.json())
                        .then(data => {
                            let district = data.data;
                            district.map(value => {
                               if (districtTo === value.DistrictName) {
                                    codeDistrict = value.DistrictID;
                                }
                            });
                            // API xã phường
                            fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + codeDistrict, {
                                headers: {
                                    'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead' // Replace with your actual API key
                                }
                            })
                                .then(response => response.json())
                                .then(data => {
                                    let ward = data.data;
                                    ward.map(value => {
                                        if (wardTo === value.WardName) {
                                            codeWard = value.WardCode;
                                        }
                                    });
                                    
                                    let totalWeight = '${totalWeight}';

                                    const dataFee = {
                                        from_district_id: 1574,
                                        from_ward_code: '550307',
                                        service_id: 53320,
                                        service_type_id: null,
                                        to_district_id: codeDistrict,
                                        to_ward_code: codeWard,
                                        weight: parseFloat(totalWeight),
                                        insurance_value: 10000,
                                        cod_failed_amount: 2000,
                                        coupon: null
                                    };

                                    const headers = {
                                        'Content-Type': 'application/json',
                                        'Token': token,
                                        'ShopId': shopId
                                    };

                                    //API tính phí vận chuyển

                                    fetch(url, {
                                        method: 'POST',
                                        headers: headers,
                                        body: JSON.stringify(dataFee)
                                    })
                                        .then(response => {
                                            if (!response.ok) {
                                                throw new Error(`HTTP error! status: ${response.status}`);
                                            }
                                            return response.json();
                                        })
                                        .then(data => {
                                        	phiVanChuyen = data.data.total;
                                        	document.getElementById('phi-van-chuyen').innerText =  parseFloat(data.data.total).toLocaleString('vi-VN');
                                        	let total = '${total}';
                                        	let spanTongTien = parseFloat(total) + parseFloat(data.data.total);
                                        	document.getElementById('tong-tien').innerText =  parseFloat(spanTongTien).toLocaleString('vi-VN');
                                        	document.getElementById('xac-nhan-dat-hang').removeAttribute('disabled');
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                        });

                                })
                                .catch(error => {
                                    console.log(error)
                                    console.log("Lỗi gọi API!!!")
                                });

                        })
                        .catch(error => {
                            console.log(error)
                            console.log("Lỗi gọi API!!!")
                        });


                })
                .catch(error => {
                    console.log(error)
                    console.log("Lỗi gọi API!!!")
                });
   		});
    	var clickXacNhanDatHang =  document.getElementById('xac-nhan-dat-hang');
    	clickXacNhanDatHang.addEventListener('click',() => {
    		var note = document.getElementById('note');
    		const selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked');;
    		let paymentMethodValue = true;
    		console.log(selectedPaymentMethod.value);
    		if (selectedPaymentMethod.value === 'cod') {
    		   	paymentMethodValue = true;
    		} else {
    			paymentMethodValue = false;
    		}
    		
    		// Lấy URL hiện tại
    	    var currentUrl = window.location.href;
    	    var urlParams = new URLSearchParams(window.location.search);
    	    
    	    // Lấy giá trị của tham số 'idGH' và 'idSP'
    	    var idGH = urlParams.get('idGH');
    	    var idSP = urlParams.get('idSP');
    	    if(idGH){
    	    	window.location.href =  window.location.protocol + '//' +  window.location.hostname+ ':' +  window.location.port + "/gio-hang/dat-hang/xac-nhan?idGH="+idGH+"&phivanchuyen="+phiVanChuyen+"&note="+note.value+"&phuongthucthanhtoan="+paymentMethodValue;    	    	
    	    } else if(idSP){
    	    	window.location.href =  window.location.protocol + '//' +  window.location.hostname+ ':' +  window.location.port + "/gio-hang/mua-hang/dat-hang/xac-nhan?idSP="+idSP+"&phivanchuyen="+phiVanChuyen+"&note="+note.value+"&phuongthucthanhtoan="+paymentMethodValue;
    	    }
    	    
    	});
    </script>
    
     <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    
</body>
</html>




