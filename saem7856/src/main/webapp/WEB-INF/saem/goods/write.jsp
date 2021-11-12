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
<link rel="stylesheet" href="assets/css/main.css" />
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

.img-box {
	max-width: 600px;
	padding: 5px;
	box-sizing: border-box;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}

.img-box img {
	width: 37px;
	height: 37px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	function sendOk() {
		var f = document.photoForm;
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

		var mode = "${mode}";
		if ((mode === "write") && (!f.selectFile.value)) {
			alert("이미지 파일을 추가 하세요. ");
			f.selectFile.focus();
			return;
		}

		f.action = "${pageContext.request.contextPath}/goods/${mode}_ok.do";
		f.submit();
	}

	<c:if test="${mode=='update'}">
	function deleteFile(fileNum) {
		if (!confirm("이미지를 삭제 하시겠습니까 ?")) {
			return;
		}

		var query = "num=${dto.num}&fileNum=" + fileNum + "&page=${page}";
		var url = "${pageContext.request.contextPath}/goods/deleteFile.do?"
				+ query;
		location.href = url;
	}
	</c:if>
</script>
</head>
<body class="is-preload">
	<div id="wrapper">
		<div id="main">
			<div class="inner">
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				</header>
				<main>
					<div class="body-container">
						<div class="body-title">
							<h3>
								<i class="far fa-image"></i> 굿즈 등록
							</h3>
						</div>

						<form name="photoForm" method="post" enctype="multipart/form-data">
							<table class="table table-border table-form">
								<tr>
									<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
									<td><input type="text" name="subject" maxlength="100"
										class="boxTF" value="${dto.subject}"></td>
								</tr>
								<tr>
									<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
									<td><textarea name="content" class="boxTA">${dto.content}</textarea>
									</td>
								</tr>

								<tr>
									<td>이미지</td>
									<td><input type="file" name="selectFile" accept="image/*"
										multiple="multiple" class="boxTF"></td>
								</tr>

								<c:if test="${mode=='update'}">
									<tr>
										<td>등록이미지</td>
										<td>
											<div class="img-box">
												<c:forEach var="vo" items="${listFile}">
													<img
														src="${pageContext.request.contextPath}/uploads/goods/${vo.imageFilename}"
														onclick="deleteFile('${vo.fileNum}');">
												</c:forEach>
											</div>
										</td>
									</tr>
								</c:if>

							</table>

							<table class="table">
								<tr>
									<td align="center">
										<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
										<button type="reset" class="btn">다시입력</button>
										<button type="button" class="btn"
											onclick="location.href='${pageContext.request.contextPath}/goods/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
										<c:if test="${mode=='update'}">
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

		<!-- Sidebar -->
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>
	</div>
	<!-- Scripts -->
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp" />

</body>
</html>