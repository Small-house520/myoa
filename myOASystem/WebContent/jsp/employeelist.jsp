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
<title>用户管理</title>

<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="css/content.css" rel="stylesheet">
<style type="text/css">
th, td {
	text-align: center;
}
</style>

<script src="js/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	//确认删除函数
	function delConf(id) {
		if (confirm("请确定是否删除该员工信息")) {
			window.location.href = "${pageContext.request.contextPath}/employeedelete?id="
					+ id;
		}
	}
</script>
</head>
<body>

	<!--路径导航-->
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>用户管理</li>
		<li class="active">用户列表</li>
	</ol>
	<!--路径导航-->

	<div class="page-content">
		<form class="form-inline">
			<div class="panel panel-default">
				<div class="panel-heading">用户信息列表</div>

				<div class="table-responsive">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th width="15%">员工ID</th>
								<th width="15%">姓名</th>
								<th width="15%">职位</th>
								<th width="30%">Email</th>
								<th width="25%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="employee" items="${employees}">
								<tr>
									<td>${employee.id}</td>
									<td>${employee.name}</td>
									<td>${employee.role}</td>
									<td>${employee.email}</td>
									<td><a
										href="${pageContext.request.contextPath}/employeeadd"
										class="btn btn-success btn-xs"><span
											class="glyphicon glyphicon-plus"></span> 添 加</a>&nbsp;&nbsp; <a
										href="${pageContext.request.contextPath}/employeeedit?id=${employee.id}"
										class="btn btn-info btn-xs"><span
											class="glyphicon glyphicon-edit"></span> 编 辑</a>&nbsp;&nbsp; <a
										href="#" onclick="delConf(${employee.id})"
										class="btn btn-danger btn-xs"><span
											class="glyphicon glyphicon-trash"></span> 删 除</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>

	</div>
</body>
</html>