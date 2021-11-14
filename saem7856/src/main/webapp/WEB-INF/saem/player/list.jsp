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
.grid-box {
	margin-top: 3px;
	margin-bottom: 5px;
	display: grid;
	/* auto-fill :  남는 공간(빈 트랙)을 그대로 유지, minmax : '최소, 최대 크기'를 정의 */
	grid-template-columns: repeat(auto-fill, minmax(300px, 2fr));
	grid-column-gap: 70px;
	grid-row-gap: 10px;
}

.grid-box .item {
	border: 1px solid #DAD9FF;
	height: 330px;
	width: 280px;
	cursor: pointer;
	align-items: center;
}

.item>img {
	width: 100%;
	height: 100%;
	cursor: pointer;
}
</style>
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

					<div class="body-container">
		<div class="body-title" style="padding-top: 50px;">
			<h1><i class="fas fa-futbol"></i> 선수 정보 </h1>
		</div>



						<table class="table">
							<tr>
								<td align="left" style="font-weight: bold;">EPL</td>

							</tr>
						</table>

						<div class="grid-box"
							style="padding-top: 5px; padding-bottom: 80px; justify-items: center;" >
							<c:forEach var="dto" items="${list}" varStatus="status">
								<c:if test="${dto.league=='EPL'}">
									<div class="item" title="${dto.subject}"
										onclick="location.href='${articleUrl}&num=${dto.num}';">
										<img
											src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.subject}</p>
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.team}</p>
									</div>
								</c:if>
							</c:forEach>
						</div>
						<table class="table" style="padding: 5px auto;">
							<tr>
								<td align="left" style="font-weight: bold;">La Liga</td>
							</tr>
						</table>
						<div class="grid-box"
							style="padding-top: 5px; padding-bottom: 80px;">
							<c:forEach var="dto" items="${list}" varStatus="status">
								<c:if test="${dto.league=='La Liga'}">
									<div class="item" title="${dto.subject}"
										onclick="location.href='${articleUrl}&num=${dto.num}';">
										<img
											src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.subject}</p>
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.team}</p>
									</div>
								</c:if>
							</c:forEach>
						</div>

						<table class="table">
							<tr>
								<td align="left" style="font-weight: bold;">분데스리가</td>
							</tr>
						</table>
						<div class="grid-box"
							style="padding-top: 5px; padding-bottom: 80px;">
							<c:forEach var="dto" items="${list}" varStatus="status">
								<c:if test="${dto.league=='분데스리가'}">
									<div class="item" title="${dto.subject}"
										onclick="location.href='${articleUrl}&num=${dto.num}';">
										<img
											src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.subject}</p>
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.team}</p>
									</div>
								</c:if>
							</c:forEach>
						</div>

						<table class="table">
							<tr>
								<td align="left" style="font-weight: bold;">리그1</td>

							</tr>
						</table>
						<div class="grid-box"
							style="padding-top: 5px; padding-bottom: 80px;">
							<c:forEach var="dto" items="${list}" varStatus="status">
								<c:if test="${dto.league=='리그1'}">
									<div class="item" title="${dto.subject}"
										onclick="location.href='${articleUrl}&num=${dto.num}';">
										<img
											src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.subject}</p>
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.team}</p>
									</div>
								</c:if>
							</c:forEach>
						</div>

						<table class="table">
							<tr>
								<td align="left" style="font-weight: bold;">러시아 프리미어리그</td>

							</tr>
						</table>
						<div class="grid-box"
							style="padding-top: 5px; padding-bottom: 80px;">
							<c:forEach var="dto" items="${list}" varStatus="status">
								<c:if test="${dto.league=='러시아 프리미어리그'}">
									<div class="item" title="${dto.subject}"
										onclick="location.href='${articleUrl}&num=${dto.num}';">
										<img
											src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.subject}</p>
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.team}</p>
									</div>
								</c:if>
							</c:forEach>
						</div>

						<table class="table">
							<tr>
								<td align="left" style="font-weight: bold;">쉬페르리그</td>

							</tr>
						</table>
						<div class="grid-box"
							style="padding-top: 5px; padding-bottom: 80px;">
							<c:forEach var="dto" items="${list}" varStatus="status">
								<c:if test="${dto.league=='쉬페르리그'}">
									<div class="item" title="${dto.subject}"
										onclick="location.href='${articleUrl}&num=${dto.num}';">
										<img
											src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.subject}</p>
										<p style="text-align: center; height: 0; font-weight: bold;">${dto.team}</p>
									</div>
								</c:if>
							</c:forEach>
						</div>

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