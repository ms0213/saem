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
<script type="text/javascript">
function deleteBoard(){
	if(confirm("게시글을 삭제하시겠습니까?")){
		var query = "num=${dto.num}&${query}";
		var url = "${pageContext.request.contextPath}/contact/delete.do?"+query;
		location.href=url;
	}
}
</script>
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
				
				<!-- Section -->						
				<div class="title" style="margin:30px 0 30px 0">
					<h1>Contact Us</h1>
				</div>
				<table class="table table-border table-article">
					<tr>
						<td width="50%">
							작성자 : ${dto.userFullName}
						</td>
						<td align="left">
							이메일 : ${dto.email}
						</td>
					</tr>
					<tr>
						<td width="40%" style="font-weight: bold">
							선수 : ${dto.member}
						</td>
						<td width="40%" align="left" style="font-weight: bold">
							구단 : ${dto.league}
						</td>
					</tr>
					<tr>
						<td colspan="2"  height="500" style="border-top:2px solid gray;">
							${dto.comment}
						</td>
					</tr>
				</table>
				
				<table class="table">
					<tr>
						<td align="right">
							<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/contact/list.do?${query}';">리스트</button>
						</td>
					</tr>
				</table>
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