<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp"/>
<style type="text/css">
.grid-box {
	margin-top: 3px; margin-bottom: 5px;
	display: grid;
	/* auto-fill :  남는 공간(빈 트랙)을 그대로 유지, minmax : '최소, 최대 크기'를 정의 */
	grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
	grid-column-gap: 10px;
	grid-row-gap: 10px;
}
.grid-box .item {
	border: 1px solid #DAD9FF; height: 230px; cursor: pointer;
}
.item > img {
  width: 100%; height: 100%; cursor: pointer;
}
</style>
</head>
<body class="is-preload">
	<!--  Wrapper -->
	<div id="wrapper">
	
		<!-- Main -->
		<div id="main">
			<div class="inner">

				<!-- Header -->
				<jsp:include page="/WEB-INF/saem/layout/header.jsp"></jsp:include>
				
				<!-- Section -->						
					<div class="title" style="margin:30px 0 30px 0">
						<h1>포토갤러리</h1>
					</div>
					<table >
						<tr>
							<td width="50%">
								${dataCount}개(${page}/${total_page} 페이지)
							</td>
							<td width="20%" align="right">
								<!-- Search -->
								<section id="search" class="alt">
									<form method="post" action="#"  style="margin:0">
										<input type="text" name="query" id="query" placeholder="Search" />
									</form>
								</section>
								<input type="hidden" name="page" value="${page}">
								<input type="hidden" name="condition" value="${condition}">
								<input type="hidden" name="keyword" value="${keyword}">
							</td>
						</tr>
					</table>

		        <form name="listForm" method="post">
					
				<div class="grid-box">
					<c:forEach var="dto" items="${list}" varStatus="status">
						<div class="item" title="${dto.subject}"
							onclick="location.href='${articleUrl}&num=${dto.num}';">
							<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}">
							<p style="text-align: center;">${dto.subject}</p>
						</div>
					</c:forEach>
				</div>
				</form>
		
				<div class="page-box">
					${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
				</div>
			</div>
		</div>

		<!-- Sidebar -->
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>
			
	</div>

	<!-- Scripts -->	
	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp"/>
</body>
</html>