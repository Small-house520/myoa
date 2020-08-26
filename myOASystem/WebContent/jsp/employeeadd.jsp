<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>添加用户</title>

<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="css/content.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

	<!--路径导航-->
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>用户管理</li>
		<li class="active">用户添加</li>
	</ol>
	<!--路径导航-->

	<div class="page-content">
		<div class="panel panel-default">
			<div class="panel-heading">添加用户</div>
			<div class="panel-body">
				<form action="saveEmployee" method="post">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-8">

								<div class="form-group">
									<label>用户名</label> <input type="text" class="form-control"
										id="name" name="name" required placeholder="请输入用户名">
								</div>

								<div class="form-group">
									<label for="col_name">密码</label> <input type="password"
										class="form-control" id="password" name="password"
										required placeholder="请输入密码">
								</div>

								<div class="form-group">
									<label for="seo_title">邮箱</label> <input type="email"
										class="form-control" id="email" name="email"
										required placeholder="请输入邮箱">
								</div>

								<div class="form-group">
									<label for="seo_title">上司</label><br> <select class="span3"
										name="managerId">
										<option value="">请选择上司</option>
										<c:forEach var="employee" items="${employees }">
											<option value="${employee.id}">${employee.name}</option>
										</c:forEach>
									</select>
								</div><br>

								<div class="form-group">
									<button type="submit" class="btn btn-primary">添 加</button>
									&nbsp;&nbsp;
									<button type="reset" class="btn btn-primary">重 填</button>
								</div>

							</div>
						</div>
				</form>
			</div>

		</div>

	</div>
</body>
</html>