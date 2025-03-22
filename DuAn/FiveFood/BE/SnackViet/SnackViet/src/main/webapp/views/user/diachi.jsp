<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Địa chỉ</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/diachi.css">
     <link rel="stylesheet" href="${request.contextPath}/css/menu.css">
    <link rel="stylesheet" href="${request.contextPath}/css/footer.css">
</head>
<body>

	<!-- Menu -->
   <%@ include file="menu.jsp" %>
   <!-- End Menu -->
   
	<main>
        <div class="row pt-5 ms-5 me-5">
            <div class="col-md-8 d-flex justify-content-center mt-4">
                <div class="card card-form">
                    <div class="card-body">
                        <div class="text-center fs-2 fw-bold">Địa chỉ</div>
                        <div class="mt-4">
                            <form>
                                <div class="d-flex justify-content-center">
                                    <div class="d-flex justify-content-between div-input">
                                        <div class="input-box">
                                            <div class="input-dau"> 
                                                <i class="fa-solid fa-user"></i>
                                                <input type="text" placeholder="Họ và tên" name="hoVaTen" value="${valueHoVaTen}"><br>
                                                <span class="text-danger">${messageHoVaTen}</span>
                                            </div>
                                        </div>
                                        <div class="input-box ">
                                            <div class="input-dau"> 
                                                <i class="fa-solid fa-phone"></i>
                                                <input type="text" placeholder="Số điện thoại" name="soDienThoai" value="${valueSoDienThoai}"><br>
                                                <span class="text-danger">${messageSoDienThoai}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center ">
                                    <div class="d-flex justify-content-between div-input">
                                        <div class="input-box">
                                            <div class="input-giua"> 
                                                <select class="form-select" id="province" onchange="getProvinces(event)">
												    <option selected value="0">Tỉnh/Thành Phố</option>
												</select>
												<input type="hidden" name="province" id="inputProvince">
                                                <span class="text-danger">${messageProvince}</span>
                                            </div>
                                        </div>
                                        <div class="input-box ">
                                            <div class="input-giua"> 
                                                <select class="form-select" id="district" onchange="getDistricts(event)">
												    <option selected value="0">Quận/Huyện</option>
												</select>
												<input type="hidden" name="district" id="inputDistrict">
                                                <span class="text-danger">${messageDistrict}</span>
                                            </div>
                                        </div>
                                        <div class="input-box ">
                                            <div class="input-giua"> 
                                                <select class="form-select" id="ward" onchange="getWard(event)">
												    <option selected value="0">Xã/Phường</option>
												</select>
												<input type="hidden" name="ward" id="inputWard">
                                                <span class="text-danger">${messageWard}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center">
                                    <div class="div-input">
                                        <div class="input-box">
                                            <div class="input-cuoi"> 
                                                <i class="fa-solid fa-house"></i>
                                                <input type="text" placeholder="Địa chỉ cụ thể" name="diaChiCuThe" value="${valueDiaChiCuthe}"><br>
                                                <span class="text-danger">${messageDiaChiCuThe}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center" >
                                    <div class="" style="width: 700px;">
                                        <input type="checkbox" name="macDinhDC" value="true" ${diaChi.trangThai?'checked':''} id=""> Đặt làm địa chỉ mặc định
                                    </div>   
                                </div>
                                <div class="d-flex justify-content-center mt-4 div-button">
                                    <div class="d-flex justify-content-start">
                                        <button formaction="${request.contextPath}/dia-chi/them-moi" formmethod="post" class="btn btn-success rounded-3 fw-bold" ${checkBtn?'disabled':''}>Thêm mới</button>
                                        <button formaction="${request.contextPath}/dia-chi/cap-nhat" formmethod="post" class="btn btn-primary rounded-3 fw-bold" ${checkBtn?'':'disabled'}>Cập nhật</button>
                                        <button formaction="${request.contextPath}/dia-chi/lam-moi" formmethod="get" class="btn rounded-3 fw-bold text-light" style="background-color: #656565;" >Làm mới</button>
                                    </div>
                                   
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="d-flex justify-content-center">
                    <img src="${request.contextPath}/image/crush-chips.png" alt="">
                </div>
            </div>
            <div class="col-md-12 ms-5 me-4 p-0 mt-4 mb-5">
                <div class="fs-2">Danh sách địa chỉ </div>
                <div class="card card-list">
                    <div class="card-body">
                        <table class="table">
                            <thead>
                                <tr class="row">
                                    <th class="col-md-3 text-center">Họ và Tên</th>
                                    <th class="col-md-2 text-center">Số điện thoại</th>
                                    <th class="col-md-4 text-center">Địa chỉ</th>
                                    <th class="col-md-1 text-center"></th>
                                    <th class="col-md-2 text-center"></th>
                                </tr> 
                            </thead>
                            <tbody>   
                            	<c:forEach var="d" items="${listDiaChi}">
	                                <tr class="row">
	                                    <td class="col-md-3 text-center ">${d.hoVaTen}</td>
	                                    <td class="col-md-2 text-center ">${d.soDienThoai}</td>
	                                    <td class="col-md-4 text-center ">${d.diaChi}</td>
	                                    <td class="col-md-1 text-center ">
	                                    <c:if test="${d.trangThai}">
	                                    	<button class="btn btn-success">Mặc định</button>
	                                   </c:if>	
	                                    </td>
	                                    <td class="col-md-2 text-center ">
	                                        <a href="${request.contextPath}/dia-chi/edit/${d.maDiaChi}"><i class="fa-solid fa-pencil" style="color: #0091ff;"></i></a> <i>|</i>
	                                        <a data-bs-toggle="modal" data-bs-target="#xoaDiaChi" data-name="${d.diaChi}" data-id="${d.maDiaChi}"><i class="fa-solid fa-trash-can" style="color: #ff0000;"></i></a>
	                                    </td>
	                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <div class="modal fade" id="xoaDiaChi" tabindex="-1" aria-labelledby="xoaDiaChiLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h1 class="modal-title fs-5" id="xoaDiaChiLabel">Thông báo</h1>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            <div class="modal-body">
	             	Bạn có muốn xóa địa chỉ <span id="valueModal"></span> này không? 
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Thoát</button>
	                <button type="button" class="btn btn-primary" id="deleteModal">Xác nhận</button>
	            </div>
	        </div>
	    </div>
	</div>
     <!-- Footer -->
    <%@ include file="footer.jsp" %>
    <!-- End Footer -->
    
    <script type="text/javascript">
        let provinceLet;
        let districtLet;
        let wardLet;
        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', {
            headers: {
                'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead' // Replace with your actual API key
            }
        })
            .then(response => response.json())
            .then(data => {
                let provinces = data.data;
                let valueProvince = '${valueProvince}';
                provinces.map(value => {
                    const option = document.createElement('option');
                    option.text = value.ProvinceName;
                    option.value = value.ProvinceID;
                    if(value.ProvinceName == valueProvince){
                    	option.selected = true;
                        document.getElementById('inputProvince').value = value.ProvinceName;

                    }
                    document.getElementById('province').appendChild(option);
                })
                provinceLet = provinces;
                if(valueProvince !== ""){
                    document.getElementById('province').dispatchEvent(new Event('change')); 
                }
  
            })
            .catch(error => {
                console.log(error)
                console.log("Lỗi gọi API!!!")
            });

        function fetchDistrict(provinceID) {
        	// Xóa tất cả các quận/huyện cũ trong thẻ <select>
            document.getElementById('district').innerHTML = '<option selected value="0">Quận/Huyện</option>';

            fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id='+provinceID, {
                headers: {
                    'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead' // Replace with your actual API key
                }
            })
                .then(response => response.json())
                .then(data => {
                    let district = data.data;
                    let valueDistrict = '${valueDistrict}';
                    district.map(value => {
                        const option = document.createElement('option');
                        option.text = value.DistrictName;
                        option.value = value.DistrictID;
                        if(value.DistrictName == valueDistrict){
                        	option.selected = true;
                            document.getElementById('inputDistrict').value = value.DistrictName;
                        }
                        document.getElementById('district').appendChild(option);
                    })
                    districtLet = district;
                    if(valueDistrict !== ""){
                    	 document.getElementById('district').dispatchEvent(new Event('change'));
                    }
                   
                 })
                .catch(error => {
                    console.log(error)
                    console.log("Lỗi gọi API!!!")
                });
        }

        function fetchWard(districtId) {
        	// Xóa tất cả các xã/phường cũ trong thẻ <select>
            document.getElementById('ward').innerHTML = '<option selected value="0">Xã/Phường</option>';

            fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id='+districtId, {
                headers: {
                    'Token': 'bab9fb8c-2323-11ef-a951-76299e96dead' // Replace with your actual API key
                }
            })
                .then(response => response.json())
                .then(data => {
                    let ward = data.data;
                    let valueWard = '${valueWard}';
                    ward.map(value => {
                        const option = document.createElement('option');
                        option.text = value.WardName;
                        option.value = value.WardCode;
                        if(value.WardName == valueWard){
                        	option.selected = true;
                            document.getElementById('inputWard').value = value.WardName;
                        }
                        document.getElementById('ward').appendChild(option);
                    })
                    wardLet = ward;
                   
                })
                .catch(error => {
                    console.log(error)
                    console.log("Lỗi gọi API!!!")
                });
        }

        
        function getProvinces(event){
            fetchDistrict(event.target.value);
            provinceLet.map(value => {
                if(event.target.value == value.ProvinceID){
                    document.getElementById('inputProvince').value = value.ProvinceName;
                }
            })
            if(event.target.value == 0){
                document.getElementById('inputProvince').value = '';
            }
        }
        function getDistricts(event){
            fetchWard(event.target.value);
            districtLet.map(value => {
           		if(event.target.value == value.DistrictID){
                	document.getElementById('inputDistrict').value = value.DistrictName;
                }
            })
            if(event.target.value == 0){
                document.getElementById('inputDistrict').value = '';
            }
        }

        function getWard(event){
            wardLet.map(value => {
            	if(event.target.value == value.WardCode){
                	document.getElementById('inputWard').value = value.WardName;
                }
            })
            if(event.target.value == 0){
                document.getElementById('inputWard').value = '';
            }
        }
        document.addEventListener('DOMContentLoaded', (event) => {
	        var modal = document.getElementById('xoaDiaChi');
	        var deleteButton = document.getElementById('deleteModal');
			var deleteAllButton = document.getElementById('deleteAllModal');
	        
	        modal.addEventListener('show.bs.modal', function (event) {
	            var triggerButton = event.relatedTarget; // Button that triggered the modal
	            var addressId = triggerButton.getAttribute('data-id'); // Extract info from data-* attributes
	            var addressName = triggerButton.getAttribute('data-name'); // Extract info from data-* attributes
	            
	            document.getElementById('valueModal').innerText = addressName;
	            // Set the data-id attribute on the delete button
	            deleteButton.setAttribute('data-id', addressId);
	        });
	
	        deleteButton.addEventListener('click', (event) => {
	            const addressId = deleteButton.getAttribute('data-id');	
	            if (addressId) {
	                // Perform the delete operation, for example:
	                window.location.href = '/dia-chi/remove/'+addressId;
	            } 
	        });	      
	    });
    </script>
 
    
    
</body>
</html>