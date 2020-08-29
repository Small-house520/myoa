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
				<div class="panel-heading">
					用户列表&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" title="新建"
						data-toggle="modal" data-target="#createUserModal">新建用户</button>
				</div>

				<div class="table-responsive">
					<table class="table table-striped table-hover" id="tb">
						<thead>
							<tr>
								<th width="10%">ID</th>
								<th width="10%">用户名</th>
								<th width="20%">邮箱</th>
								<th width="15%">职位</th>
								<th width="15%">上司</th>
								<th width="20%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="user" items="${userList}">
								<tr>
									<td>${user.id}</td>
									<td>${user.name}</td>
									<td>${user.email}</td>
									<td><select class="form-control"
										onchange="assignRole(this.value,'${user.name}')">
											<c:forEach var="role" items="${allRoles}">
												<option value="${role.id}"
													<c:if test="${role.name==user.rolename}">selected</c:if>>${role.name}</option>
											</c:forEach>
									</select></td>
									<td>${user.manager}</td>
									<td><a href="#" onclick="viewPermission('${user.name}')"
										class="btn btn-success btn-xs" data-toggle="modal"
										data-target="#editModal"> <span
											class="glyphicon glyphicon-eye-open"></span> 查看权限
									</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form>
	</div>

	<!--添加用户 编辑窗口 -->
	<div class="modal fade" id="createUserModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form id="permissionForm" action="saveUser" method="post">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h3 id="myModalLabel">编辑用户</h3>
					</div>
					<div class="modal-body">
						<table class="table table-bordered table-striped" width="800px">
							<tr>
								<td>用户名</td>
								<td><input class="form-control" name="name"
									placeholder="请输入用户名"></td>
							</tr>
							<tr>
								<td>密码</td>
								<td><input class="form-control" type="password"
									name="password" placeholder="请输入密码"></td>
							</tr>
							<tr>
								<td>邮箱</td>
								<td><input class="form-control" name="email"
									placeholder="请输入电子邮箱"></td>
							</tr>
							<tr>
								<td>职位</td>
								<td><select class="form-control" name="role"
									onchange="getNextManager(this.value)">
										<c:forEach var="role" items="${allRoles }">
											<option value="${role.id}">${role.name}</option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td>上司</td>
								<td><select id="selManager" class="form-control"
									name="managerId"></select></td>
							</tr>

						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-success" data-dismiss="modal"
							aria-hidden="true"
							onclick="javascript:document.getElementById('permissionForm').submit()">保存</button>
						<button class="btn btn-default" data-dismiss="modal"
							aria-hidden="true">关闭</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- 查看用户角色权限窗口 -->
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h3 id="myModalLabel">权限列表</h3>
				</div>
				<div class="modal-body" id="roleList">
					<table class="table table-bordered" width="800px">
						<thead>
							<tr>
								<th>角色
								</td>
								<th>权限</th>
							</tr>
						</thead>
						<tbody id="roleListBody">
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default" data-dismiss="modal"
						aria-hidden="true">关闭</button>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			//查看当前员工的角色和权限列表
			function viewPermission(user_name) {
				$.ajax({
					url : 'viewPermissionByUser',
					type : 'post',
					data : {
						userName : user_name
					},
					dataType : 'json',
					success : function(sysRole) {
						//先清空原来的内容
						$("#roleListBody").empty();

						var role_td = "<td>" + sysRole.name + "</td>";
						var permission_td = "<td>";
						var permission_list = sysRole.permissionList;
						$.each(permission_list, function(i, perm) {
							permission_td += perm.name + "【" + perm.type
									+ "】 <br/>"
						});
						permission_td += "</td>";

						var roleRow = $("<tr>" + role_td + permission_td
								+ "</tr>");
						$("#roleListBody").append($(roleRow));
					},
					error : function(req, error) {
						alert(req.status + ':' + error);
					}
				});
			}

			//重新分配待办人
			function assignRole(rid, uname) {
				var url = "assignRole";
				var params = {
					roleId : rid,
					userId : uname
				};
				$.getJSON(url, params, function(result) {
					alert(result.msg);
					role = rid;
				});
			}

			$(function () {
				var levelNo = $("select[name=role]").val();
				getNextManager(levelNo);
			})
			
			//根据员工级别查找下一级别主管
			function getNextManager(levelNo) {
				var url = "findNextManager";
				var params = {
					level : levelNo
				};
				$.getJSON(url, params, function(managerList) {
					$("#selManager").empty();
					$.each(managerList, function(i, manager) {
						var opt = $("<option value='"+manager.id+"'>"
								+ manager.name + "</option>");
						$("#selManager").append($(opt));
					});
				});
			}
		</script>
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