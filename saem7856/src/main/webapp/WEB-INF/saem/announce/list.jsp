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
.table-list tr:first-child {
	background: #eee;
}

.table-list th, .table-list td {
	text-align: center;
}

.table-list .notice {
	display: inline-block;
	padding: 1px 3px;
	background: #ed4c00;
	color: #fff;
}

.table-list .left {
	text-align: left;
	padding-left: 5px;
}

.table-list .chk {
	width: 40px;
	color: #787878;
}

.table-list .num {
	width: 60px;
	color: #787878;
}

.table-list .subject {
	color: #787878;
}

.table-list .name {
	width: 100px;
	color: #787878;
}

.table-list .date {
	width: 100px;
	color: #787878;
}

.table-list .hit {
	width: 70px;
	color: #787878;
}

.table-list input[type=checkbox] {
	vertical-align: middle;
}

td a {
	color: black;
	border-bottom: none;
}

.search_wrapper {
	display: flex;
	align-items: center;
	margin: 0;
	min-width: 350px;
}
</style>

<script type="text/javascript">
	function changeList() {
		var f = document.listForm;
		f.action = "${pageContext.request.contextPath}/announce/list.do";
		f.submit();
	}

	function searchList() {
		var f = document.listForm;
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
				<div class="body-container">
					<div class="body-title" style="margin: 30px 0 30px 0">
						<h3>
							<i class="fas fa-clipboard-list"></i> 공지사항
						</h3>
					</div>

					<form method="post" name="listForm" class="search_wrapper"
						action="${pageContext.request.contextPath}/announce/list.do">
						<table class="table">
							<tr>
								<td width="20%">${dataCount}개(${page}/${total_page}페이지)</td>
								<td width="20%" align="center"><c:if
										test="${dataCount!=0 }">
										<select name="rows" class="selectField"
											onchange="changeList();">
											<option value="5" ${rows==5 ? "selected='selected' ":""}>5개씩
												출력</option>
											<option value="10" ${rows==10 ? "selected='selected' ":""}>10개씩
												출력</option>
											<option value="20" ${rows==20 ? "selected='selected' ":""}>20개씩
												출력</option>
											<option value="30" ${rows==30 ? "selected='selected' ":""}>30개씩
												출력</option>
											<option value="50" ${rows==50 ? "selected='selected' ":""}>50개씩
												출력</option>
										</select>
									</c:if> 
								</td>
								<td align="right">
									<section id="search" class="alt">

										<select name="condition" class="selectField" style="width: 150px; display: inline-block;">
											<option value="all"
												${condition=="all"?"selected='selected'":"" }>제목+내용</option>
											<option value="reg_date"
												${condition=="reg_date"?"selected='selected'":"" }>등록일</option>
											<option value="subject"
												${condition=="subject"?"selected='selected'":"" }>제목</option>
											<option value="content"
												${condition=="content"?"selected='selected'":"" }>내용</option>
										</select>
										 <input type="text" name="keyword" value="${keyword}"
											class="boxTF" style="width: 100px; display: inline-block;">		
											 
										<button type="button" class="btn" style="display: inline-block;" onclick="searchList();">검색</button>

									</section> 
								</td>
							</tr>
						</table>
					</form>
					<table class="table table-border table-list">
						<tr>
							<th class="num">번호</th>
							<th class="subject">제목</th>
							<th class="name">작성자</th>
							<th class="date">작성일</th>
							<th class="hit">조회수</th>
						</tr>

						<c:forEach var="dto" items="${list}">
							<tr>
								<td>${dto.listNum}</td>
								<td class="left"><a href="${articleUrl}&num=${dto.anum}">${dto.subject}</a>
								<td><span>관리자</span></td>
								<td>${dto.reg_date}</td>
								<td>${dto.hitCount}</td>
							</tr>
						</c:forEach>
					</table>


					<div class="page-box">${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
					</div>

					<table class="table">
						<tr>
							<td width="100">
								<button type="button" class="btn"
									onclick="location.href='${pageContext.request.contextPath}/announce/list.do';">새로고침</button>
							</td>

							<td align="right" width="100"><c:if
									test="${sessionScope.member.userId == 'admin'}">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/announce/write.do?rows=${rows}';">글올리기</button>
								</c:if></td>
						</tr>
					</table>

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