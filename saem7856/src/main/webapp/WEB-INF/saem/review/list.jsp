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
.search_wrapper {
	display: flex;
	align-items: center;
	margin:0;
	min-width : 350px;
}
</style>
<script type="text/javascript">
function searchList() {
	var f = document.searchForm;
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
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"/>
				</header>
				
				<!-- Section -->						
					<div class="title" style="margin:30px 0 30px 0">
						<h1>리뷰 게시판</h1>
					</div>
					<table >
						<tr>
							<td width="50%">
								<c:if test="${sessionScope.member.userId!='admin'}">
									${dataCount}개(${page}/${total_page} 페이지)
								</c:if>
							</td>
							<td width="20%" align="right">
								<!-- Search -->
								<section id="search" class="alt">
									<form method="post" class="search_wrapper" 
									name="searchForm" action="${pageContext.request.contextPath}/review/list.do">
										<select name="condition" style="width:60%">
											<option value="all"      ${condition=="all"?"selected='selected'":"" }>제목+내용</option>
											<option value="userName" ${condition=="userName"?"selected='selected'":"" }>작성자</option>
											<option value="reg_date"  ${condition=="reg_date"?"selected='selected'":"" }>등록일</option>
											<option value="subject"  ${condition=="subject"?"selected='selected'":"" }>제목</option>
											<option value="content"  ${condition=="content"?"selected='selected'":"" }>내용</option>
										</select>
										<input type="text" name="keyword" id="query" value="${keyword}" placeholder="Search" 
												onkeyup="if(window.event.keyCode==13){searchList()}"/>
									</form>		
								</section>
								<input type="hidden" name="page" value="${page}">
								<input type="hidden" name="condition" value="${condition}">
								<input type="hidden" name="keyword" value="${keyword}">
							</td>
						</tr>
					</table>
				
		        <form name="listForm" method="post">
					<table class="table table-border table-list">
						<tr>
							<c:if test="${sessionScope.member.userId=='admin'}">
								<th class="chk">
									<input type="checkbox" name="chkAll" id="chkAll">        
								</th>
							</c:if>
							<th class="num">번호</th>
							<th class="subject">제목</th>
							<th class="name">작성자</th>
							<th class="date">작성일</th>
							<th class="hit">조회수</th>
						</tr>
						<c:forEach var="dto" items="${list}">
							<tr>
								<c:if test="${sessionScope.member.userId=='admin'}">
									<td>
										<input type="checkbox" name="nums" value="${dto.num}">
									</td>
								</c:if>
								<td>${dto.listNum}</td>	
								<td>
									<a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
									<c:if test="${dto.replyCount!=0}">&nbsp;[${dto.replyCount}]</c:if>
								</td>				
								<td>${dto.userName}</td>
								<td>${dto.reg_date}</td>
								<td>${dto.hitCount}</td>
							</tr>
						</c:forEach>
					</table>
				</form>
				<div class="page-box">
					${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
				</div>
                <div style='padding: 0px 13px 10px 10px; text-align: right;'>
                    <button type='button' onclick="location.href='${pageContext.request.contextPath}/review/write.do';">글쓰기</button>
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