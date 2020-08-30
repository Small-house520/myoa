<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>OA办公自动化系统-登录</title>
<link rel="stylesheet" href="static/css/bootstrap.min.css">
<link rel="stylesheet" href="static/css/style1.css">
<link rel="stylesheet" href="static/css/bootstrapValidator.min.css">
<script src="static/js/jquery-1.11.0.min.js"></script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/bootstrapValidator.min.js"></script>

<style type="text/css">
body {
	background-image:
		url("${pageContext.request.contextPath}/static/images/login.jpg");
	background-repeat: no-repeat;
	background-position: center center;
	background-attachment: fixed;
	background-size: cover;
}

.row {
	margin: 30px auto 0;
}

#loginForm {
	background-color: rgba(0, 0, 0, 0.26);
}

#pic1 {
	position: absolute;
	left: 185px;
	top: 100px;
}

#t {
	width: 100%;
	background-color: red;
	height: 30px;
}

#f {
	text-align: center;
}
</style>

<!-- 切换验证码 -->
<script>
	window.onload = function() {
		document.getElementById("img1").onclick = function() {
			this.src = "${pageContext.request.contextPath }/checkcode?time="
					+ new Date().getTime();
		}
	}
</script>

</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-offset-3 col-md-6">

				<form class="form-horizontal" action="login" method="post"
					id="loginForm">
					<span class="heading">欢迎登录办公系统</span>
					<div class="form-group">
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${username!=null }">
									<input type="text" required="required" name="username"
										class="form-control" id="username" value="${username }"
										placeholder="请输入用户名">
									<i class="fa fa-user"></i>
								</c:when>
								<c:otherwise>
									<input type="text" required="required" name="username"
										class="form-control" id="username" placeholder="请输入用户名">
									<i class="fa fa-user"></i>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${password!=null }">
									<input type="password" required="required" name="password"
										class="form-control" id="password" value="${password }"
										placeholder="请输入密码">
									<i class="fa fa-lock"></i>
								</c:when>
								<c:otherwise>
									<input type="password" required="required" name="password"
										class="form-control" id="password" placeholder="请输入密码">
									<i class="fa fa-lock"></i>
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-6">
							<input type="text" required="required" name="checkCode"
								class="form-control" id="password" placeholder="请输入验证码">
							<i class="fa fa-lock"></i>
						</div>
						<div class="col-sm-2">
							<img id="img1"
								src="${pageContext.request.contextPath }/checkcode"
								width="110px" height="42px" alt="验证码飞了">
						</div>
					</div>


					<div class="form-group">

						<div class="main-checkbox">
							<input type="checkbox" checked="checked" name="rememberMe"
								id="checkbox1" /> <label for="checkbox1"></label>
						</div>

						<span class="text">自动登录</span>
						<div class="error">${errorMsg}</div>
						<div class="col-sm-10">
							<button type="submit" id="sub" name="sub" class="btn btn-primary">登
								录</button>
						</div>
					</div>
				</form>
				<div id="f">Copyright © 2020 August by JAVA课程-小房子</div>
			</div>
		</div>
	</div>

</body>
</html>