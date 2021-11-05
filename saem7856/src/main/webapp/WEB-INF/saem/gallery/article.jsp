<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp" />
<style type="text/css">
.ui-widget-header { /* 타이틀바 */
	background: none;
	border: none;
	border-bottom: 1px solid #ccc;
	border-radius: 0;
}

.ui-dialog .ui-dialog-title {
	padding-top: 5px;
	padding-bottom: 5px;
}

.ui-widget-content { /* 내용 */
	/* border: none; */
	border-color: #ccc;
}

.table-article tr>td {
	padding-left: 5px;
	padding-right: 5px;
}

.img-box {
	max-width: 1140px;
	padding: 5px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	display: flex;
	flex-direction: row;
	flex-wrap: nowrap;
	overflow-x: auto;
}

.img-box img {
	width: 100px;
	height: 100px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}

.photo-layout img {
	width: 570px;
	height: 450px;
}
</style>
<script type="text/javascript">
function deletePhoto() {
	if (confirm("게시글을 삭제 하시 겠습니까 ? ")) {
		var query = "num=${dto.num}&page=${page}";
		var url = "${pageContext.request.contextPath}/photo/delete.do?"
				+ query;
		location.href = url;
	}
}

function imageViewer(img) {
	var viewer = $(".photo-layout");
	var s = "<img src='"+img+"'>";
	viewer.html(s);

	$(".dialog-photo").dialog({
		title : "image",
		width : 600,
		height : 530,
		modal : true
	});
}
</script>
</head>

<body class="is-preload">
	<!--  Wrapper -->
	<div id="wrapper">

		<!-- Main -->
		<div id="main">
			<div class="inner">

				<!-- Header -->
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				</header>

				<!-- Section -->
				<div class="title" style="margin: 30px 0 30px 0">
					<h1>포토갤러리</h1>
					<div class="title" style="margin: 30px 0 30px 0">
						<div class="body-title">
							<h3>
								<i class="far fa-image"></i> 포토 앨범
							</h3>
						</div>

						<table class="table table-border table-article">
							<tr>
								<td colspan="2" align="center">${dto.subject}</td>
							</tr>

							<tr>
								<td align="right">${dto.reg_date}</td>
							</tr>
							
							<tr>
								<td colspan="2" height="110" style="background-color: white;">
									<div class="img-box">
										<c:forEach var="vo" items="${listFile}">
											<img
												src="${pageContext.request.contextPath}/uploads/photo/${vo.imageFilename}"
												onclick="imageViewer('${pageContext.request.contextPath}/uploads/photo/${vo.imageFilename}');">
										</c:forEach>
									</div>
									<p  style="text-align: center;">(사진을 클릭하면 커집니다.)</p>
									<p>${dto.content}</p>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="background-color: white;">다음글 : <c:if test="${not empty preReadDto}">
										<a
											href="${pageContext.request.contextPath}/photo/article.do?num=${preReadDto.num}&page=${page}">${preReadDto.subject}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="background-color: white;">이전글 : <c:if test="${not empty nextReadDto}">
										<a
											href="${pageContext.request.contextPath}/photo/article.do?num=${nextReadDto.num}&page=${page}">${nextReadDto.subject}</a>
									</c:if>
								</td>
							</tr>
						</table>
						<table class="table">
							<tr>
								<td width="50%">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/photo/update.do?num=${dto.num}&page=${page}';">수정</button>
									<button type="button" class="btn" onclick="deletePhoto();">삭제</button>
								</td>
								<td align="right">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/photo/list.do?page=${page}';">리스트</button>
								</td>
							</tr>
						</table>
						<div class="dialog-photo">
							<div class="photo-layout"></div>
						</div>
					</div>
				</div>
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