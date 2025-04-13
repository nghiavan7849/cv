
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
</head>
<body class="hold-transition sidebar-mini layout-fixed" >
	<!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
        <section class="content">
        <div style="height: 10px"></div>
			<div class="container-fluid">
			<div class="card card-warning">
				<!-- /.card-header -->
				<div class="card-body">
					<form method="post">
						<div class="row">
							<div class="col-sm-12">
								<a href="/admin/product" style="color: black;">
								<label> <img width="24" height="24" src="https://img.icons8.com/material-rounded/24/back--v1.png" alt="back--v1"/> Trở lại </label> 
								</a>	
								<div class="form-group">
									<label>Tên Loại Sản Phẩm <span style="color:red;">${errorName}</span></label> 
									<input type="text" class="form-control" name="name" value="${category.name}" required="required">
								</div>
								<div class="col-sm-12">
								<button type="submit" formaction="/admin/category/save" class="btn btn-primary">Lưu</button>
								<a type="button" href="/admin/category/reset" class="btn btn-secondary">Làm Mới</a>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
        <div class="row">
          <div class="col-12">
            <div class="card">
              <!-- /.card-header -->
              <div class="card-body">
                <table id="example2" class="table table-bordered table-hover">
                  <thead>
                  <tr>
                    <th>Mã Loại Sản Phẩm</th>
                    <th>Tên Loại Sản Phẩm</th>
                    <th></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="categorys" items="${listCategory}">
                  <tr>
                    <td>${categorys.id}</td>
                    <td>${categorys.name}</td>
                    <td>
                    <a type="button" class="btn btn-success" href="/admin/category/edit?id=${categorys.id}">Sửa</a>
                    <c:if test="${categorys.products[0] == null}">
                    <a type="button" class="btn btn-danger" href="/admin/category/delete?id=${categorys.id} "onclick="confirmDeletion(event, this.href)">Xóa</a>
                    </c:if>
                    </td>
                  </tr>
                  </c:forEach>

                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            </div>
            </div>
            </div>
            </section>
  </div>
  <!-- /.content-wrapper -->
  <script type="text/javascript">
  function confirmDeletion(event, url) {
      event.preventDefault(); // Ngăn chặn điều hướng mặc định
      if (confirm("Bạn có chắc chắn muốn xóa không?")) {
          window.location.href = url; // Điều hướng tới URL nếu xác nhận
      }
  }</script>
</body>
</html>
