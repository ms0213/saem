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
	grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
	grid-column-gap: 10px;
	grid-row-gap: 10px;
}

.grid-box .item {
	border: 1px solid #DAD9FF;
	height: 230px;
	cursor: pointer;
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
	<div class="body-container" style="width: 800px;">
		<div class="body-title">
			<h3><i class="far fa-image"></i> 선수 정보 </h3>
		</div>
        
		<table class="table">
			<tr>
				<td align="right">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/player/write.do';">선수 등록</button>
				</td>
			</tr>
		</table>
	
		<div class="grid-box">
			<c:forEach var="dto" items="${list}" varStatus="status">
				<div class="item" title="${dto.subject}"
					onclick="location.href='${articleUrl}&num=${dto.num}';">
					<img src="${pageContext.request.contextPath}/uploads/player/${dto.imageFilename}">
					<p style="text-align: center;">${dto.subject}</p>
					<p style="text-align: center;">${dto.team}</p>
				</div>
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