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
<title>流程管理</title>

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
		if (confirm("是否删除该角色")) {
			window.location.href = "${pageContext.request.contextPath}/roledel?id="
					+ id;
		}
	}
</script>
</head>
<body>

	<!--路径导航-->
	<ol class="breadcrumb breadcrumb_nav">
		<li>首页</li>
		<li>系统管理</li>
		<li class="active">角色管理</li>
	</ol>
	<!--路径导航-->

	<div class="page-content">
		<div class="panel panel-default">
			<div class="panel-heading">角色列表&nbsp;&nbsp;&nbsp;</div>
			<div class="table-responsive">
				<table class="table table-striped table-hover" id="tb">
					<thead>
						<tr>
							<th width="30%">角色名称</th>
							<th width="50%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="role" items="${allRoles}">
							<tr id="${role.id}">
								<td>${role.name}</td>
								<td><a href="#" onclick="viewPermissions('${role.id}')"
									class="btn btn-info btn-xs"><span
										class="glyphicon glyphicon-edit"></span> 编辑</a> &nbsp;&nbsp;<a
									href="#" onclick="delConf(${role.id})"
									class="btn btn-danger btn-xs"><span
										class="glyphicon glyphicon-remove"></span> 删除</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<!-- 编辑窗口 -->
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="closePanel()">×</button>
					<h3 id="myModalLabel">权限列表</h3>
				</div>
				<form id="permissionForm" action="updateRoleAndPermission"
					method="post">
					<input type="hidden" id="roleId" name="roleId" />
					<div class="modal-body" id="roleList">
						<table class="table table-bordered" width="800px">
							<thead>
								<tr>
									<th width="30%">角色
									</td>
									<th width="70%">权限</th>
								</tr>
							</thead>
							<tbody id="roleListBody">
								<c:forEach var="menu" items="${allMenuAndPermissions}">
									<tr>
										<td><input id="chk${menu.id}" type="checkbox"
											name="permissionIds" value="${menu.id}" /> ${menu.name}</td>
										<td><c:forEach var="permission" items="${menu.children}">
												<input id="chk${permission.id}" type="checkbox"
													name="permissionIds" value="${permission.id}" />
                					${permission.name}[${permission.type}]
                					<br />
											</c:forEach></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-success" data-dismiss="modal"
							aria-hidden="true"
							onclick="javascript:document.getElementById('permissionForm').submit()">保存</button>
						<button class="btn btn-default" onclick="closePanel()">关闭</button>
					</div>
				</form>
			</div>
		</div>
		<script type="text/javascript">
			var role_id;
			function viewPermissions(role_id) {
				$('#roleId').val(role_id);
				$.ajax({
					url : 'loadMyPermissions',
					type : 'post',
					data : {
						roleId : role_id
					},
					dataType : 'json',
					success : function(permissionList) {
						//$("input[type=checkbox]").attr('checked',false);
						$.each(permissionList, function(i, perm) {
							$('#chk' + perm.id).attr('checked', true);
						});
						$("#editModal").modal("show");
					},
					error : function(req, error) {
						alert(req.status + ':' + error);
					}
				});
			}

			function closePanel() {
				location = "findRoles";
			}
		</script>
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