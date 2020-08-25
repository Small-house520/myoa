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
<title>请假信息</title>

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
		if (confirm("请确定是否删除该条请假记录")) {
			window.location.href = "${pageContext.request.contextPath}/leavebilldel?id="
					+ id;
		}
	}
</script>
</head>
<body>

	<!--路径导航-->
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>请假管理</li>
		<li class="active">我的请假信息</li>
	</ol>
	<!--路径导航-->

	<div class="page-content">
		<form class="form-inline">
			<div class="panel panel-default">
				<div class="panel-heading">请假信息列表</div>

				<div class="table-responsive">
					<table class="table table-striped table-hover"
						style="table-layout: fixed;">
						<thead>
							<tr>
								<th width="15%">请假标题</th>
								<th width="25%"
									style="text-overflow: ellipsis; white-space: nowrap; overflow: hidden;">请假事由</th>
								<th width="15%">申请时间</th>
								<th width="15%">请假天数</th>
								<th width="15%">请假状态</th>
								<th width="15%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="leavebill" items="${leavebills}">
								<tr>
									<td>${leavebill.content}</td>
									<td>${leavebill.remark}</td>
									<td><fmt:formatDate value="${leavebill.leavedate}"
											pattern="yyyy-MM-dd hh:mm:ss" /></td>
									<td>${leavebill.days}</td>
									<c:choose>
										<c:when test="${leavebill.state==1}">
											<td>审核中</td>
										</c:when>
										<c:otherwise>
											<td>已完成</td>
										</c:otherwise>
									</c:choose>

									<td><a href="#" onclick="delConf(${leavebill.id})"
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