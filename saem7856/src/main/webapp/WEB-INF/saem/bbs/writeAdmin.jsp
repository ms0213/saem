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
</style>

<script type="text/javascript">
	function sendOk() {
		var f = document.boardForm;
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

		f.action = "${pageContext.request.contextPath}/bbs/${mode}_ok.do";
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
				<main>
				<div class="body-container" style="padding-top: 30px;">
		<div class="title" style="margin: 30px 0 30px 0">
							<h1>
								<i></i> 자유 게시판
							</h1>
						</div>

						<form name="boardForm" method="post">
							<table class="table table-border table-form">
								<colgroup>
									<col width="10%" />
									<col width="90%" />
								</colgroup>
								<tr>
									<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
									<td><input type="text" name="subject" maxlength="100"
										class="boxTF" value="${dto.subject}"></td>
								</tr>

								<tr>
									<td style="height: 10px;">공지여부</td>
									<td>
										<p style="height: 10px;">
											<input type="checkbox" name="notice" id="notice" value="1"
												${dto.notice==1 ? "checked='checked' ":"" }> <label
												for="notice">공지</label>
										</p>
									</td>
								</tr>

								<tr>
									<td>작성자</td>
									<td>
										<p style="height: 10px;">${sessionScope.member.userName}</p>
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
										<button type="button" class="btn" onclick="sendOk();">${mode=='updateN'?'수정완료':'등록하기'}</button>
										<button type="reset" class="btn">다시입력</button>
										<button type="button" class="btn"
											onclick="location.href='${pageContext.request.contextPath}/bbs/list.do';">${mode=='updateN'?'수정취소':'등록취소'}</button>
										<c:if test="${mode=='updateN'}">
											<input type="hidden" name="num" value="${dto.num}">
											<input type="hidden" name="page" value="${page}">
										</c:if>
									</td>
								</tr>
							</table>

						</form>

					</div>
				</main>
			</div>
		</div>
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>

	</div>
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />
</body>
</html>