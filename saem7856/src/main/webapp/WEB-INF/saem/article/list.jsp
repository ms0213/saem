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
.search_wrapper {
	display: flex;
	align-items: center;
	margin: 0;
	min-width: 350px;
}
.grid-box {
	margin-top: 3px; margin-bottom: 100px;
	display: grid;
	/* auto-fill :  남는 공간(빈 트랙)을 그대로 유지, minmax : '최소, 최대 크기'를 정의 */
	grid-template-columns: repeat(5, minmax(180px, 1fr));
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
  text-align: center;
}
</style>
<script type="text/javascript">
function searchList() {
	if(event.keyCode==13){
		var f = document.searchForm;
		f.submit();
	}
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
					<div class = "title-body" style="margin:30px 0 30px 0">
						<h1>NEWS </h1>
					</div>
				
				<table class="table">
					<tr>
						<td width="50%">
							${dataCount}개(${page}/${total_page} 페이지)
						</td>
							<!-- search -->
						<td align="right">
							<section id ="search" class="alt">
								<form method="post" class="search_wrapper" 
								name="searchForm" action="${pageContext.request.contextPath}/article/list.do">
									<select name="condition" class="selectField" style="width:60%">
										<option value = "all" ${condition=="all"?"selected='selected'":"" }>제목+내용</option>
										<option value = "reg_date" ${condition=="reg_date"?"selected='selected'":""}>등록일</option>
										<option value = "subject" ${condition=="subject"?"selected='selected'":""}>제목</option>
										<option value = "content" ${condition=="content"?"selected='selected'":""}>내용</option>
									</select>
									<input type="text" name="keyword" value="${keyword}" placeholder="Search" onkeyup=" searchList();"/>
								</form>
							</section>
							<input type="hidden" name="page" value="${page}">
							<input type="hidden" name="condition" value="${condition}">
							<input type="hidden" name="keyword" value="${keyword}">
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
							<div style="text-align: center;">
								${dto.reg_date}
							</div>
						</div>
					</c:forEach>
				</div>
				
				<div class="page-box">
					${dataCount == 0? "등록된 게시물이 없습니다." : paging}
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