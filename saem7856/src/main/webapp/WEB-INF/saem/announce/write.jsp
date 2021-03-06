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
.table-form td {
	padding: 7px 0;
}

.table-form p {
	line-height: 200%;
}

.table-form tr>td:first-child {
	width: 110px;
	text-align: center;
	background: #eee;
}

.table-form tr>td:nth-child(2) {
	padding-left: 10px;
}

.table-form input[type=text], .table-form input[type=file], .table-form textarea
	{
	width: 96%;
}

.table-form input[type=checkbox] {
	vertical-align: middle;
}
</style>

<script type="text/javascript">
	function sendOk() {
		var f = document.noticeForm;
		var str;

		str = f.subject.value.trim();
		if (!str) {
			alert("제목을 입력하세요. ");
			f.subject.focus();
			return;
		}

		str = f.content.value.trim();
		if (!str) {
			alert("내용을 입력하세요. ");
			f.content.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/announce/${mode}_ok.do";
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

				<div class="body-container" style="width: 700px;">
					<div class="body-title">
						<h3>
							<i class="fas fa-clipboard-list"></i> 공지사항
						</h3>
					</div>

					<form name="noticeForm" method="post" enctype="multipart/form-data">
						<table class="table table-border table-form">
							<tr>
								<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
								<td><input type="text" name="subject" maxlength="100"
									class="boxTF" value="${dto.subject}"></td>
							</tr>

							<tr>
								<td>작성자</td>
								<td>
									<p>관리자</p>
								</td>
							</tr>

							<tr>
								<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
								<td><textarea name="content" class="boxTA">${dto.content}</textarea>
								</td>
							</tr>
						</table>

						<table class="table">
							<tr>
								<td align="center">
									<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
									<button type="reset" class="btn">다시입력</button>
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/announce/list.do?rows=${rows}';">${mode=='update'?'수정취소':'등록취소'}</button>
									<input type="hidden" name="rows" value="${rows}"> <c:if
										test="${mode=='update'}">
										<input type="hidden" name="num" value="${dto.anum}">
										<input type="hidden" name="page" value="${page}">
									</c:if>
								</td>
							</tr>
						</table>
					</form>

				</div>

			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>