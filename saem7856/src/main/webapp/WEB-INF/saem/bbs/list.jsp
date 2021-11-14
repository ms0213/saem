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
.search_wrapper {
	display: flex;
	align-items: center;
	margin: 0;
	min-width: 350px;
	float: right;
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
						<table class="table">
							<tr>

								<td width="30%" align="right">
									<form name="searchForm"
										action="${pageContext.request.contextPath}/bbs/list.do"
										method="post" class="search_wrapper">
										<select name="condition" style="width: 35%">
											<option value="all"
												${condition=="all"?"selected='selected'":"" }>제목+내용</option>
											<option value="userName"
												${condition=="userName"?"selected='selected'":"" }>작성자</option>
											<option value="reg_date"
												${condition=="reg_date"?"selected='selected'":"" }>등록일</option>
											<option value="subject"
												${condition=="subject"?"selected='selected'":"" }>제목</option>
											<option value="content"
												${condition=="content"?"selected='selected'":"" }>내용</option>
										</select> <input type="text" name="keyword" value="${keyword}"
											class="boxTF" style="width: 300px;">
										<button type="button" class="btn" onclick="searchList();">검색</button>
									</form>
								</td>

							</tr>
						</table>
						<table class="table table-border table-list">
							<tr>
								<th class="num">번호</th>
								<th class="subject">제목</th>
								<th class="name">작성자</th>
								<th class="date">작성일</th>
								<th class="hit">조회수</th>
							</tr>

							<c:forEach var="dto" items="${listNotice}">
								<tr>
									<td><span class="notice" style="font-weight: bold;">공지</span></td>
									<td class="left" style="font-weight: bold;"><a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
										<c:if test="${dto.replyCount!=0}">(${dto.replyCount})</c:if></td>
									<td>${dto.userName}</td>
									<td>${dto.reg_date}</td>
									<td>${dto.hitCount}</td>
								</tr>
							</c:forEach>

							<c:forEach var="dto" items="${list}">
								<tr>
									<td>${dto.listNum}</td>
									<td class="left"><a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
										<c:if test="${dto.replyCount!=0}">(${dto.replyCount})</c:if></td>
									<td>${dto.userName}</td>
									<td>${dto.reg_date}</td>
									<td>${dto.hitCount}</td>
								</tr>
							</c:forEach>
						</table>
						
						<table>
							<tr>
								<td width="10%">${dataCount}개(${page}/${total_page}페이지)</td>
								
								<td align="left">${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}</td>
								<td width="10%"></td>
							</tr>
						</table>
						
						<div style='padding: 10px 13px 10px 10px; float: left;'>
							<button type="button" class="btn"
								onclick="location.href='${pageContext.request.contextPath}/bbs/list.do';">새로고침</button>
						</div>
						<div style='padding: 0px 13px 10px 10px; float: right;'>
							<c:choose>
								<c:when test="${sessionScope.member.userId=='admin'}">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/bbs/notice.do';">글올리기</button>
								</c:when>
								<c:otherwise>

									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/bbs/write.do';">글올리기</button>

								</c:otherwise>
							</c:choose>
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