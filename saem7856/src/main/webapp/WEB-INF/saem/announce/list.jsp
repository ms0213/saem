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
.table-list tr:first-child{
	background: #eee;
}
.table-list th, .table-list td {
	text-align: center;
}

.table-list .notice {
	display: inline-block; padding:1px 3px; background: #ed4c00; color: #fff;
}
.table-list .left {
	text-align: left; padding-left: 5px; 
}

.table-list .chk {
	width: 40px; color: #787878;
}
.table-list .num {
	width: 60px; color: #787878;
}
.table-list .subject {
	color: #787878;
}
.table-list .name {
	width: 100px; color: #787878;
}
.table-list .date {
	width: 100px; color: #787878;
}
.table-list .hit {
	width: 70px; color: #787878;
}

.table-list input[type=checkbox]{
	vertical-align: middle;
}
</style>

<script type="text/javascript">

function changeList() {
    var f=document.listForm;
    f.page.value="1";
    f.action="${pageContext.request.contextPath}/announce/list.do";
    f.submit();
} 

function searchList() {
	var f = document.searchForm;
	f.submit();
}


$(function(){
	$("#chkAll").click(function(){
		if($(this).is(":checked")) {
			$("input[name=nums]").prop("checked", true);
		} else {
			$("input[name=nums]").prop("checked", false);
		}
	});
		
	$("#btnDeleteList").click(function(){
		var cnt=$("input[name=nums]:checked").length;
		if(cnt==0) {
			alert("삭제할 게시물을 먼저 선택하세요.");
			return false;
		}
			
		if(confirm("선택한 게시물을 삭제 하시겠습니까 ?")) {
			var f = document.listForm;
			f.action="${pageContext.request.contextPath}/announce/deleteList.do";
			f.submit();
		}
	});
});
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
				<div class="body-container" style="width: 700px;">
					<div class="body-title">
						<h3>
							<i class="fas fa-clipboard-list"></i> 공지사항
						</h3>
					</div>

					<form name="listForm" method="post">
						<table class="table">
							<tr>
								<td width="50%"><c:if
										test="${sessionScope.member.userId=='admin'}">
										<button type="button" class="btn" id="btnDeleteList">삭제</button>
									</c:if> <c:if test="${sessionScope.member.userId!='admin'}">
							${dataCount}개(${page}/${total_page} 페이지)
						</c:if></td>
								<td align="right"><c:if test="${dataCount!=0 }">
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
									</c:if> <input type="hidden" name="page" value="${page}"> <input
									type="hidden" name="condition" value="${condition}"> <input
									type="hidden" name="keyword" value="${keyword}"></td>
							</tr>
						</table>

						<table class="table table-border table-list">
							<tr>
								<c:if test="${sessionScope.member.userId=='admin'}">
									<th class="chk"><input type="checkbox" name="chkAll"
										id="chkAll"></th>
								</c:if>
								<th class="num">번호</th>
								<th class="subject">제목</th>
								<th class="name">작성자</th>
								<th class="date">작성일</th>
								<th class="hit">조회수</th>
							</tr>
							
							<c:forEach var="dto" items="${listNotice}">
								<tr>
									<c:if test="${sessionScope.member.userId=='admin'}">
										<td><input type="checkbox" name="nums" value="${dto.num}">
										</td>
									</c:if>
									<td><span class="notice">공지</span></td>
									<td class="left"><a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
									</td>
									<td>${dto.userName}</td>
									<td>${dto.reg_date}</td>
									<td>${dto.hitCount}</td>
								</tr>
							</c:forEach>

							<c:forEach var="dto" items="${list}">
								<tr>
									<c:if test="${sessionScope.member.userId=='admin'}">
										<td><input type="checkbox" name="nums" value="${dto.num}">
										</td>
									</c:if>
									<td>${dto.listNum}</td>
									<td class="left"><a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
										<c:if test="${dto.gap<1}">
											<img
												src="${pageContext.request.contextPath}/resource/images/new.gif">
										</c:if></td>
									<td>${dto.userName}</td>
									<td>${dto.reg_date}</td>
									<td>${dto.hitCount}</td>
								</tr>
							</c:forEach>
						</table>
					</form>

					<div class="page-box">${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
					</div>

					<table class="table">
						<tr>
							<td width="100">
								<button type="button" class="btn"
									onclick="location.href='${pageContext.request.contextPath}/announce/list.do';">새로고침</button>
							</td>
							<td align="center">
								<form name="searchForm"
									action="${pageContext.request.contextPath}/announce/list.do"
									method="post">
									<select name="condition" class="selectField">
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
										class="boxTF"> <input type="hidden" name="rows"
										value="${rows}">
									<button type="button" class="btn" onclick="searchList();">검색</button>
								</form>
							</td>
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