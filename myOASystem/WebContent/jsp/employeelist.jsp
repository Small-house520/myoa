<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>用户管理</title>

<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="css/content.css" rel="stylesheet">

<style type="text/css">
th, td {
	text-align: center;
}
</style>

<script src="js/jquery-2.1.0.js"></script>
<script src="bootstrap/bootstrap.min.js"></script>

<link href="bootstrap/css/bootstrap-table.min.css" rel="stylesheet">
<script src="bootstrap/js/bootstrap-table.min.js"></script>
<script src="bootstrap/js/bootstrap-table-zh-CN.min.js"></script>
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
					<table class="table table-striped table-hover" id="tb">
						<thead>
							<tr>
								<th width="15%">员工ID</th>
								<th width="15%">姓名</th>
								<th width="15%">职位</th>
								<th width="30%">Email</th>
								<th width="25%"><shiro:hasPermission name="user:add">操作</shiro:hasPermission></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="employee" items="${employees}">
								<tr>
									<td>${employee.id}</td>
									<td>${employee.name}</td>
									<td>${employee.role}</td>
									<td>${employee.email}</td>
									<td><shiro:hasPermission name="user:add">
											<a
												href="${pageContext.request.contextPath}/employeeedit?id=${employee.id}"
												class="btn btn-info btn-xs"><span
												class="glyphicon glyphicon-edit"></span> 编 辑</a>&nbsp;&nbsp; <a
												href="#" onclick="delConf(${employee.id})"
												class="btn btn-danger btn-xs"><span
												class="glyphicon glyphicon-trash"></span> 删 除</a>
										</shiro:hasPermission></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>

	</div>
</body>

<script>
	//实现表格分页
    $("#tb").bootstrapTable({
    	//点击行事件,element为被点击行的tr元素对象
        onClickRow: function (row, $element) {
            $element.each(function () {
                //获取所有td的值
                var tds = $(this).find("td")
                /* var id = tds.eq(0).text()
                var title = tds.eq(1).text()
                var remark = tds.eq(2).text()
                var money = tds.eq(3).text()
                var creatdate = tds.eq(4).text()
                var state = tds.eq(5).text() */
            })
        },
        pageNumber: 1,			//首页页码
        pagination: true,   	//是否显示分页条
        pageSize: 5,         	//默认一页显示的行数
        paginationLoop: false,  //是否开启分页条无限循环，最后一页时点击下一页是否转到第一页
        pageList: [5,10,20],   	//选择每页显示多少行
        search: true,			//启用关键字搜索框
        sortable: true	 		// 是否启用排序
        
    });
</script>

</html>