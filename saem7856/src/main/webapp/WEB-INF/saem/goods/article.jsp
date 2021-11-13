<%-- 작업중 --%>
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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/slider/css/slider.min.css">
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

.img-box img {
	width: 600px;
	height: auto;
	cursor: pointer;
}

.photo-layout img {
	width: 570px;
	height: 450px;
}
</style>
<script type="text/javascript">
	function deletePhoto() {
		if (confirm("게시글을 삭제하시겠습니까?")) {
			var query = "num=${dto.num}&page=${page}";
			var url = "${pageContext.request.contextPath}/goods/delete.do?"
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

	$(function() {
		$(".slider").slider({
			speed : 500,
			delay : 2500
		/* ,paginationType : 'thumbnails' */// 아래부분에 작은 이미지 출력
		});
	});
	
	function sendList() {
		var f = document.sendForm;
		f.action = "${pageContext.request.contextPath}/review/list.do?num=${dto.num}&page=${page}";
		f.submit();
	}
	
	function sendWrite() {
		var f = document.sendForm;
		f.action = "${pageContext.request.contextPath}/review/write.do?num=${dto.num}";
		f.submit();
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
					<h1>
						<i class="far fa-image"></i> 굿즈 소개
					</h1>
					<div class="title" style="margin: 30px 0 30px 0">

						<table class="table table-border table-article">
							<tr>
								<td colspan="2" align="center">${dto.subject}</td>
							</tr>

							<tr>
								<td align="right">${dto.reg_date}| 조회 ${dto.hitCount}</td>
							</tr>

							<tr>
								<td colspan="2" style="background-color: white;"><c:choose>
										<c:when test="${listFile.size() > 1}">
											<ul class="slider">
												<c:forEach var="dto" items="${listFile}">
													<li data-num="${dto.num}"><img
														src="${pageContext.request.contextPath}/uploads/goods/${dto.imageFilename}"
														title="${dto.subject}" alt="${dto.subject}"
														onclick="imageViewer('${pageContext.request.contextPath}/uploads/goods/${dto.imageFilename}');"></li>
												</c:forEach>
											</ul>
										</c:when>
										<c:otherwise>
											<div class="img-box" align="center">
												<c:forEach var="dto" items="${listFile}">
													<img
														src="${pageContext.request.contextPath}/uploads/goods/${dto.imageFilename}"
														title="${dto.subject}" alt="${dto.subject}"
														onclick="imageViewer('${pageContext.request.contextPath}/uploads/goods/${dto.imageFilename}');">
												</c:forEach>
											</div>
										</c:otherwise>
									</c:choose>
									<p style="text-align: center;">(사진을 클릭하면 새창으로 열립니다.)</p>
									<p>${dto.content}</p>
									<form name="sendForm" method="post" enctype="multipart/form-data">
										<p style="text-align: center;">
											<button type="button" class="btn"
												onclick="sendList();">리뷰보기</button>
											<button type="button" class="btn"
												onclick="sendWrite();">리뷰쓰기</button>
										</p>
										<input type="hidden" name="gdsNum" value="${dto.num}">
									</form>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="background-color: white;">다음글 : <c:if
										test="${not empty preReadDto}">
										<a
											href="${pageContext.request.contextPath}/goods/article.do?num=${preReadDto.num}&page=${page}">${preReadDto.subject}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="background-color: white;">이전글 : <c:if
										test="${not empty nextReadDto}">
										<a
											href="${pageContext.request.contextPath}/goods/article.do?num=${nextReadDto.num}&page=${page}">${nextReadDto.subject}</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<td width="50%"><c:choose>
										<c:when test="${sessionScope.member.userId=='admin'}">
											<button type="button" class="btn"
												onclick="location.href='${pageContext.request.contextPath}/goods/update.do?num=${dto.num}&page=${page}';">수정</button>
										</c:when>
										<c:otherwise>
											<button type="button" class="btn" disabled="disabled">수정</button>
										</c:otherwise>
									</c:choose> <c:choose>
										<c:when test="${sessionScope.member.userId=='admin'}">
											<button type="button" class="btn" onclick="deletePhoto();">삭제</button>
										</c:when>
										<c:otherwise>
											<button type="button" class="btn" disabled="disabled">삭제</button>
										</c:otherwise>
									</c:choose></td>
								<td align="right">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/goods/list.do?page=${page}';">리스트</button>
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
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/slider/js/slider.js"></script>
</body>
</html>