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
table {
    display: table;
    border-collapse: separate;
    box-sizing: border-box;
    text-indent: initial;
    border-spacing: 2px;
    border-color: grey;
}
.table-border tr {
	border-bottom: 1px solid #ccc; 
}
.table-border tr:first-child {
	border-top: 2px solid #ccc;
}
.table-list tr:first-child{
	background: #eee;
}
.table-list th, .table-list td {
	text-align: center;
}
.table-list .left {
	text-align: left; padding-left: 5px; 
}

.table-list .num {
	width: 80px; color: #787878;
}
.table-list .subject {
	color: #787878;
}
.table-list .name {
	width: 100px; color: #787878;
}
.table-list .date {
	width: 150px; color: #787878;
}
.table-list .hit {
	width: 100px; color: #787878;
}
.body-title {
    color: #424951;
    padding-top: 25px;
    padding-bottom: 5px;
    margin: 0 0 25px 0;
    border-bottom: 1px solid #ddd;
}
.body-title h3 {
    font-size: 23px;
    min-width: 700px;
    font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
    font-weight: bold;
    margin: 0 0 -5px 0;
    padding-bottom: 5px;
    display: inline-block;
    border-bottom: 3px solid #424951;
}
.btn {
	color: #333;
	border: 1px solid #333;
	background-color: #fff;
	padding: 4px 10px;
	border-radius: 4px;
	font-weight: 500;
	cursor:pointer;
	font-size: 14px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.btn:hover, .btn:active, .btn:focus {
	background-color: #e6e6e6;
	border-color: #adadad;
	color:#333;
}
.btn[disabled], fieldset[disabled] .btn {
	pointer-events: none;
	cursor: not-allowed;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
	opacity: .65;
}
.selectField {
	border: 1px solid #999;
	padding: 4px 5px;
	border-radius: 4px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
	width: 100px;
	font-size: 14px;
}
.search_wrapper {
	display: flex;
	align-items: center;
	background-color: #fff;
	border: 1px solid #dee2e6;
	background-color: #fff;
	border-radius: 4px;
	padding: 5px 10px;
}
.body-container {
    margin: 0 auto 15px;
    width: 100%;
    min-height: 450px;
}
.body-title {
    color: #424951;
    padding-top: 25px;
    padding-bottom: 5px;
    margin: 0 0 25px 0;
    border-bottom: 1px solid #ddd;
}
.table {
    width: 100%;
    border-spacing: 0;
    border-collapse: collapse;
}
tbody {
    display: table-row-group;
    vertical-align: middle;
    border-color: inherit;
}
.page-box {
    clear: both;
    padding: 20px 0;
    text-align: center;
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
		<div class="body-title">
			<h3><i class="fas fa-chalkboard"></i> 자유 게시판 </h3>
		</div>
        
		<table class="table">
			<tr>
				<td width="50%">
					${dataCount}개(${page}/${total_page} 페이지)
				</td>
				<td align="right">&nbsp;</td>
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
						<td><span class="notice">공지</span></td>
						<td class="left">
							<a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
							<c:if test="${dto.replyCount!=0}">(${dto.replyCount})</c:if>
						</td>
						<td>${dto.userName}</td>
						<td>${dto.reg_date}</td>
						<td>${dto.hitCount}</td>
					</tr>
			</c:forEach>
			
			<c:forEach var="dto" items="${list}">
				<tr>
					<td>${dto.listNum}</td>
					<td class="left">
						<a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
					</td>
					<td>${dto.userName}</td>
					<td>${dto.reg_date}</td>
					<td>${dto.hitCount}</td>
				</tr>
			</c:forEach>
		</table>
		
		<div class="page-box">
			${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
		</div>
		
		<table class="table">
			<tr>
				<td width="100">
					<button type="button" class="button primary small" onclick="location.href='${pageContext.request.contextPath}/bbs/list.do';">새로고침</button>
				</td>
				<td align="center">
					<form name="searchForm" action="${pageContext.request.contextPath}/bbs/list.do" method="post" class="search_wrapper">
						<select name="condition" class="selectField">
							<option value="all"      ${condition=="all"?"selected='selected'":"" }>제목+내용</option>
							<option value="userName" ${condition=="userName"?"selected='selected'":"" }>작성자</option>
							<option value="reg_date"  ${condition=="reg_date"?"selected='selected'":"" }>등록일</option>
							<option value="subject"  ${condition=="subject"?"selected='selected'":"" }>제목</option>
							<option value="content"  ${condition=="content"?"selected='selected'":"" }>내용</option>
						</select>
						<input type="text" name="keyword" value="${keyword}" class="boxTF">
						<button type="button" class="button primary small" onclick="searchList();">검색</button>
					</form>
				</td>
				<td align="right" width="100">
					<c:choose>
							<c:when test="${sessionScope.member.userId=='admin'}">
								<button type="button" class="button primary small" onclick="location.href='${pageContext.request.contextPath}/bbs/notice.do';">글올리기</button>
							</c:when>
							<c:otherwise>
			
								<button type="button" class="button primary small" onclick="location.href='${pageContext.request.contextPath}/bbs/write.do';">글올리기</button>
							
							</c:otherwise>
						</c:choose>
				
				</td>
			</tr>
		</table>	

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