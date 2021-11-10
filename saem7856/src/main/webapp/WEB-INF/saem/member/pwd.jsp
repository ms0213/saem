<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp" />

<style type="text/css">

table {
	margin: 0 auto;
}

table tbody tr {
	border: none;
	background-color: rgba(255, 255, 255, 0);
}

table tbody tr:nth-child(2n+1) {
	background-color: rgba(255, 255, 255, 0);
}

.title-body {
	padding: 10px 0; text-align: center;
}

.title-body .article-title {
	font-weight: bold; font-size: 27px; cursor: pointer;
}

.form-body {
	margin: 30px auto 30px;
	width: 50%;
	padding: 10px;
	min-height: 200px; 
}

.form-body {
	text-align: center;
}

.form-body .lbl {
	position: absolute;
	margin: 10px 0 20px 10px;
	color: #999;
}

.form-body .inputTF {
	width: 100%;
	height: 45px;
	padding: 5px;
	padding-left: 15px;
	border:1px solid #666;
}

.form-body a {
	color: #999
}

.msg-box {
	text-align: center; color: #f56a6a;
}

</style>

<script type="text/javascript">
function sendOk() {
	var f = document.pwdForm;

	var str = f.userPwd.value;
	if(!str) {
		alert("패스워드를 입력하세요. ");
		f.userPwd.focus();
		return;
	}

	f.action = "${pageContext.request.contextPath}/member/pwd_ok.do";
	f.submit();
}
</script>

</head>
<body class="is-preload">

	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<!-- Header -->
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				</header>
				
				<section>
					<div class="title-body">
						<span class="article-title">패스워드 재확인</span>
					</div>
					
					<div class="form-body">
						<form name="pwdForm" method="post" action="">
							<table class="table form-table">
								<tr>
									<td align="center">
										정보보호를 위해 패스워드를 다시 한 번 입력해주세요.
									</td>
								</tr>
								<tr align="center">
									<td>
										<input type="text" name="userId" class="inputTF"
											tabindex="1"
											value="${sessionScope.member.userId}"
											readonly="readonly">
									</td>
								</tr>
								<tr align="center">
									<td>
										<input type="password" name="userPwd" class="inputTF" maxlength="20"
											placeholder="패스워드" 
											tabindex="2">
									</td>
								</tr>
								<tr align="center">
									<td>
										<button type="button" onclick="sendOk();" class="btnConfirm">로그인</button>
										<input type="hidden" name="mode" value="${mode}">
									</td>
								</tr>
							</table>
						
							<table class="table">
								<tr>
									<td class="msg-box">${message}</td>
								</tr>
							</table>
						</form>
					</div>
				
				</section>

			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>