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
		if (confirm("是否删除该条请假记录")) {
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
					<table class="table table-striped table-hover" id="tb">
						<thead>
							<tr>
								<th width="8%">ID</th>
								<th width="12%">请假标题</th>
								<th width="20%">请假事由</th>
								<th width="15%">申请时间</th>
								<th width="10%">请假天数</th>
								<th width="10%">请假状态</th>
								<th width="25%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="leavebill" items="${leavebills}">
								<c:if test="${leavebill.state!=0 }">
									<tr>
										<td>${leavebill.id}</td>
										<td>${leavebill.content}</td>
										<td>${leavebill.remark}</td>
										<td><fmt:formatDate value="${leavebill.leavedate}"
												pattern="yyyy-MM-dd hh:mm:ss" /></td>
										<td>${leavebill.days}</td>
										<c:if test="${leavebill.state==0}">
											<td>未申请</td>
										</c:if>
										<c:if test="${leavebill.state==1}">
											<td>审核中</td>
										</c:if>
										<c:if test="${leavebill.state==2}">
											<td>已完成</td>
										</c:if>

										<td><c:if test="${leavebill.state==1}">
												<a
													href="${pageContext.request.contextPath }/viewHisComment?id=${leavebill.id}&flag=1"
													class="btn btn-success btn-xs"><span
													class="glyphicon glyphicon-eye-open"></span> 审核记录</a>&nbsp;&nbsp;
											<a
													href="${pageContext.request.contextPath}/viewCurrentImageByBill?billId=${leavebill.id}&flag=1"
													target="_blank" class="btn btn-success btn-xs"><span
													class="glyphicon glyphicon-eye-open"></span> 当前流程图</a>
											</c:if> <c:if test="${leavebill.state==2}">
												<a
													href="${pageContext.request.contextPath }/viewHisComment?id=${leavebill.id}&flag=1"
													class="btn btn-success btn-xs"><span
													class="glyphicon glyphicon-eye-open"></span> 审核记录</a>&nbsp;&nbsp;
											<a href="#" onclick="delConf(${leavebill.id})"
													class="btn btn-danger btn-xs"><span
													class="glyphicon glyphicon-remove"></span> 删除</a>
											</c:if></td>

									</tr>
								</c:if>

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