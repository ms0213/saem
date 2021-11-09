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
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp" />
<style type="text/css">
.grid-box {
	margin-top: 3px; margin-bottom: 100px;
	display: grid;
	/* auto-fill :  남는 공간(빈 트랙)을 그대로 유지, minmax : '최소, 최대 크기'를 정의 */
	grid-template-columns: repeat(5, minmax(220px, 1fr));
	grid-column-gap: 10px;
	grid-row-gap: 100px;
}
.grid-box .item {
	border: 1px solid #DAD9FF; height: 230px; cursor: pointer;
}
.item > img {
  width: 100%; height: 100%; cursor: pointer;
}

.subject-text {
  display:block;
  white-space:nowrap;
  overflow:hidden;
  text-overflow:ellipsis;
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
				<section>
					<div class = "title-body">
						<span class="article-title"> NEWS </span>
					</div>
				
				<table class="table">
					<tr>
						<td width="50%">
							${dataCount}개(${page}/${total_page} 페이지)
						</td>
						<td align="right">
							<c:if test="${sessionScope.member.userId == 'admin'}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/article/write.do';"> 기사올리기 </button>
							</c:if>
						</td>
					</tr>
				</table>
				
				<div class="grid-box">
					<c:forEach var="dto" items="${list}" varStatus="status">
						<div class="item" title="${dto.subject}"
							onclick="location.href='${articleUrl}&num=${dto.num}';">
							<img src="${pageContext.request.contextPath}/uploads/articlePhoto/${dto.imageFilename}">
							<div class="subject-text">
								${dto.subject}
							</div>
							<div>
								${dto.reg_date}
							</div>
						</div>
					</c:forEach>
				</div>
				
				<div class="page-box">
					${dataCount == 0? "등록된 게시물이 없습니다." : paging}
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