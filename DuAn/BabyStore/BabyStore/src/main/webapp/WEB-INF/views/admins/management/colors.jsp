<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Colors</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${request.contextPath}/css/deliveryaddress.css">
</head>
<body class="hold-transition sidebar-mini layout-fixed"> 
	<main class="content-wrapper">
        <div class="row" style="width: 1200px;">
            <div class="col-md-8 d-flex justify-content-center mt-4">
                <div class="card card-form">
                    <div class="card-body">
                        <div class="text-center fs-2 fw-bold">Colors</div>
                        <div class="mt-4">
                            <form method="post">
                                <div class="d-flex justify-content-center">
                                    <div class="d-flex justify-content-between div-input">
                                        <div class="input-box">
                                            <div class="input-dau mg-3">
                                            	<input type="hidden" name="id" value="${color.id}">
										     	<input type="text" name="name" class="form-control" placeholder="Màu" value="${color.name}">
										    </div>
                                        </div>
                                    </div>
                                </div>
                              
                                <div class="d-flex justify-content-center mt-4 div-button">
                                    <div class="d-flex justify-content-start">
                                        <button formaction="${request.contextPath}/admin/color/create" formmethod="post" class="btn btn-success rounded-3 fw-bold" ${checkBtn?'disabled':''}>Thêm mới</button>
                                        <button formaction="${request.contextPath}/admin/color/update" formmethod="post" class="btn btn-primary rounded-3 fw-bold" ${checkBtn?'':'disabled'}>Cập nhật</button>
                                        <button formaction="${request.contextPath}/admin/color/lam-moi" formmethod="get" class="btn rounded-3 fw-bold text-light" style="background-color: #656565;" >Làm mới</button>
                                    </div>
                                   
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3 mt-4">
                <div class="d-flex justify-content-center" style="width: 250px;">
                </div>
            </div>
            <div class="col-md-12 ms-5 me-4 p-0 mt-4 mb-5" style="width: 1100px">
                <div class="fs-2" style="width: 400px">Danh sách địa chỉ </div>
                <div class="card card-list">
                    <div class="card-body">
                        <table class="table">
                            <thead>
	                                <tr class="row">
	                                <th class="col-md-1 text-center">ID</th>
	                                <th class="col-md-2 text-center">Name</th>
	                                <th class="col-md-2 text-center">Color</th>
                                    <th class="col-md-1 text-center"></th>
                                    <th class="col-md-1 text-center"></th>
                                </tr> 
                            </thead>
                            <tbody>   
                           
                            	<c:forEach var="colors" items="${colors}">
                            		 <form action="${request.ContextPath}/admin/color/update" method="post">
		                               		<input value="${acolors.id}" name="id" type="hidden"/>
		                                	<tr class="row">
		                                    <td class="col-md-1 text-center">${colors.id}</td>
		                                    <td class="col-md-2 text-center">${colors.name}</td>
		                                    <td class="col-md-1 text-center ">
		                                        <a href="${request.contextPath}/admin/color/edit/${colors.id}"><i class="fa-solid fa-pencil" style="color: #0091ff;"></i></a> <i>|</i>
		                                        <a data-bs-toggle="modal" data-bs-target="#xoaDiaChi" data-name="${colors.name}" data-id="${colors.id}"><i class="fa-solid fa-trash-can" style="color: #ff0000;"></i></a>
		                                    </td>
		                                </tr>
	                             	  </form>
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
	             	Bạn có muốn xóa màu: <span id="valueModal"></span> này không? 
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Thoát</button>
	                <button type="button" class="btn btn-primary" id="deleteModal">Xác nhận</button>
	            </div>
	        </div>
	    </div>
	</div>
    <script type="text/javascript">
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
	            const colorId = deleteButton.getAttribute('data-id');	
	            if (colorId) {
	                // Perform the delete operation, for example:
	                window.location.href = '/admin/color/remove/'+colorId;
	            } 
	        });	      
	    });
    </script>
</body>
</html>