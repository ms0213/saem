<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>saem 7856</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" href="assets/css/main.css" />
<jsp:include page="/WEB-INF/saem/layout/staticHeader.jsp"/>
<script type="text/javascript">

function ajaxFun(url, method, query, dataType, fn){
	$.ajax({
		type:method,
		url:url,
		data:query,
		dataType:dataType,
		success:function(data){
			fn(data);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		error:function(jqXHR) {
			if(jqXHR.status === 403) {
				login();
				return false;
			} else if(jqXHR.status === 405) {
				alert("접근을 허용하지 않습니다.");
				return false;
			}
	    	
			console.log(jqXHR.responseText);
		}
	});
}

function searchList() {
	var f = document.searchForm;
	f.submit();
}

$(function(){
	$(".checked").click(function(){
		var $i = $(this).find("i");
		var isLike = $i.props == "far";
		alert(isLike);
	});
});
</script>

<style type="text/css">
.search_wrapper {
	display: flex;
	align-items: center;
	margin:0;
	min-width : 350px;
}

i:hover{
	cursor:pointer;
}
</style>
</head>
<body class="is-preload">
	<!-- Wrapper -->
	<div id="wrapper">
		<!-- Main -->
		<div id="main">
			<div class="inner">
				<!-- Header -->
				<header id="header">
					<jsp:include page="/WEB-INF/saem/layout/header.jsp"/>
				</header>
				
				<div class="title" style="margin:30px 0 30px 0">
					<h1>Contact Us</h1>
				</div>
			
				<table>
					<tr>
						<td width="50%">
							${dataCount}개(${page}/${total_page} 페이지)
						</td>
						<td width="20%" align="right">
							<!-- Search -->
							<section id="search" class="alt">
								<form method="post" class="search_wrapper" 
								name="searchForm" action="${pageContext.request.contextPath}/contact/list.do">
									<select name="condition" style="width:60%">
										<option value="member" ${condition=="member"?"selected='selected'":"" }>선수</option>
										<option value="league"  ${condition=="league"?"selected='selected'":"" }>구단</option>
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
							<th class="chk">
								<input type="checkbox" name="chkAll" id="chkAll">        
							</th>
							<th class="num">번호</th>
							<th class="name">작성자</th>
							<th class="email">이메일</th>
							<th class="member">선수</th>
							<th class="date">작성일</th>
							<th class="check">확인</th>
						</tr>
						<c:forEach var="dto" items="${list}">
							<tr>
								<td>
									<input type="checkbox" name="nums" value="${dto.num}">
								</td>
								<td>${dto.listNum}</td>
								<td class="left">
									<a href="${articleUrl}&num=${dto.num}">
										${dto.userFullName}
									</a>
								</td>
								<td>${dto.email}</td>		
								<td>${dto.member}</td>
								<td>${dto.reg_date}</td>
								<td>
									<c:choose>
										<c:when test="${ isChecked == true}">
											<i class="far fa-check-circle fa-lg" onclick = "checked();"></i>										
										</c:when>
										<c:otherwise>
											<i class="fas fa-check-circle fa-lg" onclick = "checked();"></i>																				
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</table>
				</form>
				
				<div class="page-box">
					${dataCount==0?"등록된 게시물이 없습니다.":paging }
				</div>
			</div>
		</div>
		
		<!-- Sidebar -->
		<div id="sidebar">
			<jsp:include page="/WEB-INF/saem/layout/footer.jsp"></jsp:include>
		</div>
	</div>

	<jsp:include page="/WEB-INF/saem/layout/staticFooter.jsp"/>

</body>
</html>